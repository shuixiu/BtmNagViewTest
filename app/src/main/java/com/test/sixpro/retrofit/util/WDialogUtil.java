package com.test.sixpro.retrofit.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;


public class WDialogUtil {

    private Dialog dialog;
    private ProgressDialog waitingDialog;

    private Boolean isShow;

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private static WDialogUtil dialogUtil;

    public static WDialogUtil create() {
        if (dialogUtil == null) {
            synchronized (WDialogUtil.class) {
                if (dialogUtil == null) {
                    dialogUtil = new WDialogUtil();
                }
            }
        }
        return dialogUtil;
    }

    public void show(Context context) {
        if (dialog != null && isShow) {
            if (!dialog.isShowing())
                dialog.show();
        } else if (dialog == null && isShow) {
            waitingDialog = new ProgressDialog(context);
            dialog = waitingDialog;
            waitingDialog.setMessage("正在加载数据...");
            if (!waitingDialog.isShowing()) {
                waitingDialog.show();
            }
        }
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
            dialog.dismiss();
            dialog = null;
        }

        if (waitingDialog != null) {
            waitingDialog.hide();
            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }


    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }
}
