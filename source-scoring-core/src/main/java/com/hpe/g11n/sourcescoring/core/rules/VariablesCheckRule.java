package com.hpe.g11n.sourcescoring.core.rules;


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
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.hpe.g11n.sourcescoring.utils.ReportDataUtil;
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
		int totalNCCount=0;
		int hitNCCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
			totalNCCount = totalNCCount + ido.getSourceStrings().split(" ").length;
			int wordsCount =ido.getSourceStrings().trim().split(" ").length;
			int variablesCount =0;
			for(String v:variables){
				//check xxxx one{xxx} other{xxx} xxxx
				if(pattern(ido.getSourceStrings(),".*one\\s?\\{.*\\}\\s?other\\s?\\{.*\\}.*$")){
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
							Constant.VARIABLES,"",null));
					hitStrCount++;
					hashSet.add(ido.getSourceStrings());
					hitNCCount = hitNCCount + ido.getSourceStrings().split(" ").length;
					break;
				}
				//check {0,xxx,xxx}
				if(pattern(ido.getSourceStrings(),".*\\{0\\,.*\\,.*\\}.*")){
					variablesCount = variablesCount + 1;
				}
				if(ido.getSourceStrings().contains(" "+v+" ")){
					variablesCount = variablesCount + ido.getSourceStrings().split(" "+v+" ").length-1;
				}
				if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
							Constant.VARIABLES,"",null));
					hitStrCount++;
					hashSet.add(ido.getSourceStrings());
					hitNCCount = hitNCCount + ido.getSourceStrings().split(" ").length;
				}
			}
			
			if(log.isDebugEnabled()){
				log.debug("END VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.VARIABLES, hitStrCount, hashSet.size(), totalNCCount, hitNCCount);
		report.add(new ReportData(null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	private boolean pattern(String source,String rule){
		Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
	}
}
