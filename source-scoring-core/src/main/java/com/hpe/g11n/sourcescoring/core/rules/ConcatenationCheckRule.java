package com.hpe.g11n.sourcescoring.core.rules;


import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> keywords;
	private final List<ReportData> report = new ArrayList<>();
	private Config config;

	public ConcatenationCheckRule(){

	}

	@Override
	public boolean check(String key, String value) {
		Preconditions.checkNotNull(keywords);
		Preconditions.checkNotNull(value);
		if(log.isDebugEnabled()){
			log.debug("Start ConcatenationCheckRule check key/value:"+key+"/"+value);
		}

		for(String v : keywords) {
			if (value.startsWith(v) || value.endsWith(v)) {
				report.add(new ReportData("XXX.LPU","file name",key, value,"errortype",""));
				if(log.isDebugEnabled()){
					log.debug("ConcatenationCheckRule, value:"+ value +" start or end with:+"+v);
				}
				return true;
			}
		}
		if(log.isDebugEnabled()){
			log.debug("END ConcatenationCheckRule check key/value:"+key+"/"+value);
		}
		return false;
	}

	@Override
	public List<ReportData> gatherReport() {
		return report;
	}

	@Override
	public void setConfig(Config config) {
		this.config=config;
		keywords=this.config.getStringList(KEY_WORDS);
	}

}
