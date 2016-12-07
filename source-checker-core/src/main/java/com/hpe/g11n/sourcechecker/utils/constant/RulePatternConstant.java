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
	//匹配驼峰单词规则，匹配一个单词中至少有两个或以上大写字母如 CamelCaseCheck，camelCaseCheck，CamelcaseCheck
	 public static final String CAMEL_CASE_CHECK_RULE ="^[A-Z]+([a-z]+[A-Z]+){1,}$|^[A-Z]+[a-z]*([A-Z]+[a-z]+){1,}$|^([a-z]+[A-Z]+[A-Za-z]*){1,}$";
	 //匹配全大写， 如CAPITAL
	 public static final String CAPITAL_CHECK_RULE ="^[A-Z][\\sA-Z.]*$";
	 //匹配以log或Log开头并以in结尾的一句话
	 public static final String CONCATENATION_CHECK_RULE_0 ="^(log|Log)\\s(\\<|\\(|\\{|\\[)?[A-Za-z]+(\\>|\\)|\\}|\\])?\\s(in)$";
	 //匹配首字母为大写的打次包含一个大写字母
	 public static final String CONCATENATION_CHECK_RULE_1 ="^[A-Z][a-z]*$";
	 //匹配以一个单词或数字并以.或,结尾
	 public static final String CONCATENATION_CHECK_RULE_2 ="^[A-Za-z|\\d]+[\\.|,]$";
	 //匹配以大写字母开头，以by结尾的一句话，其中还可以以.或,或!或:或?结尾，也可以不带任何标点结尾
	 public static final String CONCATENATION_CHECK_RULE_3 ="^[A-Z].*\\sby(\\.|,|!|:|\\?)?$";
	 //匹配以小写字母开头，以navigate to结尾的一句话，其中还可以以.或,或!或:或?结尾，也可以不带任何标点结尾
	 public static final String CONCATENATION_CHECK_RULE_4 ="^[a-z].*\\snavigate to(\\.|,|!|:|\\?)?$";
	 //匹配一句话中含有.或,或!或:或?或;
	 public static final String LONG_SENTENCES_CHECK_RULE ="[\\s\\S]+[\\?|\\!|\\;|\\,|\\.|\\:]+[\\s\\S]+$";
	 //匹配一句话中含有{和一个数字或(数字)或[和一个数字
	 public static final String VARIABLES_CHECK_RULE_1 =".*(\\{\\s?(\\d+)\\s?.?).*|.*(.?\\s?(\\d+)\\s?\\}).*|.*(\\[\\s?(\\d+)\\s?.?).*|.*(.?\\s?(\\d+)\\s?\\]).*";
	 //匹配含有{0,xxx,xxx}格式的字符串
	 public static final String VARIABLES_CHECK_RULE_2 =".*\\{0\\,.*\\,.*\\}.*$";
	 //匹配以字母开头，中间含有.或,或:或'或"或?或/或$或*或@或#或！，并以字母带标点符号结尾或不带标点符号结尾的一个单词 ，如Designer!WorkflowViewer!
	 public static final String STRINGMIXED_1 ="^[A-Za-z]+[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+.?$";
	 //匹配单词中含有.或,或:或'或"或?或/或$或*或@或#或！单不仅仅是以结尾的单词，如!Designer!WorkflowViewer!
	 public static final String STRINGMIXED_2 ="[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+[[\\.|\\,|\\:|\\'|\\\"|\\?|\\/|\\$|\\*|\\@|\\#|\\!]+[A-Za-z]+]*.?$";
	 //匹配数字或小数
	 public static final String NUMBER ="[0-9]+$|^-[0-9]+[\\.][0-9]+$|[0-9]+[\\.][0-9]+$";
	 //匹配含有{和}的一句话
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_0=".*\\{.*\\}.*$";
	 //匹配含有\\d,choice,的一句话，如  this is 4,choice,ok。
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_1=".*\\d,choice,.*$";
	//匹配含有{count, plural,的一句话
	 public static final String SPECIAL_PATTERNS_CHECK_RULE_2=".*\\{count, plural,.*";
	//匹配含有[或/或\\或:或*或?或<或>或|或]或%的一句话
	 public static final String PRODUCT_FORMAT=".*[\\[|/|\\\\|:|\\*|\\?|\\<|\\>|\\||\\]|%]{1,}.*$";
	 
}
