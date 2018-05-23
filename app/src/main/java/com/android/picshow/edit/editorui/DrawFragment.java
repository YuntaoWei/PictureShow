package com.android.picshow.edit.editorui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.android.picshow.R;
import com.android.picshow.edit.editor.BaseEditorManager;
import com.android.picshow.edit.editor.filters.BaseEditor;
import com.android.picshow.edit.editor.operate.OperateUtils;
import com.android.picshow.edit.editor.scrawl.DrawAttribute;
import com.android.picshow.edit.editor.scrawl.DrawingBoardView;
import com.android.picshow.edit.editor.scrawl.PaintUtils;
import com.android.picshow.edit.editor.scrawl.ScrawlTools;
import com.android.picshow.edit.editor.utils.FileUtils;
import com.android.picshow.utils.LogPrinter;

/**
 * Created by yuntao.wei on 2018/5/16.
 * github:https://github.com/YuntaoWei
 * bLogPrinter:http://bLogPrinter.csdn.net/qq_17541215
 */

public class DrawFragment extends Fragment implements View.OnClickListener, BaseEditor, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = DrawFragment.class.getSimpleName();

    private DrawingBoardView drawView;

    ScrawlTools casualWaterUtil = null;
    private LinearLayout drawLayout;
    String mPath;
    private EditActivity mActivity;

    private View mainView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (EditActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_draw, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        initView();
    }

    private void initView() {
        drawView = (DrawingBoardView) mainView.findViewById(R.id.drawView);
        drawLayout = (LinearLayout) drawView.findViewById(R.id.drawLayout);

        mActivity.onFragmentAttached();
    }

    private void initData() {
        Bundle b = getArguments();
        mPath = b.getString(BaseEditorManager.SRC_PIC_PATH, null);
        LogPrinter.i(TAG, "pic path : " + mPath);

        BaseEditorManager.decodeBitmapAsync(mPath, new BaseEditorManager.LoadListener() {

            @Override
            public void onLoadSuccess(Object o) {
                OperateUtils operateUtils = new OperateUtils(mActivity);
                //final Bitmap resizeBmp = operateUtils.compressionFiller((Bitmap)o, drawLayout);
                final Bitmap resizeBmp = (Bitmap)o;
                if(resizeBmp == null) {
                    LogPrinter.i(TAG, "some thing error occurs!");
                    return;
                }

                final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        resizeBmp.getWidth(), resizeBmp.getHeight());

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawView.setLayoutParams(layoutParams);

                        casualWaterUtil = new ScrawlTools(mActivity, drawView, resizeBmp);

                        onColorPicked(PaintUtils.COLOR_RED);
                    }
                });

            }

            @Override
            public void onLoadFailed() {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancel:
                break;

            case R.id.save:

                break;

            case R.id.revocation:

                break;

            case R.id.cancel_revocation:

                break;

            case R.id.color_white:
                onColorPicked(PaintUtils.COLOR_WHITE);
                break;

            case R.id.color_gray:
                onColorPicked(PaintUtils.COLOR_GRAY);
                break;

            case R.id.color_black:
                onColorPicked(PaintUtils.COLOR_BLACK);
                break;

            case R.id.color_orange:
                onColorPicked(PaintUtils.COLOR_ORANGE);
                break;

            case R.id.color_yellow:
                onColorPicked(PaintUtils.COLOR_YELLOW);
                break;

            case R.id.color_green:
                onColorPicked(PaintUtils.COLOR_GREEN);
                break;

            case R.id.color_blue:
                onColorPicked(PaintUtils.COLOR_BLUE);
                break;

            case R.id.color_red:
                onColorPicked(PaintUtils.COLOR_RED);
                break;

            case R.id.draw_text:

                break;

            case R.id.draw_curve:

                break;

            case R.id.draw_line:

                break;

            case R.id.draw_arrow:

                break;

            case R.id.draw_circle:

                break;

            case R.id.draw_rec:

                break;

            case R.id.color_picker:
                onColorPickerClicked(true);
                break;

            default:

                break;
        }
    }

    public void onColorPickerClicked(boolean show) {
        if(colorPanel != null) {
            colorPanel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void onColorPicked(int colorIndex) {
        int paintColor = PaintUtils.getColor(mActivity, colorIndex);

        setPaint(paintColor, 1);

        btnColorPicker.setBackgroundResource(PaintUtils.getColorPickerBackground(colorIndex));
        onColorPickerClicked(false);
    }

    private void setPaint(int color, int size) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = size;
        Bitmap bm = BitmapFactory.decodeResource(
                this.getResources(), R.drawable.marker, option);
        casualWaterUtil.creatDrawPainter(
                DrawAttribute.DrawStatus.PEN_WATER, bm, color);
    }

    /** color pick button **/
    private Button btnColorWhite, btnColorGray, btnColorBlack,btnColorOrange,
            btnColorYellow, btnColorGreen, btnColorBlue, btnColorRed;

    private SeekBar paintSize;

    private Button btnColorPicker;

    private Button btnCancel, btnRevocation, btnCancelRevocation, btnSave;

    /** the paint style **/
    private Button btnText, btnCurve, btnLine, btnArrow, btnCircle, btnRectangle;

    /** color picker view, it shows when color picker clicked.**/
    private View colorPanel;

    /**
     * init draw fragment bottom panel,it called by activity when fragment view created and bottom panel inflate success.
     * @param v see layout:draw_bottom_operation.xml
     */
    @Override
    public void initPanelView(View v) {
        initData();

        colorPanel = v.findViewById(R.id.color_panel);

        btnColorWhite = (Button) v.findViewById(R.id.color_white);
        btnColorGray = (Button) v.findViewById(R.id.color_gray);
        btnColorBlack = (Button) v.findViewById(R.id.color_black);
        btnColorOrange = (Button) v.findViewById(R.id.color_orange);
        btnColorYellow = (Button) v.findViewById(R.id.color_yellow);
        btnColorGreen = (Button) v.findViewById(R.id.color_green);
        btnColorBlue = (Button) v.findViewById(R.id.color_blue);
        btnColorRed = (Button) v.findViewById(R.id.color_red);

        btnColorWhite.setOnClickListener(this);
        btnColorGray.setOnClickListener(this);
        btnColorBlack.setOnClickListener(this);
        btnColorOrange.setOnClickListener(this);
        btnColorYellow.setOnClickListener(this);
        btnColorGreen.setOnClickListener(this);
        btnColorBlue.setOnClickListener(this);
        btnColorRed.setOnClickListener(this);


        paintSize = (SeekBar) v.findViewById(R.id.seek);
        paintSize.setProgress(50);
        paintSize.setMax(100);
        paintSize.setOnSeekBarChangeListener(this);

        btnColorPicker = (Button) v.findViewById(R.id.color_picker);
        btnColorPicker.setOnClickListener(this);


        btnText = (Button) v.findViewById(R.id.draw_text);
        btnCurve = (Button) v.findViewById(R.id.draw_curve);
        btnLine = (Button) v.findViewById(R.id.draw_line);
        btnArrow = (Button) v.findViewById(R.id.draw_arrow);
        btnCircle = (Button) v.findViewById(R.id.draw_circle);
        btnRectangle = (Button) v.findViewById(R.id.draw_rec);

        btnText.setOnClickListener(this);
        btnCurve.setOnClickListener(this);
        btnLine.setOnClickListener(this);
        btnArrow.setOnClickListener(this);
        btnCircle.setOnClickListener(this);
        btnRectangle.setOnClickListener(this);


        btnCancel = (Button) v.findViewById(R.id.cancel);
        btnRevocation = (Button) v.findViewById(R.id.revocation);
        btnCancelRevocation = (Button) v.findViewById(R.id.cancel_revocation);
        btnSave = (Button) v.findViewById(R.id.save);

        btnCancel.setOnClickListener(this);
        btnRevocation.setOnClickListener(this);
        btnCancelRevocation.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }


    @Override
    public void onSave() {
        Bitmap bit = casualWaterUtil.getBitmap();

        FileUtils.writeImage(bit, mPath, 100);

        onCancel();
    }

    @Override
    public void onCancel() {

        getFragmentManager().popBackStack();

    }

    @Override
    public void onCompareStart() {

    }

    @Override
    public void onCompareEnd() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int size = seekBar.getProgress() / 20;
        LogPrinter.i(TAG,"onStopTrackingTouch : " + size);
        if(size == 0)
            size = 1;
        setPaint(-1, size);
    }
}
