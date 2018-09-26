package com.android.picshow.app;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.picshow.R;
import com.android.picshow.presenter.BaseActivity;
import com.android.picshow.utils.PageFactory;
import com.android.picshow.view.activity.PicShowActivityDelegate;


public class PicShowActivity extends BaseActivity<PicShowActivityDelegate> implements View.OnClickListener, ViewPager.OnPageChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.btn_album, R.id.btn_photo, R.id.search_photo);
        viewDelegate.setOnFragmentPageChangeListener(this, R.id.vpager);
    }

    @Override
    protected void initView() {
        super.initView();
        viewDelegate.changeButtonSelectedStatus(PageFactory.INDEX_TIMELINE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album:
                viewDelegate.switchPage(PageFactory.INDEX_ALBUMSET);
                break;

            case R.id.btn_photo:
                viewDelegate.switchPage(PageFactory.INDEX_TIMELINE);
                break;

            case R.id.search_photo:

                break;

            default:
                break;
        }
    }

    @Override
    protected Class getDelegateClass() {
        return PicShowActivityDelegate.class;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewDelegate.changeButtonSelectedStatus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
