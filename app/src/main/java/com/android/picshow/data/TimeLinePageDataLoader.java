package com.android.picshow.data;

import android.content.Context;

import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;

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
    private Semaphore mSemaphore;
    private LoadThread loadTask;

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
                MediaSetUtils.queryImages(mContext, items, MediaSetUtils.CAMERA_BUCKET_ID);
                MediaSetUtils.queryVideo(mContext, items, MediaSetUtils.CAMERA_BUCKET_ID);
                LogPrinter.i(TAG,"LoadThread load complete:"+items.size());
                PhotoItem[] allItem = new PhotoItem[items.size()];
                items.toArray(allItem);
                mListener.finishLoad(allItem);
            }

        }
    }
}
