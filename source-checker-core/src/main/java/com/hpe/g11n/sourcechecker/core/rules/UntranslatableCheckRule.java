package com.hpe.g11n.sourcechecker.core.rules;

import java.util.List;

import com.hpe.g11n.sourcechecker.core.IRule;
import com.hpe.g11n.sourcechecker.core.annotation.RuleData;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;

//@RuleData(id="UntranslatableCheckRule",name=Constant.UNTRANSLATABLE,order=12,ruleClass = UntranslatableCheckRule.class)
public class UntranslatableCheckRule implements IRule{

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
