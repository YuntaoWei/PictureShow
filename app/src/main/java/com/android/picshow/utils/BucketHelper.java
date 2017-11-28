package com.android.picshow.utils;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class BucketHelper {

    public static int getBucketID(String path) {
        return path.toLowerCase().hashCode();
    }

}
