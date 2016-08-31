package com.hpe.g11n.sourcescoring.utils;

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
		string = string.replaceAll("[^A-Za-z]", " ");
		string = string.replaceAll("\\s+", " ");
		String[] words;
		words = string.trim().split("\\s+");
		return words.length;
	}
	
	public static String getStringWithChar(String string) {
		string = string.replaceAll("[^A-Za-z]", " ");
		string = string.replaceAll("\\s+", " ");
		return string.trim();
	}
}
