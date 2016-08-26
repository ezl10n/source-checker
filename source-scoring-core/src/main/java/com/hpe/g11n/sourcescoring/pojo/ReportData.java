package com.hpe.g11n.sourcescoring.pojo;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月16日
 * @Time: 下午4:46:38
 *
 */
public class ReportData {
	private String lpuName;
	private String subFileName;
	private String stringId;
	private String sourceString;
	private String errorType;
	private String details;
	private String fileVersion;
	private ReportDataCount endReportData;

	public ReportData(String lpuName, String subFileName, String stringId,String sourceString, 
			String errorType, String details,String fileVersion,ReportDataCount endReportData) {
		super();
		this.lpuName = lpuName;
		this.subFileName = subFileName;
		this.stringId = stringId;
		this.sourceString = sourceString;
		this.errorType = errorType;
		this.details = details;
		this.fileVersion=fileVersion;
		this.endReportData = endReportData;
	}
	
	public String getLpuName() {
		return lpuName;
	}

	public void setLpuName(String lpuName) {
		this.lpuName = lpuName;
	}

	public String getSubFileName() {
		return subFileName;
	}

	public void setSubFileName(String subFileName) {
		this.subFileName = subFileName;
	}

	public String getStringId() {
		return stringId;
	}

	public void setStringId(String stringId) {
		this.stringId = stringId;
	}

	public String getSourceString() {
		return sourceString;
	}

	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public ReportDataCount getEndReportData() {
		return endReportData;
	}

	public void setEndReportData(ReportDataCount endReportData) {
		this.endReportData = endReportData;
	}

	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

}
