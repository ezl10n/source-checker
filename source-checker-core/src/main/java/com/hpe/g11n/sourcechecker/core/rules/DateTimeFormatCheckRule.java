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
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.pojo.ReportDataCount;
import com.hpe.g11n.sourcechecker.utils.ReportDataUtil;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月29日
 * @Time: 上午9:05:20
 *
 */
@RuleData(id = "DateTimeFormatCheckRule", name = Constant.DATETIMEFORMAT, order = 3, ruleClass = DateTimeFormatCheckRule.class)
public class DateTimeFormatCheckRule implements IRule {
	private static final String KEY_WORDS = "psl.psl-generate-sourcechecker-report.date-time-format";
	private static final String DATETIMEFORMAT_WHITELIST="psl.source-checker-white-list.DateTimeFormat";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> keywords;
	private List<ReportData> report = null;
	private List<String> whitelist;
	private Config config;

	public DateTimeFormatCheckRule() {

	}

	@Override
	public List<ReportData> gatherReport() {
		return report;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
		keywords = this.config.getStringList(KEY_WORDS);
		whitelist = this.config.getStringList(DATETIMEFORMAT_WHITELIST);
	}

	@Override
	public boolean check(List<InputData> lstIdo) {

		Preconditions.checkNotNull(keywords);
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
				log.debug("Start DateTimeFormatCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					for(String k : keywords) {
						if (ido.getSourceString().contains(k.trim())){
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
									Constant.DATETIMEFORMAT,"Warning:date & time format keyword \""+k.trim()+"\" detected.",ido.getFileVersion(),null));
							if(log.isDebugEnabled()){
								log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +"detected.");
							}
							flag = true;
							break;
						}
					}
				}
			}else{
				for(String k : keywords) {
					if (ido.getSourceString().contains(k.trim())){
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
								Constant.DATETIMEFORMAT,"Warning:date & time format keyword \""+k.trim()+"\" detected.",ido.getFileVersion(),null));
						if(log.isDebugEnabled()){
							log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +"detected.");
						}
						flag = true;
						break;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END DateTimeFormatCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.DATETIMEFORMAT, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	
	}

}
