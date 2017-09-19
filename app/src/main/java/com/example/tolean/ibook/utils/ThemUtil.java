package com.example.tolean.ibook.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.example.tolean.ibook.base.MyApplication;

/**
 * Created by Tolean on 2017/9/16.
 */

public class ThemUtil {
    private static int mColorTransParent= Color.TRANSPARENT;
    private static Context mContext= MyApplication.mContext;

    public static int getThemColor(){
        SharedPreferences sharedPreferences_=mContext.getSharedPreferences("Them",Context.MODE_PRIVATE);
        return sharedPreferences_.getInt("themcolor",Color.GRAY);
        }
    public static void setThemColor(int color){
        SharedPreferences sharedPreferences_=mContext.getSharedPreferences("Them",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_=sharedPreferences_.edit();
        editor_.putInt("themcolor",color);
        editor_.commit();
    }
}
