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
 * @Descripation 根据传入的源数据和匹配目标检查
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月5日
 * @Time: 上午10:19:16
 *
 */
@RuleData(id="ConcatenationCheckRule",name=Constant.CONCATENATION,order=1,ruleClass = ConcatenationCheckRule.class)
public class ConcatenationCheckRule implements IRule{
	private static final String KEY_WORDS="psl.psl-generate-sourcescoring-report.concatenation.key-words";
	private static final String VARIABLES="psl.psl-generate-sourcescoring-report.concatenation.variables";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> keywords;
	private List<String> variables;
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
		int totalNCCount=0;
		int hitNCCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalNCCount = totalNCCount + StringUtil.getCountWords(ido.getSourceString());
			for(String k : keywords) {
				if (ido.getSourceString().startsWith(k.trim().concat(" ")) 
						|| ido.getSourceString().endsWith(" ".concat(k.trim()))
						){
					hitStrCount++;
					hashSet.add(ido.getSourceString());
					hitNCCount = hitNCCount + StringUtil.getCountWords(ido.getSourceString());
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
							Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),null));
					if(log.isDebugEnabled()){
						log.debug("ConcatenationCheckRule, value:"+ ido.getSourceString() +" start or end with:+"+k);
					}
					flag = true;
					break;
				}
				if ((ido.getSourceString().trim().hashCode()-32==k.trim().hashCode()) 
						&& pattern(ido.getSourceString(),"^[A-Z].*$")) {
					hitStrCount++;
					hashSet.add(ido.getSourceString());
					hitNCCount = hitNCCount + StringUtil.getCountWords(ido.getSourceString());
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
							Constant.CONCATENATION,"Warning: with the first letter in capital \""+ido.getSourceString()+"\". Possible concatenated strings.",ido.getFileVersion(),null));
					flag = true;
					break;
				}
			}
			for(String v:variables){
				if (ido.getSourceString().contains(v.trim().toLowerCase())
						|| ido.getSourceString().contains(v.trim().toUpperCase())) {
					hitStrCount++;
					hashSet.add(ido.getSourceString());
					hitNCCount = hitNCCount + StringUtil.getCountWords(ido.getSourceString());
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
							Constant.CONCATENATION,"Warning: composed of variables \""+v.trim()+"\". Possible concatenated strings.",ido.getFileVersion(),null));
					flag = true;
					break;
				}
			}
			
			if(log.isDebugEnabled()){
				log.debug("END ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.CONCATENATION, hitStrCount, hashSet.size(), totalNCCount, hitNCCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}

	private boolean pattern(String source,String rule){
		Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
	}
}
