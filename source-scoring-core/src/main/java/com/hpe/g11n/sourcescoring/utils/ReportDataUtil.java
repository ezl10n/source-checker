package com.hpe.g11n.sourcescoring.utils;

import java.math.BigDecimal;

import com.hpe.g11n.sourcescoring.pojo.ReportDataCount;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 上午11:21:57
 *
 */
public class ReportDataUtil {
	public ReportDataCount getEndReportData(String errorType, int hitStringCount,
			int hitNewChangeWordCount, int duplicatedStringCount, int duplicatedWordCount,
			int validatedStringCount,int validatedWordCount,int totalStringCount,
			int totalWordCount,BigDecimal errorTypeScore) {
		ReportDataCount endReportData = new ReportDataCount();
		endReportData.setErrorType(errorType);
		endReportData.setHitStringCount(hitStringCount);
		endReportData.setHitNewChangeWordCount(hitNewChangeWordCount);
		endReportData.setDuplicatedStringCount(duplicatedStringCount);
		endReportData.setDuplicatedWordCount(duplicatedWordCount);
		endReportData.setValidatedStringCount(validatedStringCount);
		endReportData.setValidatedWordCount(validatedWordCount);
		endReportData.setTotalStringCount(totalStringCount);
		endReportData.setTotalWordCount(totalWordCount);
		endReportData.setErrorTypeScore(errorTypeScore);
		return endReportData;
	}
}
