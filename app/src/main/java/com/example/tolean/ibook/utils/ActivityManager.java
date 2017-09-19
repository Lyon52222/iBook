package com.example.tolean.ibook.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Tolean on 2017/9/9.
 */
//用来管理所有的Activity
public class ActivityManager {
    private static ActivityManager sActivityManager=null;
    //保存所有的Activity
    private static ArrayList<Activity> sActivityArrayList=new ArrayList<>();
    public ActivityManager(){};
    //单列模式 整个APP中只有一个ActivityManager实例
    public static ActivityManager getInstance(){
        if(sActivityManager==null){
            sActivityManager=new ActivityManager();
        }
        return sActivityManager;
    }
    public void addActivity(Activity activity){sActivityArrayList.add(activity);}
    public void removeActivity(Activity activity){sActivityArrayList.remove(activity);}
    public void finishAllActivitys(){
        for(Activity activity:sActivityArrayList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
