package com.hpe.g11n.sourcechecker.pojo;

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
	private int hitNewChangeWordCount;
	private int duplicatedStringCount;
	private int duplicatedWordCount;
	private int validatedStringCount;
	private int validatedWordCount;
	private int totalStringCount;
	private int totalWordCount;
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

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = totalWordCount;
	}

	public int getHitNewChangeWordCount() {
		return hitNewChangeWordCount;
	}

	public void setHitNewChangeWordCount(int hitNewChangeWordCount) {
		this.hitNewChangeWordCount = hitNewChangeWordCount;
	}

	public int getDuplicatedStringCount() {
		return duplicatedStringCount;
	}

	public void setDuplicatedStringCount(int duplicatedStringCount) {
		this.duplicatedStringCount = duplicatedStringCount;
	}

	public int getDuplicatedWordCount() {
		return duplicatedWordCount;
	}

	public void setDuplicatedWordCount(int duplicatedWordCount) {
		this.duplicatedWordCount = duplicatedWordCount;
	}

	public int getValidatedStringCount() {
		return validatedStringCount;
	}

	public void setValidatedStringCount(int validatedStringCount) {
		this.validatedStringCount = validatedStringCount;
	}

	public int getValidatedWordCount() {
		return validatedWordCount;
	}

	public void setValidatedWordCount(int validatedWordCount) {
		this.validatedWordCount = validatedWordCount;
	}

	public int getTotalStringCount() {
		return totalStringCount;
	}

	public void setTotalStringCount(int totalStringCount) {
		this.totalStringCount = totalStringCount;
	}

	public BigDecimal getErrorTypeScore() {
		return errorTypeScore;
	}

	public void setErrorTypeScore(BigDecimal errorTypeScore) {
		this.errorTypeScore = errorTypeScore;
	}

}
