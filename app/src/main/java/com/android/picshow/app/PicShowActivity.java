package com.android.picshow.app;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.picshow.R;
import com.android.picshow.adapter.PageControlAdapter;
import com.android.picshow.utils.PageFactory;


public class PicShowActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "PicShowActivity";
    private ViewPager vPager;
    private Toolbar bottomBar;
    private PageControlAdapter pageAdapter;
    private Button searchView;
    private TextView topTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picshow_main);
        if(isPermissionGranted()) {
            initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*new Thread(){

            @Override
            public void run() {
                Cursor c = getContentResolver().query(MediaStore.Files.getContentUri("external"),
                        new String[]{MediaStore.Files.FileColumns._ID}, null, null, null);
                if(c != null) {
                    Log.i("www", "file count : " + c.getCount());
                    c.close();
                    c = null;
                }
            }

        }.start();*/
    }

    private void initView(){
        bottomBar = findViewById(R.id.bottombar);
        setSupportActionBar(bottomBar);

        topTitle = findViewById(R.id.album_title);

        btn_Album = findViewById(R.id.btn_album);
        btn_Photo = findViewById(R.id.btn_photo);
        btn_Album.setOnClickListener(this);
        btn_Photo.setOnClickListener(this);

        searchView = findViewById(R.id.search_photo);
        searchView.setOnClickListener(this);


        vPager = findViewById(R.id.vpager);
        pageAdapter = new PageControlAdapter(getSupportFragmentManager(), PageFactory.getMainPage());
        vPager.setAdapter(pageAdapter);
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeButtonSelectedStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeButtonSelectedStatus(PageFactory.INDEX_TIMELINE);
    }


    @Override
    protected void onGetPermissionsFailure() {
        finish();
    }

    @Override
    protected void onGetPermissionsSuccess() {
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album:
                if(vPager != null)
                    vPager.setCurrentItem(PageFactory.INDEX_ALBUMSET);
                break;

            case R.id.btn_photo:
                if(vPager != null)
                    vPager.setCurrentItem(PageFactory.INDEX_TIMELINE);
                break;

            case R.id.search_photo:

                break;

            default:
                break;
        }
    }

    @Override
    public void changeTopLayout(boolean album) {
        if(album) {
            topTitle.setVisibility(View.INVISIBLE);
        } else {
            topTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
