package com.hpe.g11n.sourcechecker.config.guice;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class ConfigModule extends AbstractModule {
    protected static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);

    private String configPath;
    public ConfigModule(String configPath){
    	this.configPath = configPath;
    }
    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(Names.named(Constant.SOURCE_INJECT_CONFIG_NAME)).toInstance(loadConfig(configPath));
        bind(Config.class).annotatedWith(Names.named(Constant.TEMPLET_INJECT_CONFIG_NAME)).toInstance(loadTempletConfig());
        if(configPath == null || "".equals(configPath)){
        	bind(String.class).annotatedWith(Names.named("configPath")).toInstance(getConfigPath());	
        }else{
        	bind(String.class).annotatedWith(Names.named("configPath")).toInstance("");
        }
    }

    public static Config loadConfig(String configPath){
    	if(configPath == null || "".equals(configPath)){
    		String preFix=String.format(Constant.SOURCE_CONFIG_PATH,File.separator);
            String fileName=String.format(Constant.SOURCE_CONFIG_NAME,File.separator);
            String passInDir=System.getProperty(Constant.SOURCE_CONFIG_DIR);
            if(passInDir == null){
                passInDir = System.getProperty(Constant.USER_DIR);
                fileName = preFix + fileName;
            }
            return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    	}else{
    		return ConfigFactory.parseFileAnySyntax(Paths.get(configPath, "\\config\\source-checker-standalone-config.conf").toFile());
    	}
        
    }
   
    public static Config loadTempletConfig(){
        String preFix=String.format(Constant.SOURCE_CONFIG_PATH,File.separator);
        String fileName=String.format(Constant.TEMPLET_CONFIG_NAME,File.separator);
        String passInDir=System.getProperty(Constant.SOURCE_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(Constant.USER_DIR);
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
    
    public String getConfigPath(){
    	return configPath;
    }
}
