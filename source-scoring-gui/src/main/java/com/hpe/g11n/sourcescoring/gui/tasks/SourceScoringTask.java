package com.hpe.g11n.sourcescoring.gui.tasks;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceList;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceLists;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceString;
import com.hp.g11n.sdl.psl.interop.core.enums.PslState;
import com.hpe.g11n.sourcescoring.core.ISourceScoring;
import com.hpe.g11n.sourcescoring.fileparser.FileParserManager;
import com.hpe.g11n.sourcescoring.fileparser.IFileParser;
import com.hpe.g11n.sourcescoring.gui.utils.PassoloTemplate;
import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;
import com.hpe.g11n.sourcescoring.pojo.InputData;
import com.hpe.g11n.sourcescoring.pojo.ReportData;


import com.typesafe.config.Config;





import javafx.concurrent.Task;









import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<InputData> lstIdo = new ArrayList<InputData>();
	int totalProgress =0;
	int totalCount =0;

	public void setUp(String sourceDir, String reportDir,List<Integer> rulesCheckedIdx) {
		this.source = sourceDir;
		this.report = reportDir;
		this.rulesCheckedIdx = rulesCheckedIdx;
		checkReport.build(rulesCheckedIdx);
	}
	

	@Override
	protected Void call() throws Exception {
		// output
		String[] sourcePaths = source.split(";");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		final FileWriter fw = new FileWriter(report+"SourceScoring"+sdf.format(new Date())+".csv");
		for(String sourcePath:sourcePaths){
			lstIdo.addAll(fileParser.parser(sourcePath));
		}
		checkReport.check(lstIdo);
		//report
		List<ReportData> report = checkReport.report();
		fw.write("LPU NAME,FILE NAME,STRING ID,SOURCE STRINGS,ERROR TYPE,DETAILS\n");
		List<ReportDataCount> lstEndReportData = new ArrayList<ReportDataCount>();
		int k=0;
		for(ReportData r:report){
			k++;
			try {
				if(r.getStringId() !=null){
					fw.write(r.getLpuName()+","+r.getFileName()+","+r.getStringId()
							+","+r.getSourceStrings()+","+r.getErrorType()+","+r.getDetails()+"\n");
				}
				if(r.getEndReportData() !=null){
					lstEndReportData.add(r.getEndReportData());
					
				}
				
			} catch (IOException e) {
				log.error("write report CSV failure.",e);
			}
            this.updateProgress(k, report.size());
		}
		if(lstEndReportData.size()>0){
			fw.write("\n");
			fw.write("\n");
			fw.write("Error type,Hit string count,Duplicated count,Validated count,Total New & Change Word Count,Hit New & Change Word Count\n");
			lstEndReportData.forEach( erd -> {
				try {
					fw.write(erd.getErrorType()+","+erd.getHitStrCount()+","+erd.getDupliCount()+","+erd.getValidatCount()
							+","+erd.getTotalNCCount()+","+erd.getHitNCCount()+"\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		fw.close();
		return null;
	}
}
