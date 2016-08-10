package com.hpe.g11n.sourcescoring.core.rules;


import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;

import java.util.List;

@RuleData(id="CamelCaseCheckRule",name=Constant.CAMELCASE,order=2,ruleClass = CamelCaseCheckRule.class)
public class CamelCaseCheckRule implements IRule{

	@Override
	public boolean check(String source, String target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReportData> gatherReport() {
		return null;
	}

	@Override
	public void setConfig(Config config) {

	}

}
