package com.cj.android.chooseimages.imagesearch;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cj.android.chooseimages.progress.FileSearchProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian.cao on 2016/2/24.
 */
public class MediaStoreImageFileSearch implements ImageFileSearch {
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_PNG = "image/png";
    private ContentResolver mContentResolver;

    public MediaStoreImageFileSearch(ContentResolver contentResolver) {
        mContentResolver = contentResolver;

    }

    @Override
    public List<String> getFilePaths(FileSearchProgress<File> fileFileSearchProgress) {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // 只查询JPEG和PNG扩展名的图片，并按照最近修改时间排序
        StringBuffer selection = new StringBuffer();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ").append(MediaStore.Images.Media.MIME_TYPE);
        selection.append(" = ? ");
        String[] args = new String[]{IMAGE_JPEG, IMAGE_PNG};
        Cursor cursor = mContentResolver.query(imageUri, null, selection.toString(), args, MediaStore.Images.Media.DATE_MODIFIED);
        List<String> photoUrlList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            // 获取图片的路径
            String photoUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            fileFileSearchProgress.pushProgress(new File(photoUrl));
            photoUrlList.add(0, photoUrl);
        }
        cursor.close();
        return photoUrlList;
    }
}
