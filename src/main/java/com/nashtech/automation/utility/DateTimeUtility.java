package com.nashtech.automation.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtility {
	/**
	 * @param timeStamp
	 * @return
	 */
	public static String getCurrentDateTimeString(String timeStamp) {

		DateFormat dateFormat = new SimpleDateFormat(timeStamp);
		Date date = new Date();

		return dateFormat.format(date);
	}
}
