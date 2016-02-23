package com.cj.android.chooseimages;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import java.io.File;

/**
 * Created by jian.cao on 2016/2/23.
 */
public class ProgressDialog extends Dialog {
    private TextView tv_path, tv_name;

    public ProgressDialog(Context context) {
        super(context, R.style.progress_dialog);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_progress);
        setCancelable(false);
        tv_path = (TextView) findViewById(R.id.tv_path);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }

    public void setProgress(File file) {
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            tv_path.setText(parentFile.getAbsolutePath());
        }
        tv_name.setText(file.getName());
    }
}
