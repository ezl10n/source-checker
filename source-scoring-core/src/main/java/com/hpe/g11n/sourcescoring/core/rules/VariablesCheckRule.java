package com.hpe.g11n.sourcescoring.core.rules;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;
import com.hpe.g11n.sourcescoring.utils.constant.Constant;
import com.hpe.g11n.sourcescoring.utils.ReportDataUtil;
import com.hpe.g11n.sourcescoring.utils.constant.RulePatternConstant;
import com.hpe.g11n.sourcescoring.utils.StringUtil;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月16日
 * @Time: 上午11:09:30
 *
 */
@RuleData(id="VariablesCheckRule",name=Constant.VARIABLES,order=4,ruleClass = VariablesCheckRule.class)
public class VariablesCheckRule implements IRule{

	private static final String VARIABLES="psl.psl-generate-sourcescoring-report.many-variables";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> variables;
	private List<ReportData> report =null;
	private Config config;

	public VariablesCheckRule(){

	}

	@Override
	public List<ReportData> gatherReport() {
		return report;
	}

	@Override
	public void setConfig(Config config) {
		this.config=config;
		variables=this.config.getStringList(VARIABLES);
	}

	@Override
	public boolean check(List<InputData> lstIdo) {
		Preconditions.checkNotNull(variables);
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<>();
		HashSet<String> hashSet = new HashSet<String>();
		int hitStrCount=0;
		int totalWordCount=0;
		int hitNewChangeWordCount =0;
		int duplicatedStringCount =0;
		int duplicatedWordCount =0;
		int validatedWordCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			int wordsCount =StringUtil.getCountWords(ido.getSourceString());
			int variablesCount =0;
			String[] sourceStrings = ido.getSourceString().split(" ");
			//check xxxx one{xxx} other{xxx} xxxx
			if(pattern(ido.getSourceString(),RulePatternConstant.VARIABLES_CHECK_RULE_1)){
				report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
						Constant.VARIABLES,"Warning: variable pattern \"one {xxx} other {xxx}\" detected. Please confirm which string(s) are translatable.",ido.getFileVersion(),null));
				hitStrCount++;
				int hs = hashSet.size();
				hashSet.add(ido.getSourceString());
				if(hs == hashSet.size()){
					duplicatedStringCount++;
					duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
				}else{
					validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
				}
				hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
				continue;
			}
			for(String v:variables){
				//check {0,xxx,xxx}
				for(String ss:sourceStrings){
					if(pattern(ss,RulePatternConstant.VARIABLES_CHECK_RULE_2) || ss.equals(v.trim())){
						variablesCount = variablesCount + 1;
					}
				}
				if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
							Constant.VARIABLES,"Warning: variables count exceeded threshold.",ido.getFileVersion(),null));
					hitStrCount++;
					int hs = hashSet.size();
					hashSet.add(ido.getSourceString());
					if(hs == hashSet.size()){
						duplicatedStringCount++;
						duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
					}else{
						validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
					}
					hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
					break;
				}
			}
			
			if(log.isDebugEnabled()){
				log.debug("END VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.VARIABLES, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	private boolean pattern(String source,String rule){
		Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
	}
}
