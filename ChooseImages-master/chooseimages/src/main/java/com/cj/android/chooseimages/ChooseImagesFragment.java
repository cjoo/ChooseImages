package com.cj.android.chooseimages;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责显示和选中sd卡里的图片Fragment
 * Created by jian.cao on 2016/2/17.
 */
public class ChooseImagesFragment extends Fragment {
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
    //文件筛选异步任务
    private FileFilterAsyncTask fileFilterAsyncTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);//在配置变化的时候将这个fragment保存下来
        fileFilterAsyncTask = new FileFilterAsyncTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        initView(view);
        return view;
    }

    private void initView(View parent) {
        imagesAdapter = new ImagesAdapter(getActivity(), displayImage);
        gridView = (GridView) parent.findViewById(R.id.gridView);
        gridView.setAdapter(imagesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemCheck(position);
            }
        });
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "没有SD卡", Toast.LENGTH_LONG).show();
        } else {
            //开始遍历SD卡找出图片
            fileFilterAsyncTask.execute();
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
     * 设置图片选中监听
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
     * 清空
     */
    public void clear() {
        itemsCheck(this.paths);
    }

    //文件筛选异步任务
    private class FileFilterAsyncTask extends AsyncTask<Void, File, List<String>> {
        private BitmapFactory.Options mOptions;
        private ProgressDialog mProgressDialog;

        public FileFilterAsyncTask() {
            mOptions = new BitmapFactory.Options();
            mOptions.inJustDecodeBounds = true;
            mProgressDialog = new ProgressDialog(getActivity());
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            return getFilePaths(Environment.getExternalStorageDirectory());
        }

        @Override
        protected void onProgressUpdate(File... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            imagesAdapter.setPaths(strings);
            //初始选中的图片
            List<String> paths = new ArrayList<String>(ChooseImagesFragment.this.paths);
            ChooseImagesFragment.this.paths.clear();
            itemsCheck(paths);

            mProgressDialog.dismiss();
        }

        // 遍历一个文件路径，然后把文件子目录中的图片文件地址返回
        private List<String> getFilePaths(File root) {
            List<String> result = new ArrayList<String>();//返回对象
            List<File[]> fileList = new ArrayList<File[]>();//需要遍历的文件或文件夹
            fileList.add(root.listFiles());//首先把根目录下所有文件和文件夹添加进fileList
            //size>0遍历
            while (fileList.size() > 0) {
                File files[] = fileList.get(0);
                if (files != null) {
                    for (File f : files) {
                        //进度
                        publishProgress(f);
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
        imagesAdapter.check(position);
        if (onChooseClickListener != null) {
            onChooseClickListener.onItemClick(typePath.path, typePath.checked);
        }
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
}
