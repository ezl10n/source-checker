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

@RuleData(id="SpecialPatternsCheckRule",name=Constant.SPECIALPATTERNS,order=11,ruleClass = SpecialPatternsCheckRule.class)
public class SpecialPatternsCheckRule implements IRule{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private List<ReportData> report =null;
	
	boolean flag = false;
	
	public SpecialPatternsCheckRule(){

	}
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	@Override
	public void setConfig(Config config) {
	}
	@Override
	public boolean check(List<InputData> lstIdo,String product) {
		Preconditions.checkNotNull(lstIdo);
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
				log.debug("Start SpecialPatternsCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(StringUtil.pattern(ido.getSourceString(), RulePatternConstant.SPECIAL_PATTERNS_CHECK_RULE_0)
					&& StringUtil.pattern(ido.getSourceString(), RulePatternConstant.SPECIAL_PATTERNS_CHECK_RULE_1)){
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
						Constant.SPECIALPATTERNS,MessageConstant.SPECIAL_PATTERNS_MSG1,ido.getFileVersion(),null));
				flag = true;
			}
			if(StringUtil.pattern(ido.getSourceString(), RulePatternConstant.SPECIAL_PATTERNS_CHECK_RULE_2)){
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
						Constant.SPECIALPATTERNS,MessageConstant.SPECIAL_PATTERNS_MSG2,ido.getFileVersion(),null));
				flag = true;
			}
			if(log.isDebugEnabled()){
				log.debug("END SpecialPatternsCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.SPECIALPATTERNS, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
}
