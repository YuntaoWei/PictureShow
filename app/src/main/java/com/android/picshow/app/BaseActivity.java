package com.android.picshow.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import com.android.picshow.R;
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
        updateStatusBar();
    }

    protected void changeButtonSelectedStatus(int index) {
        LogPrinter.i(TAG,"changeButtonSelectedStatus:"+index);
        switch (index) {
            case PageFactory.INDEX_ALBUMSET:
                //album button selected,should display high light status.
                Drawable albumSelectDrawable = getResources().getDrawable(R.drawable.album_n);
                albumSelectDrawable.setBounds(0, 0,
                        albumSelectDrawable.getMinimumWidth(), albumSelectDrawable.getMinimumHeight());

                if(btn_Album != null) {
                    btn_Album.setTextColor(getResources().getColor(R.color.select_text_color));
                    btn_Album.setCompoundDrawables(null, albumSelectDrawable, null, null);
                }

                //photo button display default status.
                Drawable photoDefaultDrawable = getResources().getDrawable(R.drawable.photo_gray_n);
                photoDefaultDrawable.setBounds(0, 0,
                        photoDefaultDrawable.getMinimumWidth(), photoDefaultDrawable.getMinimumHeight());

                if(btn_Photo != null) {
                    btn_Photo.setTextColor(getResources().getColor(R.color.default_text_color));
                    btn_Photo.setCompoundDrawables(null, photoDefaultDrawable, null, null);
                }
                changeTopLayout(true);
                return;

            case PageFactory.INDEX_TIMELINE:
                //album button should display default status.
                Drawable albumDefaultDrawable = getResources().getDrawable(R.drawable.album_gray_n);
                albumDefaultDrawable.setBounds(0, 0,
                        albumDefaultDrawable.getMinimumWidth(), albumDefaultDrawable.getMinimumHeight());

                if(btn_Album != null) {
                    btn_Album.setTextColor(getResources().getColor(R.color.default_text_color));
                    btn_Album.setCompoundDrawables(null, albumDefaultDrawable, null, null);
                }


                //photo button selected,should display high light status.
                Drawable photoSelectDrawable = getResources().getDrawable(R.drawable.photo_n);
                photoSelectDrawable.setBounds(0, 0,
                        photoSelectDrawable.getMinimumWidth(), photoSelectDrawable.getMinimumHeight());

                if(btn_Photo != null) {
                    btn_Photo.setTextColor(getResources().getColor(R.color.select_text_color));
                    btn_Photo.setCompoundDrawables(null, photoSelectDrawable, null, null);
                }
                changeTopLayout(false);
                return;

            default:
                break;
        }
    }

    private void updateStatusBar() {

    }


    public abstract void changeTopLayout(boolean album);


}
