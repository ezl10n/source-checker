package com.hpe.g11n.sourcechecker.cli;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcechecker.cli.command.CommandOptions;
import com.hpe.g11n.sourcechecker.cli.guice.CliModule;
import com.hpe.g11n.sourcechecker.config.guice.ConfigModule;
import com.hpe.g11n.sourcechecker.core.guice.CoreModule;
import com.hpe.g11n.sourcechecker.gui.guice.GUIModule;
import com.hpe.g11n.sourcechecker.gui.tasks.SourceCheckerCommand;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-23
 * Time: 15:24
 */

public class CommandLineApplication {
    protected Injector injector = Guice.createInjector(new CoreModule(),
            new ConfigModule(),new GUIModule() ,new CliModule());
    CommandOptions options = new CommandOptions();
    SourceCheckerCommand task = new SourceCheckerCommand();
    public CommandLineApplication() {
        injector.injectMembers(options);
        injector.injectMembers(task);
    }

    public static void main(String[] args) {
        CommandLineApplication application = new CommandLineApplication();
        JCommander commander = new JCommander(application.options, args);
        commander.setProgramName("Source Checker CommandLine");
        if(application.options.isHelp() || !application.options.validate()){
            StringBuilder help = new StringBuilder();
            commander.usage(help);
            help.append(application.options.rulesUseage());
            System.out.println(help);
        }else{
            application.execute();
        }
    }
    public void execute(){
    	task.setUp(options.getProduct(),options.getVersion(),options.getScope(),
    			options.getSourceUrl(),options.getOutputUrl(),options.getSelectRules());
    	try {
			task.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
}
