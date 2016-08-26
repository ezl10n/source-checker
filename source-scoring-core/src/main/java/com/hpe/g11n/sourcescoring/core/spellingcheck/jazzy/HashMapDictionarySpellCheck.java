package com.hpe.g11n.sourcescoring.core.spellingcheck.jazzy;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcescoring.core.spellingcheck.ISpellingCheck;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.typesafe.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

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

    @Inject
    @Named("sourceScoringConfig")
    Config config;

    public HashMapDictionarySpellCheck() {
        try {
            SpellDictionaryHashMap defaultDict = new SpellDictionaryHashMap();
            spellCheck = new SpellChecker(defaultDict);
            //try to add all  file as dictionary into spellcheck.
            File[] wordDicts = Paths.get(getDictBasePath(), dict).toFile().listFiles();
            for (File f : wordDicts) {
                if (f.isFile()) {
                    spellCheck.addDictionary(new SpellDictionaryHashMap(f));
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
    public List<String> getSuggestions(String word) {
        Preconditions.checkNotNull(spellCheck);
        Preconditions.checkNotNull(word);
        return spellCheck.getSuggestions(word, threshold);
    }
}
