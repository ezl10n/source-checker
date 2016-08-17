package com.hpe.g11n.sourcescoring.gui.guice;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcescoring.fileparser.BaseParser;
import com.hpe.g11n.sourcescoring.fileparser.FileParserManager;
import com.hpe.g11n.sourcescoring.fileparser.IFileParser;
import com.hpe.g11n.sourcescoring.fileparser.impl.DefaultParser;
import com.hpe.g11n.sourcescoring.fileparser.impl.LPUFileParser;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-10
 * Time: 18:02
 */
public class GUIModule extends AbstractModule {
    @Override
    protected void configure() {
    	 bind(new TypeLiteral<List<BaseParser>>(){}).annotatedWith(Names.named("sourceScoringParsers")).toInstance(getFileParsers());
    	 bind(IFileParser.class).to(FileParserManager.class);    
    }
    
    public List<BaseParser> getFileParsers(){
    	List<BaseParser> parsers=new ArrayList<>(15);
    	parsers.add(new LPUFileParser());
    	parsers.add(new DefaultParser());
    	return parsers;
    }
}
