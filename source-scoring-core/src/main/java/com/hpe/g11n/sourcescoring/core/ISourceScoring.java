package com.hpe.g11n.sourcescoring.core;




import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;

import java.util.List;

import javafx.concurrent.Task;

public interface ISourceScoring {
	String check(List<InputData> lstIdo);
    String check(List<InputData> lstIdo,ITaskProgressCallback callBack);
    List<ReportData> report();
    void build(List<Integer> rulesChecked);
}
