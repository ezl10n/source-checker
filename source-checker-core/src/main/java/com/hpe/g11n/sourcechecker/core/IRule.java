package com.hpe.g11n.sourcechecker.core;




import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.typesafe.config.Config;

import java.util.List;

public interface IRule {
	boolean check(List<InputData> lstIdo,String projectName);
	List<ReportData> gatherReport();
	void setConfig(Config config);
}
