package com.hyd.testlockdemo;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * TODO 这里描述下用途吧
 * Created by lingxiaoming on 2016/7/20 0020.
 */
public class CommonDialog {

    public static Dialog getNoButtonDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_nobutton);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static Dialog getOneButtonDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_onebutton);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static Dialog getTwoButtonDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_twobutton);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
