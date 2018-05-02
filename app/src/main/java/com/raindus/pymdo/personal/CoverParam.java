package com.raindus.pymdo.personal;

import android.content.Context;
import android.content.SharedPreferences;

import com.raindus.pymdo.PymdoApplication;
import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/4/29.
 */

public class CoverParam {

    private static final String COVER_NAME = "cover";

    private static final String KEY_VERTICAL_IN_DOWN = "inDown";
    private static final String KEY_HORIZONTAL_IN_RIGHT = "inRight";

    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_COVER = "cover";
    private static final String KEY_BANNER = "banner";
    private static final String KEY_REPORT = "report";

    public static final boolean DEFAULT_VERTICAL_IN_DOWN = false;
    public static final boolean DEFAULT_HORIZONTAL_IN_RIGHT = false;

    // int -> path
    public static final String DEFAULT_NICKNAME = String.valueOf(R.string.app_name);
    public static final String DEFAULT_COVER = String.valueOf(R.drawable.bg_main);
    public static final String DEFAULT_BANNER = String.valueOf(R.drawable.bg_main_banner);
    public static final String DEFAULT_REPORT = String.valueOf(R.drawable.bg_main_report);

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

    // 封面页 昵称
    public static void setNickname(String nickname) {
        CovetShared().edit().putString(KEY_NICKNAME, nickname).apply();
    }

    public static String getNickname() {
        return CovetShared().getString(KEY_NICKNAME, DEFAULT_NICKNAME);
    }

    public static boolean isNicknameDefault() {
        return getNickname().equals(DEFAULT_NICKNAME);
    }

    // 封面页 封面
    public static void setCover(String cover) {
        CovetShared().edit().putString(KEY_COVER, cover).apply();
    }

    public static String getCover() {
        return CovetShared().getString(KEY_COVER, DEFAULT_COVER);
    }

    public static boolean isCoverDefault() {
        return getCover().equals(DEFAULT_COVER);
    }

    // 封面页 banner
    public static void setBanner(String banner) {
        CovetShared().edit().putString(KEY_BANNER, banner).apply();
    }

    public static String getBanner() {
        return CovetShared().getString(KEY_BANNER, DEFAULT_BANNER);
    }

    public static boolean isBannerDefault() {
        return getBanner().equals(DEFAULT_BANNER);
    }

    // 封面页 报告
    public static void setReport(String report) {
        CovetShared().edit().putString(KEY_REPORT, report).apply();
    }

    public static String getReport() {
        return CovetShared().getString(KEY_REPORT, DEFAULT_REPORT);
    }

    public static boolean isReportDefault() {
        return getReport().equals(DEFAULT_REPORT);
    }
}
