package com.hpe.g11n.sourcescoring.core;




import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.typesafe.config.Config;

import java.util.List;

public interface IRule {
	boolean check(List<InputDataObj> lstIdo);
	List<ReportData> gatherReport();
	void setConfig(Config config);
}
