package com.android.picshow.app;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.LoadListener;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.data.TimeLinePageDataLoader;
import com.android.picshow.adapter.TimeLineAdapter;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLinePage extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "TimeLinePage";
    public static final int UPDATE = 0x111;


    private LoadListener myLoadListener;
    private TimeLinePageDataLoader dataLoader;
    private Handler mainHandler;
    private View rootView;
    private GridView gridView;
    private View bottomView;
    private TimeLineAdapter gridAdapter;
    private int decodeBitmapWidth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataLoader != null)
            dataLoader.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dataLoader != null)
            dataLoader.pause();
        GlideApp.get(getContext()).clearMemory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.picshow_timeline,null);
        rootView = v;
        LogPrinter.i(TAG,"onCreateView:" + v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogPrinter.i(TAG,"onViewCreated:" + view);
        initView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(gridAdapter != null)
            gridAdapter.destroy();
        gridAdapter = null;
    }

    private void init() {
        decodeBitmapWidth = PicShowUtils.getImageWidth(getContext());
        LogPrinter.i(TAG,"decodeBitmapWidth:" + decodeBitmapWidth
                + "  density:" + getResources().getDisplayMetrics().density);
        myLoadListener = new LoadListener() {
            @Override
            public void startLoad() {
                LogPrinter.i(TAG,"startLoad");
            }

            @Override
            public void finishLoad(Object[] items) {
                LogPrinter.i(TAG,"finishLoad:" + gridAdapter);
                Message msg = mainHandler.obtainMessage();
                msg.what = UPDATE;
                msg.obj = items;
                mainHandler.sendMessage(msg);
            }
        };
        dataLoader = new TimeLinePageDataLoader(getActivity().getApplication(),myLoadListener);

        mainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE:
                        if(gridAdapter != null) {
                            gridAdapter.setData((PhotoItem[]) msg.obj);
                        }
                        break;

                    default:

                        break;
                }
            }

        };
    }

    private void initView() {
        gridView = (GridView) rootView.findViewById(R.id.grid);
        bottomView = rootView.findViewById(R.id.bottom_layout);
        gridAdapter = new TimeLineAdapter(getActivity());
        gridAdapter.setDecodeSize(decodeBitmapWidth);
        gridView.setAdapter(gridAdapter);
        gridAdapter.registerDataSetObserver(new DataSetObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }

        });

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra(MediaSetUtils.PHOTO_ID, position);
        intent.putExtra(MediaSetUtils.PHOTO_PATH, gridAdapter.getItem(position).getPath());
        intent.putExtra(MediaSetUtils.BUCKET, MediaSetUtils.CAMERA_BUCKET_ID);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        bottomView.setVisibility(View.VISIBLE);

        return true;
    }
}
