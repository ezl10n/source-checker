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
	public ReportDataCount getEndReportData(String errorType, int hitStrCount,
			int validCount, int totalNCCount, int hitNCCount,BigDecimal errorTypeScore) {
		ReportDataCount endReportData = new ReportDataCount();
		endReportData.setErrorType(errorType);
		endReportData.setHitWordCount(hitNCCount);
		endReportData.setHitStringCount(hitStrCount);
		endReportData.setTotalWordCount(totalNCCount);
		endReportData.setValidatedCount(validCount);
		endReportData.setDuplicatedCount(hitStrCount - validCount);
		endReportData.setErrorTypeScore(errorTypeScore);
		return endReportData;
	}
}
