package com.cj.android.chooseimages;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cj.android.chooseimages.adapter.CheckView;
import com.cj.android.chooseimages.adapter.DefaultCheckView;
import com.cj.android.chooseimages.adapter.ImagesAdapter;
import com.cj.android.chooseimages.displayimage.DefaultDisplayImage;
import com.cj.android.chooseimages.displayimage.DisplayImage;
import com.cj.android.chooseimages.imagesearch.ImageFileSearch;
import com.cj.android.chooseimages.imagesearch.SDCardImageFileSearch;
import com.cj.android.chooseimages.progress.ProgressAsyncTask;
import com.cj.android.chooseimages.progress.view.DefaultProgressView;
import com.cj.android.chooseimages.progress.view.ProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责显示和选中sd卡里的图片Fragment
 * Created by jian.cao on 2016/2/17.
 */
public class ChooseImagesV4Fragment extends Fragment {
    private static final String TAG = "ChooseImages";
    //存放选中的图片的地址路径
    private ArrayList<String> paths = new ArrayList<String>();
    //显示图片
    private GridView gridView;
    private ImagesAdapter imagesAdapter;
    //图片选择监听
    private OnChooseClickListener onChooseClickListener;
    //负责加载图片
    private DisplayImage displayImage;
    //最大选中数
    private int maxNumber = 6;
    //文件筛选任务
    private FileFilterAsyncTask fileFilterAsyncTask;
    //查找出来的图片地址集合
    private static List<String> pathsCache;
    //文件查找类对象
    private ImageFileSearch imageFileSearch;
    //查找进度视图
    private ProgressView progressView;
    //check
    private CheckView checkView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);//在配置变化的时候将这个fragment保存下来
        fileFilterAsyncTask = new FileFilterAsyncTask();
        //取个默认值
        if (progressView == null) {
            progressView = new DefaultProgressView(getActivity());
        }
        if (displayImage == null) {
            displayImage = new DefaultDisplayImage(getActivity());
        }
        if (imageFileSearch == null) {
            imageFileSearch = new SDCardImageFileSearch(getActivity());
        }
        if(checkView==null){
            checkView = new DefaultCheckView(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        displayImage.destroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cis_fragment_main, null);
        initView(view);
        return view;
    }

    private void initView(View parent) {
        imagesAdapter = new ImagesAdapter(getActivity(), displayImage, checkView);
        gridView = (GridView) parent.findViewById(R.id.gridView);
        gridView.setAdapter(imagesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemCheck(position);
            }
        });
        if (pathsCache == null) {
            //开始找出图片
            fileFilterAsyncTask.execute();
        } else {
            fileFilterAsyncTask.complete(pathsCache);
        }
        //不支持横竖屏
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }

    /**
     * 设置最大数
     *
     * @param maxNumber
     */
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    /**
     * 获取选中的图片路径
     *
     * @return
     */
    public ArrayList<String> getPaths() {
        return paths;
    }

    /**
     * 设置图片选中监听器
     *
     * @param onChooseClickListener
     */
    public void setOnChooseClickListener(OnChooseClickListener onChooseClickListener) {
        this.onChooseClickListener = onChooseClickListener;
    }

    /**
     * 设置初始值
     *
     * @param paths
     */
    public void setPaths(ArrayList<String> paths) {
        this.paths = paths != null ? paths : this.paths;
    }

    /**
     * 设置图片地址查找对象
     *
     * @param imageFileSearch
     */
    public void setImageFileSearch(ImageFileSearch imageFileSearch) {
        this.imageFileSearch = imageFileSearch;
    }

    /**
     * 设置图片查找进度视图
     *
     * @param progressView
     */
    public void setProgressView(ProgressView progressView) {
        this.progressView = progressView;
    }

    /**
     * 设置图片的加载对象
     *
     * @param displayImage
     */
    public void setDisplayImage(DisplayImage displayImage) {
        this.displayImage = displayImage;
        if (imagesAdapter != null) {
            imagesAdapter.setDisplayImage(displayImage);
        }
    }

    /**
     * 设置checkView
     *
     * @param checkView
     */
    public void setCheckView(CheckView checkView) {
        this.checkView = checkView;
    }

    /**
     * 清空选中状态
     */
    public void clearChecked() {
        itemsCheck(this.paths);
    }

    /**
     * 清空查找出来的图片地址
     */
    public void clearPathsCache() {
        pathsCache = null;
    }

    /**
     * 重新查找图片地址
     */
    public void againLoad() {
        if (fileFilterAsyncTask.isComplete) {
            fileFilterAsyncTask.execute();
        }
    }

    //文件筛选异步任务
    private class FileFilterAsyncTask extends ProgressAsyncTask {
        public boolean isComplete = true;

        @Override
        protected List<String> doInBackground(Void... params) {
            return pathsCache = imageFileSearch.getFilePaths(this);
        }

        @Override
        protected void onProgressUpdate(File... values) {
            super.onProgressUpdate(values);
            progressView.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            isComplete = false;
            super.onPreExecute();
            progressView.show();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            complete(strings);
            progressView.dismiss();
        }

        public void complete(List<String> strings) {
            imagesAdapter.setPaths(strings);
            //初始选中的图片
            List<String> paths = new ArrayList<String>(ChooseImagesV4Fragment.this.paths);
            ChooseImagesV4Fragment.this.paths.clear();
            itemsCheck(paths);
            isComplete = true;
        }
    }

    //图片点击,选中状态随之改变
    private void itemsCheck(List<String> paths) {
        List<TypePath> strings = imagesAdapter.getPaths();
        if (paths.size() > 0) {
            for (int i = 0; i < strings.size(); i++) {
                String path = strings.get(i).path;
                for (int j = 0; j < paths.size(); j++) {
                    String initPath = paths.get(j);
                    if (path.equals(initPath)) {
                        itemCheck(i);
                        break;
                    }
                }
            }
        }
    }

    //图片点击,选中状态随之改变（1:之前为选中，则变为未选中；2:之前为未选中，则变为选中）
    private void itemCheck(int position) {
        TypePath typePath = (TypePath) imagesAdapter.getItem(position);
        if (typePath.checked) {
            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                if (path.equals(typePath.path)) {
                    paths.remove(i);
                    break;
                }
            }
        } else {
            if (paths.size() >= maxNumber) {
                Toast.makeText(getActivity(), "最多只能选择" + maxNumber + "张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            paths.add(typePath.path);
        }
        imagesAdapter.check(position, gridView);
        if (onChooseClickListener != null) {
            onChooseClickListener.onItemClick(typePath.path, typePath.checked);
        }
    }
}
