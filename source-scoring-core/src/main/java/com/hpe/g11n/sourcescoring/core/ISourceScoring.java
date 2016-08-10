package com.hpe.g11n.sourcescoring.core;




import com.hpe.g11n.sourcescoring.pojo.ReportData;

import java.util.List;

public interface ISourceScoring {
    String check(String key, String value);
    List<ReportData> report();
    static ISourceScoring getInstance(){
        return new SourceScoringPipelineManager();
    }
    static ISourceScoring getInstance(List<Integer> rulesIndex){
        return new SourceScoringPipelineManager(rulesIndex);
    }
}
