package com.android.picshow.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.picshow.R;
import com.android.picshow.adapter.PhotoPageAdapter;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.PhotoDataLoader;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by yuntao.wei on 2017/12/9.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoActivity extends AppCompatActivity implements PhotoDataLoader.PhotoLoadListener {

    private static final String TAG = "PhotoActivity";

    private int currentID;
    private int bucketID;
    private String currentPath;
    private PhotoView photoView;
    private ViewPager photoPager;
    private PhotoPageAdapter photoPageAdapter;
    private PhotoDataLoader mLoader;
    private Handler mainHandler;
    private final static int UPDATE = 0x111;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picshow_photo_page);
        init();
        initView();
    }

    private void init() {
        Intent intent = getIntent();
        currentPath = intent.getStringExtra(MediaSetUtils.PHOTO_PATH);
        currentID = intent.getIntExtra(MediaSetUtils.PHOTO_ID, PhotoDataLoader.INVALID);
        bucketID = intent.getIntExtra(MediaSetUtils.BUCKET, MediaSetUtils.CAMERA_BUCKET_ID);
        mLoader = new PhotoDataLoader(getApplication(), bucketID, this);
        mainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE:
                        if(photoPager != null) {
                            photoPager.setAdapter(photoPageAdapter);
                            photoPager.setCurrentItem(currentID);
                        }
                        break;
                }
            }

        };
    }

    private void initView() {
        photoPager = findViewById(R.id.photo_pager);
        photoPager.setOffscreenPageLimit(PicShowUtils.MAX_LOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLoader != null)
            mLoader.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLoader != null)
            mLoader.pause();
        GlideApp.get(getApplicationContext()).clearMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void startLoad() {

    }

    @Override
    public void loadFinish(Cursor cursor) {
        LogPrinter.i(TAG, "loadFinish:" + cursor.getCount());
        photoPageAdapter = new PhotoPageAdapter(getApplicationContext(), getSupportFragmentManager(), cursor);
        mainHandler.sendEmptyMessage(UPDATE);
    }
}