package com.elearning.tm.android.client.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

public class DateTimeHelper {
	private static final String TAG = "DateTimeHelper";

	// Wed Dec 15 02:53:36 +0000 2010
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			"E MMM d HH:mm:ss Z yyyy", Locale.US);

	public static final DateFormat SEARCH_API_DATE_FORMATTER = new SimpleDateFormat(
			"E, d MMM yyyy HH:mm:ss Z", Locale.US); // TODO: Z -> z ?

	public static final Date parseDateTime(String dateString) {
		try {
			Log.v(TAG, String.format("in parseDateTime, dateString=%s",
					dateString));
			return DATE_FORMATTER.parse(dateString);
		} catch (ParseException e) {
			Log.w(TAG, "Could not parse Tm date string: " + dateString);
			return null;
		}
	}

	public static final Date parseSearchApiDateTime(String dateString) {
		try {
			return SEARCH_API_DATE_FORMATTER.parse(dateString);
		} catch (ParseException e) {
			Log.w(TAG, "Could not parse Twitter search date string: "
					+ dateString);
			return null;
		}
	}

	public static final DateFormat AGO_FULL_DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String getRelativeDate(Date date) {
		Date now = new Date();
		// Seconds.
		long diff = (now.getTime() - date.getTime()) / 1000;
		if (diff < 0) {
			diff = 0;
		}
		// Minutes.
		diff /= 60;
		// Hours.
		diff /= 60;
		return AGO_FULL_DATE_FORMATTER.format(date);
	}

	public static long getNowTime() {
		return Calendar.getInstance().getTime().getTime();
	}

	/**
	 * 格式化string为Date
	 * 
	 * @param datestr
	 * @return date
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {

				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转化为String
	 * 
	 * @param date
	 * @return date string
	 */
	public static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static Calendar getADayOfWeek(Calendar day, int dayOfWeek) {
		int week = day.get(Calendar.DAY_OF_WEEK);
		if (week == dayOfWeek)
			return day;
		int diffDay = dayOfWeek - week;
		if (week == Calendar.SUNDAY) {
			diffDay -= 7;
		} else if (dayOfWeek == Calendar.SUNDAY) {
			diffDay += 7;
		}
		day.add(Calendar.DATE, diffDay);
		return day;
	}

}
