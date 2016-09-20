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

	private List<String> keywords;
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
			boolean on =false;
			if(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.CAPITAL_CHECK_RULE)
					||(!StringUtil.getStringWithChar(ido.getSourceString().trim()).contains(" ")
							&& (StringUtil.pattern(ido.getSourceString().trim(),RulePatternConstant.CAMEL_CASE_CHECK_RULE)))
					||(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.STRINGMIXED_1)
							||StringUtil.pattern(ido.getSourceString(),RulePatternConstant.STRINGMIXED_2))
				    ||(StringUtil.pattern(ido.getSourceString(), RulePatternConstant.NUMBER))
				    ||(checkDateFormat(ido))){
				on = true;
			}
			if(!on){
				String[] words = StringUtil.getWordsFromString(StringUtil.getStringWithNoPunctuation(ido.getSourceString()));
				String wrongWords="";
				String suggestion="";
				for(String word:words){
					word = StringUtil.getStringWithChar(word);
					word = word.trim();
					if(!"".equals(word) && !spellingCheck.isCorrect(word)){
						wrongWords = wrongWords + word + ";";
						suggestion = suggestion + spellingCheck.getSuggestionsLessThanThree(word) + ";";
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
							Constant.SPELLING,"Warning:unknown strings \"" + wrongWords.trim() + "\".\n Suggestion:"+suggestion ,ido.getFileVersion(),null));
					flag = true;
				}
				if(log.isDebugEnabled()){
					log.debug("END SpellingCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
				}
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.SPELLING, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	public boolean checkDateFormat(InputData ido){
		for(String k : keywords) {
			if (ido.getSourceString().contains(k.trim())){
				return true;
			}
		}
		return false;
	}
}
