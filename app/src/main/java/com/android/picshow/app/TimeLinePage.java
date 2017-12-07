package com.android.picshow.app;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.LoadListener;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.data.TimeLinePageDataLoader;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.PicShowUtils;
import com.bumptech.glide.load.DecodeFormat;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLinePage extends Fragment {

    private static final String TAG = "TimeLinePage";
    public static final int UPDATE = 0x111;


    private LoadListener myLoadListener;
    private TimeLinePageDataLoader dataLoader;
    private Handler mainHandler;
    private View rootView;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private int decodeBitmapWidth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataLoader.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        if(isVisibleToUser) {
            //Likes activity onResume lifecycle,Should load data here.
            if(dataLoader != null)
                dataLoader.resume();
        } else {
            //like activity onPause lifecycle
            if(dataLoader != null) {
                dataLoader.pause();
            }
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
        decodeBitmapWidth = PicShowUtils.getImageWidth(getContext());
        LogPrinter.i("yt","decodeBitmapWidth:" + decodeBitmapWidth
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
        dataLoader = new TimeLinePageDataLoader(getContext(),myLoadListener);

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
        gridAdapter = new GridAdapter();
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
    }

    private class GridAdapter extends BaseAdapter {

        private PhotoItem[] mDatas;

        public GridAdapter() {
            mDatas = new PhotoItem[0];
        }

        public GridAdapter(PhotoItem[] datas) {
            mDatas = datas;
        }

        public void setData(PhotoItem[] items) {
            mDatas = items;
            GridAdapter.this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDatas.length;
        }

        @Override
        public PhotoItem getItem(int position) {
            return mDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogPrinter.i(TAG,"getView:"+position);
            ViewHolder v = null;
            if(convertView != null) {
                v = (ViewHolder)convertView.getTag();
                if(v == null) {
                    v = new ViewHolder();
                    v.imgView = convertView.findViewById(R.id.img);
                }
            } else {
                convertView = getLayoutInflater().inflate(R.layout.img_item,null);
                v = new ViewHolder();
                v.imgView = convertView.findViewById(R.id.img);
            }
            convertView.setTag(v);
            if(v != null && v.imgView != null) {
                LogPrinter.i(TAG,"call glide to load and show image:"+getItem(position).getPath());
                GlideApp.with(TimeLinePage.this)
                        .load(getItem(position).getPath())
                        //.override(decodeBitmapWidth,decodeBitmapWidth)
                        .placeholder(R.drawable.other)
                        .centerCrop()
                        .dontAnimate()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .into(v.imgView);
            }
            return convertView;
        }

    }

    private class ViewHolder {
        public ImageView imgView;
    }

}
