package com.hpe.g11n.sourcechecker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月29日
 * @Time: 下午2:51:03
 *
 */
public class StringUtil {
	public static int getCountWords(String string) {
//		string = string.replaceAll("[^A-Za-z]", " ");
//		string = string.replaceAll("\\s+", " ");
//		String[] words;
//		words = string.trim().split("\\s+");
//		return words.length;
		String[] words;
		words = string.trim().split(" ");
		return words.length;
	}
	
	public static String getStringWithChar(String string) {
		if(pattern(string,"[A-Za-z]+'?[A-Za-z]+$")){
			return string.trim();
		}else{
			string = string.replaceAll("[^A-Za-z\\[\\]\\<\\>\\@\\&\\*\\%\\#\\$\\^\\{\\}]", " ");
			string = string.replaceAll("\\s+", " ");
			return string.trim();
		}
		
	}
//	public static String getStringWithNoPunctuation(String string) {
//		string = string.replaceAll("[^A-Za-z'\\-\\[\\]\\<\\>\\@\\&\\*\\%\\#\\$\\^]", " ");
//		string = string.replaceAll("\\s+", " ");
//		return string.trim();
//	}
	
	public static String[] getWordsFromString(String string){
		String[] words;
		words = string.trim().split(" ");
		return words;
	}
	
	public static boolean pattern(String source,String rule){
		Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
	}
	
	public static boolean haveTag(String string){
		string= string.trim();
		if((string.startsWith("<") && string.endsWith(">") && string.contains("</"))
				||(string.startsWith("<") && string.endsWith("/>")) ){
			return true;
		}
		return false;
	}
	
	public static boolean untranstlatable(String string){
		if(!string.contains(" ") && string.length() >=50){
			return true;
		}
		return false;
	}
	
	public static boolean isRightWord(String string){
		return pattern(string, "^[A-Za-z]+['\\-]?[A-Za-z]+$");
	}
}
