package com.hpe.g11n.sourcescoring.core.spellingcheck;

import java.io.File;
import java.util.List;

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
    
    //TDDO
    default String getSuggestionsLessThanThree(String word){
    	List<String> list= getSuggestions(word);
    	String result="";
    	for(int i=0;i<list.size();i++){
    		result = result + word +";";
    		i++;
    		if(i==2){
    			break;
    		}
    	}
    	if(result.length()>0){
    		result.substring(0,result.length()-1);
    	}
    	return result;
    }

    default String getDictBasePath(){
        String baseDir=System.getProperty("source.scoring.spellingcheck.dict.basedir");
        if(baseDir == null || baseDir.isEmpty()){
            //"src/main/dict";
            String subDir=String.format("%1$s..%1$s%1$ssrc%1$smain%1$sdict", File.separator);
            baseDir=System.getProperty("user.dir")+subDir;
        }
        return baseDir;
    }
}
