package com.example.tolean.ibook.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.utils.ThemUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate{

    @BindView(R.id.scan_toolbar)
    Toolbar mScanToolbar;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    @BindView(R.id.flashlight)
    ImageView mFlashLight;
    private boolean IsFlashnightOpen=false;
    private final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY=666;
    public static String picturePath;
    private static DecodePhotoTask mDecodePhotoTask=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setSupportActionBar(mScanToolbar);
        ActionBar actionBar_=getSupportActionBar();
        if(actionBar_!=null){
            actionBar_.setDisplayHomeAsUpEnabled(true);
        }
        setListener();
    }

    @Override
    protected void initThem() {
    }

    private void setListener() {

        mZxingview.setDelegate(this);
        mFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsFlashnightOpen){
                    mZxingview.closeFlashlight();
                    mFlashLight.setImageResource(R.drawable.flashlight_off);
                }else {
                    mZxingview.openFlashlight();
                    mFlashLight.setImageResource(R.drawable.flashlight_on);
                }
                IsFlashnightOpen=!IsFlashnightOpen;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scan_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.show_album:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startCamera();
        mZxingview.showScanRect();
        mZxingview.startSpot();

    }

    @Override
    protected void onStop() {
        mZxingview.stopCamera();
        mFlashLight.setImageResource(R.drawable.flashlight_off);
        IsFlashnightOpen=false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingview.onDestroy();
        if(mDecodePhotoTask!=null){
            mDecodePhotoTask.cancel(true);
        }
        super.onDestroy();
    }
    private void vibrate(){
        Vibrator vibrator_=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator_.vibrate(200);
    }
    @Override
    protected void handleThemChange() {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        CommonUtil.handleScanResult(this,result);
        vibrate();
        mZxingview.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(ScanActivity.this, "打开相机错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZxingview.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {

            picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);

            mDecodePhotoTask = new DecodePhotoTask(this);
            mDecodePhotoTask.execute();
        }

    }
    private  class DecodePhotoTask extends AsyncTask<Void,Void,String>{
        //这里用到了弱引用
        private WeakReference<Context>weakReference;
        private DecodePhotoTask(Context context) {
            weakReference=new WeakReference<Context>(context);
        }
        @Override
        protected String doInBackground(Void... voids) {
            return QRCodeDecoder.syncDecodeQRCode(picturePath);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CommonUtil.handleScanResult(weakReference.get(),s);
        }
    }

}
