package com.hpe.g11n.sourcechecker.config.guice;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class ConfigModule extends AbstractModule {
    protected static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);

    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(Names.named(MessageConstant.SOURCE_INJECT_CONFIG_NAME)).toInstance(loadConfig());
        bind(Config.class).annotatedWith(Names.named(MessageConstant.TEMPLET_INJECT_CONFIG_NAME)).toInstance(loadTempletConfig());
    }

    public static Config loadConfig(){
        String preFix=String.format(MessageConstant.SOURCE_CONFIG_PATH,File.separator);
        String fileName=String.format(MessageConstant.SOURCE_CONFIG_NAME,File.separator);
        String passInDir=System.getProperty(MessageConstant.SOURCE_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(MessageConstant.USER_DIR);
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
   
    public static Config loadTempletConfig(){
        String preFix=String.format(MessageConstant.SOURCE_CONFIG_PATH,File.separator);
        String fileName=String.format(MessageConstant.TEMPLET_CONFIG_NAME,File.separator);
        String passInDir=System.getProperty(MessageConstant.SOURCE_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(MessageConstant.USER_DIR);
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
}
