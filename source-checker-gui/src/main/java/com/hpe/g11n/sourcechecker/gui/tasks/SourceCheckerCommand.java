package com.hpe.g11n.sourcechecker.gui.tasks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hpe.g11n.sourcechecker.core.ISourceChecker;
import com.hpe.g11n.sourcechecker.fileparser.IFileParser;
import com.hpe.g11n.sourcechecker.pojo.Excel;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.pojo.ReportDataCount;
import com.hpe.g11n.sourcechecker.pojo.SourceChecker;
import com.hpe.g11n.sourcechecker.pojo.Summary;
import com.hpe.g11n.sourcechecker.utils.DateUtil;
import com.hpe.g11n.sourcechecker.utils.ExcelPoiUtils;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.hpe.g11n.sourcechecker.xml.XMLHandler;

/**
 * 
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年9月7日
 * @Time: 下午3:07:25
 *
 */
public class SourceCheckerCommand{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String projectName;
	private String projectVersion;
	private String state;
	private String source;
	private String report;
	private List<Integer> rulesCheckedIdx;
	@Inject
	InputData ido;
	@Inject
	ISourceChecker checkReport;
	@Inject
	IFileParser fileParser;
	int totalProgress = 0;
	int totalCount = 0;

	public void setUp(String projectName, String projectVersion,String state,
			String sourceDir, String reportDir,List<Integer> rulesCheckedIdx) {
		this.projectName = projectName;
		this.projectVersion = projectVersion;
		this.state = state;
		this.source = sourceDir;
		this.report = reportDir;
		this.rulesCheckedIdx = rulesCheckedIdx;
		checkReport.build(rulesCheckedIdx);
	}

