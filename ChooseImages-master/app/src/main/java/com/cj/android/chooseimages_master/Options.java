package com.cj.android.chooseimages_master;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by maesinfo-024 on 2016/2/23.
 */
public class Options {
    private static DisplayImageOptions displayImageOptions;

    public static DisplayImageOptions getDisplayImageOptions() {
        if (displayImageOptions == null) {
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            builder.showImageOnLoading(android.R.drawable.stat_notify_error);
            builder.showImageForEmptyUri(android.R.drawable.stat_notify_error);
            builder.showImageForEmptyUri(android.R.drawable.stat_notify_error);
            builder.cacheInMemory(true);
            builder.bitmapConfig(Bitmap.Config.RGB_565);
            builder.imageScaleType(ImageScaleType.EXACTLY);
//            builder.displayer(new FadeInBitmapDisplayer(getResources().getDimensionPixelOffset(R.dimen.x266)));
            builder.resetViewBeforeLoading(true);
            displayImageOptions = builder.build();
        }
        return displayImageOptions;
    }
}
