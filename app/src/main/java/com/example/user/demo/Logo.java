package com.example.user.demo;

import android.app.Activity;
import android.os.SystemClock;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

/**
 * Created by user on 19.12.16.
 */

@EActivity(R.layout.logo)
public class Logo extends Activity {
    @AfterViews
    protected void init()
    {
        waitInBackGrond();
    }


    @Background
    void waitInBackGrond(){
        SystemClock.sleep(3000);
        Main_.intent(this).start();
    }

}