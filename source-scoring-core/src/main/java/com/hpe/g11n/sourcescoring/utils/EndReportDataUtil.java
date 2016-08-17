package com.hpe.g11n.sourcescoring.utils;

import com.hpe.g11n.sourcescoring.pojo.EndReportData;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 上午11:21:57
 *
 */
public class EndReportDataUtil {
	public EndReportData getEndReportData(String errorType, int hitStrCount,
			int validatCount, int totalNCCount, int hitNCCount) {
		EndReportData endReportData = new EndReportData();
		endReportData.setErrorType(errorType);
		endReportData.setHitNCCount(hitNCCount);
		endReportData.setHitStrCount(hitStrCount);
		endReportData.setTotalNCCount(totalNCCount);
		endReportData.setValidatCount(validatCount);
		endReportData.setDupliCount(hitStrCount - validatCount);
		return endReportData;
	}
}
