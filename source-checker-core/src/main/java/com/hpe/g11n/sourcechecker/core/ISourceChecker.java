package com.hpe.g11n.sourcechecker.core;




import java.util.List;

import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;

public interface ISourceChecker {
	String check(List<InputData> lstIdo,String projectName);
    String check(List<InputData> lstIdo,String projectName,ITaskProgressCallback callBack);
    List<ReportData> report();
    void build(List<Integer> rulesChecked);
}
