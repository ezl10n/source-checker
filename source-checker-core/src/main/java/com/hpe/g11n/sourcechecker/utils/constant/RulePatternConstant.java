package com.hpe.g11n.sourcechecker.utils.constant;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年9月2日
 * @Time: 下午4:59:41
 *
 */
public class RulePatternConstant {
	 // rule patterns
	 public static final String CAMEL_CASE_CHECK_RULE ="^[A-Z]+([a-z]+[A-Z]+){1,}$|^[A-Z]+[a-z]*([A-Z]+[a-z]+){1,}$|^([a-z]+[A-Z]+[A-Za-z]*){1,}$";
	 public static final String CAPITAL_CHECK_RULE ="^[A-Z][\\sA-Z.]*$";
	 public static final String CONCATENATION_CHECK_RULE_0 ="^(log|Log)\\s(\\<|\\(|\\{|\\[)?[A-Za-z]+(\\>|\\)|\\}|\\])?\\s(in)$";
	 public static final String CONCATENATION_CHECK_RULE_1 ="^[A-Z][a-z]*$";
	 public static final String CONCATENATION_CHECK_RULE_2 ="^[A-Za-z|\\d]+[\\.|,]$";
	 public static final String CONCATENATION_CHECK_RULE_3 ="^[A-Z].*\\sby(\\.|,|!|:|\\?)?$";
	 public static final String CONCATENATION_CHECK_RULE_4 ="^[a-z].*\\snavigate to(\\.|,|!|:|\\?)?$";
	 public static final String LONG_SENTENCES_CHECK_RULE ="[\\s\\S]+[\\?|\\!|\\;|\\,|\\.|\\:]+[\\s\\S]+$";
	 public static final String VARIABLES_CHECK_RULE_1 =".*(\\{\\s?(\\d+)\\s?.?).*|.*(.?\\s?(\\d+)\\s?\\}).*|.*(\\[\\s?(\\d+)\\s?.?).*|.*(.?\\s?(\\d+)\\s?\\]).*";
	 public static final String VARIABLES_CHECK_RULE_2 =".*\\{0\\,.*\\,.*\\}.*$";
	 public static final String STRINGMIXED_1 ="^[A-Za-z]+[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+.?$";
	 public static final String STRINGMIXED_2 ="[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+[[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+]*.?$";
	 public static final String NUMBER ="[0-9]+$|^-[0-9]+[\\.][0-9]+$|[0-9]+[\\.][0-9]+$";
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_0=".*\\{.*\\}.*$";
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_1=".*\\d,choice,.*$";
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_2=".*\\{count, plural,.*";
	 public static final String PRODUCT_FORMAT=".*[\\[|/|\\\\|:|\\*|\\?|\\<|\\>|\\||\\]|%]{1,}.*$";
	 
}
