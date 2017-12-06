package com.android.picshow.data;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by yuntao.wei on 2017/11/30.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLinePageDataLoader {

    private static final String TAG = "TimeLinePageDataLoader";

    private Context mContext;
    private LoadListener mListener;
    private AsyncQueryHandler queryHandler;
    private Semaphore mSemaphore;
    private LoadThread loadTask;



    public interface LoadListener{

        public void startLoad();

        public void finishLoad(PhotoItem[] items);

    }

    public TimeLinePageDataLoader(Context context,LoadListener l) {
        mContext = context;
        mListener = l;
    }

    public void resume() {
        if(loadTask == null) {
            loadTask = new LoadThread();
        }
        loadTask.start();

        if(mSemaphore == null) {
            mSemaphore = new Semaphore(0);
        }
        mSemaphore.release();
    }

    public void pause() {
        if(loadTask != null) {
            loadTask.stopTask();
            loadTask = null;
        }
        if(mSemaphore != null) {
            mSemaphore.release();
            mSemaphore = null;
        }
    }

    private void reloadData() {
        if(mSemaphore != null)
            mSemaphore.release();
    }

    private class LoadThread extends Thread {

        private boolean stopTask = false;

        public LoadThread() {}

        public void stopTask() {
            stopTask = true;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    mSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(stopTask) {
                    return;
                }
                mListener.startLoad();
                ArrayList<PhotoItem> items = new ArrayList<>();
                queryImages(items);
                queryVideo(items);
                LogPrinter.i(TAG,"LoadThread load complete:"+items.size());
                PhotoItem[] allItem = new PhotoItem[items.size()];
                items.toArray(allItem);
//                Arrays.sort(allItem, new Comparator<PhotoItem>() {
//
//                    @Override
//                    public int compare(PhotoItem o1, PhotoItem o2) {
//                        return (int) (o1.getDateToken() - o2.getDateToken());
//                    }
//
//                });
                PicShowUtils.sortItem(allItem,true);
                mListener.finishLoad(allItem);
            }

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
