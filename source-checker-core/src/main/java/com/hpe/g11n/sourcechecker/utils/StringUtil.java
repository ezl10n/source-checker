package com.hpe.g11n.sourcechecker.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

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
	
	public static boolean isWhiteList(List<String> lst,String sourceString){
		boolean flag=false;
		for(String string:lst){
			if(sourceString.equals(string)){
				flag =true;
				break;
			}
		}
		return flag;
	}
	
	public static String filter(String string){
		if(string !=null && !"".equals(string)){
			return string.replaceAll("'\\{'", "").replaceAll("'\\}'", "").replaceAll("'\\['", "").replaceAll("'\\]'", "")
					.replaceAll("']]>'", "").replaceAll("'\\:\\]'", "").replaceAll("'\\:\\['", "").replaceAll("'\\\\\\['", "")
					.replaceAll("'\\\\\\]'", "").replaceAll("'\\&\\['", "").replaceAll("'\\&\\]'", "").trim();
		}
		return string;
	}
	
	public static boolean formatRight(String string) {
		if (string.contains("\\") 
				|| string.contains("/")
				|| string.contains("<") 
				|| string.contains(">")) {
			return false;
		}else{
			return true;
		}
	}
	
	public static String getChangedString(String string){
		 string =string.replaceAll("\\[","").replaceAll("\\]","");
		 if(string !=null && !"".equals(string)){
			 String[] arrayStr = string.split(",");
			 String newStr="";
			 for(String temp:arrayStr){
				 newStr = newStr +temp.trim() +",";
			 }
			 return newStr.trim().substring(0, newStr.length()-1);
		 }else{
			 return "";
		 }
	}
	
	public static Config loadConfig(String projectName,String configPath){
		if(configPath == null || "".equals(configPath)){
			String configName = "%1$s"+projectName+".conf";
	        String preFix=String.format(Constant.PRODUCT_CONFIG_PATH,File.separator);
	        String fileName=String.format(configName,File.separator);
	        String passInDir=System.getProperty(Constant.PRODUCT_CONFIG_DIR);
	        if(passInDir == null){
	            passInDir = System.getProperty(Constant.USER_DIR);
	            fileName = preFix + fileName;
	        }
	        return ConfigFactory.parseFileAnySyntax(Paths.get(passInDir, fileName).toFile());
		}else{
			return ConfigFactory.parseFileAnySyntax(new File(configPath + "/productConfig/" + projectName+".conf"));
		}
    }
	
	public static String getNewString(String details){
		if(details.contains("\"")){
			String[] arrayStr = details.split("\"");
			return arrayStr[1];
		}else{
			return "";
		}
		
	}
	
	public static List<String> getUniqueList(List<String> list){
	    Set<String> set=new HashSet<String>();         
	    set.addAll(list);    
	    List<String> lst = new ArrayList<String>();
	    lst.addAll(set);
	    return lst;
	}
	
	public static Properties getProperties(){
		InputStream inputStream = StringUtil.class.getClassLoader().getResourceAsStream("version.properties");  
		 Properties p = new Properties();  
	        try {  
	            p.load(inputStream);  
	            inputStream.close();  
	        } catch (IOException e1) {  
	            e1.printStackTrace();  
	        }  
		return p;
	}
	
	public static String getVersion(){
		Properties p = getProperties();
		return p.getProperty("version");
	}
}
