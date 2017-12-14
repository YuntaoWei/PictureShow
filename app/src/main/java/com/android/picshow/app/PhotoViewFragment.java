package com.android.picshow.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.picshow.R;
import com.android.picshow.data.GlideApp;
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
    private String currentPath;
    private static final int INVALID = -1;
    private static final String CURRENT_POSITION = "current_position";
    private PhotoView mPhotoView;

    public PhotoViewFragment() {}

    public static Fragment newInstance(Cursor c, int position) {
        final Fragment f = new PhotoViewFragment();
        final Bundle b = new Bundle();

        int photoID = c.getInt(0);
        b.putInt(MediaSetUtils.PHOTO_ID, photoID);

        String path = c.getString(2);
        b.putString(MediaSetUtils.PHOTO_PATH, path);

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
    }

    private void init() {
        Bundle b = getArguments();
        currentPosition = b.getInt(CURRENT_POSITION, INVALID);
        currentPhotoID = b.getInt(MediaSetUtils.PHOTO_ID, INVALID);
        currentPath = b.getString(MediaSetUtils.PHOTO_PATH);
        LogPrinter.i(TAG, "currentPosition:" + currentPosition + "  currentPhotoID:" + currentPhotoID
                + "    currentPath:" + currentPath);
    }

    private void initView() {
        mPhotoView = getView().findViewById(R.id.photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PhotoActivity)getActivity()).toggleFullScreen();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GlideApp.with(this)
                .load(currentPath)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(mPhotoView);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
