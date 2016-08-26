package com.hpe.g11n.sourcescoring.pojo;

import java.math.BigDecimal;

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
	private int hitStringCount;
	private int duplicatedCount;
	private int validatedCount;
	private int totalWordCount;
	private int hitWordCount;
	private BigDecimal errorTypeScore;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public int getHitStringCount() {
		return hitStringCount;
	}

	public void setHitStringCount(int hitStringCount) {
		this.hitStringCount = hitStringCount;
	}

	public int getDuplicatedCount() {
		return duplicatedCount;
	}

	public void setDuplicatedCount(int duplicatedCount) {
		this.duplicatedCount = duplicatedCount;
	}

	public int getValidatedCount() {
		return validatedCount;
	}

	public void setValidatedCount(int validatedCount) {
		this.validatedCount = validatedCount;
	}

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = totalWordCount;
	}

	public int getHitWordCount() {
		return hitWordCount;
	}

	public void setHitWordCount(int hitWordCount) {
		this.hitWordCount = hitWordCount;
	}

	public BigDecimal getErrorTypeScore() {
		return errorTypeScore;
	}

	public void setErrorTypeScore(BigDecimal errorTypeScore) {
		this.errorTypeScore = errorTypeScore;
	}

}
