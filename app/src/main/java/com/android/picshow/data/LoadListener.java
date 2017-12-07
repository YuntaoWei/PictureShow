package com.android.picshow.data;

/**
 * Created by yuntao.wei on 2017/12/6.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public interface LoadListener {

    public void startLoad();

    public void finishLoad(Object[] items);

}
