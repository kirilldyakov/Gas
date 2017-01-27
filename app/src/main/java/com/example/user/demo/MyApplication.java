package com.example.user.demo;

import android.app.Application;

import com.example.user.demo.database.HelperFactory;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}