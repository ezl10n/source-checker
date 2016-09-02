package com.hpe.g11n.sourcescoring.core.rules;

import java.util.List;

import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.constant.Constant;
import com.typesafe.config.Config;

@RuleData(id="InconsistenciesCheckRule",name=Constant.INCONSISTENCIES,order=8,ruleClass = InconsistenciesCheckRule.class)
public class InconsistenciesCheckRule implements IRule{

	@Override
	public boolean check(List<InputData> lstIdo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReportData> gatherReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfig(Config config) {
		// TODO Auto-generated method stub
		
	}

}
