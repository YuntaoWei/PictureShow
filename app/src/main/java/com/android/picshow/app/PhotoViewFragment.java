package com.android.picshow.app;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.bumptech.glide.load.DecodeFormat;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by yuntao.wei on 2017/12/11.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoViewFragment extends Fragment {

    private static final String TAG = "PhotoViewFragment";

    private int currentPosition;
    private int currentPhotoID;
    private static final String TYPE = "type";
    private int type;
    private String currentPath;
    private static final int INVALID = -1;
    private static final String CURRENT_POSITION = "current_position";
    private PhotoView mPhotoView;
    private ImageView videoIcon;

    public PhotoViewFragment() {}

    public static Fragment newInstance(Cursor c, int position) {
        final Fragment f = new PhotoViewFragment();
        final Bundle b = new Bundle();

        int photoID = c.getInt(0);
        b.putInt(MediaSetUtils.PHOTO_ID, photoID);

        String path = c.getString(1);
        b.putString(MediaSetUtils.PHOTO_PATH, path);

        String type = c.getString(2);
        b.putInt(TYPE, type == null ? PhotoItem.TYPE_IMAGE :
                (type.startsWith("video") ? PhotoItem.TYPE_VIDEO : PhotoItem.TYPE_IMAGE));

        b.putInt(CURRENT_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.picshow_photo_page_item, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        GlideApp.with(this)
                .load(currentPath)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(mPhotoView);
        if(type == PhotoItem.TYPE_VIDEO)
            videoIcon.setVisibility(View.VISIBLE);
        else
            videoIcon.setVisibility(View.GONE);
    }

    private void init() {
        Bundle b = getArguments();
        currentPosition = b.getInt(CURRENT_POSITION, INVALID);
        currentPhotoID = b.getInt(MediaSetUtils.PHOTO_ID, INVALID);
        currentPath = b.getString(MediaSetUtils.PHOTO_PATH);
        type = b.getInt(TYPE);
        LogPrinter.i(TAG, "currentPosition:" + currentPosition + "  currentPhotoID:" + currentPhotoID
                + "    currentPath:" + currentPath);
    }

    private void initView() {
        mPhotoView = getView().findViewById(R.id.photo);
        videoIcon = getView().findViewById(R.id.videoIcon);

        videoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PhotoActivity)getActivity()).toggleFullScreen();
            }
        });
    }

    private void playVideo() {
        Uri videoUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.getContentUri("external"), currentPhotoID);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(videoUri,"video/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogPrinter.i(TAG, "onPause.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlideApp.with(this).onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPhotoView != null) {
            GlideApp.with(this).clear(mPhotoView);
            mPhotoView = null;
        }
    }

}
