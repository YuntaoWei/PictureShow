package com.android.picshow.app;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import picshow.android.com.R;

public class PicShowActivity extends BaseActivity {

    private ViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picshow_main);
        if(isPermissionGranted()) {
            initView();
        }
    }

    private void initView(){}


    @Override
    protected void onGetPermissionsFailure() {
        finish();
    }

    @Override
    protected void onGetPermissionsSuccess() {
        initView();
    }
}
