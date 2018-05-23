package com.android.picshow.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.picshow.R;
import com.android.picshow.adapter.PhotoPageAdapter;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.PhotoDataLoader;
import com.android.picshow.ui.MenuExecutor;
import com.android.picshow.ui.PicPopupWindow;
import com.android.picshow.utils.ApiHelper;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuntao.wei on 2017/12/9.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoActivity extends AppCompatActivity implements PhotoDataLoader.PhotoLoadListener, View.OnClickListener {

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
    private final static int ENTER_FULL_SCREEN = 0x112;
    private final static int EXIT_FULL_SCREEN = 0x113;
    private Toolbar mToolbar;
    private boolean fullScreen = false;
    private View rootView;
    private View bottomView;
    private Button btnShare,btnEdit,btnDelete,btnMore;
    private String[] moreMenu;
    private MenuExecutor menuExecutor;
    private AlertDialog detailDialog;
    private LayoutInflater inflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PicShowUtils.extendLayoutToFulScreen(this);
        rootView = getLayoutInflater().inflate(R.layout.picshow_photo_page, null);
        setContentView(rootView);
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

                    case ENTER_FULL_SCREEN:
                        PicShowUtils.enterFullScreen(mToolbar);
                        break;

                    case EXIT_FULL_SCREEN:
                        PicShowUtils.exitFullScreen(mToolbar);
                        break;
                }
            }

        };
        menuExecutor = new MenuExecutor(this);
    }

    public void toggleFullScreen() {
        if(fullScreen) {
            exitFullScreen();
        } else {
            enterFullScreen();

        }
        toggleBottomView();
    }

    private void toggleBottomView() {
        if(bottomView == null)
            return;

        if(bottomView.getVisibility() == View.VISIBLE)
            bottomView.setVisibility(View.GONE);
        else
            bottomView.setVisibility(View.VISIBLE);
    }

    private void enterFullScreen() {
        if(mToolbar == null || rootView == null || bottomView == null) return;

        //bottomView.setVisibility(View.GONE);

        int flag = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        if(ApiHelper.HAS_VIEW_SYSTEM_UI_FLAG_LAYOUT_STABLE)
            flag = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        mToolbar.setVisibility(View.INVISIBLE);
        rootView.setSystemUiVisibility(flag);
        fullScreen = true;
    }

    private void exitFullScreen() {
        if(mToolbar == null || rootView == null || bottomView == null) return;

        //bottomView.setVisibility(View.VISIBLE);

        mToolbar.setVisibility(View.VISIBLE);
        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        fullScreen = false;
    }

    private void initView() {
        bottomView = findViewById(R.id.bottom_view);
        photoPager = findViewById(R.id.photo_pager);
        photoPager.setOffscreenPageLimit(PicShowUtils.MAX_LOAD);
        photoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentID = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        btnShare = findViewById(R.id.share);
        btnEdit = findViewById(R.id.edit);
        btnDelete = findViewById(R.id.delete);
        btnMore = findViewById(R.id.more);

        btnShare.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnMore.setOnClickListener(this);


        mToolbar = findViewById(R.id.photo_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("");
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
    protected void onStop() {
        super.onStop();
        if(fullScreen)
            exitFullScreen();
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

    @Override
    public void onClick(View v) {

        if(photoPageAdapter == null) return;

        Cursor c = (Cursor)photoPageAdapter.getDataItem(currentID);
        String type = c.getString(MediaSetUtils.INDEX_ITEM_TYPE);
        int rowID = c.getInt(MediaSetUtils.INDEX_ID);
        boolean image = PicShowUtils.isImage(type);
        Uri baseUri = image ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI :
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Uri u = baseUri.buildUpon().appendPath(String.valueOf(rowID)).build();
        ArrayList<Uri> list = new ArrayList<>();
        list.add(u);

        switch (v.getId()) {
            case R.id.share:
                menuExecutor.execute(MenuExecutor.MENU_ACTION_SHARE, list, image, null);
                break;

            case R.id.edit:
                menuExecutor.execute(MenuExecutor.MENU_ACTION_EDIT, list, image, null);
                break;

            case R.id.delete:
                menuExecutor.execute(MenuExecutor.MENU_ACTION_DELETE, list, image, null);
                break;

            case R.id.more:
                showMoreMenu();
                break;
        }

    }

    private void showMoreMenu() {
        if(moreMenu == null) {
            moreMenu = getResources().getStringArray(R.array.more_menu);
        }

        PicPopupWindow moreMenuWindow = PicPopupWindow.getTanyuPopupWindow(this);
        PicPopupWindow.PicPopupWindowListener itemClickListener = new PicPopupWindow.PicPopupWindowListener() {

            @Override
            public void onClick(View v) {
                String itemName = (String)v.getTag();
                if(itemName == null)
                    return;

                if(photoPageAdapter == null) return;
                Cursor c = (Cursor)photoPageAdapter.getDataItem(currentID);
                String type = c.getString(MediaSetUtils.INDEX_ITEM_TYPE);
                int rowID = c.getInt(MediaSetUtils.INDEX_ID);
                boolean image = PicShowUtils.isImage(type);
                Uri baseUri = image ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI :
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                Uri u = baseUri.buildUpon().appendPath(String.valueOf(rowID)).build();
                ArrayList<Uri> uris = new ArrayList<>();
                uris.add(u);

                Resources res = getResources();
                if(itemName.equals(res.getString(R.string.rename))) {
                    //rename
                    menuExecutor.execute(MenuExecutor.MENU_ACTION_RENAME, uris, image, null);
                } else if(itemName.equals(res.getString(R.string.set_as))) {
                    //set as
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.setDataAndType(u, type);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(
                            intent, getString(R.string.set_as)));
                } else if(itemName.equals(res.getString(R.string.detail))) {
                    //detail
                    String path = c.getString(1);
                    List<String> detail = PicShowUtils.getExifInfo(path);
                    String title = c.getString(3);
                    detail.add(0, "Title : /" + title);
                    detail.add("Path : /" + path);
                    checkInflater();
                    PicShowUtils.showDetailDialog(PhotoActivity.this, detail);
                }
            }

        };
        for (String item: moreMenu
             ) {
            moreMenuWindow.addPopupWindowItem(item, item, itemClickListener);
        }

        moreMenuWindow.setPopupWindowTitle(R.string.more);
        moreMenuWindow.show(bottomView);

    }

    private void checkInflater() {
        if(inflater == null)
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
