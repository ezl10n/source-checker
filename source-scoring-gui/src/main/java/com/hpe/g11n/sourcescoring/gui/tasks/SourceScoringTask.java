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
	public List<InputDataObj> lstIdo = new ArrayList<InputDataObj>();

	public void setUp(String sourceDir, String reportDir,List<Integer> rulesCheckedIdx) {
		this.source = sourceDir;
		this.report = reportDir;
		this.rulesCheckedIdx = rulesCheckedIdx;
		checkReport.build(rulesCheckedIdx);
	}

	@Override
	protected Void call() throws Exception {

		// output
		final FileWriter fw = new FileWriter(report);


		PassoloTemplate.build(source).process((p,sourceLists) -> {
			int progress=0;
			for (IPslSourceList sourceList : sourceLists.toList()) {
				for (IPslSourceString sourceString : sourceList.getSourceStrings()) {
					ido = new InputDataObj();
					ido.setFileName(new File(source).getName());
					ido.setLpuName(sourceString.getIDName());
					ido.setSourceStrings(sourceString.getText());
					ido.setStringId(sourceString.getID());
					lstIdo.add(ido);
				}
				progress++;
				this.updateProgress(progress,sourceLists.getCount());
			}

		});
		checkReport.check(lstIdo);
		//report
		List<ReportData> report = checkReport.report();
		report.forEach( r -> {
			try {
				fw.write(r.getStringId()+","+r.getSourceStrings()+"\n");
			} catch (IOException e) {
				log.error("write report CSV failure.",e);
			}

		});

		fw.close();

		return null;
	}
}
