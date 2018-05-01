package com.raindus.pymdo.common;

import java.util.Date;

/**
 * Created by Raindus on 2018/4/29.
 */

public class DateUtils {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_WEEK = 7 * ONE_DAY;

    // true = begin; false = end
    public static long getTodayTime(boolean beginOrEnd) {
        Date cur = new Date();
        long time = new Date(cur.getYear(), cur.getMonth(), cur.getDate()).getTime();
        return beginOrEnd ? time : time + ONE_DAY - 1;
    }

    public static boolean isToday(long time) {
        long begin = getTodayTime(true);
        long end = getTodayTime(false);
        if (time >= begin && time <= end)
            return true;
        else
            return false;
    }

    /**
     * @return 返回该月份的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        boolean isLeapYear = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return isLeapYear ? 29 : 28;
        }
        return -1;
    }

    // X h X min
    public static String parseTime(int time) {
        StringBuilder builder = new StringBuilder();
        int hour = time / 60;
        int min = time % 60;
        if (hour != 0) {
            builder.append(hour).append("h");
        }
        builder.append(min);
        if (min != 0)
            builder.append("min");
        return builder.toString();
    }
}
