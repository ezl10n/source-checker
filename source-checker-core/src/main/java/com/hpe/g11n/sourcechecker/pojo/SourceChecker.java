package com.hpe.g11n.sourcechecker.pojo;

import java.util.List;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月25日
 * @Time: 下午5:06:54
 *
 */
public class SourceChecker {
	private String version;
	private List<ReportData> lstReportData;
	private Summary summary;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ReportData> getLstReportData() {
		return lstReportData;
	}

	public void setLstReportData(List<ReportData> lstReportData) {
		this.lstReportData = lstReportData;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

}
