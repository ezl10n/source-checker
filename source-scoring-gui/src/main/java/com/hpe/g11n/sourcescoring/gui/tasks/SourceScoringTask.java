package com.hpe.g11n.sourcescoring.gui.tasks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hpe.g11n.sourcescoring.core.ISourceScoring;
import com.hpe.g11n.sourcescoring.fileparser.IFileParser;
import com.hpe.g11n.sourcescoring.pojo.Excel;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;
import com.hpe.g11n.sourcescoring.pojo.SourceScoring;
import com.hpe.g11n.sourcescoring.pojo.Summary;
import com.hpe.g11n.sourcescoring.utils.constant.Constant;
import com.hpe.g11n.sourcescoring.utils.DateUtil;
import com.hpe.g11n.sourcescoring.utils.ExcelPoiUtils;
import com.hpe.g11n.sourcescoring.xml.XMLHandler;

public class SourceScoringTask extends Task<Void> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String source;
	private String report;
	private List<Integer> rulesCheckedIdx;
	@Inject
	InputData ido;
	@Inject
	ISourceScoring checkReport;
	@Inject
	IFileParser fileParser;
	int totalProgress = 0;
	int totalCount = 0;

	public void setUp(String sourceDir, String reportDir,
			List<Integer> rulesCheckedIdx) {
		this.source = sourceDir;
		this.report = reportDir;
		this.rulesCheckedIdx = rulesCheckedIdx;
		checkReport.build(rulesCheckedIdx);
	}

	@Override
	protected Void call() throws Exception {
		// output
		SourceScoring sourceScoring = new SourceScoring();
		sourceScoring.setProductVersion(Constant.PRODUCT_VERSION);
		
		List<InputData> lstIdo = new ArrayList<InputData>();
		DateUtil dateUtil = new DateUtil();
		Date startScanTime = new Date();
		String[] sourcePaths = source.split(";");
		for (String sourcePath : sourcePaths) {
            lstIdo.addAll(fileParser.parser(sourcePath));
		}
		checkReport.check(lstIdo,(now,total) ->{this.updateProgress(now, total);});
		Date startEndTime = new Date();
		// report
		List<ReportData> lstReport = checkReport.report();
		sourceScoring.setLstReportData(lstReport);
		
		//create xml 
		String dateFileName = dateUtil.format("YYYYMMddHHmmss",new Date());
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
		summary.setProjectName("");//TODO get project name
		summary.setReleaseVersion("");//TODO get project version
		summary.setScanStartTime(startScanTime);
		summary.setScanEndTime(startEndTime);
		summary.setTotalScore(totalScore);
		summary.setDuration(dateUtil.getDurationDate(startScanTime, startEndTime));
		sourceScoring.setSummary(summary);
		
		//create xml 
		XMLHandler handler = new XMLHandler();
		handler.createXML(report + "SourceChecker"
				+ dateFileName + ".xml", sourceScoring);
		
		//create excel 
		String excelPath= report + "SourceChecker" + dateFileName + ".xls";
		List<Excel> lstExcel = new ArrayList<Excel>();

		Excel summaryExcel = new Excel();
		summaryExcel.setName("Summary");
		
		List<String> lstSummaryExcelHeader = new ArrayList<String>();
		lstSummaryExcelHeader.add("PROJECT");
		lstSummaryExcelHeader.add("VERSION");
		lstSummaryExcelHeader.add("TOTAL SCORE");
		lstSummaryExcelHeader.add("SCAN START TIME");
		lstSummaryExcelHeader.add("SCAN END TIME");
		lstSummaryExcelHeader.add("DURATION");
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
		
		detail.setName("Details");
		List<String> lstDetailsHeader = new ArrayList<String>();
		lstDetailsHeader.add("FILE NAME");
		lstDetailsHeader.add("SUB FILE NAME");
		lstDetailsHeader.add("STRING ID");
		lstDetailsHeader.add("SOURCE STRING");
		lstDetailsHeader.add("ERROR TYPE");
		lstDetailsHeader.add("DETAILS");
		detail.setHeader(lstDetailsHeader);
		
		List<List<String>> lstDetailsValue = new ArrayList<List<String>>();
		Iterator iterator = set.iterator();
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
				}
			}
		}
		detail.setValue(lstDetailsValue);
		lstExcel.add(detail);
		
		ExcelPoiUtils.exportExcel(lstExcel,excelPath.toString());
		return null;
	}

	@Override
	protected void succeeded() {
		super.succeeded();
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"All files are finished!");
		alert.setHeaderText("Note:");
		alert.showAndWait().filter(response -> response == ButtonType.OK)
				.ifPresent(response -> {

				});
	}
}
