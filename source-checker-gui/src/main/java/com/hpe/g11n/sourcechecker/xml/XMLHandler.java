package com.hpe.g11n.sourcechecker.xml;

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

import com.hpe.g11n.sourcechecker.pojo.ReportData;
import com.hpe.g11n.sourcechecker.pojo.SourceChecker;
import com.hpe.g11n.sourcechecker.pojo.Summary;
import com.hpe.g11n.sourcechecker.utils.DateUtil;

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

	public void createXML(String filePath,SourceChecker sourceChecker) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			List<ReportData> lstReportData = sourceChecker.getLstReportData();
			Summary summary = sourceChecker.getSummary();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");

			Element root = document.createElement("SourceChecker");
			root.setAttribute("version", sourceChecker.getVersion());
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
					
					Element hitStringCount = document.createElement("HitStringCount");
					hitStringCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitStringCount()));
					resultCountData.appendChild(hitStringCount);
					
					Element hitNewChangeWordCount = document.createElement("HitNewChangeWordCount");
					hitNewChangeWordCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitNewChangeWordCount()));
					resultCountData.appendChild(hitNewChangeWordCount);
					
					Element duplicatedStringCount = document.createElement("DuplicatedStringCount");
					duplicatedStringCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getDuplicatedStringCount()));
					resultCountData.appendChild(duplicatedStringCount);
					
					Element duplicatedWordCount = document.createElement("DuplicatedWordCount");
					duplicatedWordCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getDuplicatedWordCount()));
					resultCountData.appendChild(duplicatedWordCount);
					
					Element validatedStringCount = document.createElement("ValidatedStringCount");
					validatedStringCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getValidatedStringCount()));
					resultCountData.appendChild(validatedStringCount);
					
					Element validatedWordCount = document.createElement("ValidatedWordCount");
					validatedWordCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getValidatedWordCount()));
					resultCountData.appendChild(validatedWordCount);
					
					Element totalStringCount = document.createElement("TotalStringCount");
					totalStringCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getTotalStringCount()));
					resultCountData.appendChild(totalStringCount);
					
					Element totalWordCount = document.createElement("TotalWordCount");
					totalWordCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getTotalWordCount()));
					resultCountData.appendChild(totalWordCount);
					
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
			
			Element projectName = document.createElement("Product");
			projectName.setTextContent(summary.getProduct());
			summaryElement.appendChild(projectName);
			
			Element releaseVersion = document.createElement("Version");
			releaseVersion.setTextContent(summary.getVersion());
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