package com.hpe.g11n.sourcescoring.pojo;

public class InputDataObj {
	private String lpuName;
	private String fileName;
	private String stringId;
	private String sourceStrings;
	public InputDataObj(){
		super();
	}
	public InputDataObj(String lpuName, String fileName, String stringId,
			String sourceStrings) {
		super();
		this.lpuName = lpuName;
		this.fileName = fileName;
		this.stringId = stringId;
		this.sourceStrings = sourceStrings;
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

}
