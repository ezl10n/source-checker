package com.hpe.g11n.sourcescoring.cli;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcescoring.cli.command.CommandOptions;
import com.hpe.g11n.sourcescoring.cli.guice.CliModule;
import com.hpe.g11n.sourcescoring.config.guice.ConfigModule;
import com.hpe.g11n.sourcescoring.core.guice.CoreModule;
import com.hpe.g11n.sourcescoring.gui.guice.GUIModule;
import com.hpe.g11n.sourcescoring.gui.tasks.SourceScoringTask;

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
    SourceScoringTask task = new SourceScoringTask();
    public CommandLineApplication() {
        injector.injectMembers(options);
        injector.injectMembers(task);
    }

    public static void main(String[] args) {
        CommandLineApplication application = new CommandLineApplication();
        JCommander commander = new JCommander(application.options, args);
        commander.setProgramName("Source Scoring CommandLine");
        if(application.options.isHelp() || !application.options.validate()){
            StringBuilder help = new StringBuilder();
            commander.usage(help);
            help.append(application.options.rulesUseage());
            System.out.println(help);
        }

    }
    public void execute(){
        Thread t= new Thread(task);
        t.start();
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("\tcurrent progress: %.2f%%\n",task.getProgress()*100));
        }

    }
}
