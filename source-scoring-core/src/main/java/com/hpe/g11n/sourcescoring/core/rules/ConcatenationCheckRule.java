package com.hpe.g11n.sourcescoring.core.rules;


import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.IRule;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
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
	public boolean check(List<InputDataObj> lstIdo) {
		Preconditions.checkNotNull(keywords);
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<>();
		for(InputDataObj ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
			for(String k : keywords) {
				if (ido.getSourceStrings().startsWith(k.trim().concat(" ")) 
						|| ido.getSourceStrings().endsWith(" ".concat(k.trim()))
						){
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
							Constant.CONCATENATION,"Warning: starting or ending with keyword \""+k+"\". Possible concatenated strings."));
					if(log.isDebugEnabled()){
						log.debug("ConcatenationCheckRule, value:"+ ido.getSourceStrings() +" start or end with:+"+k);
					}
					flag = true;
				}
				
				if (ido.getSourceStrings().toLowerCase().equals(k.trim().toLowerCase())
						&& ido.getSourceStrings().hashCode()-32 ==k.trim().hashCode()) {
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
							Constant.CONCATENATION,""));
					flag = true;
				}
			}
			for(String v:variables){
				if (ido.getSourceStrings().contains(v.trim().toLowerCase())
						|| ido.getSourceStrings().contains(v.trim().toUpperCase())) {
					report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceStrings(),
							Constant.CONCATENATION,""));
					flag = true;
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END ConcatenationCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceStrings());
			}
		}
		return flag;
	}

}
