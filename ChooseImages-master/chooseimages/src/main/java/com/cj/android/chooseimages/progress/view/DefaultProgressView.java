package com.cj.android.chooseimages.progress.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.cj.android.chooseimages.R;

import java.io.File;

/**
 * Created by jian.cao on 2016/2/23.
 */
public class DefaultProgressView extends Dialog implements ProgressView {
    private TextView tv_path, tv_name;

    public DefaultProgressView(Context context) {
        super(context, R.style.progress_dialog);
        initView();
    }

    private void initView() {
        setContentView(R.layout.cis_dialog_progress);
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
