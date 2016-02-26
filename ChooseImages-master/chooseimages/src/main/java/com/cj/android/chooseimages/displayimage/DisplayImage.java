package com.cj.android.chooseimages.displayimage;

import android.view.View;

/**
 * Created by jian.cao on 2016/2/17.
 */
public abstract class DisplayImage<T extends View> {
    /**
     * 加载图片
     *
     * @param t    显示控件
     * @param path 本地地址
     */
    public abstract void display(T t, String path);

    /**
     * 生命周期结束时调用
     */
    public void destroy() {
    }
}
