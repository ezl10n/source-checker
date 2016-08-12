package com.hpe.g11n.sourcescoring.gui.tasks;


import com.google.inject.Inject;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceList;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceString;
import com.hpe.g11n.sourcescoring.core.ISourceScoring;
import com.hpe.g11n.sourcescoring.gui.utils.PassoloTemplate;
import com.hpe.g11n.sourcescoring.pojo.InputDataObj;
import com.hpe.g11n.sourcescoring.pojo.ReportData;

import javafx.concurrent.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceScoringTask extends Task<Void> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String source;
	private String report;
	private List<Integer> rulesCheckedIdx;
	@Inject
	InputDataObj ido;
	@Inject
	ISourceScoring checkReport;
	public List<InputDataObj> lstIdo = null;
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
		for(String sourcePath:sourcePaths){
			lstIdo = new ArrayList<InputDataObj>();
			final FileWriter fw = new FileWriter(report+getFileName(sourcePath)+".csv");
			PassoloTemplate.build(sourcePath).process((p,sourceLists) -> {
				int progress=totalProgress;
				totalCount=totalCount + sourceLists.getCount();
				for (int i=0;i<sourceLists.toList().size();i++) {
					for (IPslSourceString sourceString : sourceLists.toList().get(i).getSourceStrings()) {
						ido = new InputDataObj();
						ido.setLpuName(new File(sourcePath).getName());
						ido.setFileName(sourceString.getIDName());
						ido.setSourceStrings(sourceString.getText());
						ido.setStringId(sourceString.getID());
						lstIdo.add(ido);
					}
					progress++;
					if(i== sourceLists.toList().size()-1){
						totalProgress = progress;
					}
					this.updateProgress(progress,totalCount);
				}

			});
			checkReport.check(lstIdo);
			//report
			List<ReportData> report = checkReport.report();
			fw.write("LPU NAME,FILE NAME,STRING ID,SOURCE STRINGS,ERROR TYPE,DETAILS\n");
			report.forEach( r -> {
				try {
					fw.write(r.getLpuName()+","+r.getFileName()+","+r.getStringId()
							+","+r.getSourceStrings()+","+r.getErrorType()+","+r.getDetails()+"\n");
					
				} catch (IOException e) {
					log.error("write report CSV failure.",e);
				}

			});
			fw.close();
		}
		return null;
	}
	
	public String getFileName(String filePath) {
		File file = new File(filePath);
		String name = file.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index);
	}
}
