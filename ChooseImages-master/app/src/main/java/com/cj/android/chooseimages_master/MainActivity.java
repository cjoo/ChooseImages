package com.cj.android.chooseimages_master;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;

/**
 * Created by maesinfo-024 on 2016/2/23.
 */
public class MainActivity extends Activity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        init();
    }

    private void init() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheSize(50 * 1024 * 1024)        // 缓冲大小
                .discCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(this)) // default
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(height + "X" + width);
    }

    public void b1(View view) {
        Intent intent = new Intent(this, Choose1Activity.class);
        intent.putStringArrayListExtra("data", paths);
        startActivityForResult(intent, 10000);

    }

    public void b2(View view) {
        Intent intent = new Intent(this, Choose2Activity.class);
        intent.putStringArrayListExtra("data", paths);
        startActivityForResult(intent, 10000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10000) {
                linearLayout.removeAllViews();
                paths = data.getStringArrayListExtra("data");
                for (String path : paths) {
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLoader.getInstance().displayImage("file://" + path, imageView, Options.getDisplayImageOptions());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                    params.leftMargin = 6;
                    linearLayout.addView(imageView, params);
                }
            }
        }
    }

    private ArrayList<String> paths;
}
