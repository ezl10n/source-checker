package com.hpe.g11n.sourcescoring.pojo;

public class ReportData {
	private String id;
	private String value;
	public ReportData(){

	}
	public ReportData(String id, String val){
		this.id = id;
		this.value=val;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
