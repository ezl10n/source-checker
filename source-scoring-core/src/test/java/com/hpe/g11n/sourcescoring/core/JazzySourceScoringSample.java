package com.hpe.g11n.sourcescoring.core;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-26
 * Time: 9:35
 */
public class JazzySourceScoringSample {
    private static String dictFile = "src/main/dict/jazzy/english.0";

    private static SpellChecker spellCheck = null;

    public static void main(String[] args) {
        try {
            String path=System.getProperty("user.dir");
            //TODO may be should use SpellDictionaryDisk instead...
            SpellDictionary dictionary = new SpellDictionaryHashMap(Paths.get(path, dictFile).toFile());

            spellCheck = new SpellChecker(dictionary);
            //add word or dictionary..
            spellCheck.addToDictionary("foo");
            spellCheck.addToDictionary("bar");
            //spellCheck.addDictionary();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter text to spell check: ");
                String line = in.readLine();
                if (line.length() <= 0)
                    break;

                    boolean isSpellRight = spellCheck.isCorrect(line);
                    if(isSpellRight){
                        System.out.println(line+": OK!");
                    }else{
                        List<String> suggestions = spellCheck.getSuggestions(line,0);
                        System.out.println(line+": spell error, suggestions:"+ suggestions);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
