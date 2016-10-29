package me.zsj.interessant.utils;

/**
 * Created by zsj on 2016/10/17.
 */

public class TimeUtils {

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
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        } else {
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

}
