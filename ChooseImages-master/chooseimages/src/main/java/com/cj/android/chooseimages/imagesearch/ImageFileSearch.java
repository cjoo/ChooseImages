package com.cj.android.chooseimages.imagesearch;

import com.cj.android.chooseimages.progress.FileSearchProgress;

import java.io.File;
import java.util.List;


/**
 * 图片文件查找
 * Created by jian.cao on 2016/2/24.
 */
public interface ImageFileSearch {
    List<String> getFilePaths(FileSearchProgress<File> fileFileSearchProgress);
}
