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
}
