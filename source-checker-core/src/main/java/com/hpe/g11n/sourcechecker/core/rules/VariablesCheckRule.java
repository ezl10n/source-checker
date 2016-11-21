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
 * @Date: 2016年8月16日
 * @Time: 上午11:09:30
 *
 */
@RuleData(id="VariablesCheckRule",name=Constant.VARIABLES,order=4,ruleClass = VariablesCheckRule.class)
public class VariablesCheckRule implements IRule{

	private static final String VARIABLES="psl.psl-generate-sourcechecker-report.many-variables";
	private static final String VARIABLES_WHITELIST="psl.source-checker-white-list.Variables";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> variables;
	private List<ReportData> report =null;
	private Config config;

	public VariablesCheckRule(){

	}

	@Override
	public List<ReportData> gatherReport() {
		return report;
	}

	@Override
	public void setConfig(Config config) {
		this.config=config;
		variables=this.config.getStringList(VARIABLES);
	}

	@Override
	public boolean check(List<InputData> lstIdo,String projectName) {
		List<String> whitelist = config.getStringList(VARIABLES_WHITELIST);
		Preconditions.checkNotNull(variables);
		Preconditions.checkNotNull(lstIdo);
		boolean flag = false;
		report = new ArrayList<>();
		HashSet<String> hashSet = new HashSet<String>();
		int hitStrCount=0;
		int totalWordCount=0;
		int hitNewChangeWordCount =0;
		int duplicatedStringCount =0;
		int duplicatedWordCount =0;
		int validatedWordCount =0;
		for(InputData ido:lstIdo){
			if(log.isDebugEnabled()){
				log.debug("Start VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					if(!isPass(ido.getSourceString())){
						int wordsCount =StringUtil.getCountWords(ido.getSourceString());
						int variablesCount =0;
						String[] sourceStrings = ido.getSourceString().split(" ");
						for(String ss:sourceStrings){
							//check {0,xxx,xxx}
							for(String v:variables){
								if(StringUtil.pattern(ss,RulePatternConstant.VARIABLES_CHECK_RULE_2) || ss.equals(v.trim())){
									variablesCount = variablesCount + 1;
									break;
								}
							}
						}
						if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
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
									Constant.VARIABLES,MessageConstant.VARIABLES_MSG1,ido.getFileVersion(),null));
						}
					}
					if(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.VARIABLES_CHECK_RULE_1)){
						byte[] bytes  = ido.getSourceString().getBytes();
						int count_3 =0;//counting {
						int count_4 =0;//counting }
						int count_5 =0;//counting [
						int count_6 =0;//counting [
					    for(byte b:bytes){
					    	if(b==123){
					    		count_3++;
					    	}
					    	if(b==125){
					    		count_4++;
					    	}
					    	if(b==91){
					    		count_5++;
					    	}
					    	if(b==93){
					    		count_6++;
					    	}
					    }
					    String info=getInfo(count_3,count_4,count_5,count_6);
						if (!info.equals("")) {
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
									Constant.VARIABLES,MessageConstant.VARIABLES_MSG2_START + info + MessageConstant.VARIABLES_MSG2_END,
									ido.getFileVersion(),null));
							flag = true;
						}
					}
				}
			}else{
				if(!isPass(ido.getSourceString())){
					int wordsCount =StringUtil.getCountWords(ido.getSourceString());
					int variablesCount =0;
					String[] sourceStrings = ido.getSourceString().split(" ");
					for(String ss:sourceStrings){
						//check {0,xxx,xxx}
						for(String v:variables){
							if(StringUtil.pattern(ss,RulePatternConstant.VARIABLES_CHECK_RULE_2) || ss.equals(v.trim())){
								variablesCount = variablesCount + 1;
								break;
							}
						}
					}
					if(new Float(variablesCount)/new Float(wordsCount) >new Float(0.5)){
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
								Constant.VARIABLES,MessageConstant.VARIABLES_MSG1,ido.getFileVersion(),null));
					}
				}
				if(StringUtil.pattern(ido.getSourceString(),RulePatternConstant.VARIABLES_CHECK_RULE_1)){
					byte[] bytes  = ido.getSourceString().getBytes();
					int count_3 =0;//counting {
					int count_4 =0;//counting }
					int count_5 =0;//counting [
					int count_6 =0;//counting [
				    for(byte b:bytes){
				    	if(b==123){
				    		count_3++;
				    	}
				    	if(b==125){
				    		count_4++;
				    	}
				    	if(b==91){
				    		count_5++;
				    	}
				    	if(b==93){
				    		count_6++;
				    	}
				    }
				    String info=getInfo(count_3,count_4,count_5,count_6);
					if (!info.equals("")) {
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
								Constant.VARIABLES,MessageConstant.VARIABLES_MSG2_START + info + MessageConstant.VARIABLES_MSG2_END,
								ido.getFileVersion(),null));
						flag = true;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END VariablesCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.VARIABLES, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,reportDataCount));
		return flag;
	}
	
	/**
	 * check the string is needed or not to checking
	 * @param sourceString
	 * @return
	 */
	public boolean isPass(String sourceString){
		boolean flag = false;
		String[] sourceStrings = sourceString.split(" ");
		if(sourceStrings.length==1){
			flag = true;
		}else{
			int count =0;
			for(String s:sourceStrings){
				for(String v:variables){
					if(!s.equals(v) && !s.equals("and") && !s.equals("or")){
						count ++;
					}
				}
			}
			if(count==0){
				flag = true;
			}
		}
		return flag;
	}
	
	private String getInfo(int count_3,int count_4,int count_5,int count_6){
		String info="";
		if(count_3>count_4){
			info = info + "Missing close '}' and ";
		}
		if(count_5>count_6){
			info = info + "Missing close ']' and ";
		}
		if(count_4>count_3){
			info = info + "Missing open '{' and ";
		}
		if(count_6>count_5){
			info = info + "Missing open '[' and ";
		}
		if(!"".equals(info)){
			int index =info.lastIndexOf("and");
			return info.substring(0,index);
		}else{
			return info;
		}
	}
}
