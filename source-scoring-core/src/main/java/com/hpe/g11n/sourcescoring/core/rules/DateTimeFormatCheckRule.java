package com.hpe.g11n.sourcescoring.core.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.hpe.g11n.sourcescoring.utils.ReportDataUtil;
import com.hpe.g11n.sourcescoring.utils.StringUtil;
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
	private static final String KEY_WORDS = "psl.psl-generate-sourcescoring-report.date-time-format";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> keywords;
	private List<ReportData> report = null;
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
	}

	@Override
	public boolean check(List<InputData> lstIdo) {

		Preconditions.checkNotNull(keywords);
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<ReportData>();
		HashSet<String> hashSet = new HashSet<String>();
		int hitStrCount=0;
		int totalNCCount=0;
		int hitNCCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start DateTimeFormatCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalNCCount = totalNCCount + StringUtil.getCountWords(ido.getSourceString());
			for(String k : keywords) {
				if (ido.getSourceString().contains(k.trim())){
					hitStrCount++;
					hashSet.add(ido.getSourceString());
					hitNCCount = hitNCCount + StringUtil.getCountWords(ido.getSourceString());
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
							Constant.DATETIMEFORMAT,"Warning:date & time format keyword \""+k.trim()+"\" detected.",ido.getFileVersion(),null));
					if(log.isDebugEnabled()){
						log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +"detected.");
					}
					flag = true;
					break;
				}
			}
			
			if(log.isDebugEnabled()){
				log.debug("END DateTimeFormatCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.DATETIMEFORMAT, hitStrCount, hashSet.size(), totalNCCount, hitNCCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	
	}

}
