package com.cj.android.chooseimages.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 如果你只是为了修改选中和未选中的图片的话,选择实现这个吧
 * Created by jian.cao on 2016/2/26.
 */
public abstract class IconCheckView implements CheckView {
    private Context context;
    private Point screenSize = new Point();

    public IconCheckView(Context context) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getSize(screenSize);//getSize(Point) API level 13 以上
        screenSize.x = wm.getDefaultDisplay().getWidth();
        screenSize.y = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public View getView() {
        return new ImageView(context);
    }

    @Override
    public RelativeLayout.LayoutParams getLayoutParams() {
        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo=new PercentLayoutHelper.PercentLayoutInfo();
        percentLayoutInfo.leftMarginPercent = 0.006f;
        percentLayoutInfo.topMarginPercent = 0.006f / screenSize.y * screenSize.x;
        layoutParams.setPercentLayoutInfo(percentLayoutInfo);
        return layoutParams;
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