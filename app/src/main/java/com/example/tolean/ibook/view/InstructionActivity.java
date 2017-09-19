package com.example.tolean.ibook.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.utils.ThemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructionActivity extends BaseActivity {
    @BindView(R.id.nav_instruction_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_instruction_textview)
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_instruction);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        mToolbar.setTitle("说明");
        mTextView.setText(R.string.instruction);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initThem() {
        mToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
    }

    @Override
    protected void handleThemChange() {
        mToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
    }
}
