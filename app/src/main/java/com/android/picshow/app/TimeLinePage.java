package com.android.picshow.app;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.GridView;

import com.android.picshow.R;
import com.android.picshow.adapter.TimeLineAdapter;
import com.android.picshow.data.GlideApp;
import com.android.picshow.data.LoadListener;
import com.android.picshow.data.Path;
import com.android.picshow.data.PhotoItem;
import com.android.picshow.data.TimeLinePageDataLoader;
import com.android.picshow.ui.MenuExecutor;
import com.android.picshow.ui.SelectionManager;
import com.android.picshow.utils.LogPrinter;
import com.android.picshow.utils.MediaSetUtils;
import com.android.picshow.utils.PicShowUtils;

import java.util.List;

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
    private SelectionManager selectionManager;
    private SelectionManager.SelectionListener selectionListener;
    private Button btnShare, btnDelete, btnEdit, btnDetail;
    private MenuExecutor menuExecutor;


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

        selectionListener = new SelectionManager.SelectionListener() {
            @Override
            public void enterSelectionMode() {

            }

            @Override
            public void exitSelectionMode() {
                bottomView.setVisibility(View.GONE);
                gridAdapter.setSelectState(false);
            }

            @Override
            public void onSelectionChange(Path p, boolean select) {
                if(selectionManager.getSelectCount() > 1) {
                    if(btnEdit != null) {
                        btnEdit.setClickable(false);
                        btnEdit.setAlpha(0.3f);
                    }
                    if(btnDetail != null) {
                        btnDetail.setClickable(false);
                        btnDetail.setAlpha(0.3f);
                    }
                } else {
                    if (btnEdit != null) {
                        btnEdit.setClickable(true);
                        btnEdit.setAlpha(1.0f);
                    }
                    if (btnDetail != null) {
                        btnDetail.setClickable(true);
                        btnDetail.setAlpha(1.0f);
                    }
                }
            }
        };
    }

    private void initView() {
        gridView = (GridView) rootView.findViewById(R.id.grid);
        bottomView = rootView.findViewById(R.id.bottom_layout);
        selectionManager = new SelectionManager();
        gridAdapter = new TimeLineAdapter(getActivity(), null, selectionManager);
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

        selectionManager.setSelectionListener(selectionListener);
        menuExecutor = new MenuExecutor(getContext());
        initBottomMenu();
    }

    private void initBottomMenu() {

        if(bottomView == null) return;

        btnDelete = bottomView.findViewById(R.id.delete);
        btnDetail = bottomView.findViewById(R.id.more);
        btnEdit = bottomView.findViewById(R.id.edit);
        btnShare = bottomView.findViewById(R.id.share);
        final MenuExecutor.ExcuteListener excuteListener = new MenuExecutor.ExcuteListener() {

            @Override
            public void startExcute() {
                //show dialog here.
                showProgreeDialog();
            }

            @Override
            public void excuteSuccess() {
                //exit select mode and hide dialog.
                gridAdapter.setSelectState(false);
                if(dialog != null) {
                    dialog.dismiss();
                }
                selectionManager.clearSelection();
            }

            @Override
            public void excuteFailed() {
                //some error occurs.
                if(dialog != null)
                    dialog.dismiss();
            }
        };

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.delete:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_DELETE,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;

                    case R.id.more:
                        PhotoItem item = gridAdapter.getItem(selectionManager.getSelectPostion());
                        String path = item.getPath();
                        List<String> detail = PicShowUtils.getExifInfo(path);
                        String title = item.getTitle();
                        detail.add(0, "Title : /" + title);
                        detail.add("Path : /" + path);
                        PicShowUtils.showDetailDialog(getActivity(), detail);
                        break;

                    case R.id.edit:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_EDIT,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;

                    case R.id.share:
                        menuExecutor.execute(MenuExecutor.MENU_ACTION_SHARE,
                                selectionManager.getSelectItems(), true, excuteListener);
                        break;


                }
            }
        };
        btnDelete.setOnClickListener(onclick);
        btnDetail.setOnClickListener(onclick);
        btnEdit.setOnClickListener(onclick);
        btnShare.setOnClickListener(onclick);

    }

    private ProgressDialog dialog;
    private void showProgreeDialog() {
        if(dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
        }
        dialog.show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(gridAdapter.getSelectState()) {
            PhotoItem item = gridAdapter.getItem(position);
            updateItem(view, selectionManager.togglePath(position, item.toPath()));
        } else {
            Intent intent = new Intent(getActivity(), PhotoActivity.class);
            intent.putExtra(MediaSetUtils.PHOTO_ID, position);
            intent.putExtra(MediaSetUtils.PHOTO_PATH, gridAdapter.getItem(position).getPath());
            intent.putExtra(MediaSetUtils.BUCKET, MediaSetUtils.CAMERA_BUCKET_ID);
            startActivity(intent);
        }
    }

    private void updateItem(View v, boolean select) {
        TimeLineAdapter.ViewHolder vh = (TimeLineAdapter.ViewHolder)v.getTag();
        if(vh != null) {
            vh.selectIcon.setChecked(select);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(!gridAdapter.getSelectState()) {
            gridAdapter.setSelectState(true);
            bottomView.setVisibility(View.VISIBLE);
        } else {
            gridAdapter.setSelectState(false);
            bottomView.setVisibility(View.GONE);
        }
        return true;
    }
}
