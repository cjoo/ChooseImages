package com.cj.android.chooseimages_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cj.android.chooseimages.ChooseImagesV4Fragment;

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
        fragment.clearChecked();
    }

    private void initView() {
        fragment = new ChooseImagesV4Fragment();
        fragment.setMaxNumber(6);
        fragment.setPaths(getIntent().getStringArrayListExtra("data"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

}