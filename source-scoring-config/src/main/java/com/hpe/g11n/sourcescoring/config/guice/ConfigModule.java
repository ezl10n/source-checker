package com.hpe.g11n.sourcescoring.config.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-10
 * Time: 16:21
 */
public class ConfigModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(Names.named("sourceScoringConfig")).toInstance(loadConfig());
    }

    @Provides
    @Singleton
    public static Config loadConfig(){
        String preFix=String.format("%1$ssrc%1$smain%1$sconfig",File.separator);
        String fileName=String.format("%1$ssource-scoring-standalone-config.conf",File.separator);
        String passInDir=System.getProperty("source.scoring.config.basedir");
        if(passInDir == null){
            passInDir = System.getProperty("user.dir");
            fileName=preFix+fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
}
