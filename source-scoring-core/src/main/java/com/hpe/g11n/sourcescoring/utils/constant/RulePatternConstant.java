package com.hpe.g11n.sourcescoring.utils.constant;

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
	 public static final String CAMEL_CASE_CHECK_RULE ="^([A-Z]+[a-z]+[A-Za-z]*){2,}$|^([a-z]+[A-Z]+[A-Za-z]*){2,}$";
	 public static final String CAPITAL_CHECK_RULE ="^[A-Z][\\sA-Z.]*$";
	 public static final String CONCATENATION_CHECK_RULE ="^[A-Z][a-z]*$";
	 public static final String LONG_SENTENCES_CHECK_RULE ="[\\s\\S]+[\\?|\\!|\\;|\\,|\\.|\\:]+[\\s\\S]+$";
	 public static final String VARIABLES_CHECK_RULE_1 =".*one\\s?\\{.*\\}\\s?other\\s?\\{.*\\}.*$";
	 public static final String VARIABLES_CHECK_RULE_2 =".*\\{0\\,.*\\,.*\\}.*$";
}
