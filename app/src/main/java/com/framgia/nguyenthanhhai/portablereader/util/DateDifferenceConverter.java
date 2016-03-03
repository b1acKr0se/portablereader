package com.framgia.nguyenthanhhai.portablereader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateDifferenceConverter {
    public static final String DIFFERENT_SINGLE_DAY = "%s day ago";
    public static final String DIFFERENT_MORE_DAYS = "%s days ago";
    public static final String DIFFERENT_SINGLE_HOUR = "%s hour ago";
    public static final String DIFFERENT_MORE_HOURS = "%s hours ago";
    public static final String DIFFERENT_SINGLE_MINUTE = "%s minute ago";
    public static final String DIFFERENT_MORE_MINUTES = "%s minutes ago";
    public static final String UNDEFINED_TIME = "undefined time";

    public static String getDateDifference(String input) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calendar = Calendar.getInstance();
        String current = format.format(calendar.getTime());
        int differentInDays;
        Date inputDate;
        Date currentDate;
        try {
            inputDate = format.parse(input);
            currentDate = format.parse(current);
            long difference = currentDate.getTime() - inputDate.getTime();
            differentInDays = (int) (difference / (1000 * 60 * 60 * 24));
            if (differentInDays > 0) {
                if (differentInDays == 1) {
                    return String.format(DIFFERENT_SINGLE_DAY, String.valueOf(differentInDays));
                } else {
                    return String.format(DIFFERENT_MORE_DAYS, String.valueOf(differentInDays));
                }
            } else {
                int differentInHours = (int) (difference / (1000 * 60 * 60));
                if (differentInHours > 0) {
                    if (differentInHours == 1) {
                        return String.format(DIFFERENT_SINGLE_HOUR, String.valueOf(differentInHours));
                    } else {
                        return String.format(DIFFERENT_MORE_HOURS, String.valueOf(differentInHours));
                    }
                } else {
                    int differentInMinutes = (int) ((difference / (60 * 1000) % 60));
                    if (differentInMinutes == 0 | differentInMinutes == 1) {
                        return String.format(DIFFERENT_SINGLE_MINUTE, String.valueOf(differentInMinutes));
                    } else {
                        return String.format(DIFFERENT_MORE_MINUTES, String.valueOf(differentInMinutes));
                    }
                }
            }
        } catch (ParseException e) {
            return UNDEFINED_TIME;
        }
    }

    public static boolean isOutdated(String date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calendar = Calendar.getInstance();
        String current = format.format(calendar.getTime());
        int differentInDays;
        Date inputDate;
        Date currentDate;
        try {
            inputDate = format.parse(date);
            currentDate = format.parse(current);
            long difference = currentDate.getTime() - inputDate.getTime();
            differentInDays = (int) (difference / (1000 * 60 * 60 * 24));
            return differentInDays > 10;
        } catch (ParseException e) {
            return false;
        }
    }
}
