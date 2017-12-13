package com.android.picshow.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.android.picshow.R;
import com.android.picshow.data.Album;

import java.util.ArrayList;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PicShowUtils {

    public static final int MAX_LOAD = 5;


    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     *Get the TimeLine page thumbnail size from xml configuration.
     * @param ctx
     * @return
     */
    public static int getImageWidth(Context ctx) {
        int screenWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int colNumbers = ctx.getResources().getInteger(R.integer.col_num);
        int allSpaceing = (colNumbers + 1) * (ctx.getResources().getDimensionPixelSize(R.dimen.col_spaceing));
        return (screenWidth - allSpaceing) / colNumbers;
    }

    /**
     * Get the AlbumSet Page thumbnail size from the xml configuration.
     * @param ctx
     * @return
     */
    public static int getAlbumImageWidth(Context ctx) {
        int screenWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int colNumbers = ctx.getResources().getInteger(R.integer.album_col_num);
        int allSpaceing = (int)ctx.getResources().getDimension(R.dimen.album_col_spaceing);
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

    /**
     * Hide the toolbar
     * @param toolbar
     * @param needTitle
     */
    private static void setNoTitle(Toolbar toolbar, boolean needTitle) {
        if(needTitle)  {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.INVISIBLE);
        }
    }

    public static void extendLayoutToFulScreen(Activity a) {
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * Set display full screen options.
     * @param toolbar
     */
    public static void enterFullScreen(Toolbar toolbar) {
        setNoTitle(toolbar, false);
    }

    /**
     * Exit the full screen mode.
     * @param toolbar
     */
    public static void exitFullScreen(Toolbar toolbar) {
        setNoTitle(toolbar, true);
    }

    /**
     * Use merge sort to sort the datas.
     * @param original src data.
     * @param desc use DESC or ASC.
     */
    public static void sortItem(Album[] original,boolean desc) {
        mergeSort(original,0,1);
    }

    private static void mergeSort(Album[] a, int s, int len) {
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

    private static void merge(Album[] a, int s, int m, int t) {
        Album[] tmp = new Album[t - s + 1];
        int i = s, j = m, k = 0;
        while (i < m && j <= t) {
            if (a[i].dateToken >= a[j].dateToken) {
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
