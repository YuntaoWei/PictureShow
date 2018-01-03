package com.android.picshow.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.android.picshow.R;
import com.android.picshow.utils.PicShowUtils;

/**
 * Created by yuntao.wei on 2017/12/29.
 * github:https://github.com/YuntaoWei
 * blog:http://blog.csdn.net/qq_17541215
 */

public class MenuExecutor {

    public static final int MENU_ACTION_DELETE = 0x001;
    public static final int MENU_ACTION_SETAS = 0x002;
    public static final int MENU_ACTION_DETAIL = 0x003;
    public static final int MENU_ACTION_SHARE = 0x004;
    public static final int MENU_ACTION_EDIT = 0x005;
    public static final int MENU_ACTION_RENAME = 0x006;


    private Context mContext;


    public MenuExecutor(Context ctx) {
        mContext = ctx;
    }

    public boolean execute(int action, Uri u, boolean image) {
        switch (action) {
            case MENU_ACTION_DELETE :
                PicShowUtils.deleteItem(mContext, u, image);
                break;
            case MENU_ACTION_SETAS :

                break;

            case MENU_ACTION_DETAIL :
                break;

            case MENU_ACTION_SHARE :
                PicShowUtils.shareItem(mContext, u, image);
                break;

            case MENU_ACTION_EDIT :
                PicShowUtils.editItem(mContext, u, image);
                break;

            case MENU_ACTION_RENAME :
                showConfirmDialog(u, image);
                break;

            default:
                return false;
        }
        return true;
    }

    private void showConfirmDialog(final Uri u, final boolean image) {
        ConfirmDialog confirmDialog = ConfirmDialog.getInstance(mContext);
        confirmDialog.setTitle(R.string.rename);
        confirmDialog.setView(R.layout.editable_dialog_layout, R.id.edit_able);
        confirmDialog.setPositiveListener(R.string.confirm, new ConfirmDialog.ConfirDialogClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which, String editable) {
                PicShowUtils.renameItem(mContext, u, image, editable);
            }

        });
        confirmDialog.setNegativeListener(R.string.cancle, null);
        confirmDialog.show();
    }

}
