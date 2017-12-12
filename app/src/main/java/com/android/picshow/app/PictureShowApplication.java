package com.android.picshow.app;

import android.app.Application;

import com.android.picshow.data.DataManager;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PictureShowApplication extends Application {

    DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mDataManager = new DataManager(this);
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

}
