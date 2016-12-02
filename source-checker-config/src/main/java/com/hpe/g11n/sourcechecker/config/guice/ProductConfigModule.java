package com.hpe.g11n.sourcechecker.config.guice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class ProductConfigModule extends AbstractModule{
	 protected static final Logger logger = LoggerFactory.getLogger(TempletConfigModule.class);

	@Override
	protected void configure() {
		
	}
    public static Config loadConfig(String product){
		String configName = "%1$s"+product+".conf";
        String preFix=String.format(Constant.PRODUCT_CONFIG_PATH,File.separator);
        String fileName=String.format(configName,File.separator);
        String passInDir=System.getProperty(Constant.PRODUCT_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(Constant.USER_DIR);
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
    
    public static void saveConfig(Config config,String product){
    	String configName = "%1$s"+product+".conf";
        String preFix=String.format(Constant.PRODUCT_CONFIG_PATH,File.separator);
        String fileName=String.format(configName,File.separator);
        String passInDir=System.getProperty(Constant.PRODUCT_CONFIG_DIR);
        if(passInDir == null){
            passInDir = System.getProperty(Constant.USER_DIR);
            fileName=preFix+fileName;
        }
       ConfigRenderOptions renderOptions = ConfigRenderOptions.defaults()
                .setJson(true).setOriginComments(false);
        String content = config.root().render(renderOptions);
        try {
            Files.write(Paths.get(passInDir, fileName), Lists.newArrayList(content.trim()));
        } catch (IOException e) {
            logger.error("update config file failure:"+passInDir+fileName,e);
        }
    }
    
    public static void deleteConfig(String product){
    	String configName = "%1$s"+product+".conf";
    	String preFix=String.format(Constant.PRODUCT_CONFIG_PATH,File.separator);
    	String fileName=String.format(configName,File.separator);
    	String passInDir=System.getProperty(Constant.PRODUCT_CONFIG_DIR);
    	if(passInDir == null){
    		passInDir = System.getProperty(Constant.USER_DIR);
    		fileName=preFix+fileName;
    	}
    	try {
    		File file =Paths.get(passInDir, fileName).toFile();
    		if(file.exists()){
    			file.delete();
    		}
    	} catch (Exception e) {
    		logger.error("delete config file failure:"+passInDir+fileName,e);
    	}
    }
}
