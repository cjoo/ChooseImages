package com.cj.android.chooseimages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cj.android.chooseimages.displayimage.DisplayImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian.cao on 2016/2/17.
 */
public class ImagesAdapter extends BaseAdapter {
    private Context mContext;
    //图片地址
    private List<TypePath> paths;
    //负责加载图片
    private DisplayImage displayImage;

    public ImagesAdapter(Context context) {
        this(context, null);
    }

    public ImagesAdapter(Context context, DisplayImage displayImage) {
        this.mContext = context;
        this.displayImage = displayImage;
        paths = new ArrayList<TypePath>();
    }

    public List<TypePath> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths.clear();
        this.addPaths(paths);
    }

    public void addPaths(List<String> paths) {
        for (String path : paths) {
            this.paths.add(new TypePath(path));
        }
        notifyDataSetChanged();
    }

    public void addPath(String path) {
        this.paths.add(new TypePath(path));
        notifyDataSetChanged();
    }

    public void check(int position, AbsListView absListView) {
        TypePath typePath = (TypePath) getItem(position);
        typePath.checked = !typePath.checked;
        int firstVisiblePosition = absListView.getFirstVisiblePosition();
        int lastVisiblePosition = absListView.getLastVisiblePosition();
        if (position >= firstVisiblePosition && firstVisiblePosition <= lastVisiblePosition) {
            View childView = absListView.getChildAt(position - firstVisiblePosition);
            Holder holder = (Holder) childView.getTag();
            holder.checkType.setImageResource(typePath.checked ?
                    android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background);
        }
    }

    /**
     * 设置图片的加载对象
     *
     * @param displayImage
     */
    public void setDisplayImage(DisplayImage displayImage) {
        this.displayImage = displayImage;
    }

    @Override
    public int getCount() {
        return paths == null ? 0 : paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cis_adapter_main, null);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_main);
            holder.checkType = (ImageView) convertView.findViewById(R.id.iv_checkType);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final TypePath typePath = (TypePath) getItem(position);
        if (displayImage != null) {
            displayImage.display(holder.imageView, typePath.path);
        }
        holder.checkType.setImageResource(typePath.checked ?
                android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background);
        return convertView;
    }

    class Holder {
        //SD卡图片
        public ImageView imageView;
        //选中状态图
        public ImageView checkType;
    }
}
