package com.hpe.g11n.sourcechecker.core.spellingcheck.jazzy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcechecker.core.spellingcheck.ISpellingCheck;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-26
 * Time: 10:03
 */
public class HashMapDictionarySpellCheck implements ISpellingCheck {

    private String dict = "jazzy";

    private SpellChecker spellCheck = null;

    private int threshold = 0;
    
    Vector dictionaries = new Vector();
    private String configPath;

    public HashMapDictionarySpellCheck(String path) {
        try {
        	this.configPath = path;
            SpellDictionaryHashMap defaultDict = new SpellDictionaryHashMap();
            spellCheck = new SpellChecker(defaultDict);
            //try to add all  file as dictionary into spellcheck.
            File[] wordDicts = Paths.get(getDictBasePath(configPath), dict).toFile().listFiles();
            for (File f : wordDicts) {
                if (f.isFile()) {
                	SpellDictionaryHashMap spellDictionary = new SpellDictionaryHashMap(f);
                	dictionaries.add(spellDictionary);
                    spellCheck.addDictionary(spellDictionary);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCorrect(String word) {
        Preconditions.checkNotNull(spellCheck);
        Preconditions.checkNotNull(word);
        return spellCheck.isCorrect(word);
    }

    @Override
    public List getSuggestions(String word) {
        Preconditions.checkNotNull(spellCheck);
        Preconditions.checkNotNull(word);
        return spellCheck.getSuggestions(word, threshold);
    }
    
    @Override
    public boolean isInDictionary(String word){
    	 Preconditions.checkNotNull(dictionaries);
    	 for (Enumeration e = dictionaries.elements(); e.hasMoreElements();) {
    	      SpellDictionary dictionary = (SpellDictionary) e.nextElement();
    	      if (dictionary.isCorrect(word)) return true;
    	    }
    	 return false;
    }
    
    private String getDictBasePath(String configPath){
    	if(configPath == null){
    		String baseDir=System.getProperty(Constant.SPELLING_DICT_DIR);
            if(baseDir == null || baseDir.isEmpty()){
                String subDir=String.format(Constant.SPELLING_DICT_DIR1, File.separator);
                baseDir=System.getProperty(Constant.USER_DIR)+subDir;
            }
            return baseDir;
    	}else{
    		 return configPath + "\\dict";
    	}
        
    }
}
