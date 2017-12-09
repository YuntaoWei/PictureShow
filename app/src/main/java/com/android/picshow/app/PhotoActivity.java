package com.android.picshow.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.PhotoDataLoader;
import com.android.picshow.utils.MediaSetUtils;

/**
 * Created by yuntao.wei on 2017/12/9.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoActivity extends AppCompatActivity {

    private ImageView imgShow;
    private int currentID;
    private String currentPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.picshow_photo_page);
        init();
        initView();
    }

    private void init() {
        Intent intent = getIntent();
        currentPath = intent.getStringExtra(MediaSetUtils.PHOTO_PATH);
        currentID = intent.getIntExtra(MediaSetUtils.PHOTO_ID, PhotoDataLoader.INVALID);
    }

    private void initView() {
        imgShow = findViewById(R.id.photo);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GlideApp.with(PhotoActivity.this)
                .load(currentPath)
                .centerCrop()
                .into(imgShow);
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
