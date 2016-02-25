package com.cj.android.chooseimages.displayimage;

import android.view.View;

/**
 * Created by jian.cao on 2016/2/17.
 */
public interface DisplayImage<T extends View> {
    /**
     * 加载图片
     *
     * @param t    显示控件
     * @param path 本地地址
     */
    void display(T t, String path);
}
