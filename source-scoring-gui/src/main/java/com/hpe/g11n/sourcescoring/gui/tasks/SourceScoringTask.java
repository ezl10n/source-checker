package com.hpe.g11n.sourcescoring.gui.tasks;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceList;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceLists;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceString;
import com.hp.g11n.sdl.psl.interop.core.enums.PslState;
import com.hpe.g11n.sourcescoring.core.ISourceScoring;
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
	@Named("sourceScoringConfig")
	private Config config;
	public List<InputData> lstIdo = new ArrayList<InputData>();
	private static final String STATE="psl.psl-generate-sourcescoring-report.state";
	private List<String> lstState;
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
		lstState=config.getStringList(STATE);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		final FileWriter fw = new FileWriter(report+"SourceScoring"+sdf.format(new Date())+".csv");
		List<IPslSourceLists> lstIPslSourceLists = new ArrayList<IPslSourceLists>();
		for(String sourcePath:sourcePaths){
			PassoloTemplate.build(sourcePath).process((p,sourceLists) -> {
				lstIPslSourceLists.add(sourceLists);
				int progress=totalProgress;
				totalCount=totalCount + sourceLists.getCount();
				for (int i=0;i<sourceLists.toList().size();i++) {
					for (IPslSourceString sourceString : sourceLists.toList().get(i).getSourceStrings()) {
						for(String sourceStringState:lstState){
							if(sourceString.hasState(PslState.valueOf(sourceStringState))){
								ido = new InputData();
								ido.setLpuName(new File(sourcePath).getName());
								ido.setFileName(new File(sourceLists.toList().get(i).getSourceFile()).getName());
								ido.setSourceStrings(sourceString.getText());
								ido.setStringId(sourceString.getID());
								lstIdo.add(ido);
							}
						}
					}
					progress++;
					if(i== sourceLists.toList().size()-1){
						totalProgress = progress;
					}
					this.updateProgress(progress,totalCount);
				}

			});
		}
		checkReport.check(lstIdo);
		//report
		List<ReportData> report = checkReport.report();
		fw.write("LPU NAME,FILE NAME,STRING ID,SOURCE STRINGS,ERROR TYPE,DETAILS\n");
		List<ReportDataCount> lstEndReportData = new ArrayList<ReportDataCount>();
		report.forEach( r -> {
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

		});
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
