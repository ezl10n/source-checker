package com.hpe.g11n.sourcescoring.gui.tasks;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;
import com.hpe.g11n.sourcescoring.pojo.SourceScoring;
import com.hpe.g11n.sourcescoring.pojo.Summary;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.hpe.g11n.sourcescoring.utils.DateUtil;
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
		handler.createXML(report + "SourceScoring"
				+ dateFileName + ".xml", sourceScoring);
		
		//create csv
		final FileWriter fw = new FileWriter(report + "SourceScoring"
				+ dateFileName + ".csv");
		fw.write("FILE NAME,SUB FILE NAME,STRING ID,SOURCE STRINGS,ERROR TYPE,DETAILS\n");
		
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			String name = (String)iterator.next();
			for(ReportData rd : lstReport){
				if (rd.getLpuName() != null && name.equals(rd.getLpuName())) {
					fw.write(rd.getLpuName() + "," + rd.getSubFileName() + ","
							+ rd.getStringId() + "," + rd.getSourceString()
							+ "," + rd.getErrorType() + "," + rd.getDetails()
							+ "\n");
				}
			}
		}
		
		if (lstEndReportData.size() > 0) {
			fw.write("\n");
			fw.write("\n");
			fw.write("Error type,Hit string count,Duplicated count,Validated count,Total New & Change Word Count,Hit New & Change Word Count,Error Type Score\n");
			lstEndReportData.forEach(erd -> {
				try {
					fw.write(erd.getErrorType() + "," + erd.getHitStringCount()
							+ "," + erd.getDuplicatedCount() + ","
							+ erd.getValidatedCount() + ","
							+ erd.getTotalWordCount() + "," + erd.getHitWordCount()
							+ "," + erd.getErrorTypeScore() + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		fw.write("\n");
		fw.write("\n");
		fw.write("Total Score,Scan Start Time,Scan End Time,Duration,Release Name,Release Version\n");
		fw.write(summary.getTotalScore() + "," + dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanStartTime())
				+ "," + dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanEndTime()) + "," + summary.getDuration()
				+ "," + summary.getProjectName() + "," + summary.getReleaseVersion() + "\n");
		fw.close();
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
