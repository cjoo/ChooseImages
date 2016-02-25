package com.cj.android.chooseimages.progress;

import android.os.AsyncTask;

import java.io.File;
import java.util.List;

/**
 * Created by jian.cao on 2016/2/24.
 */
public abstract class ProgressAsyncTask extends AsyncTask<Void, File, List<String>> implements FileSearchProgress<File> {
    public void pushProgress(File progress) {
        publishProgress(progress);
    }
}
