package com.hpe.g11n.sourcechecker.utils.constant;

public class MessageConstant {
	 public static final String CONCATENATION_MSG1_START = "Warning: The string starts or ends with keyword \"";
	 public static final String CONCATENATION_MSG1_END = "\". Possible concatenation.";
	 public static final String CONCATENATION_MSG2_START = "Warning: Single word \"";
	 public static final String CONCATENATION_MSG2_END = "\" detected. Possible concatenation.";
	 public static final String CONCATENATION_MSG3 = "Warning: The string contains only a single word with punctuation. Possible concatenation.";
	 
	 public static final String VARIABLES_MSG1 = "Warning: Too many variables.";
	 public static final String VARIABLES_MSG2_START = "Warning: Variables integrity - \"";
	 public static final String VARIABLES_MSG2_END = "\".";

	 public static final String CAMELCASE_MSG1_START = "Warning: CamelCase string(s) detected - \"";
	 public static final String CAMELCASE_MSG1_END = "\".";
	 
	 public static final String CAPITAL_MSG1_START = "Warning: Capital string(s) detected - \"";
	 public static final String CAPITAL_MSG1_END = "\".";
	 
	 public static final String DATE_TIME_FORMAT_MSG1_START = "Warning:  Date & Time format detected - \"";
	 public static final String DATE_TIME_FORMAT_MSG1_END = "\".";
	 
	 public static final String SPELLING_MSG1_START = "Warning: Unknown strings detected - \"";
	 public static final String SPELLING_MSG1_END = "\".\nSuggestion: ";
	 
	 public static final String LONG_SENTENCES_MSG1= "Warning: The string(s) or sentence(s) are too long.";
	 
	 public static final String SPECIAL_PATTERNS_MSG1= "Warning: Special pattern \"x,choice,\" detected. Please check how to handle the translation.";
	 public static final String SPECIAL_PATTERNS_MSG2= "Warning: Special pattern \"{count, plural,\" detected. Please check how to handle the translation.";
	 
	 public static final String PRODUCT_NAME_MSG1="Product is a mandatory field.";
	 public static final String PRODUCT_NAME_MSG2="Product field must not contain special characters of \"\\\",\"/\",\"<\",\">\"";
	 public static final String VERSION_MSG1="Version is a mandatory field.";
	 public static final String VERSION_MSG2="Version field must not contain special characters of \"\\\",\"/\",\"<\",\">\"";
	 public static final String SCOPE_MSG1="Checking scope not selected.";
	 public static final String FILE_MSG1="No source file(s) selected or entered.";
	 public static final String FILE_MSG2="The file entered is not a valid file format. Please enter a valid file and try again.";
	 public static final String FILE_MSG3_START="The file entered '";
	 public static final String FILE_MSG3_END="' does not exist. Please check and try again.";
	 public static final String FILE_FOLDER_MSG1="No output folder selected or entered. Please select or enter an output folder and try again.";
	 public static final String FILE_FOLDER_MSG2="The folder entered does not exist. Do you want to create it?";
	 public static final String CHECKPOINT_MSG1="At least one checkpoint must be selected. Please check and try again.";
	 public static final String PASSOLO_RUN_MSG1="Passolo is running. Please manually shut it down.";
	 public static final String PASSOLO_NO_LICENSE_MSG1="No Passolo license available. Please check if Passolo is already in run.";
	 public static final String NO_PRODUCT_MSG1="Go to Menu -> New to create a new PRODUCT";
	 public static final String CLOSE_CLICK="Source file checking is in progress, do you really want to cancel it?";
	 public static final String NOT_FILE="It is not file!";
	 public static final String REFRESH_MSG1="New product configuration created, please click \"Refresh\" to load it.";
	 public static final String UPDATE_MSG1="Update product config is success!";
	 public static final String IMPORT_MSG1="Please choose a excel file!";
	 public static final String IMPORT_MSG2_START="Totally ";
	 public static final String IMPORT_MSG2_MIND1=" comments are found, of which ";
	 public static final String IMPORT_MSG2_MIND2=" are marked \"invalid\" and ";
	 public static final String IMPORT_MSG2_END=" of which have been successfully imported to whitelist. Duplicated whitelist strings found after importing:";
	 public static final String FINISH_MSG="All files are finished!";
	 public static final String DELETE_MSG="Are you sure you want to delete this Whitelist? All contents will be removed!";
	 public static final String PRODUCT_FORMAT_MSG="product can not contains \"[\",\"]\",\"/\",\"\\\",\":\",\"*\",\"?\",\"<\",\">\",\"%\" and \"|\"";
}
