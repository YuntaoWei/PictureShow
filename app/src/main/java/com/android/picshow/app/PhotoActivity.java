package com.android.picshow.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.picshow.R;
import com.android.picshow.adapter.PhotoPageAdapter;
import com.android.picshow.data.PhotoDataLoader;
import com.android.picshow.utils.MediaSetUtils;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by yuntao.wei on 2017/12/9.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoActivity extends AppCompatActivity {

    private int currentID;
    private int bucketID;
    private String currentPath;
    private PhotoView photoView;
    private ViewPager photoPager;
    private PhotoPageAdapter photoPageAdapter;

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
        makeCursor();
    }

    private void initView() {
        photoPager = findViewById(R.id.photo_pager);
        photoPageAdapter = new PhotoPageAdapter(getApplicationContext(), getSupportFragmentManager(), null);
    }

    private void makeCursor() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
