package com.hpe.g11n.sourcechecker.fileparser.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

	
	private List<InputData> parser(String filePath) {
		List<InputData> lstIdo = new ArrayList<InputData>();
		long start = System.currentTimeMillis();
		lstState=config.getStringList(STATE);
		IPassoloApp app = PassoloApp.getInstance();
		IPslProject project = app.open(filePath);
		long startTime =System.currentTimeMillis();
		log.debug("PassoloApp to read file start at:"+startTime);
		try {
			IPslSourceLists sourceLists = project.getSourceLists();
			for (int i = 0; i < sourceLists.toList().size(); i++) {
				for (IPslSourceString sourceString : sourceLists.toList()
						.get(i).getSourceStrings()) {
					if(lstState != null && lstState.size()>0){
						for (String sourceStringState : lstState) {
							if (sourceString.hasState(PslState
									.valueOf(sourceStringState))) {
								ido = new InputData();
								ido.setLpuName(new File(filePath).getName());
								ido.setFileName(new File(sourceLists.toList()
										.get(i).getSourceFile()).getName());
								ido.setSourceString(sourceString.getText());
								ido.setStringId(sourceString.getID());
								lstIdo.add(ido);
								break;
							}
						}
					}else{
						ido = new InputData();
						ido.setLpuName(new File(filePath).getName());
						ido.setFileName(new File(sourceLists.toList()
								.get(i).getSourceFile()).getName());
						ido.setSourceString(sourceString.getText());
						ido.setStringId(sourceString.getID());
						lstIdo.add(ido);
					}
				}
			}
		} catch (Exception ex) {
			log.error("LPUFileParser exception." + ex);
		} finally {
			project.close();
			IPassoloApp.quit();
			long end = System.currentTimeMillis();
			log.info("LPUFileParser.process execute time:" + (end - start)
					+ "ms");
		}
		long endTime =System.currentTimeMillis();
		log.debug("PassoloApp to read to read file end at:"+endTime);
		log.debug("Duration time:"+(endTime-startTime));
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

}
