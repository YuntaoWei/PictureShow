package com.android.picshow.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.android.picshow.R;
import com.android.picshow.data.PhotoItem;

import java.util.ArrayList;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PicShowUtils {


    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static int getImageWidth(Context ctx) {
        int screenWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int colNumbers = ctx.getResources().getInteger(R.integer.col_num);
        int allSpaceing = (colNumbers + 1) * (ctx.getResources().getDimensionPixelSize(R.dimen.col_spaceing));
        return (screenWidth - allSpaceing) / colNumbers;
    }


    //PackageManager.PERMISSION_GRANTED
    private static final int PERMISSION_GRANTED = 0;
    //PackageManager.PERMISSION_DENIED
    private static final int PERMISSION_DENIED = -1;

    public static boolean checkPermissions(Context ctx) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        ArrayList<String> needRequest = new ArrayList<>();
        for (String permission:PERMISSIONS
             ) {
            if(ContextCompat.checkSelfPermission(ctx,permission) == PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static void sortItem(PhotoItem[] original,boolean desc) {
        mergeSort(original,0,1);
    }

    private static void mergeSort(PhotoItem[] a, int s, int len) {
        int size = a.length;
        int mid = size / (len << 1);
        int c = size & ((len << 1) - 1);
        if (mid == 0)
            return;
        for (int i = 0; i < mid; ++i) {
            s = i * 2 * len;
            merge(a, s, s + len, (len << 1) + s - 1);
        }
        if (c != 0)
            merge(a, size - c - 2 * len, size - c, size - 1);
        mergeSort(a, 0, 2 * len);
    }

    private static void merge(PhotoItem[] a, int s, int m, int t) {
        PhotoItem[] tmp = new PhotoItem[t - s + 1];
        int i = s, j = m, k = 0;
        while (i < m && j <= t) {
            if (a[i].getDateToken() >= a[j].getDateToken()) {
                tmp[k] = a[i];
                k++;
                i++;
            } else {
                tmp[k] = a[j];
                j++;
                k++;
            }
        }
        while (i < m) {
            tmp[k] = a[i];
            i++;
            k++;
        }
        while (j <= t) {
            tmp[k] = a[j];
            j++;
            k++;
        }
        System.arraycopy(tmp, 0, a, s, tmp.length);
    }


}
