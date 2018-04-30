package com.raindus.pymdo.common;

import java.text.DecimalFormat;

/**
 * Created by Raindus on 2018/4/28.
 */

public class MathUtils {

    // 方向—（上、下、左、右）
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_RIGHT = 4;

    // 计算两坐标点之间的方向
    public static int calculateDirection(float x1, float y1, float x2, float y2) {
        // 绝对垂直
        if (x1 == x2) {
            if (y2 >= y1)
                return DIRECTION_DOWN;
            else
                return DIRECTION_UP;
        }
        // 斜率 k=0 水平方向 k>=0 垂直方向
        int k = (int) Math.abs((y2 - y1) / (x2 - x1));
        if (k == 0) {
            if (x2 >= x1)
                return DIRECTION_RIGHT;
            else
                return DIRECTION_LEFT;
        } else {
            if (y2 >= y1)
                return DIRECTION_DOWN;
            else
                return DIRECTION_UP;
        }
    }

    // 只取一位小数
    public static String getFloatOnePoint(float t, boolean isMin) {
        final DecimalFormat FloatValueFormat = new DecimalFormat("0.0");
        if (isMin)
            t /= 60f;
        return FloatValueFormat.format(t);
    }

    // 不保留小数
    public static String getFloatNoPoint(float t) {
        final DecimalFormat FloatValueFormat = new DecimalFormat("0");
        return FloatValueFormat.format(t);
    }

}
