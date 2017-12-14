package com.android.picshow.app;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.picshow.R;
import com.android.picshow.data.AlbumDataLoader;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.LoadListener;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.ui.TimeLineAdapter;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class AlbumPage extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "AlbumPage";

    private static final int UPDATE = 0x111;
    private GridView gridView;
    private TimeLineAdapter mAdapter;
    private int decodeBitmapWidth;
    private LoadListener myLoadListener;
    private Handler mainHandler;
    private AlbumDataLoader albumDataLoader;
    private int bucketID;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picshow_album);
        init();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        albumDataLoader.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        albumDataLoader.pause();
        GlideApp.get(getApplicationContext()).clearMemory();
    }

    private void init() {
        decodeBitmapWidth = PicShowUtils.getImageWidth(getApplicationContext());
        myLoadListener = new LoadListener() {
            @Override
            public void startLoad() {
                LogPrinter.i(TAG,"startLoad");
            }

            @Override
            public void finishLoad(Object[] items) {
                LogPrinter.i(TAG,"finishLoad:" + mAdapter);
                Message msg = mainHandler.obtainMessage();
                msg.what = UPDATE;
                msg.obj = items;
                mainHandler.sendMessage(msg);
            }
        };

        bucketID = getIntent().getIntExtra(MediaSetUtils.BUCKET, MediaSetUtils.CAMERA_BUCKET_ID);

        albumDataLoader = new AlbumDataLoader(getApplication(), myLoadListener, bucketID);

        mainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE:
                        if(mAdapter != null) {
                            mAdapter.setData((PhotoItem[]) msg.obj);
                        }
                        break;

                    default:

                        break;
                }
            }

        };

    }

    private void initView() {
        gridView = findViewById(R.id.grid);
        mToolbar = findViewById(R.id.topbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle(getIntent().getStringExtra(MediaSetUtils.SET_NAME));
        mAdapter = new TimeLineAdapter(this);
        //mAdapter.setDecodeSize(decodeBitmapWidth);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoItem item = mAdapter.getItem(position);
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(MediaSetUtils.PHOTO_ID, position);
        intent.putExtra(MediaSetUtils.PHOTO_PATH, item.getPath());
        intent.putExtra(MediaSetUtils.BUCKET, bucketID);
        startActivity(intent);
    }
}
