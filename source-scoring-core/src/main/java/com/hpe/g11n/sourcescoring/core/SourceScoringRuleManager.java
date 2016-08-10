package com.hpe.g11n.sourcescoring.core;


import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SourceScoringRuleManager implements ISourceScoring{
    private final Logger log = LoggerFactory.getLogger(getClass());
    List<IRule> checkRules;
    @Inject
    @Named("ruleClasses")
    List<Class> rules;

    @Inject
    @Named("sourceScoringConfig")
    Config config;

    public SourceScoringRuleManager() {

    }
    public void build(List<Integer> rulesChecked){
        checkRules = new ArrayList<>();
        if(log.isDebugEnabled()){
            log.debug("init Selected source scoring check rules.");
        }
        rulesChecked.forEach( i -> {
            Class c= rules.get(i);
            try {
                IRule rule= (IRule) c.newInstance();
                rule.setConfig(config);
                checkRules.add((rule));
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
