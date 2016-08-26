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

	public String getDurationDate(Date startDate, Date enddate) throws ParseException {
		Long durationDate = enddate.getTime() - startDate.getTime();

		long hours = durationDate / (1000 * 60 * 60);
		long minutes = (durationDate - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		long second = (durationDate - hours
				* (1000 * 60 * 60) -minutes *(1000 * 60))/1000;
		return hours+" hours "+minutes+" minutes "+second+" s";
	}
}
