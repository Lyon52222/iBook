package com.example.tolean.ibook.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tolean on 2017/9/16.
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        mContext=getApplicationContext();
        super.onCreate();
    }
}
