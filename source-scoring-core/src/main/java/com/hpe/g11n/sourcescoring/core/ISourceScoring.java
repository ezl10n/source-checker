package com.hpe.g11n.sourcescoring.core;




import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
import com.hpe.g11n.sourcescoring.pojo.ReportData;

import java.util.List;

public interface ISourceScoring {
    String check(List<InputDataObj> lstIdo);
    List<ReportData> report();
    void build(List<Integer> rulesChecked);
}
