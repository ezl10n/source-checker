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
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月30日
 * @Time: 上午9:54:54
 *
 */
@RuleData(id="CamelCaseCheckRule",name=Constant.CAMELCASE,order=2,ruleClass = CamelCaseCheckRule.class)
public class CamelCaseCheckRule implements IRule{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String CAMELCASE_WHITELIST="psl.source-checker-white-list.CamelCase";
	private List<ReportData> report =null;
	private List<String> whitelist;
	private Config config;
	private Config projectConfig;
	private List<String> projectWhitelist;

	public CamelCaseCheckRule(){

	}
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	
	@Override
	public void setConfig(Config config) {
		this.config=config;
		whitelist=this.config.getStringList(CAMELCASE_WHITELIST);
	}

	@Override
	public boolean check(List<InputData> lstIdo,String projectName) {
		projectConfig = StringUtil.loadConfig(projectName);
		projectWhitelist = projectConfig.getStringList(Constant.CAMELCASE_PATH);
		whitelist.addAll(projectWhitelist);
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
				log.debug("Start CamelCaseCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					if (!StringUtil.getStringWithChar(ido.getSourceString().trim()).contains(" ")
							&& (StringUtil.pattern(ido.getSourceString().trim(),RulePatternConstant.CAMEL_CASE_CHECK_RULE))) {
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
								Constant.CAMELCASE,MessageConstant.CAMELCASE_MSG1_START + ido.getSourceString() + MessageConstant.CAMELCASE_MSG1_END,
								ido.getFileVersion(),null));
						flag = true;
					}
				}
			}else{
				if (!StringUtil.getStringWithChar(ido.getSourceString().trim()).contains(" ")
						&& (StringUtil.pattern(ido.getSourceString().trim(),RulePatternConstant.CAMEL_CASE_CHECK_RULE))) {
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
							Constant.CAMELCASE,MessageConstant.CAMELCASE_MSG1_START + ido.getSourceString() + MessageConstant.CAMELCASE_MSG1_END,
							ido.getFileVersion(),null));
					flag = true;
				}
			}
			
			
			if(log.isDebugEnabled()){
				log.debug("END LongSentencesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.CAMELCASE, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), 
				totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
}
