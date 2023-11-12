package com.distrupify.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    public static Date from(String dateString) throws ParseException {
        return defaultDateFormat.parse(dateString);
    }

    public static Date from(String dateString, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateString);
    }
}
