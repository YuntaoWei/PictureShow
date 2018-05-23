package com.android.picshow.edit.editorui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.picshow.R;
import com.android.picshow.edit.editor.BaseEditorManager;
import com.android.picshow.edit.editor.filters.BaseEditor;
import com.android.picshow.edit.editor.filters.FilterType;
import com.android.picshow.edit.editor.filters.NativeFilter;
import com.android.picshow.edit.editor.utils.ImageUtils;

/**
 * Created by yuntao.wei on 2018/5/16.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class FilterFragment extends Fragment implements View.OnClickListener, BaseEditor {

    private static final String TAG = FilterFragment.class.getSimpleName();

    private ImageView pictureShow;
    private String picturePath = null;
    private Bitmap pictureBitmap = null;

    private NativeFilter nativeFilters = new NativeFilter();

    private int srcWidth, srcHeight;
    private View mainView;
    private EditActivity mActivity;
    private SeekBar mSeek;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (EditActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_filter, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        initializateView();
        getData();
    }

    private void initializateView() {
        pictureShow = (ImageView) mainView.findViewById(R.id.pictureShow);
    }


    Bitmap newBitmap;

    private void getData() {
        Bundle b = getArguments();
        picturePath = b.getString(BaseEditorManager.SRC_PIC_PATH, null);

        BaseEditorManager.decodeBitmapAsync(picturePath, /*BaseEditorManager.MAX_SIZE,*/
                new BaseEditorManager.LoadListener() {
                    @Override
                    public void onLoadSuccess(Object o) {

                        pictureBitmap = (Bitmap) o;
                        newBitmap = pictureBitmap.copy(pictureBitmap.getConfig(), true);
                        srcWidth = newBitmap.getWidth();
                        srcHeight = newBitmap.getHeight();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updatePicture(1);
                                mActivity.onFragmentAttached();
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed() {

                    }
                });
    }

    private int filterType = FilterType.FILTER4GRAY;
    Bitmap resultImg = null;

    private void updatePicture(float degree) {
        if (newBitmap == null)
            return;
        int[] dataResult = null;

        int[] pix = new int[srcWidth * srcHeight];
        newBitmap.getPixels(pix, 0, srcWidth, 0, 0, srcWidth, srcHeight);

        switch (filterType) {
            case FilterType.FILTER4GRAY:

                dataResult = nativeFilters.gray(pix, srcWidth, srcHeight,
                        degree);

                break;

            case FilterType.FILTER4MOSATIC:

                int mosatic = (int) (degree * 30);
                dataResult = nativeFilters.mosatic(pix, srcWidth, srcHeight,
                        mosatic);
                break;

            case FilterType.FILTER4LOMO:

                dataResult = nativeFilters.lomo(pix, srcWidth, srcHeight,
                        degree);
                break;

            case FilterType.FILTER4NOSTALGIC:

                dataResult = nativeFilters.nostalgic(pix, srcWidth,
                        srcHeight, degree);
                break;

            case FilterType.FILTER4COMICS:
                dataResult = nativeFilters.comics(pix, srcWidth, srcHeight,
                        degree);
                break;

            case FilterType.FILTER4BlackWhite:
                break;

            case FilterType.FILTER4NEGATIVE:
                break;

            case FilterType.FILTER4BROWN:
                dataResult = nativeFilters.brown(pix, srcWidth, srcHeight,
                        degree);
                break;

            case FilterType.FILTER4SKETCH_PENCIL:
                dataResult = nativeFilters.sketchPencil(pix, srcWidth,
                        srcHeight, degree);
                break;

            case FilterType.FILTER4NiHong:
                //dataResult = nativeFilters.neon(pix, srcWidth, srcHeight);
                resultImg = NativeFilter.neon(newBitmap);
                pictureShow.setImageBitmap(resultImg);
                return;

            default:
                break;
        }

        if(dataResult == null)
            return;

        resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                Bitmap.Config.ARGB_8888);

        pictureShow.setImageBitmap(resultImg);

    }

    private TextView filterGray, filterMosatic, filterLOMO,
            filterNostalgic, filterComics, filterBlackWhite, filterNegative,
            filterBrown, filterSketchPencil, filterOverExposure,
            filterSoftness, filterNiHong, filterSketch;

    private Button btnCancel, btnRevocation, btnCancelRevocation, btnSave;

    private void bottomOperationView(View v) {
        btnCancel = (Button) v.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(this);

        btnRevocation = (Button) v.findViewById(R.id.revocation);
        btnRevocation.setOnClickListener(this);

        btnCancelRevocation = (Button) v.findViewById(R.id.cancel_revocation);
        btnCancelRevocation.setOnClickListener(this);

        btnSave = (Button) v.findViewById(R.id.save);
        btnSave.setOnClickListener(this);
    }

    private void initFiltersView(View mainView) {

        filterGray = (TextView) mainView.findViewById(R.id.filterGray);
        filterGray.setOnClickListener(this);

        filterMosatic = (TextView) mainView.findViewById(R.id.filterMosatic);
        filterMosatic.setOnClickListener(this);

        filterLOMO = (TextView) mainView.findViewById(R.id.filterLOMO);
        filterLOMO.setOnClickListener(this);

        filterNostalgic = (TextView) mainView.findViewById(R.id.filterNostalgic);
        filterNostalgic.setOnClickListener(this);

        filterComics = (TextView) mainView.findViewById(R.id.filterComics);
        filterComics.setOnClickListener(this);

        filterBlackWhite = (TextView) mainView.findViewById(R.id.filterBlackWhite);
        filterBlackWhite.setOnClickListener(this);

        filterNegative = (TextView) mainView.findViewById(R.id.filterNegative);
        filterNegative.setOnClickListener(this);

        filterBrown = (TextView) mainView.findViewById(R.id.filterBrown);
        filterBrown.setOnClickListener(this);

        filterSketchPencil = (TextView) mainView.findViewById(R.id.filterSketchPencil);
        filterSketchPencil.setOnClickListener(this);

        filterOverExposure = (TextView) mainView.findViewById(R.id.filterOverExposure);
        filterOverExposure.setOnClickListener(this);

        filterSoftness = (TextView) mainView.findViewById(R.id.filterSoftness);
        filterSoftness.setOnClickListener(this);

        filterNiHong = (TextView) mainView.findViewById(R.id.filterNiHong);
        filterNiHong.setOnClickListener(this);

        filterSketch = (TextView) mainView.findViewById(R.id.filterSketch);
        filterSketch.setOnClickListener(this);

        mSeek = (SeekBar) mainView.findViewById(R.id.seek);
        mSeek.setMax(100);
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                float degree = mProgress / 100.0f;

                updatePicture(degree);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mProgress = progress;

            }
        });

        loadFilterThumbIcon();
    }

    private void loadFilterThumbIcon() {
        ImageUtils.loadFilterThumb(pictureBitmap, BaseEditorManager.FILTER_THUMB_SIZE,
                BaseEditorManager.FILTER_THUMB_SIZE, new ImageUtils.ThumbLoadListener() {

                    @Override
                    public void onLoadSuccess(final Bitmap bm, final int filterType) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFilterIcon(bm, filterType);
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(int filterType) {

                    }

                });
    }

    private void setDrawableTop(TextView v, Drawable d) {
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        v.setCompoundDrawables(null, d, null, null);
    }

    private void setFilterIcon(Bitmap bm, int type) {
        Drawable d = new BitmapDrawable(mActivity.getResources(), bm);
        switch (type) {

            case FilterType.FILTER4GRAY:
                setDrawableTop(filterGray, d);
                break;

            case FilterType.FILTER4LOMO:
                setDrawableTop(filterLOMO, d);
                break;

            case FilterType.FILTER4NOSTALGIC:
                setDrawableTop(filterNostalgic, d);
                break;

            case FilterType.FILTER4BROWN:
                setDrawableTop(filterBrown, d);
                break;

            case FilterType.FILTER4COMICS:
                setDrawableTop(filterComics, d);
                break;

            case FilterType.FILTER4MOSATIC:
                setDrawableTop(filterMosatic, d);
                break;

            case FilterType.FILTER4SKETCH_PENCIL:
                setDrawableTop(filterSketchPencil, d);
                break;

            case FilterType.FILTER4NiHong:
                setDrawableTop(filterNiHong, d);
                break;


        }

    }


    @Override
    public void initPanelView(View v) {
        if (v != null) {
            initFiltersView(v);
            bottomOperationView(v);
        }

    }

    @Override
    public void onSave() {
        mActivity.shouldReLoadPicture(resultImg);
        //FileUtils.writeImage(resultImg, picturePath, 100);
        //getFragmentManager().popBackStack();
    }

    @Override
    public void onCancel() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onCompareStart() {
        pictureShow.setImageBitmap(pictureBitmap);
    }

    @Override
    public void onCompareEnd() {
        pictureShow.setImageBitmap(resultImg);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.filterWhite:
                filterType = FilterType.FILTER4WHITELOG;
                break;

            case R.id.filterGray:
                filterType = FilterType.FILTER4GRAY;
                break;

            case R.id.filterBlackWhite:
                filterType = FilterType.FILTER4BlackWhite;
                break;

            case R.id.filterMosatic:
                filterType = FilterType.FILTER4MOSATIC;
                break;

            case R.id.filterComics:
                filterType = FilterType.FILTER4COMICS;
                break;

            case R.id.filterBrown:
                filterType = FilterType.FILTER4BROWN;
                break;

            case R.id.filterLOMO:
                filterType = FilterType.FILTER4LOMO;
                break;

            case R.id.filterNegative:
                filterType = FilterType.FILTER4NEGATIVE;
                break;

            case R.id.filterNostalgic:
                filterType = FilterType.FILTER4NOSTALGIC;
                break;

            case R.id.filterOverExposure:
                filterType = FilterType.FILTER4OVEREXPOSURE;
                break;

            case R.id.filterSketchPencil:
                filterType = FilterType.FILTER4SKETCH_PENCIL;
                break;

            case R.id.filterSoftness:
                filterType = FilterType.FILTER4SOFTNESS;
                break;

            case R.id.filterNiHong:
                filterType = FilterType.FILTER4NiHong;
                break;

            case R.id.filterSketch:
                filterType = FilterType.FILTER4SKETCH;
                break;

            case R.id.cancel:
                onCancel();
                break;

            case R.id.revocation:
                if (pictureBitmap != null && pictureShow != null)
                    pictureShow.setImageBitmap(pictureBitmap);
                return;

            case R.id.cancel_revocation:
                if (resultImg != null && pictureShow != null)
                    pictureShow.setImageBitmap(resultImg);
                return;

            case R.id.save:
                onSave();
                onCancel();
                break;

            default:
                break;
        }

        if (filterType == FilterType.FILTER4MOSATIC) {
            mSeek.setVisibility(View.VISIBLE);
        } else {
            mSeek.setVisibility(View.GONE);
        }
        updatePicture(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycle();
    }

    private void recycle() {
        if (pictureBitmap != null) {
            pictureBitmap.recycle();
            pictureBitmap = null;
        }

        if (newBitmap != null) {
            newBitmap.recycle();
            newBitmap = null;
        }

    }

}
