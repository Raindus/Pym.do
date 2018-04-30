package com.raindus.pymdo.focus;

import android.content.Context;
import android.content.SharedPreferences;

import com.raindus.pymdo.PymdoApplication;
import com.raindus.pymdo.common.DateUtils;

/**
 * Created by Raindus on 2018/4/29.
 */

public class FocusParam {

    private static final String FOCUS_NAME = "focus";

    private static final String KEY_UPDATE = "update";
    private static final String KEY_TARGET = "target";
    private static final String KEY_TIME = "time";
    private static final String KEY_SHORT_REST = "shortRest";
    private static final String KEY_LONG_REST = "longRest";
    private static final String KEY_LONG_REST_INTERVAL = "longRestInterval";
    private static final String KEY_LIGHT = "light";
    private static final String KEY_MUSIC = "music";

    private static final long DEFAULT_UPDATE = -1;
    private static final int DEFAULT_TARGET = 60; // min
    private static final int DEFAULT_TIME = 25; // min
    private static final int DEFAULT_SHORT_REST = 5; // min
    private static final int DEFAULT_LONG_REST = 15; // min
    private static final int DEFAULT_LONG_REST_INTERVAL = 4; // 个
    private static final boolean DEFAULT_LIGHT = false;
    private static final boolean DEFAULT_MUSIC = true;

    public static SharedPreferences FocusShared() {
        return PymdoApplication.get().getSharedPreferences(FOCUS_NAME, Context.MODE_PRIVATE);
    }

    public static void setUpdate() {
        FocusShared().edit().putLong(KEY_UPDATE, System.currentTimeMillis()).apply();
    }

    public static boolean isTodayUpdate() {
        long time = FocusShared().getLong(KEY_UPDATE, DEFAULT_UPDATE);
        if (time == -1)
            return false;
        return DateUtils.isToday(time);
    }

    public static void setTarget(int time) {
        FocusShared().edit().putInt(KEY_TARGET, time).apply();
    }

    public static int getTarget() {
        return FocusShared().getInt(KEY_TARGET, DEFAULT_TARGET);
    }

    public static void setTime(int time) {
        FocusShared().edit().putInt(KEY_TIME, time).apply();
    }

    public static int getTime() {
        return FocusShared().getInt(KEY_TIME, DEFAULT_TIME);
    }

    public static void setShortRest(int time) {
        FocusShared().edit().putInt(KEY_SHORT_REST, time).apply();
    }

    public static int getShortRest() {
        return FocusShared().getInt(KEY_SHORT_REST, DEFAULT_SHORT_REST);
    }

    public static void setLongRest(int time) {
        FocusShared().edit().putInt(KEY_LONG_REST, time).apply();
    }

    public static int getLongRest() {
        return FocusShared().getInt(KEY_LONG_REST, DEFAULT_LONG_REST);
    }

    public static void setLongRestInterval(int time) {
        FocusShared().edit().putInt(KEY_LONG_REST_INTERVAL, time).apply();
    }

    public static int getLongRestInterval() {
        return FocusShared().getInt(KEY_LONG_REST_INTERVAL, DEFAULT_LONG_REST_INTERVAL);
    }

    public static void setLight(boolean isLight) {
        FocusShared().edit().putBoolean(KEY_LIGHT, isLight).apply();
    }

    public static boolean getLight() {
        return FocusShared().getBoolean(KEY_LIGHT, DEFAULT_LIGHT);
    }

    public static void setMusic(boolean isMusic) {
        FocusShared().edit().putBoolean(KEY_MUSIC, isMusic).apply();
    }

    public static boolean getMusic() {
        return FocusShared().getBoolean(KEY_MUSIC, DEFAULT_MUSIC);
    }
}
