package com.example.tolean.ibook.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.adapter.BookViewPagerAdapter;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.my_view.WowSplashView;
import com.example.tolean.ibook.my_view.WowView;
import com.example.tolean.ibook.utils.ActivityManager;
import com.example.tolean.ibook.utils.ThemUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.open_drawer)
    ImageView mOpenNav;
    @BindView(R.id.main_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.search_input)
    TextView mSearchInput;
    @BindView(R.id.search_bar_scan_btn)
    ImageView mSearchBarScanBtn;
    @BindView(R.id.main_search_toolbar)
    LinearLayout mSearchToolbar;
    BookViewPagerAdapter mBookViewPagerAdapter=null;
    FragmentManager mFragmentManager=null;


    private WowSplashView mWowSplashView;

    private WowView mWowView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);
        mWowSplashView.startAnimate();
        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
                mWowSplashView.setVisibility(View.GONE);
                mWowView.setVisibility(View.VISIBLE);
                mWowView.startAnimate(wowSplashView.getDrawingCache());
            }
        });

    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void initThem() {
        mSearchToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
        mTabLayout.setSelectedTabIndicatorColor(ThemUtil.getThemColor());
    }
    @Override
    protected void handleThemChange() {
        mSearchToolbar.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
        mTabLayout.setSelectedTabIndicatorColor(ThemUtil.getThemColor());
    }
    private void initViews(){
        mOpenNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        mSearchBarScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScanActivity.class));
            }
        });
        mFragmentManager=this.getSupportFragmentManager();
        mBookViewPagerAdapter=new BookViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mBookViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mBookViewPagerAdapter);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.i("Lyon","test");
        int id = item.getItemId();
        switch (id){
            case R.id.nav_introduction:
                startActivity(new Intent(MainActivity.this,IntroductionActivity.class));
                break;
            case R.id.nav_instruction:
                startActivity(new Intent(MainActivity.this,InstructionActivity.class));
                break;
            case R.id.nav_theme:
                startActivity(new Intent(MainActivity.this,ThemChangeActivity.class));
                break;
            case R.id.nav_favorites:
                startActivity(new Intent(MainActivity.this,FavoritesActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //实现连续两次点击退出程序.
    private boolean clicked_once=false;
    private Timer mTimer=new Timer();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                handleBack();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void handleBack(){
        if(clicked_once){
            finish();
            ActivityManager.getInstance().finishAllActivitys();
        }else{
            clicked_once=true;
            Snackbar.make(this.mViewPager,"再按一次退出程序",Snackbar.LENGTH_LONG).show();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    clicked_once=false;
                }
            },2000);
        }
    }
}
