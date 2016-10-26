package com.hpe.g11n.sourcechecker.core.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcechecker.core.IRule;
import com.hpe.g11n.sourcechecker.core.annotation.RuleData;
import com.hpe.g11n.sourcechecker.core.spellingcheck.jazzy.HashMapDictionarySpellCheck;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.pojo.ReportDataCount;
import com.hpe.g11n.sourcechecker.utils.ReportDataUtil;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
import com.typesafe.config.Config;

/**
 * 
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年9月12日
 * @Time: 上午9:46:40
 *
 */
@RuleData(id="SpellingCheckRule",name=Constant.SPELLING,order=9,ruleClass = SpellingCheckRule.class)
public class SpellingCheckRule implements IRule{
	private static final String KEY_WORDS = "psl.psl-generate-sourcechecker-report.date-time-format";
	private static final String SPELLING = "psl.psl-generate-sourcechecker-report.spelling";
	private static final String SPELLING_WHITELIST="psl.source-checker-white-list.Spelling";
	private List<String> keywords;
	private List<String> spelling;
	private List<String> whitelist;
	private Config config;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<ReportData> report =null;
	
	private HashMapDictionarySpellCheck spellingCheck = new HashMapDictionarySpellCheck();
	
	public SpellingCheckRule(){

	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
		keywords = this.config.getStringList(KEY_WORDS);
		spelling = this.config.getStringList(SPELLING);
		whitelist = this.config.getStringList(SPELLING_WHITELIST);
	}
	
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	@Override
	public boolean check(List<InputData> lstIdo) {
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<ReportData>();
		HashSet<String> hashSet = new HashSet<String>();
		int hitStrCount=0;
		int totalWordCount=0;
		int hitNewChangeWordCount =0;
		int duplicatedStringCount =0;
		int duplicatedWordCount =0;
		int validatedWordCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start SpellingCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(!StringUtil.untranstlatable(ido.getSourceString())
				    && !StringUtil.haveTag(ido.getSourceString())){
				if(whitelist !=null && whitelist.size()>0){
					if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
						String[] words = StringUtil.getWordsFromString(ido.getSourceString());
						String wrongWords="";
						String suggestion="";
						for(String word:words){
							word = StringUtil.getStringWithChar(word);
							if(!isPass(word) && StringUtil.isRightWord(word) && !spellingCheck.isCorrect(word)){
								if(!isSpecialWord(word)){
									if(!word.contains("-")){
										wrongWords = wrongWords + word + ";";
										suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(word) + ";";
									}else{
										String[] newWords = word.split("-");
										wrongWords = wrongWords + word + ";";
										if(spellingCheck.isCorrect(newWords[0]) && !spellingCheck.isCorrect(newWords[1])){
											suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[1]) + ";";	
										}else if(!spellingCheck.isCorrect(newWords[0]) && spellingCheck.isCorrect(newWords[1])){
											suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[0]) + ";";
										}else if(!spellingCheck.isCorrect(newWords[0]) && !spellingCheck.isCorrect(newWords[1])){
											suggestion = suggestion + "NA;";
										}
									}
									
								}else{
									if(word.contains("-")){
										String[] newWords = word.split("-");
										if(!spellingCheck.isCorrect(newWords[1])){
											wrongWords = wrongWords + word + ";";
											suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[1]) + ";";	
										}
									}else{
										String newWord="";
										for(String s:spelling){
											if(word.toLowerCase().startsWith(s)){
												newWord = word.toLowerCase().replace(s,"");
												if(!spellingCheck.isCorrect(newWord)){
													wrongWords = wrongWords + word + ";";
													suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWord) + ";";
												}
											}
										}
									}
								}
							}
						}
						if (wrongWords !=null && !"".equals(wrongWords)) {
							hitStrCount++;
							int hs = hashSet.size();
							hashSet.add(ido.getSourceString());
							if(hs == hashSet.size()){
								duplicatedStringCount++;
								duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							}else{
								validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							}
							hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.SPELLING,"Warning:unknown strings \"" + wrongWords.trim().substring(0,wrongWords.length()-1) 
									+ "\".\n Suggestion:"+suggestion.trim().substring(0,suggestion.length()-1) ,ido.getFileVersion(),null));
							flag = true;
						}
					}
				}else{
					String[] words = StringUtil.getWordsFromString(ido.getSourceString());
					String wrongWords="";
					String suggestion="";
					for(String word:words){
						word = StringUtil.getStringWithChar(word);
						if(!isPass(word) && StringUtil.isRightWord(word) && !spellingCheck.isCorrect(word)){
							if(!isSpecialWord(word)){
								if(!word.contains("-")){
									wrongWords = wrongWords + word + ";";
									suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(word) + ";";
								}else{
									String[] newWords = word.split("-");
									wrongWords = wrongWords + word + ";";
									if(spellingCheck.isCorrect(newWords[0]) && !spellingCheck.isCorrect(newWords[1])){
										suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[1]) + ";";	
									}else if(!spellingCheck.isCorrect(newWords[0]) && spellingCheck.isCorrect(newWords[1])){
										suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[0]) + ";";
									}else if(!spellingCheck.isCorrect(newWords[0]) && !spellingCheck.isCorrect(newWords[1])){
										suggestion = suggestion + "NA;";
									}
								}
								
							}else{
								if(word.contains("-")){
									String[] newWords = word.split("-");
									if(!spellingCheck.isCorrect(newWords[1])){
										wrongWords = wrongWords + word + ";";
										suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWords[1]) + ";";	
									}
								}else{
									String newWord="";
									for(String s:spelling){
										if(word.toLowerCase().startsWith(s)){
											newWord = word.toLowerCase().replace(s,"");
											if(!spellingCheck.isCorrect(newWord)){
												wrongWords = wrongWords + word + ";";
												suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(newWord) + ";";
											}
										}
									}
								}
							}
						}
					}
					if (wrongWords !=null && !"".equals(wrongWords)) {
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
								Constant.SPELLING,"Warning:unknown strings \"" + wrongWords.trim().substring(0,wrongWords.length()-1) 
								+ "\".\n Suggestion:"+suggestion.trim().substring(0,suggestion.length()-1) ,ido.getFileVersion(),null));
						flag = true;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END SpellingCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.SPELLING, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	private boolean checkDateFormat(String string){
		for(String k : keywords) {
			if (string.contains(k.trim())){
				return true;
			}
		}
		return false;
	}
	
	private boolean isPass(String word){
		if(StringUtil.pattern(word.trim(),RulePatternConstant.CAPITAL_CHECK_RULE)
				||(StringUtil.pattern(word.trim(),RulePatternConstant.CAMEL_CASE_CHECK_RULE))
				||(StringUtil.pattern(word.trim(),RulePatternConstant.STRINGMIXED_1)
						||StringUtil.pattern(word.trim(),RulePatternConstant.STRINGMIXED_2))
			    || StringUtil.pattern(word.trim(), RulePatternConstant.NUMBER)
			    || checkDateFormat(word.trim())
			    ){
			return true;
		}
		return false;
	}
	
	/**
	 * check word is or not a special word
	 * @param word
	 * @return
	 */
	private boolean isSpecialWord(String word){
		word =  word.toLowerCase();
		boolean flag =false;
		if(word.contains("-")){
			for(String s:spelling){
				if(word.split("-")[0].equals(s)){
					flag =true;
					return flag;
				}
			}
		}else{
			for(String s:spelling){
				if(word.startsWith(s)){
					flag =true;
					return flag;
				}
			}
		}
		return flag;
	}
}
