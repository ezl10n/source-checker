package com.hpe.g11n.sourcechecker.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月25日
 * @Time: 下午5:01:38
 *
 */
public class Summary {
	private BigDecimal totalScore;
	private Date scanStartTime;
	private Date scanEndTime;
	private String duration;// scanStartTime-scanEndTime
	private String product;// product name
	private String version;// product version

	public BigDecimal getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}

	public Date getScanStartTime() {
		return scanStartTime;
	}

	public void setScanStartTime(Date scanStartTime) {
		this.scanStartTime = scanStartTime;
	}

	public Date getScanEndTime() {
		return scanEndTime;
	}

	public void setScanEndTime(Date scanEndTime) {
		this.scanEndTime = scanEndTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
