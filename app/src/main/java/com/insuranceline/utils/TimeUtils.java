package com.insuranceline.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Zeki Guler on 8/11/2015.
 */
public class TimeUtils {

    public final static int DATE_FORMAT_TYPE_1 = 1; // 12 Feb 2015 07:00 pm
    public final static int DATE_FORMAT_TYPE_2 = 2; // Fed 12 07:00 pm
    public final static int DATE_FORMAT_TYPE_3 = 3; // February 2015
    public final static int DATE_FORMAT_TYPE_4 = 4; // WED, 1 OCT, 11:09 AM


    public static long convertToUnixTimeStamp(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar cal = getCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute, second);

        return cal.getTimeInMillis();
    }

    @NonNull
    private static Calendar getCalendar(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,         monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH,  dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY,   hourOfDay);
        cal.set(Calendar.MINUTE,        minute);
        cal.set(Calendar.SECOND, second);
        return cal;
    }

    public static String converHumanReadbleFormat(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar cal = getCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute, second);

        return convertReadableDate(cal.getTimeInMillis(), DATE_FORMAT_TYPE_1);
    }

    /**
     * @param created_on in milisecond
     * @param formatType which format will be requested
     * */
    public static String convertReadableDate(long created_on, int formatType) {
        Date dt = new Date(created_on);
        if(formatType == DATE_FORMAT_TYPE_1){
            SimpleDateFormat dateformatter = new SimpleDateFormat("d MMM yyyy hh:mm a", Locale.US);
            return dateformatter.format(dt);
        }else if(formatType == DATE_FORMAT_TYPE_2) {
            SimpleDateFormat dateformatter = new SimpleDateFormat("MMM d HH:mm a", Locale.US);
            return dateformatter.format(dt);
        }else if(formatType == DATE_FORMAT_TYPE_3){
            SimpleDateFormat dateformatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
            return dateformatter.format(dt);
        }else if (formatType == DATE_FORMAT_TYPE_4){
            SimpleDateFormat dateformatter = new SimpleDateFormat("EEE, d MMM, HH:mm a", Locale.US);
            return dateformatter.format(dt);
        } else {
            SimpleDateFormat dateformatter = new SimpleDateFormat("MMM d HH:mm a", Locale.US);
            return dateformatter.format(dt);
        }

    }

    public static long getCurrentTime() {
        Calendar now = Calendar.getInstance();
        return now.getTimeInMillis();
    }

    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }
}
