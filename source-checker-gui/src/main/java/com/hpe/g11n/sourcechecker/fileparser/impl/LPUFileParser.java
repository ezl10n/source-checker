package com.hpe.g11n.sourcechecker.fileparser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.control.Alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.g11n.sdl.psl.interop.core.IPassoloApp;
import com.hp.g11n.sdl.psl.interop.core.IPslProject;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceLists;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceString;
import com.hp.g11n.sdl.psl.interop.core.enums.PslState;
import com.hp.g11n.sdl.psl.interop.core.impl.impl.PassoloApp;
import com.hpe.g11n.sourcechecker.fileparser.BaseParser;
import com.hpe.g11n.sourcechecker.pojo.InputData;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 下午3:45:06
 *
 */
public class LPUFileParser extends BaseParser {
	private final Logger log = LoggerFactory.getLogger(getClass());

	InputData ido;
	private static final String STATE = "psl.psl-generate-sourcechecker-report.state";
	private List<String> lstState;
	private IPassoloApp app;
	private IPslProject project;
	List<InputData> lstIdo;

	private List<InputData> parser(String filePath) {
		long start = System.currentTimeMillis();
		lstIdo = new ArrayList<InputData>();
		lstState = config.getStringList(STATE);
		app = PassoloApp.getInstance();
		int license = app.getLicense();
		short sh = app.getVersion(); // Result: 1100
        if(license ==0){
        	Alert alert=new Alert(Alert.AlertType.ERROR,"Failed to check, the passolo is not activated");
			alert.setHeaderText("Error:");
			alert.show();
			log.debug("Failed to check, the passolo is not activated");
			return lstIdo;
        }
        if(sh !=(short)1100){
        	Alert alert=new Alert(Alert.AlertType.ERROR,"Failed to check, the version of Passolo is not 2011");
			alert.setHeaderText("Error:");
			alert.show();
			log.debug("Failed to check, the version of Passolo is not 2011");
			return lstIdo;
        }
		project = app.open(filePath);
		long startTime = System.currentTimeMillis();
		log.debug("PassoloApp to read file start at:" + startTime);
		try {
			IPslSourceLists sourceLists = project.getSourceLists();
			ExecutorService service;
			if(sourceLists.toList().size()<50){
				for (int i = 0; i < sourceLists.toList().size(); i++) {
					List<IPslSourceString> lstSourceString = sourceLists.toList()
							.get(i).getSourceStrings();
					String sourceFileName = sourceLists.toList().get(i)
							.getSourceFile();

					lstIdo = getInputData(filePath, sourceFileName,
							lstSourceString);
				}
			}else{
				service = Executors.newFixedThreadPool(50);
				for (int i = 0; i < sourceLists.toList().size(); i++) {
					List<IPslSourceString> lstSourceString = sourceLists.toList()
							.get(i).getSourceStrings();
					String sourceFileName = sourceLists.toList().get(i)
							.getSourceFile();

					service.execute(new Runnable() {
						public void run() {
							lstIdo = getInputData(filePath, sourceFileName,
									lstSourceString);
						}
					});
				}
				service.shutdown();
			}
			
			
		} catch (Exception ex) {
			log.error("LPUFileParser exception:" + ex.getMessage());
		} finally {
			project.close();
			IPassoloApp.quit();
			long end = System.currentTimeMillis();
			log.info("LPUFileParser.process execute time:" + (end - start)
					+ "ms");
		}
		long endTime = System.currentTimeMillis();
		log.debug("PassoloApp to read to read file end at:" + endTime);
		log.debug("Duration time:" + (endTime - startTime));
		return lstIdo;
	}

	@Override
	public boolean isHandle(String source) {
		return source.endsWith(".lpu");
	}

	@Override
	public List<InputData> getInputData(String source) {
		return parser(source);
	}

	public String getName(String path) {
		int index = path.lastIndexOf("\\");
		return path.substring(index+1, path.length());
	}

	public synchronized List<InputData> getInputData(String filePath,
			String sourceFile, List<IPslSourceString> lstSourceString) {
		for (IPslSourceString sourceString : lstSourceString) {
			if (lstState != null && lstState.size() > 0) {
				if (isNewOrChanged(sourceString)) {
					ido = new InputData();
					ido.setLpuName(getName(filePath));
					ido.setFileName(getName(sourceFile));
					ido.setSourceString(sourceString.getText());
					ido.setStringId(sourceString.getID());
					lstIdo.add(ido);
					ido = null;
				}
			} else {
				if(!sourceString.hasState(PslState.valueOf("pslStateReadOnly"))
						&& !sourceString.hasState(PslState.valueOf("pslStateHidden"))
						&& !sourceString.hasState(PslState.valueOf("pslStateDeleted"))
						&& !sourceString.hasState(PslState.valueOf("pslStateCorrection"))){
					ido = new InputData();
					ido.setLpuName(getName(filePath));
					ido.setFileName(getName(sourceFile));
					ido.setSourceString(sourceString.getText());
					ido.setStringId(sourceString.getID());
					lstIdo.add(ido);
					ido = null;
				}
			}
		}
		return lstIdo;
	}
	
	private boolean isNewOrChanged(IPslSourceString sourceString){
		boolean flag = false;
		if((sourceString.hasState(PslState.valueOf("pslStateNew"))
				|| sourceString.hasState(PslState.valueOf("pslStateChanged"))
				|| (sourceString.hasState(PslState.valueOf("pslStateNew")) 
				&& (sourceString.hasState(PslState.valueOf("pslStateChanged")))))
				&& (!sourceString.hasState(PslState.valueOf("pslStateReadOnly"))
				&& !sourceString.hasState(PslState.valueOf("pslStateHidden"))
				&& !sourceString.hasState(PslState.valueOf("pslStateCorrection"))
				&& !sourceString.hasState(PslState.valueOf("pslStateCoordChanged"))
				&& !sourceString.hasState(PslState.valueOf("pslStateReview"))
				&& !sourceString.hasState(PslState.valueOf("pslStateTranslated"))
				&& !sourceString.hasState(PslState.valueOf("pslStateBookmark"))
				&& !sourceString.hasState(PslState.valueOf("pslStateDeleted"))
				&& !sourceString.hasState(PslState.valueOf("pslStateNoReplicate"))
				&& !sourceString.hasState(PslState.valueOf("pslStateVariableID"))
				&& !sourceString.hasState(PslState.valueOf("pslStateAutoTranslated"))
				&& !sourceString.hasState(PslState.valueOf("pslStateSuppressError"))
				&& !sourceString.hasState(PslState.valueOf("pslStateLocked"))
				&& !sourceString.hasState(PslState.valueOf("pslStateTagged"))
				&& !sourceString.hasState(PslState.valueOf("pslStateAllowUntag"))
				&& !sourceString.hasState(PslState.valueOf("pslStateInvisible"))
				&& !sourceString.hasState(PslState.valueOf("pslStateMultiline"))
				&& !sourceString.hasState(PslState.valueOf("pslStateToggleRL"))
				&& !sourceString.hasState(PslState.valueOf("pslStateMirror"))
				&& !sourceString.hasState(PslState.valueOf("pslStateMirrorThis"))
				&& !sourceString.hasState(PslState.valueOf("pslStateUnifyReplicates"))
				&& !sourceString.hasState(PslState.valueOf("pslStateOtherSplitPart"))
				&& !sourceString.hasState(PslState.valueOf("pslSourceListStateVirtual")))){
			flag = true;
		}
		return flag;
	}
}
