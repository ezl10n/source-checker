package com.hpe.g11n.sourcescoring.cli;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcescoring.cli.command.CommandOptions;
import com.hpe.g11n.sourcescoring.cli.guice.CliModule;
import com.hpe.g11n.sourcescoring.config.guice.ConfigModule;
import com.hpe.g11n.sourcescoring.core.guice.CoreModule;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-23
 * Time: 15:24
 */

public class CommandLineApplication {
    protected Injector injector = Guice.createInjector(new CoreModule(),
            new ConfigModule(), new CliModule());
    CommandOptions options = new CommandOptions();

    public CommandLineApplication() {
        injector.injectMembers(options);
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
}
