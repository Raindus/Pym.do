package com.raindus.pymdo.ui.cover;

import android.content.Context;
import android.content.SharedPreferences;

import com.raindus.pymdo.PymdoApplication;

/**
 * Created by Raindus on 2018/4/29.
 */

public class CoverParam {

    private static final String COVER_NAME = "cover";

    private static final String KEY_VERTICAL_IN_DOWN = "inDown";
    private static final String KEY_HORIZONTAL_IN_RIGHT = "inRight";

    public static final boolean DEFAULT_VERTICAL_IN_DOWN = false;
    public static final boolean DEFAULT_HORIZONTAL_IN_RIGHT = false;

    public static SharedPreferences CovetShared() {
        return PymdoApplication.get().getSharedPreferences(COVER_NAME, Context.MODE_PRIVATE);
    }

    // 记录封面页 垂直控件 状态
    public static void setVerticalInDown(boolean inDown) {
        CovetShared().edit().putBoolean(KEY_VERTICAL_IN_DOWN, inDown).apply();
    }

    public static boolean getVerticalInDown() {
        return CovetShared().getBoolean(KEY_VERTICAL_IN_DOWN, DEFAULT_VERTICAL_IN_DOWN);
    }

    // 记录封面页 水平控件 状态
    public static void setHorizontalInRight(boolean inRight) {
        CovetShared().edit().putBoolean(KEY_HORIZONTAL_IN_RIGHT, inRight).apply();
    }

    public static boolean getHorizontalInRight() {
        return CovetShared().getBoolean(KEY_HORIZONTAL_IN_RIGHT, DEFAULT_HORIZONTAL_IN_RIGHT);
    }
}
