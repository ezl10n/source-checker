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
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		Long durationDate = Long.valueOf((sdf.format(endDate))) - Long.valueOf((sdf.format(startDate)));

		long hours = durationDate / (60 * 60);
		long minutes = (durationDate - hours
				* (60 * 60))
				/ (60);
		long second = durationDate - hours
				* (60 * 60) -minutes *(60);
		return hours+" hours "+minutes+" minutes "+second+" seconds";
	}
}
