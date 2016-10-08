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
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation 根据传入的源数据和匹配目标检查
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月5日
 * @Time: 上午10:19:16
 *
 */
@RuleData(id="ConcatenationCheckRule",name=Constant.CONCATENATION,order=1,ruleClass = ConcatenationCheckRule.class)
public class ConcatenationCheckRule implements IRule{
	private static final String KEY_WORDS="psl.psl-generate-sourcechecker-report.concatenation.key-words";
	private static final String VARIABLES="psl.psl-generate-sourcechecker-report.concatenation.variables";
	private static final String CONCATENATION_WHITELIST="psl.source-checker-white-list.Concatenation";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> keywords;
	private List<String> variables;
	private List<String> whitelist;
	private List<ReportData> report =null;
	private Config config;

	public ConcatenationCheckRule(){

	}

	@Override
	public List<ReportData> gatherReport() {
		return report;
	}

	@Override
	public void setConfig(Config config) {
		this.config=config;
		keywords=this.config.getStringList(KEY_WORDS);
		variables=this.config.getStringList(VARIABLES);
		whitelist=this.config.getStringList(CONCATENATION_WHITELIST);
	}

	@Override
	public boolean check(List<InputData> lstIdo) {
		Preconditions.checkNotNull(keywords);
		Preconditions.checkNotNull(variables);
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
				log.debug("Start ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					for(String k : keywords) {
						if (ido.getSourceString().startsWith(k.trim().concat(" ")) 
								|| ido.getSourceString().endsWith(" ".concat(k.trim()))
								){
							hitStrCount++;
							int hs = hashSet.size();
							hashSet.add(ido.getSourceString());
							if(hs == hashSet.size()){
								duplicatedStringCount++;
								duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
							}else{
								validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
							}
							hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
							if(log.isDebugEnabled()){
								log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +" start or end with:+"+k);
							}
							flag = true;
							break;
						}
						if (StringUtil.pattern(ido.getSourceString(),RulePatternConstant.CONCATENATION_CHECK_RULE)
								&& ido.getSourceString().toLowerCase().equals(k)) {
							hitStrCount++;
							int hs = hashSet.size();
							hashSet.add(ido.getSourceString());
							if(hs == hashSet.size()){
								duplicatedStringCount++;
								duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: with the first letter in capital \""+ido.getSourceString()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
							}else{
								validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: with the first letter in capital \""+ido.getSourceString()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
							}
							hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
							flag = true;
							break;
						}
					}
					for(String v:variables){
						if (ido.getSourceString().contains(v.trim().toLowerCase())
								|| ido.getSourceString().contains(v.trim().toUpperCase())) {
							hitStrCount++;
							int hs = hashSet.size();
							hashSet.add(ido.getSourceString());
							if(hs == hashSet.size()){
								duplicatedStringCount++;
								duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: composed of variables \""+v.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
							}else{
								validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
								report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
										Constant.CONCATENATION,"Warning: composed of variables \""+v.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
							}
							hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
							flag = true;
							break;
						}
					}
				}
			}else{
				for(String k : keywords) {
					if (ido.getSourceString().startsWith(k.trim().concat(" ")) 
							|| ido.getSourceString().endsWith(" ".concat(k.trim()))
							){
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
						}
						if(log.isDebugEnabled()){
							log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +" start or end with:+"+k);
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						flag = true;
						break;
					}
					if (StringUtil.pattern(ido.getSourceString(),RulePatternConstant.CONCATENATION_CHECK_RULE)
							&& ido.getSourceString().toLowerCase().equals(k)) {
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: with the first letter in capital \""+ido.getSourceString()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: with the first letter in capital \""+ido.getSourceString()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						flag = true;
						break;
					}
				}
				for(String v:variables){
					if (ido.getSourceString().contains(v.trim().toLowerCase())
							|| ido.getSourceString().contains(v.trim().toUpperCase())) {
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: composed of variables \""+v.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.CONCATENATION,"Warning: composed of variables \""+v.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),false,null));
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						flag = true;
						break;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.CONCATENATION, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,false,reportDataCount));
		return flag;
	}
}
