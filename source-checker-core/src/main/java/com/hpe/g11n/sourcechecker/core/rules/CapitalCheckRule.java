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
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月29日
 * @Time: 下午1:40:09
 *
 */
@RuleData(id="CapitalCheckRule",name=Constant.CAPITAL,order=5,ruleClass = CapitalCheckRule.class)
public class CapitalCheckRule implements IRule{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String KEY_WORDS = "psl.psl-generate-sourcechecker-report.date-time-format";
	private static final String CAPITAL_WHITELIST="psl.source-checker-white-list.Capital";
	private List<ReportData> report =null;
	private List<String> keywords;
	private Config config;
	private Config productConfig;
	private HashMapDictionarySpellCheck spellingCheck = new HashMapDictionarySpellCheck();
	
	public CapitalCheckRule(){

	}
	
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	
	@Override
	public void setConfig(Config config) {
		this.config=config;
		keywords = this.config.getStringList(KEY_WORDS);
	}
	
	@Override
	public boolean check(List<InputData> lstIdo,String product) {
		List<String> whitelist=this.config.getStringList(CAPITAL_WHITELIST);
		productConfig = StringUtil.loadConfig(product);
		if(!productConfig.isEmpty()){
			List<String> projectWhitelist = productConfig.getStringList(Constant.CAPITAL_PATH);
			whitelist.addAll(projectWhitelist);
		}
		whitelist = StringUtil.getUniqueList(whitelist);
		Preconditions.checkNotNull(lstIdo);
		Preconditions.checkNotNull(whitelist);
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
				log.debug("Start CapitalCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					if (StringUtil.pattern(ido.getSourceString(),RulePatternConstant.CAPITAL_CHECK_RULE)
							&& ido.getSourceString().trim().length()>1) {
						String sourceString = getNoDateFormatString(ido.getSourceString());
						String[] words = StringUtil.getWordsFromString(sourceString);
						boolean result = false;
						for(String word:words){
							word = StringUtil.getStringWithChar(word);
							if(StringUtil.isRightWord(word) && !spellingCheck.isInDictionary(word)){
								result = true;
								break;
							}
						}
						if(result){
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
									Constant.CAPITAL,MessageConstant.CAPITAL_MSG1_START + ido.getSourceString() + MessageConstant.CAPITAL_MSG1_END,
									ido.getFileVersion(),null));
							flag = true;
						}
					}
				}
			}else{
				if (StringUtil.pattern(ido.getSourceString(),RulePatternConstant.CAPITAL_CHECK_RULE)
						&& ido.getSourceString().trim().length()>1) {
					String sourceString = getNoDateFormatString(ido.getSourceString());
					String[] words = StringUtil.getWordsFromString(sourceString);
					boolean result = false;
					for(String word:words){
						word = StringUtil.getStringWithChar(word);
						if(StringUtil.isRightWord(word) && !spellingCheck.isInDictionary(word)){
							result = true;
							break;
						}
					}
					if(result){
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
								Constant.CAPITAL,MessageConstant.CAPITAL_MSG1_START + ido.getSourceString() + MessageConstant.CAPITAL_MSG1_END,
								ido.getFileVersion(),null));
						flag = true;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END CapitalCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.CAPITAL, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	private String getNoDateFormatString(String string){
		for(String k : keywords) {
			if (string.contains(k.trim())){
				string = string.replaceAll(k.trim(), "");
			}
		}
		return string;
	}
}
