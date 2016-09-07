package com.hpe.g11n.sourcechecker.pojo;

public class InputData {
	private String lpuName;
	private String fileName;
	private String stringId;
	private String sourceString;
	private String fileVersion;
	public InputData(){
		super();
	}
	public InputData(String lpuName, String fileName, String stringId,
			String sourceString) {
		super();
		this.lpuName = lpuName;
		this.fileName = fileName;
		this.stringId = stringId;
		this.sourceString = sourceString;
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

	public String getSourceString() {
		return sourceString;
	}

	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}
	public String getFileVersion() {
		return fileVersion;
	}
	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

}
