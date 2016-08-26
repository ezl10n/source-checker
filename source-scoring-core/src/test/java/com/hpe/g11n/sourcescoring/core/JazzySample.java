package com.hpe.g11n.sourcescoring.core;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-25
 * Time: 17:42
 */
public class JazzySample implements SpellCheckListener {

        private static String dictFile = "src/main/dict/jazzy/english.0";


        private SpellChecker spellCheck = null;


        public JazzySample() {
            try {
                String path=System.getProperty("user.dir");
                //TODO may be should use SpellDictionaryDisk instead...
                SpellDictionary dictionary = new SpellDictionaryHashMap(Paths.get(path,dictFile).toFile());

                spellCheck = new SpellChecker(dictionary);
                spellCheck.addSpellCheckListener(this);
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    System.out.print("Enter text to spell check: ");
                    String line = in.readLine();

                    if (line.length() <= 0)
                        break;
                   spellCheck.checkSpelling(new StringWordTokenizer(line));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void spellingError(SpellCheckEvent event) {
            List suggestions = event.getSuggestions();
            if (suggestions.size() > 0) {
                System.out.println("MISSPELT WORD: " + event.getInvalidWord());
                for (Iterator suggestedWord = suggestions.iterator(); suggestedWord.hasNext();) {
                    System.out.println("\tSuggested Word: " + suggestedWord.next());
                }
            } else {
                System.out.println("MISSPELT WORD: " + event.getInvalidWord());
                System.out.println("\tNo suggestions");
            }
            //Null actions
        }

        public static void main(String[] args) {
            new JazzySample();
        }
    }

