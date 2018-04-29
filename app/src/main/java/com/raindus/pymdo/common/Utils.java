package com.raindus.pymdo.common;

import com.raindus.pymdo.PymdoApplication;

/**
 * Created by Raindus on 2018/4/29.
 */

public class Utils {

    /**
     * dp转px
     *
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(float dpValue) {
        final float scale = PymdoApplication.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
