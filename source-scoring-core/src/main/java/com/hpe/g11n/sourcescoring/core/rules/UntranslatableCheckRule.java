package com.hpe.g11n.sourcescoring.core.rules;

import java.util.List;

import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;

@RuleData(id="UntranslatableCheckRule",name=Constant.UNTRANSLATABLE,order=12,ruleClass = UntranslatableCheckRule.class)
public class UntranslatableCheckRule implements IRule{

	@Override
	public boolean check(List<InputDataObj> lstIdo) {
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
