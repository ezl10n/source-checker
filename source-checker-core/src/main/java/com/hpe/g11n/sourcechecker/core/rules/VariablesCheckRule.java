package com.hpe.g11n.sourcechecker.core.rules;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcechecker.core.IRule;
import com.hpe.g11n.sourcechecker.core.annotation.RuleData;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.pojo.ReportDataCount;
import com.hpe.g11n.sourcechecker.utils.ReportDataUtil;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
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

	private static final String VARIABLES="psl.psl-generate-sourcechecker-report.many-variables";
	private static final String VARIABLES_WHITELIST="psl.source-checker-white-list.Variables";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> variables;
	private List<String> whitelist;
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
		whitelist=this.config.getStringList(VARIABLES_WHITELIST);
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
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					int wordsCount =StringUtil.getCountWords(ido.getSourceString());
					int variablesCount =0;
					String[] sourceStrings = ido.getSourceString().split(" ");
					//check xxxx one{xxx} other{xxx} xxxx
					if(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.VARIABLES_CHECK_RULE_1)){
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.VARIABLES,"Warning: variable pattern \"one {xxx} other {xxx}\" detected. Please confirm which string(s) are translatable.",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.VARIABLES,"Warning: variable pattern \"one {xxx} other {xxx}\" detected. Please confirm which string(s) are translatable.",ido.getFileVersion(),false,null));
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						continue;
					}
					for(String v:variables){
						//check {0,xxx,xxx}
						for(String ss:sourceStrings){
							if(StringUtil.pattern(ss,RulePatternConstant.VARIABLES_CHECK_RULE_2) || ss.equals(v.trim())){
								variablesCount = variablesCount + 1;
							}
						}
						if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
							hitStrCount++;
							int hs = hashSet.size();
							hashSet.add(ido.getSourceString());
							if(hs == hashSet.size()){
								duplicatedStringCount++;
								duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.VARIABLES,"Warning: variables count exceeded threshold.",ido.getFileVersion(),true,null));
							}else{
								validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.VARIABLES,"Warning: variables count exceeded threshold.",ido.getFileVersion(),false,null));
							}
							hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
							break;
						}
					}
				}
			}else{
				int wordsCount =StringUtil.getCountWords(ido.getSourceString());
				int variablesCount =0;
				String[] sourceStrings = ido.getSourceString().split(" ");
				//check xxxx one{xxx} other{xxx} xxxx
				if(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.VARIABLES_CHECK_RULE_1)){
					hitStrCount++;
					int hs = hashSet.size();
					hashSet.add(ido.getSourceString());
					if(hs == hashSet.size()){
						duplicatedStringCount++;
						duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
								Constant.VARIABLES,"Warning: variable pattern \"one {xxx} other {xxx}\" detected. Please confirm which string(s) are translatable.",ido.getFileVersion(),true,null));
					}else{
						validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
								Constant.VARIABLES,"Warning: variable pattern \"one {xxx} other {xxx}\" detected. Please confirm which string(s) are translatable.",ido.getFileVersion(),false,null));
					}
					hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
					continue;
				}
				for(String v:variables){
					//check {0,xxx,xxx}
					for(String ss:sourceStrings){
						if(StringUtil.pattern(ss,RulePatternConstant.VARIABLES_CHECK_RULE_2) || ss.equals(v.trim())){
							variablesCount = variablesCount + 1;
						}
					}
					if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.VARIABLES,"Warning: variables count exceeded threshold.",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.VARIABLES,"Warning: variables count exceeded threshold.",ido.getFileVersion(),false,null));
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						break;
					}
				}
			}
			
			
			
			if(log.isDebugEnabled()){
				log.debug("END VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.VARIABLES, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,false,reportDataCount));
		return flag;
	}
}
