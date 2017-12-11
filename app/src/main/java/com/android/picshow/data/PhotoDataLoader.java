package com.android.picshow.data;

import java.util.concurrent.Semaphore;

/**
 * Created by yuntao.wei on 2017/12/9.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoDataLoader {

    public static final int INVALID = -1;
    private Semaphore semaphore;
    private LoadThread loadTask;

    public PhotoDataLoader() {
        semaphore = new Semaphore(1);
    }

    public void resume() {
        if(semaphore == null)
            semaphore = new Semaphore(1);
        if(loadTask == null)
            loadTask = new LoadThread();
        loadTask.start();
    }

    public void pause() {}

    private class LoadThread extends Thread {

        private boolean stop = false;

        public LoadThread() {}

        public void stopTask() {
            stop = true;
        }

        @Override
        public void run() {
            while (stop) {
                try {
                    if(semaphore == null)
                        continue;
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(stop)
                    return;

                semaphore.release();
            }
        }
    }

}
