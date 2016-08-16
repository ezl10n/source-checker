package com.hpe.g11n.sourcescoring.core.rules;


import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public boolean check(List<InputDataObj> lstIdo) {
		Preconditions.checkNotNull(variables);
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<>();
		for(InputDataObj ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
			int wordsCount =ido.getSourceStrings().trim().split(" ").length;
			int variablesCount =0;
			for(String v:variables){
				if(ido.getSourceStrings().contains(" "+v+" ")){
					variablesCount = variablesCount + ido.getSourceStrings().split(" "+v+" ").length-1;
				}
			}
			if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
				report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
						Constant.VARIABLES,""));
			}
			
			if(log.isDebugEnabled()){
				log.debug("END VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
		}
		return flag;
	}
}
