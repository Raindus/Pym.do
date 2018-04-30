package com.raindus.pymdo;

import android.app.Application;

import com.raindus.pymdo.focus.entity.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Created by Raindus on 2018/4/25.
 */

public class PymdoApplication extends Application {

    private static Application mApplication;
    private static BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mBoxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public static Application get() {
        return mApplication;
    }

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }
}
