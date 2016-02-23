package com.cj.android.chooseimages;

/**
 * 地址+选中状态
 */
public class TypePath {
    public String path;
    public boolean checked;

    public TypePath(String path) {
        this.path = path;
        this.checked = false;
    }
}
