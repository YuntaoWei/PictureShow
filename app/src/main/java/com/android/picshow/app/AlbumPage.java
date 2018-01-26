package com.android.picshow.app;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.GridView;

import com.android.picshow.R;
import com.android.picshow.data.AlbumDataLoader;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.LoadListener;
import com.android.picshow.data.Path;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.adapter.TimeLineAdapter;
import com.android.picshow.ui.MenuExecutor;
import com.android.picshow.ui.SelectionManager;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

import java.util.List;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class AlbumPage extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private static final String TAG = "AlbumPage";

    private static final int UPDATE = 0x111;
    private GridView gridView;
    private View bottomView;
    private TimeLineAdapter mAdapter;
    private int decodeBitmapWidth;
    private LoadListener myLoadListener;
    private Handler mainHandler;
    private AlbumDataLoader albumDataLoader;
    private int bucketID;
    private Toolbar mToolbar;
    private SelectionManager selectionManager;
    private SelectionManager.SelectionListener selectionListener;
    private Button btnShare, btnDelete, btnEdit, btnDetail;
    private MenuExecutor menuExecutor;

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
        if(albumDataLoader != null)
            albumDataLoader.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(albumDataLoader != null)
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

        selectionListener = new SelectionManager.SelectionListener() {
            @Override
            public void enterSelectionMode() {

            }

            @Override
            public void exitSelectionMode() {
                bottomView.setVisibility(View.GONE);
                mAdapter.setSelectState(false);
            }

            @Override
            public void onSelectionChange(Path p, boolean select) {
                if(selectionManager.getSelectCount() > 1) {
                    if(btnEdit != null) {
                        btnEdit.setClickable(false);
                        btnEdit.setAlpha(0.3f);
                    }
                    if(btnDetail != null) {
                        btnDetail.setClickable(false);
                        btnDetail.setAlpha(0.3f);
                    }
                } else {
                    if (btnEdit != null) {
                        btnEdit.setClickable(true);
                        btnEdit.setAlpha(1.0f);
                    }
                    if (btnDetail != null) {
                        btnDetail.setClickable(true);
                        btnDetail.setAlpha(1.0f);
                    }
                }
            }
        };

    }

    private void initView() {
        gridView = findViewById(R.id.grid);
        mToolbar = findViewById(R.id.topbar);
        bottomView = findViewById(R.id.bottom_layout);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle(getIntent().getStringExtra(MediaSetUtils.SET_NAME));
        mAdapter = new TimeLineAdapter(this);
        mAdapter.setDecodeSize(decodeBitmapWidth);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        selectionManager = new SelectionManager();
        selectionManager.setSelectionListener(selectionListener);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        menuExecutor = new MenuExecutor(AlbumPage.this);
        initBottomMenu();
    }

    private void initBottomMenu() {

        if(bottomView == null) return;

        btnDelete = bottomView.findViewById(R.id.delete);
        btnDetail = bottomView.findViewById(R.id.more);
        btnEdit = bottomView.findViewById(R.id.edit);
        btnShare = bottomView.findViewById(R.id.share);
        final MenuExecutor.ExcuteListener excuteListener = new MenuExecutor.ExcuteListener() {

            @Override
            public void startExcute() {
                //show dialog here.
                showProgreeDialog();
            }

            @Override
            public void excuteSuccess() {
                //exit select mode and hide dialog.
                mAdapter.setSelectState(false);
                if(dialog != null) {
                    dialog.dismiss();
                }
                selectionManager.clearSelection();
            }

            @Override
            public void excuteFailed() {
                //some error occurs.
                if(dialog != null)
                    dialog.dismiss();
            }
        };

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.delete:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_DELETE,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;

                    case R.id.more:
                        PhotoItem item = mAdapter.getItem(selectionManager.getSelectPostion());
                        String path = item.getPath();
                        List<String> detail = PicShowUtils.getExifInfo(path);
                        String title = item.getTitle();
                        detail.add(0, "Title : /" + title);
                        detail.add("Path : /" + path);
                        PicShowUtils.showDetailDialog(AlbumPage.this, detail);
                        break;

                    case R.id.edit:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_EDIT,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;

                    case R.id.share:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_SHARE,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;


                }
            }
        };
        btnDelete.setOnClickListener(onclick);
        btnDetail.setOnClickListener(onclick);
        btnEdit.setOnClickListener(onclick);
        btnShare.setOnClickListener(onclick);

    }

    private ProgressDialog dialog;
    private void showProgreeDialog() {
        if(dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
        }
        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter != null)
            mAdapter.destroy();
        mAdapter = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter.getSelectState()) {
            PhotoItem item = mAdapter.getItem(position);
            updateItem(view, selectionManager.togglePath(position, item.toPath()));
        } else {
            PhotoItem item = mAdapter.getItem(position);
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra(MediaSetUtils.PHOTO_ID, position);
            intent.putExtra(MediaSetUtils.PHOTO_PATH, item.getPath());
            intent.putExtra(MediaSetUtils.BUCKET, bucketID);
            startActivity(intent);
        }
    }

    private void updateItem(View v, boolean select) {
        TimeLineAdapter.ViewHolder vh = (TimeLineAdapter.ViewHolder)v.getTag();
        if(vh != null) {
            vh.selectIcon.setChecked(select);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(!mAdapter.getSelectState()) {
            mAdapter.setSelectState(true);
            bottomView.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setSelectState(false);
            bottomView.setVisibility(View.GONE);
        }
        return true;
    }
}
