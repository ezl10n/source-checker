package com.hpe.g11n.sourcescoring.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月26日
 * @Time: 上午8:35:28
 *
 */
public class DateUtil {
	public String format(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public String getDurationDate(Date startDate, Date endDate) throws ParseException {
		Long durationDate = endDate.getTime() - startDate.getTime();

		long hours = durationDate / (60 * 60*1000);
		long minutes = (durationDate - hours
				* (60 * 60*1000))
				/ (60*1000);
		long second = (durationDate - hours
				* (60 * 60*1000) -minutes *(60*1000))/1000;
		return hours+" hours "+minutes+" minutes "+second+" seconds";
	}
}
