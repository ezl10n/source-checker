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
	private String fileName;
	private String stringId;
	private String sourceStrings;
	private String errorType;
	private String details;
	private EndReportData endReportData;

	public ReportData(String lpuName, String fileName, String stringId,
			String sourceStrings, String errorType, String details,EndReportData endReportData) {
		super();
		this.lpuName = lpuName;
		this.fileName = fileName;
		this.stringId = stringId;
		this.sourceStrings = sourceStrings;
		this.errorType = errorType;
		this.details = details;
		this.endReportData = endReportData;
	}
	
	public String getLpuName() {
		return lpuName;
	}

	public void setLpuName(String lpuName) {
		this.lpuName = lpuName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStringId() {
		return stringId;
	}

	public void setStringId(String stringId) {
		this.stringId = stringId;
	}

	public String getSourceStrings() {
		return sourceStrings;
	}

	public void setSourceStrings(String sourceStrings) {
		this.sourceStrings = sourceStrings;
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

	public EndReportData getEndReportData() {
		return endReportData;
	}

	public void setEndReportData(EndReportData endReportData) {
		this.endReportData = endReportData;
	}

}
