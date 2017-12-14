package com.android.picshow.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.PhotoItem;
import com.bumptech.glide.load.DecodeFormat;
import com.trustyapp.gridheaders.TrustyGridSimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuntao.wei on 2017/12/14.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class TimeLineAdapter extends BaseAdapter implements TrustyGridSimpleAdapter {

    private PhotoItem[] datas = new PhotoItem[0];
    private Activity attachActivity;
    private int thubNailSize = 0;

    public TimeLineAdapter(Activity a) {
        attachActivity = a;
    }

    public TimeLineAdapter(Activity a, PhotoItem[] items) {
        attachActivity = a;
        datas = items;
    }

    public void setData(PhotoItem[] items) {
        datas = items;
        notifyDataSetChanged();
    }

    public void setDecodeSize(int size) {
        thubNailSize = size;
    }

    public long getTimeId(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date mDate = null;

        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mDate.getTime();
    }

    @Override
    public long getHeaderId(int i) {
        return getTimeId(getItem(i).getDateAdd());
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder mHeadViewHolder = null;
        if (convertView==null){
            mHeadViewHolder = new HeaderViewHolder();
            convertView = attachActivity.getLayoutInflater().inflate(R.layout.picshow_item_time_header,null);

            mHeadViewHolder.tvTimeHeader = (TextView) convertView.findViewById(R.id.tv_time_header);

            convertView.setTag(mHeadViewHolder);
        }else {
            mHeadViewHolder = (HeaderViewHolder)convertView.getTag();
        }

        mHeadViewHolder.tvTimeHeader.setText(getItem(position).getDateAdd());

        return convertView;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public PhotoItem getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return datas[position].getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if(convertView != null) {
            v = (ViewHolder)convertView.getTag();
            if(v == null) {
                v = new ViewHolder();
                v.imgView = convertView.findViewById(R.id.img);
            }
        } else {
            convertView = attachActivity.getLayoutInflater().inflate(R.layout.picshow_img_item,null);
            v = new ViewHolder();
            v.imgView = convertView.findViewById(R.id.img);
        }
        convertView.setTag(v);
        if(v != null && v.imgView != null) {
            if(thubNailSize != 0) {
                GlideApp.with(attachActivity)
                        .load(getItem(position).getPath())
                        .override(thubNailSize)
                        .placeholder(R.drawable.other)
                        .centerCrop()
                        .dontAnimate()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .into(v.imgView);
            } else {
                GlideApp.with(attachActivity)
                        .load(getItem(position).getPath())
                        .placeholder(R.drawable.other)
                        .centerCrop()
                        .dontAnimate()
                        .format(DecodeFormat.PREFER_RGB_565)
                        //.signature(new MediaStoreSignature("",getItem(position).getDateToken(), 0))
                        .into(v.imgView);
            }
        }
        return convertView;
    }

    private class HeaderViewHolder {
        public TextView tvTimeHeader;
    }

    private class ViewHolder {
        public ImageView imgView;
    }

}
