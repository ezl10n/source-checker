package com.hpe.g11n.sourcechecker.adapter.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcechecker.adapter.IAdapter;
import com.hpe.g11n.sourcechecker.config.guice.ConfigModule;
import com.hpe.g11n.sourcechecker.core.guice.CoreModule;
import com.hpe.g11n.sourcechecker.gui.guice.GUIModule;
import com.hpe.g11n.sourcechecker.gui.tasks.SourceCheckerCommand;

public class Adapter implements IAdapter {
	protected Injector injector = Guice.createInjector(new CoreModule(),
            new ConfigModule(),new GUIModule());
    SourceCheckerCommand sourceChecker = new SourceCheckerCommand();
    public Adapter() {
        injector.injectMembers(sourceChecker);
    }

    @Override
	public void execute(String projectName,String projectVersion,String state,String sourcePath, String targetPath, String rules) {
		String[] rule =rules.split(",");
		List<Integer> lst = new ArrayList<Integer>();
		for(String r:rule){
			lst.add(Integer.valueOf(r));
		}
		sourceChecker.setUp(projectName,projectVersion,state,sourcePath,targetPath,lst);
	    	try {
	    		sourceChecker.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
