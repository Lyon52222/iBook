package com.example.tolean.ibook.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.adapter.ThemRecyclerViewAdapter;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.utils.ThemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemChangeActivity extends BaseActivity {

    @BindView(R.id.them_change_toolbar)
    Toolbar mThemChangeToolbar;
    @BindView(R.id.confirm)
    TextView mConfirm;
    private int[] mColor;
    @BindView(R.id.them_recyclerview)
    RecyclerView mThemRecyclerview;
    ThemRecyclerViewAdapter mAdapter = null;
    private LinearLayoutManager mLayoutManager = null;
    private int mPosition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
    }

    @Override
    protected void initView() {
        initColor();
        setContentView(R.layout.activity_them_change);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mThemRecyclerview.setLayoutManager(mLayoutManager);
        mAdapter = new ThemRecyclerViewAdapter(mColor);
        mThemRecyclerview.setAdapter(mAdapter);
        mThemChangeToolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        mThemChangeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAdapter.setOnItemClickListener(new ThemRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                mPosition=position;
                mThemChangeToolbar.setBackgroundColor(mColor[position]);
                applyKitKatTranslucency(mColor[position]);
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPosition!=-1) {
                    ThemUtil.setThemColor(mColor[mPosition]);
                    Intent intent_ = new Intent(CommonUtil.BROADCASETHEMCHANGE);
                    sendBroadcast(intent_);
                    finish();
                }
            }
        });
    }

    @Override
    protected void initThem() {
        mThemChangeToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
    }

    private void initColor() {
        mColor = new int[]{
                getResources().getColor(R.color.brown),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.pink),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.colorAccent)
        };
    }
    @Override
    protected void handleThemChange() {
        mThemChangeToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
    }
}
