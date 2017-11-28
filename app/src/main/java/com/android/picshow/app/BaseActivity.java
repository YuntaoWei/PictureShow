package com.android.picshow.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.PageFactory;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public abstract class BaseActivity extends PermissionActivity {

    private static final String TAG = "BaseActivity";
    Button btn_Album,btn_Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        //updateStatusBar();
    }

    protected void changeButtonSelectedStatus(int index) {
        LogPrinter.i(TAG,"changeButtonSelectedStatus:"+index);
        switch (index) {
            case PageFactory.INDEX_ALBUMSET:
                if(btn_Album != null)
                    btn_Album.setAlpha(1.0f);
                if(btn_Photo != null)
                    btn_Photo.setAlpha(0.5f);
                return;

            case PageFactory.INDEX_TIMELINE:
                if(btn_Album != null)
                    btn_Album.setAlpha(0.5f);
                if(btn_Photo != null)
                    btn_Photo.setAlpha(1.0f);
                return;
        }
    }

    private void updateStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}
