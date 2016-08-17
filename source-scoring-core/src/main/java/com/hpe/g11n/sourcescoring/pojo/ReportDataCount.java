package com.hpe.g11n.sourcescoring.pojo;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月16日
 * @Time: 下午4:45:20
 *
 */
public class ReportDataCount {
	private String errorType;
	private int hitStrCount;
	private int dupliCount;
	private int validatCount;
	private int totalNCCount;
	private int hitNCCount;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public int getHitStrCount() {
		return hitStrCount;
	}

	public void setHitStrCount(int hitStrCount) {
		this.hitStrCount = hitStrCount;
	}

	public int getDupliCount() {
		return dupliCount;
	}

	public void setDupliCount(int dupliCount) {
		this.dupliCount = dupliCount;
	}

	public int getValidatCount() {
		return validatCount;
	}

	public void setValidatCount(int validatCount) {
		this.validatCount = validatCount;
	}

	public int getTotalNCCount() {
		return totalNCCount;
	}

	public void setTotalNCCount(int totalNCCount) {
		this.totalNCCount = totalNCCount;
	}

	public int getHitNCCount() {
		return hitNCCount;
	}

	public void setHitNCCount(int hitNCCount) {
		this.hitNCCount = hitNCCount;
	}
}
