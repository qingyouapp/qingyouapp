package com.xwq.qingyouapp.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateHandler {

	// 1990-6-15
	public static Date stringToDate(String dateStr) {
		String[] arr = dateStr.split("-");
		int year = Integer.parseInt(arr[0]);
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
		return new Date(year - 1900, month - 1, day);
	}

	// Nov 7, 3:01 PM
	public static String getFormatedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(" dd, hh:mm ");
		return date.toString().substring(4, 7) + sdf.format(date)
				+ (date.getHours() > 11 ? "PM" : "AM");
	}

	public static String dateToString(Date date) {
		return new SimpleDateFormat("yyMMddHHmmss").format(date);
	}

	public static boolean isToday(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		today = format.parse(format.format(today));
		date = format.parse(format.format(date));

		if (today.getTime() == date.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static Date toDate(String s) {

		Date date = new Date();
		int y = Integer.parseInt(s.substring(0, 2)) + 100;
		int m = Integer.parseInt(s.substring(2, 4)) - 1;
		int d = Integer.parseInt(s.substring(4, 6));
		int h = Integer.parseInt(s.substring(6, 8));
		int mi = Integer.parseInt(s.substring(8, 10));
		int se = Integer.parseInt(s.substring(10, 12));

		date.setYear(y);
		date.setMonth(m);
		date.setDate(d);
		date.setHours(h);
		date.setMinutes(mi);
		date.setSeconds(se);

		return date;
	}

	public static boolean isYesterday(Date date) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		today = format.parse(format.format(today));
		date = format.parse(format.format(date));

		if ((today.getTime() - date.getTime()) > 0
				&& (today.getTime() - date.getTime()) <= 86400000) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDayOfWeek(Date date) throws Exception {

		String str = null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			str = "SUNDAY";
			break;
		case 2:
			str = "MONDAY";
			break;
		case 3:
			str = "TUESDAY";
			break;
		case 4:
			str = "WEDNESDAY";
			break;
		case 5:
			str = "THURSDAY";
			break;
		case 6:
			str = "FRIDAY";
			break;
		case 7:
			str = "SATURDAY";
			break;
		}
		return str;
	}

	public static boolean isInLastWeek(Date date) throws ParseException {
		Date today = new Date();
		return isSameDate(date, today);
	}

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (subYear == 0) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}
}
