package com.hpe.g11n.sourcechecker.gui.guice;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcechecker.fileparser.BaseParser;
import com.hpe.g11n.sourcechecker.fileparser.FileParserManager;
import com.hpe.g11n.sourcechecker.fileparser.IFileParser;
import com.hpe.g11n.sourcechecker.fileparser.impl.DefaultParser;
import com.hpe.g11n.sourcechecker.fileparser.impl.LPUFileParser;
import com.hpe.g11n.sourcechecker.fileparser.impl.PropertyParser;
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-10
 * Time: 18:02
 */
public class GUIModule extends AbstractModule {
    @Override
    protected void configure() {
    	 bind(new TypeLiteral<List<BaseParser>>(){}).annotatedWith(Names.named(MessageConstant.CHECKER_PARSER)).toInstance(getFileParsers());
    	 bind(IFileParser.class).to(FileParserManager.class);    
    }
    
    public List<BaseParser> getFileParsers(){
    	List<BaseParser> parsers=new ArrayList<>(15);
    	parsers.add(new LPUFileParser());
    	parsers.add(new PropertyParser());
    	parsers.add(new DefaultParser());
    	return parsers;
    }
}
