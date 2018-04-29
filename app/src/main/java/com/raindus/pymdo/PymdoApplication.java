package com.raindus.pymdo;

import android.app.Application;

/**
 * Created by Raindus on 2018/4/25.
 */

public class PymdoApplication extends Application {

    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application get() {
        return mApplication;
    }
}
