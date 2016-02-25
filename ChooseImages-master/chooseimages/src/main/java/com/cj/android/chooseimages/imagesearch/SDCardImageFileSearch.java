package com.cj.android.chooseimages.imagesearch;

import android.graphics.BitmapFactory;
import android.os.Environment;

import com.cj.android.chooseimages.progress.FileSearchProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian.cao on 2016/2/24.
 */
public class SDCardImageFileSearch implements ImageFileSearch {
    private BitmapFactory.Options mOptions;

    public SDCardImageFileSearch() {
        mOptions = new BitmapFactory.Options();
        mOptions.inJustDecodeBounds = true;
    }

    @Override
    public List<String> getFilePaths(FileSearchProgress<File> fileFileSearchProgress) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return new ArrayList<String>();
        }
        List<String> result = new ArrayList<String>();//返回对象
        List<File[]> fileList = new ArrayList<File[]>();//需要遍历的文件或文件夹
        fileList.add(Environment.getExternalStorageDirectory().listFiles());//首先把根目录下所有文件和文件夹添加进fileList
        //size>0遍历
        while (fileList.size() > 0) {
            File files[] = fileList.get(0);
            if (files != null) {
                for (File f : files) {
                    //进度
                    fileFileSearchProgress.pushProgress(f);
                    //是文件夹
                    if (f.isDirectory()) {
                        fileList.add(f.listFiles());
                    }
                    //是文件
                    else {
                        BitmapFactory.decodeFile(f.getAbsolutePath(), mOptions);
                        //是图片文件
                        if (mOptions.outWidth != -1) {
                            result.add(f.getAbsolutePath());
                        }
                    }
                }
            }
            fileList.remove(0);
        }
        //size=0，已遍历完SD卡所有文件
        return result;//返回图片路径集合
    }
}
