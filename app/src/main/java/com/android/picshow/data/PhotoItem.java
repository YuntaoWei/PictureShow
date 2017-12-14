package com.android.picshow.data;

import android.media.MediaFormat;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.picshow.utils.LogPrinter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoItem implements Parcelable {

    private static final String TAG = "PhotoItem";

    private int ID;
    private String mTitle;
    private String mPath;
    private long dateToken;
    private long dateAdd;

    public PhotoItem() {}

    public PhotoItem(int id, String title, String path, long date, long add) {
        ID = id;
        mTitle = title;
        mPath = path;
        dateToken = date;
        dateAdd = add;
    }

    public int getID() {
        return ID;
    }

    public String getPath() {
        return mPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getDateToken() {
        return dateToken;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDateToken(long dateToken) {
        this.dateToken = dateToken;
    }

    public void setDateAdd(long add ) {
        dateAdd = add;
    }

    public String getDateAdd() {
        return new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(dateAdd*1000L));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        String s = "ID = "+ID + ", Title = " + mTitle
                + ", Path = " + mPath;
        LogPrinter.i(TAG,s);
        return s;
    }
}
