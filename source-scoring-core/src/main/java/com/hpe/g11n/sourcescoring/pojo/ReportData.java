package com.hpe.g11n.sourcescoring.pojo;

public class ReportData {
	private String lpuName;
	private String fileName;
	private String stringId;
	private String sourceStrings;
	private String errorType;
	private String details;

	public ReportData(String lpuName, String fileName, String stringId,
			String sourceStrings, String errorType, String details) {
		super();
		this.lpuName = lpuName;
		this.fileName = fileName;
		this.stringId = stringId;
		this.sourceStrings = sourceStrings;
		this.errorType = errorType;
		this.details = details;
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

}
