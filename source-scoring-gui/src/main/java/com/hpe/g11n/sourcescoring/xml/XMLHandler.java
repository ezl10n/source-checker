package com.hpe.g11n.sourcescoring.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hpe.g11n.sourcescoring.pojo.ReportData;
import com.hpe.g11n.sourcescoring.pojo.SourceScoring;
import com.hpe.g11n.sourcescoring.pojo.Summary;
import com.hpe.g11n.sourcescoring.utils.DateUtil;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月24日
 * @Time: 下午12:49:55
 *
 */
public class XMLHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());
	public XMLHandler() {

	}

	public void createXML(String filePath,SourceScoring sourceScoring) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			List<ReportData> lstReportData = sourceScoring.getLstReportData();
			Summary summary = sourceScoring.getSummary();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");

			Element root = document.createElement("SourcesCoring");
			root.setAttribute("version", sourceScoring.getProductVersion());
			Element resultData = document.createElement("ResultData");
			Set<String> set = new HashSet<String>();
			for(ReportData reportData:lstReportData){
				if(reportData.getLpuName() != null){
					set.add(reportData.getLpuName());
				}
			}
			Iterator iterator = set.iterator();
			while(iterator.hasNext()){
				String name = (String)iterator.next();
				Element fileType = document.createElement("FileType");
				String fileVersion="";
				for(int j=0;j<lstReportData.size();j++){
					String lpuName =lstReportData.get(j).getLpuName();
					if(lpuName!= null && lpuName.equals(name)){
						fileVersion = lstReportData.get(j).getFileVersion();
						Element sourceStrings = document.createElement("SourceStrings");
						
						Element subFileName = document.createElement("SubFileName");
						subFileName.setTextContent(lstReportData.get(j).getSubFileName());
						sourceStrings.appendChild(subFileName);
						
						Element stringId = document.createElement("StringId");
						stringId.setTextContent(lstReportData.get(j).getStringId());
						sourceStrings.appendChild(stringId);
						
						Element sourceString = document.createElement("SourceString");
						sourceString.setTextContent(lstReportData.get(j).getSourceString());
						sourceStrings.appendChild(sourceString);
						
						Element errorType = document.createElement("ErrorType");
						errorType.setTextContent(lstReportData.get(j).getErrorType());
						sourceStrings.appendChild(errorType);
						
						Element details = document.createElement("Details");
						details.setTextContent(lstReportData.get(j).getDetails());
						sourceStrings.appendChild(details);
						
						fileType.appendChild(sourceStrings);
					}
				}
				fileType.setAttribute("FileVersion", fileVersion);
				fileType.setAttribute("FileName", name);
				resultData.appendChild(fileType);
			}
			
			root.appendChild(resultData);
			for(int k=0;k<lstReportData.size();k++){
				if(lstReportData.get(k).getEndReportData() !=null){
					Element resultCountData = document.createElement("ResultCountData");
					
					Element errorType = document.createElement("ErrorType");
					errorType.setTextContent(lstReportData.get(k).getEndReportData().getErrorType());
					resultCountData.appendChild(errorType);
					
					Element hitStrCount = document.createElement("HitStringCount");
					hitStrCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitStringCount()));
					resultCountData.appendChild(hitStrCount);
					
					Element dupliCount = document.createElement("DuplicatedCount");
					dupliCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getDuplicatedCount()));
					resultCountData.appendChild(dupliCount);
					
					Element validatCount = document.createElement("ValidatedCount");
					validatCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getValidatedCount()));
					resultCountData.appendChild(validatCount);
					
					Element totalNCCount = document.createElement("TotalWordCount");
					totalNCCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getTotalWordCount()));
					resultCountData.appendChild(totalNCCount);
					
					Element hitNCCount = document.createElement("HitWordCount");
					hitNCCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitWordCount()));
					resultCountData.appendChild(hitNCCount);
					
					Element errorTypeScore = document.createElement("ErrorTypeScore");
					errorTypeScore.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getErrorTypeScore()));
					resultCountData.appendChild(errorTypeScore);
					
					root.appendChild(resultCountData);
				}
			}
			
			Element summaryElement = document.createElement("Summary");
			Element totalScore = document.createElement("TotalScore");
			totalScore.setTextContent(String.valueOf(summary.getTotalScore()));
			summaryElement.appendChild(totalScore);
			
			DateUtil dateUtil = new DateUtil();
			
			Element scanStartTime = document.createElement("ScanStartTime");
			scanStartTime.setTextContent(dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanStartTime()));
			summaryElement.appendChild(scanStartTime);
			
			Element scanEndTime = document.createElement("ScanEndTime");
			scanEndTime.setTextContent(dateUtil.format("YYYY-MM-dd HH:mm:ss", summary.getScanEndTime()));
			summaryElement.appendChild(scanEndTime);
			
			Element duration = document.createElement("Duration");
			duration.setTextContent(summary.getDuration());
			summaryElement.appendChild(duration);
			
			Element projectName = document.createElement("ProjectName");
			projectName.setTextContent(summary.getProjectName());
			summaryElement.appendChild(projectName);
			
			Element releaseVersion = document.createElement("ReleaseVersion");
			releaseVersion.setTextContent(summary.getReleaseVersion());
			summaryElement.appendChild(releaseVersion);
			root.appendChild(summaryElement);
			
			document.appendChild(root);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);

			// export string
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			transFormer.transform(domSource, new StreamResult(bos));

			// save as file
			File file = new File(filePath.toString());
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StreamResult xmlResult = new StreamResult(out);
			transFormer.transform(domSource, xmlResult);
			out.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			log.error("create xml error!");
			e.printStackTrace();
		}
	}

//	public void parserXML(String strXML) {
//	}

}