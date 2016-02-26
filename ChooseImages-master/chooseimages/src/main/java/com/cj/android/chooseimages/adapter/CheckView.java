package com.cj.android.chooseimages.adapter;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jian.cao on 2016/2/26.
 */
public interface CheckView {

    View getView();

    RelativeLayout.LayoutParams getLayoutParams();

    void check(View view, boolean checked);
}
