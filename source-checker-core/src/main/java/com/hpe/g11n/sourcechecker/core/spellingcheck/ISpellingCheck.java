package com.hpe.g11n.sourcechecker.core.spellingcheck;

import java.io.File;
import java.util.List;

import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-26
 * Time: 9:56
 */
public interface ISpellingCheck {
    /**
     * Verifies if the word to analyze is contained in dictionaries.
     * @param word The word to verify that it's spelling is known.
     * @return true if the word is in a dictionary.
     */
    boolean isCorrect(String word);

    /**
     * Produces a list of suggested word after looking for suggestions in various
     * dictionaries.
     * @param word The word for which we want to gather suggestions
     * @return the list of words suggested
     */
    List<String> getSuggestions(String word);
    
    /**
     * get the suggestion words 
     * @param word
     * @return
     */
	default String getSuggestionsLessThanThree(String word) {
		String result = "";
		List list = getSuggestions(word);
		for (int i = 0; i < list.size(); i++) {
			result = result + list.get(i) + ",";
			if (i == 2) {
				break;
			}
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		} else {
			result = "NA";
		}
		return result;
	}

    default String getDictBasePath(){
        String baseDir=System.getProperty(MessageConstant.SPELLING_DICT_DIR);
        if(baseDir == null || baseDir.isEmpty()){
            String subDir=String.format(MessageConstant.SPELLING_DICT_DIR1, File.separator);
            baseDir=System.getProperty(MessageConstant.USER_DIR)+subDir;
        }
        return baseDir;
    }
}
