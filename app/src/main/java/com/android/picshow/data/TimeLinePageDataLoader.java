package com.android.picshow.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.picshow.utils.MediaSetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by yuntao.wei on 2017/11/30.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLinePageDataLoader {

    private Context mContext;
    private LoadListener mListener;



    public interface LoadListener{

        public void startLoad();

        public void finishLoad(PhotoItem[] items);

    }

    public TimeLinePageDataLoader(Context context,LoadListener l) {
        mContext = context;
        mListener = l;
    }

    public void resume() {
        ArrayList<PhotoItem> items = new ArrayList<>();
        queryImages(items);
        queryVideo(items);
        PhotoItem[] allItem = new PhotoItem[items.size()];
        items.toArray(allItem);
        Arrays.sort(allItem, new Comparator<PhotoItem>() {

            @Override
            public int compare(PhotoItem o1, PhotoItem o2) {
                return (int)(o1.getDateToken() - o2.getDateToken());
            }

        });

    }

    public void pause() {

    }

    private class LoadThread extends Thread {

        public LoadThread() {}

        @Override
        public void run() {
        }
    }

    private void queryImages(ArrayList<PhotoItem> items) {
        //query image;
        Cursor c = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaSetUtils.PROJECTION,
                MediaSetUtils.WHERE,
                new String[]{MediaSetUtils.CAMERA_BUCKET_ID+""},
                MediaSetUtils.SORT_ODER
        );

        try{
            while(c.moveToNext()) {
                items.add(new PhotoItem(
                        c.getInt(MediaSetUtils.INDEX_ID),
                        c.getString(MediaSetUtils.INDEX_DISPLAY_NAME),
                        c.getString(MediaSetUtils.INDEX_DATA),
                        c.getLong(MediaSetUtils.INDEX_DATE)));
            }
        } finally {
            c.close();
        }
    }

    private void queryVideo(ArrayList<PhotoItem> items) {
        //query image;
        Cursor c = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaSetUtils.PROJECTION,
                MediaSetUtils.WHERE,
                new String[]{MediaSetUtils.CAMERA_BUCKET_ID+""},
                MediaSetUtils.SORT_ODER
        );
        try{
            while(c.moveToNext()) {
                items.add(new PhotoItem(
                        c.getInt(MediaSetUtils.INDEX_ID),
                        c.getString(MediaSetUtils.INDEX_DISPLAY_NAME),
                        c.getString(MediaSetUtils.INDEX_DATA),
                        c.getLong(MediaSetUtils.INDEX_DATE)));
            }
        } finally {
            c.close();
        }
    }

}
