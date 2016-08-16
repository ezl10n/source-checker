package com.hpe.g11n.sourcescoring.config.guice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-10
 * Time: 16:21
 */
public class ConfigModule extends AbstractModule {
    protected static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);

    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(Names.named("sourceScoringConfig")).toInstance(loadConfig());
    }

    @Provides
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
    public static void saveConfig(Config config){
        String preFix=String.format("%1$ssrc%1$smain%1$sconfig",File.separator);
        String fileName=String.format("%1$ssource-scoring-standalone-config.conf",File.separator);
        String passInDir=System.getProperty("source.scoring.config.basedir");
        if(passInDir == null){
            passInDir = System.getProperty("user.dir");
            fileName=preFix+fileName;
        }
       ConfigRenderOptions renderOptions = ConfigRenderOptions.defaults()
                .setJson(true).setOriginComments(false);
        String content = config.root().render(renderOptions);
        try {
            Files.write(Paths.get(passInDir, fileName), Lists.newArrayList(content.trim()));
        } catch (IOException e) {
            logger.error("write config back file failure:"+passInDir+fileName,e);
        }
    }
}
