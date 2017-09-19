package com.example.tolean.ibook.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.base.MyApplication;
import com.example.tolean.ibook.view.BookDetailActivity;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Tolean on 2017/9/9.
 */

public class CommonUtil {
//    static Context mContext= MyApplication.mContext;
//    public static int[] mColor={
//            mContext.getResources().getColor(R.color.brown),
//            mContext.getResources().getColor(R.color.white),
//            mContext.getResources().getColor(R.color.pink),
//            mContext.getResources().getColor(R.color.black),
//            mContext.getResources().getColor(R.color.blue),
//            mContext.getResources().getColor(R.color.gray),
//            mContext.getResources().getColor(R.color.green),
//            mContext.getResources().getColor(R.color.orange),
//            mContext.getResources().getColor(R.color.red),
//    };
    public static final String BROADCASETHEMCHANGE="com.example.tolean.ibook.THEMCHANGE";
    public static final int RECYCLERVIEW_ITEM=0;
    public static final int RECYCLERVIEW_BOTTOM=1;
    public static final String[] VIEWPAGER_TAGS={
        "文学","科技","生活","top250"
    };
    public static final String BASE_URL="https://api.douban.com/v2/";
    public static final long SEARCH_BOOK_COUNT=20;
    //检测是否有网络连接
    public static boolean isConnectivity(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }
    /**
     * 强制隐藏输入法键盘
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     * @param context
     */
    public static void hideOrShowSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //处理扫描到的
    public static void handleScanResult(Context context,String result){
        Log.d("hello","result = "+result);
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(context,"格式不支持",Toast.LENGTH_SHORT).show();
        } else if(result .startsWith("http://")||result.startsWith("https://")){
            searchWeb(context,result);
        }else if((result.length()==13&&result.startsWith("978"))||result.length()==10){
            vibrate(context);
            requestForBook(context,result);
        } else{
            Toast.makeText(context,"格式不支持",Toast.LENGTH_SHORT).show();
        }
    }
    private static void requestForBook(Context context,String result) {
        Bundle bundle_=new Bundle();
        bundle_.putString("book_ibsn",result);
        bundle_.putInt("mode",1);
        Intent intent = new Intent(context,BookDetailActivity.class);
        intent.putExtras(bundle_);
        context.startActivity(intent);
    }
    public static void searchWeb(Context context ,String query)
    {
        Uri uri = Uri.parse(query);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);
        }

//        Intent intent = new Intent(context, ScanWebActivity.class);
//        intent.putExtra(SCAN_WEB_URL,query);
//        context.startActivity(intent);
    }
    private static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
