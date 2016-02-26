package com.cj.android.chooseimages.adapter;

import android.content.Context;

/**
 * Created by jian.cao on 2016/2/26.
 */
public class DefaultCheckView extends IconCheckView {
    public DefaultCheckView(Context context) {
        super(context);
    }

    @Override
    protected int getResourceId(boolean checked) {
        return checked ? android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background;
    }
}
