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

	public static final String WARNING = "Warning:";
	public static final String INFORMATION = "Information:";
	public static final String ERROR = "Error:";
	public static final String CONFIRMATION = "Confirmation:";
	public static final String EDIT = "Edit";
	public static final String NEW = "New";
	public static final String IMPORT = "Import";

	// keywords configuring file path
	public static final String CONCATENATION_KEYWORDS_KEY = "psl.psl-generate-sourcechecker-report.concatenation.key-words";
	public static final String CONCATENATION_KEYWORDS_VARIABLES = "psl.psl-generate-sourcechecker-report.many-variables";
	public static final String CAMELCASE_FORMAT = "psl.psl-generate-sourcechecker-report.camel-case";

	// project configuring file path
	public static final String CONCATENATION_PATH = "psl.source-checker-white-list.Concatenation";
	public static final String CAMELCASE_PATH = "psl.source-checker-white-list.CamelCase";
	public static final String DATETIMEFORMAT_PATH = "psl.source-checker-white-list.DateTimeFormat";
	public static final String CAPITAL_PATH = "psl.source-checker-white-list.Capital";
	public static final String SPELLING_PATH = "psl.source-checker-white-list.Spelling";

	// check rule name
	public static final String CAMELCASE = "CamelCase";
	public static final String CONCATENATION = "Concatenation";
	public static final String DATETIMEFORMAT = "Date&Time Format";
	public static final String VARIABLES = "Variables";
	public static final String CAPITAL = "All Capital";
	public static final String STRINGMIXED = "Punctuation";
	public static final String VERBNOUNADJECTIVE = "Verb Noun Adjective";
	public static final String INCONSISTENCIES = "Inconsistencies";
	public static final String SPELLING = "Spelling";
	public static final String LONGSENTENCES = "Long Sentences";
	public static final String SPECIALPATTERNS = "Special patterns";
	public static final String UNTRANSLATABLE = "Untranslatable";
	public static final String HTML = "Html";

	public static final String STATE_ALL = "All";
	public static final String STATE_NEW_CHANGED = "New & Changed";
	public static final String INVALID = "invalid";

	public static final String SOURCE_CONFIG_PATH = "..%1$s%1$ssrc%1$smain%1$sconfig";
	public static final String SOURCE_CONFIG_NAME = "%1$ssource-checker-standalone-config.conf";
	public static final String SOURCE_CONFIG_DIR = "source.checker.config.basedir";
	public static final String USER_DIR = "user.dir";
	public static final String TEMPLET_CONFIG_NAME = "%1$sproduct-templet-config.conf";
	public static final String PRODUCT_CONFIG_PATH = "..%1$s%1$ssrc%1$smain%1$sproductConfig";
	public static final String SPELLING_DICT_DIR = "source.checker.spellingcheck.dict.basedir";
	public static final String SPELLING_DICT_DIR1 = "%1$s..%1$s%1$ssrc%1$smain%1$sdict";

	public static final String PRODUCT_CONFIG_DIR = "source.checker.productConfig.basedir";
	public static final String SOURCE_INJECT_CONFIG_NAME = "sourceCheckerConfig";
	public static final String TEMPLET_INJECT_CONFIG_NAME = "templetConfig";
	public static final String CHECKER_PARSER = "sourceCheckerParsers";
	public static final String RULES_PACKAGE = "com.hpe.g11n.sourcechecker.core.rules";
}