	public void call() throws Exception {
		// output
		SourceChecker sourceChecker = new SourceChecker();
		sourceChecker.setProductVersion(Constant.PRODUCT_VERSION);
		
		List<InputData> lstIdo = new ArrayList<InputData>();
		DateUtil dateUtil = new DateUtil();
		Date startScanTime = new Date();
		String[] sourcePaths = source.split(";");
		for (String sourcePath : sourcePaths) {
            lstIdo.addAll(fileParser.parser(sourcePath,state));
		}
		checkReport.check(lstIdo,null);
		Date startEndTime = new Date();
		// report
		List<ReportData> lstReport = checkReport.report();
		sourceChecker.setLstReportData(lstReport);
		
		//create xml 
		String dateFileName = dateUtil.format("YYMMddHHmmss",new Date());
		List<ReportDataCount> lstEndReportData = new ArrayList<ReportDataCount>();
		Set<String> set = new HashSet<String>();
		BigDecimal totalScore= new BigDecimal(0);
		for (ReportData r : lstReport) {
			if (r.getLpuName() != null) {
				set.add(r.getLpuName());
			}
			if (r.getEndReportData() != null) {
				lstEndReportData.add(r.getEndReportData());
				totalScore = totalScore.add(r.getEndReportData().getErrorTypeScore());
			}

		}
		
		Summary summary = new Summary();
		summary.setProjectName(projectName);
		summary.setReleaseVersion(projectVersion);
		summary.setScanStartTime(startScanTime);
		summary.setScanEndTime(startEndTime);
		summary.setTotalScore(totalScore);
		summary.setDuration(dateUtil.getDurationDate(startScanTime, startEndTime));
		sourceChecker.setSummary(summary);
		
		//create xml 
		XMLHandler handler = new XMLHandler();
		handler.createXML(report + projectName + "_" + projectVersion + "_"
				+ dateFileName + ".xml", sourceChecker);
		
		//create excel 
		String excelPath= report + projectName + "_" + projectVersion + "_"
		        + dateFileName + ".xls";
		List<Excel> lstExcel = new ArrayList<Excel>();

		Excel summaryExcel = new Excel();
		summaryExcel.setName("Summary");
		
		List<String> lstSummaryExcelHeader = new ArrayList<String>();
		lstSummaryExcelHeader.add("Project");
		lstSummaryExcelHeader.add("Version");
		lstSummaryExcelHeader.add("Total Score");
		lstSummaryExcelHeader.add("Scan Start Time");
		lstSummaryExcelHeader.add("Scan End Time");
		lstSummaryExcelHeader.add("Duation");
		summaryExcel.setHeader(lstSummaryExcelHeader);
		
		List<List<String>> lstSummaryExcelValue = new ArrayList<List<String>>();
		List<String> lstSummary = new ArrayList<String>();
		lstSummary.add(summary.getProjectName());
		lstSummary.add(summary.getReleaseVersion());
		lstSummary.add(String.valueOf(summary.getTotalScore()));
		lstSummary.add(dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanStartTime()));
		lstSummary.add(dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanEndTime()));
		lstSummary.add(summary.getDuration());
		lstSummaryExcelValue.add(lstSummary);
		
		summaryExcel.setValue(lstSummaryExcelValue);
		lstExcel.add(summaryExcel);
		
		if (lstEndReportData.size() > 0) {
			Excel count = new Excel();
			count.setName("Count");
			
			List<String> lstCountHeader = new ArrayList<String>();
			lstCountHeader.add("Error Type");
			lstCountHeader.add("Hit String Count");
			lstCountHeader.add("Hit New & Change Word Count");
			lstCountHeader.add("Duplicated String Count");
			lstCountHeader.add("Duplicated Word Count");
			lstCountHeader.add("Validated String Count");
			lstCountHeader.add("Validated Word Count");
			lstCountHeader.add("Total String Count");
			lstCountHeader.add("Total Word Count");
			lstCountHeader.add("Error Type Score");
			count.setHeader(lstCountHeader);
			
			List<List<String>> lstCountValue = new ArrayList<List<String>>();
			lstEndReportData.forEach(erd -> {
				try {
					List<String> lstCount = new ArrayList<String>();
					lstCount.add(erd.getErrorType());
					lstCount.add(String.valueOf(erd.getHitStringCount()));
					lstCount.add(String.valueOf(erd.getHitNewChangeWordCount()));
					lstCount.add(String.valueOf(erd.getDuplicatedStringCount()));
					lstCount.add(String.valueOf(erd.getDuplicatedWordCount()));
					lstCount.add(String.valueOf(erd.getValidatedStringCount()));
					lstCount.add(String.valueOf(erd.getValidatedWordCount()));
					lstCount.add(String.valueOf(erd.getTotalStringCount()));
					lstCount.add(String.valueOf(erd.getTotalWordCount()));
					lstCount.add(String.valueOf(erd.getErrorTypeScore()));
					lstCountValue.add(lstCount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			count.setValue(lstCountValue);
			lstExcel.add(count);
		}
		
		Excel detail = new Excel();
		Excel uniqueDetail = new Excel();
		
		detail.setName("Details");
		uniqueDetail.setName("Unique");
		
		List<String> lstDetailsHeader = new ArrayList<String>();
		lstDetailsHeader.add("FILE NAME");
		lstDetailsHeader.add("SUB FILE NAME");
		lstDetailsHeader.add("STRING ID");
		lstDetailsHeader.add("SOURCE STRING");
		lstDetailsHeader.add("ERROR TYPE");
		lstDetailsHeader.add("DETAILS");
		detail.setHeader(lstDetailsHeader);
		uniqueDetail.setHeader(lstDetailsHeader);
		
		List<List<String>> lstDetailsValue = new ArrayList<List<String>>();
		List<List<String>> lstUniqueDetailsValue = new ArrayList<List<String>>();
		Iterator iterator = set.iterator();
		HashSet<String> setErrorType = new HashSet<String>();
		while(iterator.hasNext()){
			String name = (String)iterator.next();
			for(ReportData rd : lstReport){
				if (rd.getLpuName() != null && name.equals(rd.getLpuName())) {
					List<String> lstDetail = new ArrayList<String>();
					lstDetail.add(rd.getLpuName());
					lstDetail.add(rd.getSubFileName());
					lstDetail.add(rd.getStringId());
					lstDetail.add(rd.getSourceString());
					lstDetail.add(rd.getErrorType());
					lstDetail.add(rd.getDetails());
					lstDetailsValue.add(lstDetail);
					setErrorType.add(rd.getErrorType());
				}
			}
		}
		
		Iterator iteratorErrorType = setErrorType.iterator();
		while(iteratorErrorType.hasNext()){
			String errorType = (String)iteratorErrorType.next();
			HashSet<String> setUnique = new HashSet<String>();
			for(ReportData rd : lstReport){
				if (rd.getErrorType() != null && errorType.equals(rd.getErrorType())) {
					int size = setUnique.size();
					if(rd.getErrorType().equals(Constant.SPELLING)
							|| rd.getErrorType().equals(Constant.CAMELCASE)){
						setUnique.add(rd.getDetails());
					}else{
						setUnique.add(rd.getSourceString());	
					}
					if(setUnique.size()>size){
						List<String> lstUniqueDetail = new ArrayList<String>();
						lstUniqueDetail.add(rd.getLpuName());
						lstUniqueDetail.add(rd.getSubFileName());
						lstUniqueDetail.add(rd.getStringId());
						lstUniqueDetail.add(rd.getSourceString());
						lstUniqueDetail.add(rd.getErrorType());
						lstUniqueDetail.add(rd.getDetails());
						lstUniqueDetailsValue.add(lstUniqueDetail);
					}
				}
			}
		}
		
		detail.setValue(lstDetailsValue);
		lstExcel.add(detail);
		uniqueDetail.setValue(lstUniqueDetailsValue);
		lstExcel.add(uniqueDetail);
		
		ExcelPoiUtils.exportExcel(lstExcel,excelPath.toString());
		log.info("All files are finished!");
	}
}
