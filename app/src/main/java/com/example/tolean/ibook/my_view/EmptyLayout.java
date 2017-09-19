package com.example.tolean.ibook.my_view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolean on 2017/9/9.
 */

public class EmptyLayout extends LinearLayout implements View.OnClickListener {
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int HIDE_LAYOUT = 4;
    public static final int NODATA_ENABLE_CLICK = 5;
    private String TAG=getClass().getSimpleName();
    @BindView(R.id.img_error_layout)
    ImageView mImgErrorLayout;
    @BindView(R.id.loading_progressbar)
    ProgressBar mLoadingProgressbar;
    @BindView(R.id.tv_error)
    TextView mTvError;
    @BindView(R.id.page_layout)
    RelativeLayout mPageLayout;
    private int mErrorState;
    private boolean isClickEnable=true;
    private Context mContext = null;
    private OnClickListener clicklistener=null;
    //在xml文件中定义时调用这个构造函数
    public EmptyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_error_layout, null);
        ButterKnife.bind(this, view);
        setBackgroundColor(-1);
        setOnClickListener(this);
        mImgErrorLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClickEnable){
                    setErrorType(NETWORK_LOADING);
                    if(clicklistener!=null){
                        clicklistener.onClick(view);
                    }
                }
            }
        });
        addView(view);
    }
    public void setErrorType(int errortype){
        setVisibility(View.VISIBLE);
        switch (errortype){
            case NETWORK_ERROR:
                mErrorState=NETWORK_ERROR;
                if(CommonUtil.isConnectivity(mContext)){
                    mTvError.setText(R.string.error_view_loading_error_click_to_refresh);
                    mImgErrorLayout.setBackgroundResource(R.drawable.page_icon_failed);
                }else{
                    mTvError.setText(R.string.error_view_network_error_click_to_refresh);
                    mImgErrorLayout.setBackgroundResource(R.drawable.page_icon_network);
                }
                mImgErrorLayout.setVisibility(View.VISIBLE);
                mLoadingProgressbar.setVisibility(View.GONE);
                isClickEnable=true;
                break;
            case NETWORK_LOADING:
                mErrorState=NETWORK_LOADING;
                mLoadingProgressbar.setVisibility(VISIBLE);
                mImgErrorLayout.setVisibility(GONE);
                mTvError.setText(R.string.error_view_loading);
                isClickEnable=false;
                break;
            case NODATA:
                mErrorState=NODATA;
                mImgErrorLayout.setImageResource(R.drawable.page_icon_empty);
                mImgErrorLayout.setVisibility(VISIBLE);
                mLoadingProgressbar.setVisibility(GONE);
                mTvError.setText("");
                isClickEnable=true;
                break;
            case HIDE_LAYOUT:
                setVisibility(GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState=NODATA_ENABLE_CLICK;
                mImgErrorLayout.setImageResource(R.drawable.page_icon_empty);
                mImgErrorLayout.setVisibility(VISIBLE);
                mLoadingProgressbar.setVisibility(GONE);
                mTvError.setText(R.string.error_view_nodata);
                isClickEnable=true;
                break;
            default: break;
        }
    }
    @Override
    public void onClick(View view) {
        if(isClickEnable){
            setErrorType(NETWORK_LOADING);
            if(clicklistener!=null){
                clicklistener.onClick(view);
            }else{
                Log.i(TAG,"no clicklistener");
            }
        }
    }
    public void setOnLayoutClickListener(OnClickListener clicklistener){
        this.clicklistener=clicklistener;
    }
    @Override
    public void setVisibility(int visibility) {
        if(visibility==GONE){
            mErrorState=HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }
}
