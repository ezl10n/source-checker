package com.hpe.g11n.sourcescoring.core.rules;


import java.util.List;

import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;

@RuleData(id="CamelCaseCheckRule",name=Constant.CAMELCASE,order=2,ruleClass = CamelCaseCheckRule.class)
public class CamelCaseCheckRule implements IRule{

	@Override
	public List<ReportData> gatherReport() {
		return null;
	}

	@Override
	public void setConfig(Config config) {

	}

	@Override
	public boolean check(List<InputData> lstIdo) {
		// TODO Auto-generated method stub
		return false;
	}

}
