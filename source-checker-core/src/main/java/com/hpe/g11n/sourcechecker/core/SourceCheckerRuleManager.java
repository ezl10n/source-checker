package com.hpe.g11n.sourcechecker.core;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.typesafe.config.Config;

public class SourceCheckerRuleManager implements ISourceChecker{
    private final Logger log = LoggerFactory.getLogger(getClass());
    List<IRule> checkRules;
    @Inject
    @Named("ruleClasses")
    List<Class> rules;

    @Inject
    @Named("sourceCheckerConfig")
    Config config;
    
    @Inject
    @Named("configPath")
    String configPath;

    public SourceCheckerRuleManager() {

    }
    public void build(List<Integer> rulesChecked){
        checkRules = new ArrayList<>();
        if(log.isDebugEnabled()){
            log.debug("init Selected source checker check rules.");
        }
        rulesChecked.forEach( i -> {
            Class c= rules.get(i);
            try {
                IRule rule= (IRule) c.newInstance();
                rule.setConfig(config);
                rule.setConfigPath(configPath);
                checkRules.add((rule));
            } catch (InstantiationException e) {
                log.error("can't instance IRule:" + c, e);
            } catch (IllegalAccessException e) {
                log.error("can't instance IRule:" + c, e);
            }
        });
    }

    public String check(List<InputData> lstIdo,String product){
    	return check(lstIdo,product,null);
    }
    @Override
    public String check(List<InputData> lstIdo,String product,ITaskProgressCallback callBack) {
        Preconditions.checkNotNull(checkRules);
        Preconditions.checkArgument(checkRules.size() > 0, "checkRules should not be empty");
        for(int i =0;i<checkRules.size();i++){
        	checkRules.get(i).check(lstIdo,product);
        	
        	if(callBack != null){
        		callBack.callBack(i+1,checkRules.size());
        	}
        	
        }
        
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
