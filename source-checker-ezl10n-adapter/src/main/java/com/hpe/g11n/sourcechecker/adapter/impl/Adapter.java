package com.hpe.g11n.sourcechecker.adapter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcechecker.adapter.IAdapter;
import com.hpe.g11n.sourcechecker.config.guice.ConfigModule;
import com.hpe.g11n.sourcechecker.core.guice.CoreModule;
import com.hpe.g11n.sourcechecker.gui.guice.GUIModule;
import com.hpe.g11n.sourcechecker.gui.tasks.SourceCheckerCommand;
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;

public class Adapter implements IAdapter {
	protected Injector injector = Guice.createInjector(new CoreModule(),
            new ConfigModule(),new GUIModule());
    SourceCheckerCommand sourceChecker = new SourceCheckerCommand();
    public Adapter() {
        injector.injectMembers(sourceChecker);
    }

    @Override
	public Map<String,String> execute(Map<String,String> paramMap) {
    	
    	String projectName=paramMap.get("-p");
    	String projectVersion=paramMap.get("-v");
    	String state=paramMap.get("-s");
    	String sourcePath=paramMap.get("-i");
    	String targetPath=paramMap.get("-o");
    	String rules=paramMap.get("-r");
		String[] rule =rules.split(",");
		List<Integer> lst = new ArrayList<Integer>();
		for(String r:rule){
			lst.add(Integer.valueOf(r));
		}
		sourceChecker.setUp(projectName,projectVersion,state,sourcePath,targetPath,lst);
		Map<String,String> result = null;
		try {
			result = sourceChecker.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return result;
	}

    @Override
    public List<String> getProjectName(){
		String preFix = String.format(MessageConstant.PROJECT_CONFIG_PATH,
				File.separator);
		File file = new File(preFix);
		File[] files = file.listFiles();
		List<String> lstName = new ArrayList<String>();
		if (files.length > 0) {
			for (File f : files) {
				String fileName = f.getName().substring(0,
						f.getName().length() - 5);
				lstName.add(fileName);
			}
		}
		return lstName;
	}
    
}
