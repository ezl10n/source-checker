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
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class ProjectConfigModule extends AbstractModule{
	 protected static final Logger logger = LoggerFactory.getLogger(TempletConfigModule.class);

	@Override
	protected void configure() {
//		bind(Config.class).annotatedWith(Names.named("projectConfig")).toInstance(loadConfig());
		
	}
//	@Provides
    public static Config loadConfig(String projectName){
		String configName = "%1$s"+projectName+".conf";
        String preFix=String.format("..%1$s%1$ssrc%1$smain%1$sprojectConfig",File.separator);
        String fileName=String.format(configName,File.separator);
        String passInDir=System.getProperty("source.checker.projectConfig.basedir");
        if(passInDir == null){
            passInDir = System.getProperty("user.dir");
            fileName = preFix + fileName;
        }
        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
    }
    
    public static void saveConfig(Config config,String projectName){
    	String configName = "%1$s"+projectName+".conf";
        String preFix=String.format("..%1$s%1$ssrc%1$smain%1$sprojectConfig",File.separator);
        String fileName=String.format(configName,File.separator);
        String passInDir=System.getProperty("source.checker.projectConfig.basedir");
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
            logger.error("update config file failure:"+passInDir+fileName,e);
        }
    }
}
