package com.myapp.sporify.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}