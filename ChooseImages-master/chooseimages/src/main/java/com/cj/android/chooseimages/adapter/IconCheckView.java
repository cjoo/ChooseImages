package com.cj.android.chooseimages.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 如果你只是为了修改选中和未选中的图片的话,选择实现这个吧
 * Created by jian.cao on 2016/2/26.
 */
public abstract class IconCheckView implements CheckView {
    private Context context;

    public IconCheckView(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        return new ImageView(context);
    }

    @Override
    public RelativeLayout.LayoutParams getLayoutParams() {
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void check(View view, boolean checked) {
        ImageView imageView = (ImageView) view;
        imageView.setImageResource(this.getResourceId(checked));
    }

    /**
     * 获取资源路径ID，子类重写
     *
     * @param checked
     * @return
     */
    protected abstract int getResourceId(boolean checked);
}