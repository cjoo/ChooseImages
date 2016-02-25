package com.cj.android.chooseimages.progress;

/**
 * Created by jian.cao on 2016/2/25.
 */
public interface FileSearchProgress<T> {
    void pushProgress(T progress);
}
