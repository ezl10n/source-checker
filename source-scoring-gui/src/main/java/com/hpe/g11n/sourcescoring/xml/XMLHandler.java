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

	public void createXML(String version,String filePath,List<ReportData> lstReportData) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");

			Element root = document.createElement("SourcesCoring");
			root.setAttribute("version", version);
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
				fileType.setAttribute("name", name);
				for(int j=0;j<lstReportData.size();j++){
					String lpuName =lstReportData.get(j).getLpuName();
					if(lpuName!= null && lpuName.equals(name)){
						Element sourceStrings = document.createElement("SourceStrings");
						
						Element fileName = document.createElement("fileName");
						fileName.setTextContent(lstReportData.get(j).getFileName());
						sourceStrings.appendChild(fileName);
						
						Element stringId = document.createElement("stringId");
						stringId.setTextContent(lstReportData.get(j).getStringId());
						sourceStrings.appendChild(stringId);
						
						Element sourceString = document.createElement("sourceString");
						sourceString.setTextContent(lstReportData.get(j).getSourceStrings());
						sourceStrings.appendChild(sourceString);
						
						Element errorType = document.createElement("errorType");
						errorType.setTextContent(lstReportData.get(j).getErrorType());
						sourceStrings.appendChild(errorType);
						
						Element details = document.createElement("details");
						details.setTextContent(lstReportData.get(j).getDetails());
						sourceStrings.appendChild(details);
						
						fileType.appendChild(sourceStrings);
					}
				}
				resultData.appendChild(fileType);
			}
			
			root.appendChild(resultData);
			for(int k=0;k<lstReportData.size();k++){
				if(lstReportData.get(k).getEndReportData() !=null){
					Element resultCountData = document.createElement("ResultCountData");
					
					Element errorType = document.createElement("errorType");
					errorType.setTextContent(lstReportData.get(k).getEndReportData().getErrorType());
					resultCountData.appendChild(errorType);
					
					Element hitStrCount = document.createElement("hitStringCount");
					hitStrCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitStrCount()));
					resultCountData.appendChild(hitStrCount);
					
					Element dupliCount = document.createElement("duplicatedCount");
					dupliCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getDupliCount()));
					resultCountData.appendChild(dupliCount);
					
					Element validatCount = document.createElement("validatedCount");
					validatCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getValidCount()));
					resultCountData.appendChild(validatCount);
					
					Element totalNCCount = document.createElement("totalNewOrChangeWordCount");
					totalNCCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getTotalNCCount()));
					resultCountData.appendChild(totalNCCount);
					
					Element hitNCCount = document.createElement("hitNewOrChangeWordCount");
					hitNCCount.setTextContent(String.valueOf(lstReportData.get(k).getEndReportData().getHitNCCount()));
					resultCountData.appendChild(hitNCCount);
					
					root.appendChild(resultCountData);
				}
			}
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
			e.printStackTrace();
		}
	}

//	public void parserXML(String strXML) {
//	}

//	public static void main(String[] args) {
//		XMLHandler handler = new XMLHandler();
//		String xml = handler.createXML();
//		System.out.println(xml);
		// handler.parserXML(xml);
//	}
}