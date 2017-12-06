package com.android.picshow.utils;

import android.os.Environment;
import android.provider.MediaStore;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class MediaSetUtils {

    public static final int CAMERA_BUCKET_ID
            = BucketHelper.getBucketID(Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/DCIM/Camera");


    public static final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_DISPLAY_NAME = 1;
    public static final int INDEX_DATA = 2;
    public static final int INDEX_DATE = 3;

    public static final String WHERE = MediaStore.Images.Media.BUCKET_ID +" = ?";
    public static final String SORT_ODER = "datetaken DESC";



}
