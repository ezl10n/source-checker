package com.hpe.g11n.sourcechecker.core.spellingcheck.jazzy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.core.spellingcheck.ISpellingCheck;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.typesafe.config.Config;

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

    @Inject
    @Named("sourceCheckerConfig")
    Config config;

    public HashMapDictionarySpellCheck() {
        try {
            SpellDictionaryHashMap defaultDict = new SpellDictionaryHashMap();
            spellCheck = new SpellChecker(defaultDict);
            //try to add all  file as dictionary into spellcheck.
            File[] wordDicts = Paths.get(getDictBasePath(), dict).toFile().listFiles();
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
}
