package com.android.picshow.utils;

import android.os.Environment;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class MediaSetUtils {

    public static final int CAMERA_BUCKET_ID
            = BucketHelper.getBucketID(Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/DCIM/Camera");
}
