package com.android.picshow.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.picshow.R;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.data.TimeLinePageDataLoader;
import com.android.picshow.utils.LogPrinter;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLinePage extends Fragment {

    private static final String TAG = "TimeLinePage";
    private TimeLinePageDataLoader.LoadListener myLoadListener;
    private TimeLinePageDataLoader dataLoader;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.picshow_timeline,null);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            //Likes activity onResume lifecycle,Should load data here.
            if(dataLoader != null)
                dataLoader.resume();
        } else {
            //like activity onPause lifecycle
            if(dataLoader != null)
                dataLoader.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        myLoadListener = new TimeLinePageDataLoader.LoadListener() {
            @Override
            public void startLoad() {
                LogPrinter.i(TAG,"startLoad");
            }

            @Override
            public void finishLoad(PhotoItem[] items) {
                LogPrinter.i(TAG,"finishLoad");
            }
        };
        dataLoader = new TimeLinePageDataLoader(getContext(),myLoadListener);
    }

}
