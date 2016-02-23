package com.cj.android.chooseimages_master;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.cj.android.chooseimages.ChooseImagesV4Fragment;
import com.cj.android.chooseimages.DisplayImage;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by maesinfo-024 on 2016/2/22.
 */
public class Choose1Activity extends FragmentActivity {
    private ChooseImagesV4Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose1);

        initView();
    }

    public void back(View view) {
        finish();
    }

    public void complete(View view) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", fragment.getPaths());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void clear(View view) {
        fragment.clear();
    }

    private void initView() {
        fragment = new ChooseImagesV4Fragment();
        fragment.setMaxNumber(6);
        fragment.setPaths(getIntent().getStringArrayListExtra("data"));
        fragment.setDisplayImage(new DisplayImage() {
            @Override
            public void display(View view, String path) {
                ImageLoader.getInstance().displayImage("file://" + path, (ImageView) view, Options.getDisplayImageOptions());
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

}