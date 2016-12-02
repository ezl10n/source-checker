package com.hpe.g11n.sourcechecker.config.guice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class TempletConfigModule extends AbstractModule{
	 protected static final Logger logger = LoggerFactory.getLogger(TempletConfigModule.class);

	@Override
	protected void configure() {
		 bind(Config.class).annotatedWith(Names.named(Constant.TEMPLET_INJECT_CONFIG_NAME)).toInstance(loadConfig());
		
	}
	@Provides
    public static Config loadConfig(){
        String preFix = String.format(Constant.SOURCE_CONFIG_PATH,File.separator);
        String fileName = String.format(Constant.TEMPLET_CONFIG_NAME,File.separator);
        String passInDir = System.getProperty(Constant.SOURCE_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(Constant.USER_DIR);
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
	
	 public static void saveConfig(Config config){
	        String preFix = String.format(Constant.SOURCE_CONFIG_PATH,File.separator);
	        String fileName = String.format(Constant.TEMPLET_CONFIG_NAME,File.separator);
	        String passInDir = System.getProperty(Constant.SOURCE_CONFIG_DIR);
	        if(passInDir == null){
	            passInDir = System.getProperty(Constant.USER_DIR);
	            fileName = preFix + fileName;
	        }
	       ConfigRenderOptions renderOptions = ConfigRenderOptions.defaults()
	                .setJson(true).setOriginComments(false);
	        String content = config.root().render(renderOptions);
	        try {
	            Files.write(Paths.get(passInDir, fileName), Lists.newArrayList(content.trim()));
	        } catch (IOException e) {
	            logger.error("write config back file failure:" + passInDir + fileName,e);
	        }
	    }
}