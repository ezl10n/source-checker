package com.hpe.g11n.sourcescoring.core.rules;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @Date: 2016年8月30日
 * @Time: 上午9:54:54
 *
 */
@RuleData(id="CamelCaseCheckRule",name=Constant.CAMELCASE,order=2,ruleClass = CamelCaseCheckRule.class)
public class CamelCaseCheckRule implements IRule{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<ReportData> report =null;
	
	public CamelCaseCheckRule(){

	}
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	
	@Override
	public void setConfig(Config config) {

	}

	@Override
	public boolean check(List<InputData> lstIdo) {
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<ReportData>();
		HashSet<String> hashSet = new HashSet<String>();
		int hitStrCount=0;
		int totalNCCount=0;
		int hitNCCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start CamelCaseCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalNCCount = totalNCCount + StringUtil.getCountWords(ido.getSourceString());
			if (!StringUtil.getStringWithChar(ido.getSourceString().trim()).contains(" ")
					&& (Pattern.matches("^([A-Z]+[a-z]+[A-Za-z]*){2,}$", ido.getSourceString().trim()) 
							|| Pattern.matches("^([a-z]+[A-Z]+[A-Za-z]*){2,}$", ido.getSourceString().trim()))) {
				hitStrCount++;
				hashSet.add(ido.getSourceString());
				hitNCCount = hitNCCount + StringUtil.getCountWords(ido.getSourceString());
				report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
						Constant.CAMELCASE,"Warning:camelcase \"" + ido.getSourceString() +"\" detected.",ido.getFileVersion(),null));
				flag = true;
			}
			
			if(log.isDebugEnabled()){
				log.debug("END LongSentencesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.CAMELCASE, hitStrCount, hashSet.size(), totalNCCount, hitNCCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	private boolean pattern(String source,String rule){
		Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
	}

}
