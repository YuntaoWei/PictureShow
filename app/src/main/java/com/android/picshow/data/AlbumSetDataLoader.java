package com.android.picshow.data;

import android.content.Context;
import android.net.Uri;

import com.android.picshow.app.PictureShowApplication;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

import java.util.concurrent.Semaphore;

/**
 * Created by yuntao.wei on 2017/12/6.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class AlbumSetDataLoader implements DataLoader {

    private static final String TAG = "AlbumSetDataLoader";

    private PictureShowApplication mContext;
    private LoadListener mListener;
    private Semaphore mSemaphore;
    private LoadThread loadTask;
    private ChangeNotify notifier;


    public AlbumSetDataLoader(Context context, LoadListener l) {
        mContext = (PictureShowApplication)context;
        mListener = l;
        notifier = new ChangeNotify(this, new Uri[] {
                MediaSetUtils.VIDEO_URI,
                MediaSetUtils.IMAGE_URI
        }, mContext);
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

    @Override
    public void notifyContentChanged() {
        reloadData();
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
                    if(mSemaphore == null)
                        continue;
                    mSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(stopTask) {
                    return;
                }
                mListener.startLoad();
                Album[] allAlbum = MediaSetUtils.queryAllAlbumSetFromFileTable(mContext);
                LogPrinter.i(TAG,"LoadThread load complete:"+allAlbum.length);
                PicShowUtils.sortItem(allAlbum,true);
                mListener.finishLoad(allAlbum);
                if(mSemaphore != null)
                    mSemaphore.release();
            }

        }

    }



}
