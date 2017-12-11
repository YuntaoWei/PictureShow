package com.android.picshow.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.picshow.R;
import com.android.picshow.utils.MediaSetUtils;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by yuntao.wei on 2017/12/11.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class PhotoViewFragment extends Fragment {

    private int currentPosition;
    private int currentPhotoID;
    private static final int INVALID = -1;
    private static final String CURRENT_POSITION = "current_position";
    private PhotoView mPhotoView;

    public PhotoViewFragment() {}

    public static Fragment newInstance(Cursor c, int position) {
        final Fragment f = new PhotoViewFragment();
        final Bundle b = new Bundle();
        int index = c.getColumnIndexOrThrow(MediaSetUtils.PHOTO_ID);
        int currentPhotoIndex = c.getInt(index);
        b.putInt(MediaSetUtils.PHOTO_ID, currentPhotoIndex);
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
    }

    private void initView() {
        mPhotoView = getView().findViewById(R.id.photo);
    }

    @Override
    public void onResume() {
        super.onResume();
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
