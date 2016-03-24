package com.cj.android.chooseimages.progress.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.cj.android.chooseimages.R;

import java.io.File;

/**
 * Created by jian.cao on 2016/2/23.
 */
public class DefaultProgressView extends Dialog implements ProgressView {
    private static final String TAG = "DefaultProgressView";
    private TextView tv_path, tv_name;
    private AsyncTask asyncTask;
    private Context context;

    public DefaultProgressView(Context context, AsyncTask asyncTask) {
        super(context, R.style.progress_dialog);
        this.asyncTask = asyncTask;
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.cis_dialog_progress);
//        setCancelable(false);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                asyncTask.cancel(true);
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
        tv_path = (TextView) findViewById(R.id.tv_path);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }

    private long lastTime;
    private static final long TIME_INTERVAL = 200;

    public void setProgress(File file) {
        long nowTime = SystemClock.uptimeMillis();
        Log.i(TAG, "nowTime - lastTime=" + (nowTime - lastTime));
        if (nowTime - lastTime > TIME_INTERVAL) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                tv_path.setText(parentFile.getAbsolutePath());
            }
            tv_name.setText(file.getName());
            lastTime = nowTime;
        }
    }
}
