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
    public Config loadConfig(){
        String fileName="config"+ File.separator +"source-scoring-standalone-config.conf";
        String passInDir=System.getProperty("source.scoring.config.basedir");
        if(passInDir == null){
            passInDir = System.getProperty("user.dir");
            fileName=File.separator+"src"+File.separator+"main"+File.separator+fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
}
