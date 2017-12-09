package com.android.picshow.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yuntao.wei on 2017/11/28.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoItem implements Parcelable {

    private int ID;
    private String mTitle;
    private String mPath;
    private long dateToken;

    public PhotoItem() {}

    public PhotoItem(int id, String title, String path, long date) {
        ID = id;
        mTitle = title;
        mPath = path;
        dateToken = date;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
