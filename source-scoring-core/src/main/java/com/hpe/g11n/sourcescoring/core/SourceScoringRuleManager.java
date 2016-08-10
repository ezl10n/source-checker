package com.hpe.g11n.sourcescoring.core;


import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.SourceScoringConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SourceScoringRuleManager implements ISourceScoring{
    private final Logger log = LoggerFactory.getLogger(getClass());
    List<IRule> checkRules;

    public SourceScoringRuleManager() {
    	checkRules = new ArrayList<>();
        if(log.isDebugEnabled()){
            log.debug("init ALL source scoring check Rules.");
        }
        List<Class> list = SourceScoringConfigUtil.ruleClassList();
        for (Class c : list) {
            try {
            	checkRules.add(((IRule) c.newInstance()));
            } catch (InstantiationException e) {
                log.error("can't instance IRule:" + c, e);
            } catch (IllegalAccessException e) {
                log.error("can't instance IRule:" + c, e);
            }
        }
    }
    public SourceScoringRuleManager(List<Integer> rulesIndex){
        List<Class> list = SourceScoringConfigUtil.ruleClassList();
        checkRules = new ArrayList<>();
        if(log.isDebugEnabled()){
            log.debug("init Selected source scoring check rules.");
        }
        rulesIndex.forEach( i -> {
            Class c= list.get(i);
            try {
            	checkRules.add(((IRule) c.newInstance()));
            } catch (InstantiationException e) {
                log.error("can't instance IRule:" + c, e);
            } catch (IllegalAccessException e) {
                log.error("can't instance IRule:" + c, e);
            }
        });
    }

    @Override
    public String check(String key, String value) {
        Preconditions.checkNotNull(checkRules);
        Preconditions.checkArgument(checkRules.size() > 0, "checkRules should not be empty");
        checkRules.forEach( c -> c.check(key,value));
        return "OK";
    }

    @Override
    public List<ReportData> report() {
        List<ReportData> result=new ArrayList<>();
        Preconditions.checkNotNull(checkRules);
        Preconditions.checkArgument(checkRules.size() > 0, "checkRules should not be empty");
        checkRules.forEach(r -> {
            List<ReportData> list = r.gatherReport();
            if (list != null) {
                result.addAll(list);
            }
        });
        return result;
    }
}
