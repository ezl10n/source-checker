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

@RuleData(id="BracketCheckRule",name=Constant.BRACKET,order=11,ruleClass = BracketCheckRule.class)
public class BracketCheckRule implements IRule{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String BRACKET_WHITELIST="psl.source-checker-white-list.Bracket";
	private List<ReportData> report =null;
	private List<String> whitelist;
	private Config config;
	
	public BracketCheckRule(){

	}
	@Override
	public List<ReportData> gatherReport() {
		return report;
	}
	@Override
	public void setConfig(Config config) {
		this.config=config;
		whitelist=this.config.getStringList(BRACKET_WHITELIST);
	}
	@Override
	public boolean check(List<InputData> lstIdo) {
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
				log.debug("Start BracketCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
			totalWordCount = totalWordCount + StringUtil.getCountWords(ido.getSourceString());
			if(whitelist !=null && whitelist.size()>0){
				if(!StringUtil.isWhiteList(whitelist,ido.getSourceString())){
					byte[] bytes  = ido.getSourceString().getBytes();
					int count_1 =0;//counting <
					int count_2 =0;//counting >
					int count_3 =0;//counting {
					int count_4 =0;//counting }
					int count_5 =0;//counting [
					int count_6 =0;//counting [
					int last=0;
				    for(byte b:bytes){
				    	if(b==60){
				    		count_1++;
				    	}
				    	//filter "'>'" and "->"
				    	if(last !=39 && last !=45 && b==62){
				    		count_2++;
				    	}
				    	last = b;
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
				    String info=getInfo(count_1,count_2,count_3,count_4,count_5,count_6);
					if (!info.equals("")) {
						hitStrCount++;
						int hs = hashSet.size();
						hashSet.add(ido.getSourceString());
						if(hs == hashSet.size()){
							duplicatedStringCount++;
							duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.BRACKET,"Warning:"+info+".",ido.getFileVersion(),true,null));
						}else{
							validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
							report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
									Constant.BRACKET,"Warning:"+info+".",ido.getFileVersion(),false,null));
						}
						hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
						flag = true;
					}
				}
			}else{
				byte[] bytes  = ido.getSourceString().getBytes();
				int count_1 =0;//counting <
				int count_2 =0;//counting >
				int count_3 =0;//counting {
				int count_4 =0;//counting }
				int count_5 =0;//counting [
				int count_6 =0;//counting [
				int last=0;
			    for(byte b:bytes){
			    	if(b==60){
			    		count_1++;
			    	}
			    	//filter "'>'" and "->"
			    	if(last !=39 && last !=45 && b==62){
			    		count_2++;
			    	}
			    	last = b;
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
			    String info=getInfo(count_1,count_2,count_3,count_4,count_5,count_6);
				if (!info.equals("")) {
					hitStrCount++;
					int hs = hashSet.size();
					hashSet.add(ido.getSourceString());
					if(hs == hashSet.size()){
						duplicatedStringCount++;
						duplicatedWordCount = duplicatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
								Constant.BRACKET,"Warning:"+info+".",ido.getFileVersion(),true,null));
					}else{
						validatedWordCount = validatedWordCount + StringUtil.getCountWords(ido.getSourceString());
						report.add(new ReportData(ido.getLpuName(),ido.getFileName(),ido.getStringId(), ido.getSourceString(),
								Constant.BRACKET,"Warning:"+info+".",ido.getFileVersion(),false,null));
					}
					hitNewChangeWordCount = hitNewChangeWordCount + StringUtil.getCountWords(ido.getSourceString());
					flag = true;
				}
			}
			if(log.isDebugEnabled()){
				log.debug("END BracketCheckRule check key/value:"+ido.getStringId()+"/"+ido.getSourceString());
			}
		}
		ReportDataUtil reportDataUtil = new ReportDataUtil();
		ReportDataCount reportDataCount = reportDataUtil.getEndReportData(Constant.BRACKET, hitStrCount,hitNewChangeWordCount,
				duplicatedStringCount,duplicatedWordCount, hashSet.size(),validatedWordCount,lstIdo.size(), totalWordCount,new BigDecimal(0));
		report.add(new ReportData(null,null,null,null,null,null,null,false,reportDataCount));
		return flag;
	}

	private String getInfo(int count_1,int count_2,int count_3,int count_4,int count_5,int count_6){
		String info="";
		if(count_1>count_2){
			info = info + "Missing close '>' and ";
		}
		if(count_3>count_4){
			info = info + "Missing close '}' and ";
		}
		if(count_5>count_6){
			info = info + "Missing close ']' and ";
		}
		if(count_2>count_1){
			info = info + "Missing open '<' and ";
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
