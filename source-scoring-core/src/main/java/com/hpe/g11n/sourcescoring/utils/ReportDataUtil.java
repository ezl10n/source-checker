package com.hpe.g11n.sourcescoring.utils;

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
			int validCount, int totalNCCount, int hitNCCount) {
		ReportDataCount endReportData = new ReportDataCount();
		endReportData.setErrorType(errorType);
		endReportData.setHitNCCount(hitNCCount);
		endReportData.setHitStrCount(hitStrCount);
		endReportData.setTotalNCCount(totalNCCount);
		endReportData.setValidCount(validCount);
		endReportData.setDupliCount(hitStrCount - validCount);
		return endReportData;
	}
}
