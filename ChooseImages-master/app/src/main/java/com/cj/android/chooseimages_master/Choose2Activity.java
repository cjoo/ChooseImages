package com.cj.android.chooseimages_master;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cj.android.chooseimages.ChooseImagesFragment;
import com.cj.android.chooseimages.DisplayImage;
import com.cj.android.chooseimages.OnChooseClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maesinfo-024 on 2016/2/23.
 */
public class Choose2Activity extends Activity implements OnChooseClickListener {
    private ChooseImagesFragment fragment;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose2);

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

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        fragment = new ChooseImagesFragment();
        fragment.setOnChooseClickListener(this);
        fragment.setMaxNumber(10);
        fragment.setPaths(getIntent().getStringArrayListExtra("data"));
        fragment.setDisplayImage(new DisplayImage() {
            @Override
            public void display(View view, String path) {
                ImageLoader.getInstance().displayImage("file://" + path, (ImageView) view, Options.getDisplayImageOptions());
            }
        });
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onItemClick(String path, boolean checked) {
        if (checked) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage("file://" + path, imageView, Options.getDisplayImageOptions());
            stringList.add(path);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
            params.leftMargin = 6;
            linearLayout.addView(imageView, params);
        } else {
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equals(path)) {
                    stringList.remove(i);
                    linearLayout.removeViewAt(i);
                    break;
                }
            }
        }
    }

    private List<String> stringList = new ArrayList<String>();
}
