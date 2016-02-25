package com.cj.android.chooseimages.progress.view;

import java.io.File;

/**
 * Created by jian.cao on 2016/2/25.
 */
public interface ProgressView {
    void setProgress(File file);

    void show();

    void dismiss();
}
