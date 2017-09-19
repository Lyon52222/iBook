package com.example.tolean.ibook.base;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.example.tolean.ibook.utils.ActivityManager;
import com.example.tolean.ibook.utils.CommonUtil;

/**
 * Created by Tolean on 2017/9/9.
 */
//所有Activity的基类
public abstract class BaseActivity extends AppCompatActivity {
    private ThemChangeReceiver mThemChangeReceiver;
    protected String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getClass().getSimpleName();
        ActivityManager.getInstance().addActivity(this);
        initBroadcaseReceiver();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG=getClass().getSimpleName();
        ActivityManager.getInstance().addActivity(this);
        initBroadcaseReceiver();
    }
    protected abstract void initView();
    protected abstract void initThem();
    private void initBroadcaseReceiver(){
        IntentFilter filter_=new IntentFilter();
        filter_.addAction(CommonUtil.BROADCASETHEMCHANGE);
        mThemChangeReceiver=new ThemChangeReceiver();
        registerReceiver(mThemChangeReceiver,filter_);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mThemChangeReceiver);
        ActivityManager.getInstance().removeActivity(this);
    }

    private class ThemChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            handleThemChange();
        }
    }
    //设置沉浸式状态栏
    protected abstract void handleThemChange();
    protected void applyKitKatTranslucency(int color) {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            // mTintManager.setStatusBarTintResource(R.color.red_base);//通知栏所需颜色
            mTintManager.setStatusBarTintColor(color);
        }

    }
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
