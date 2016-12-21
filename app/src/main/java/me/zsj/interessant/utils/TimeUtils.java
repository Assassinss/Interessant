package me.zsj.interessant.utils;

/**
 * @author zsj
 */

public class TimeUtils {

    private static final String ZERO = "0";
    private static final String COLON_SYMBOL = ":";

    private TimeUtils() {}

    public static int millsToSec(int mills) {
        return mills / 1000;
    }

    public static String secToTime(int time) {
        String timeStr;
        int hour = time / 3600;
        int minute = time / 60 % 60;
        int second = time % 60;
        if (hour == 0) {
            timeStr = unitFormat(minute) + COLON_SYMBOL + unitFormat(second);
        } else {
            timeStr = unitFormat(hour) + COLON_SYMBOL +
                    unitFormat(minute) + COLON_SYMBOL + unitFormat(second);
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = ZERO + Integer.toString(i);
        } else {
            retStr = String.valueOf(i);
        }
        return retStr;
    }

}
