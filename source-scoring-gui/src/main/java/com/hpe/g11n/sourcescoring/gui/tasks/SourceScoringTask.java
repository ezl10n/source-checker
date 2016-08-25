package com.hpe.g11n.sourcescoring.gui.tasks;

import java.io.FileWriter;
import java.io.IOException;
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
		List<InputData> lstIdo = new ArrayList<InputData>();
		Date date = new Date();
		String[] sourcePaths = source.split(";");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		final FileWriter fw = new FileWriter(report + "SourceScoring"
				+ sdf.format(date) + ".csv");
		for (String sourcePath : sourcePaths) {
            lstIdo.addAll(fileParser.parser(sourcePath));
		}
		checkReport.check(lstIdo,(now,total) ->{this.updateProgress(now, total);});
		// report
		List<ReportData> lstReport = checkReport.report();
		//create xml 
		XMLHandler handler = new XMLHandler();
		handler.createXML("1.0", report + "SourceScoring"
				+ sdf.format(date) + ".xml", lstReport);
		
		//create csv
		fw.write("LPU NAME,FILE NAME,STRING ID,SOURCE STRINGS,ERROR TYPE,DETAILS\n");
		List<ReportDataCount> lstEndReportData = new ArrayList<ReportDataCount>();
		Set<String> set = new HashSet<String>();
		for (ReportData r : lstReport) {
			if (r.getLpuName() != null) {
				set.add(r.getLpuName());
			}
			if (r.getEndReportData() != null) {
				lstEndReportData.add(r.getEndReportData());

			}

		}
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			String name = (String)iterator.next();
			for(ReportData rd : lstReport){
				if (rd.getLpuName() != null && name.equals(rd.getLpuName())) {
					fw.write(rd.getLpuName() + "," + rd.getFileName() + ","
							+ rd.getStringId() + "," + rd.getSourceStrings()
							+ "," + rd.getErrorType() + "," + rd.getDetails()
							+ "\n");
				}
			}
		}
		
		if (lstEndReportData.size() > 0) {
			fw.write("\n");
			fw.write("\n");
			fw.write("Error type,Hit string count,Duplicated count,Validated count,Total New & Change Word Count,Hit New & Change Word Count\n");
			lstEndReportData.forEach(erd -> {
				try {
					fw.write(erd.getErrorType() + "," + erd.getHitStrCount()
							+ "," + erd.getDupliCount() + ","
							+ erd.getValidCount() + ","
							+ erd.getTotalNCCount() + "," + erd.getHitNCCount()
							+ "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
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
