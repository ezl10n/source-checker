package com.hpe.g11n.sourcechecker.utils.constant;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月2日
 * @Time: 下午4:27:56
 *
 */
public class Constant {
	// Source Checker release version
   public static final String PRODUCT_VERSION = "1.0";
   
   	// keywords configuring file path
   public static final String CONCATENATION_KEYWORDS_KEY="psl.psl-generate-sourcechecker-report.concatenation.key-words";
   public static final String CONCATENATION_KEYWORDS_VARIABLES="psl.psl-generate-sourcechecker-report.many-variables";
   public static final String CAMELCASE_FORMAT="psl.psl-generate-sourcechecker-report.camel-case";
   
   
   // project configuring file path
   public static final String CONCATENATION_PATH="psl.source-checker-white-list.Concatenation";
   public static final String CAMELCASE_PATH="psl.source-checker-white-list.CamelCase";
   public static final String DATETIMEFORMAT_PATH="psl.source-checker-white-list.DateTimeFormat";
   public static final String CAPITAL_PATH="psl.source-checker-white-list.Capital";
   public static final String SPELLING_PATH="psl.source-checker-white-list.Spelling";
  
   // check rule name
   public static final String CAMELCASE ="CamelCase";
   public static final String CONCATENATION ="Concatenation";
   public static final String DATETIMEFORMAT="Date&Time Format";
   public static final String VARIABLES ="Variables";
   public static final String CAPITAL ="Capital";
   public static final String STRINGMIXED ="Punctuation";
   public static final String VERBNOUNADJECTIVE ="Verb Noun Adjective";
   public static final String INCONSISTENCIES ="Inconsistencies";
   public static final String SPELLING ="Spelling";
   public static final String LONGSENTENCES ="Long Sentences";
   public static final String BRACKET ="Bracket";
   public static final String UNTRANSLATABLE ="Untranslatable";
   public static final String HTML ="Html";
   
   
   public static final String STATE_ALL="All";
   public static final String STATE_NEW_CHANGED="New & Changed";
   public static final String INVALID="invalid";
}
