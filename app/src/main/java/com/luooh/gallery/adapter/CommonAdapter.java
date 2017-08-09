package com.luooh.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luooh on 2016/7/5.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions mOptions;
    protected DisplayImageOptions.Builder mBuilder;

    public CommonAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
        mImageLoader = ImageLoader.getInstance();
        mBuilder = new DisplayImageOptions.Builder();
        mBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        mBuilder.cacheInMemory(true);
        mBuilder.cacheOnDisk(true);
    }

    public void setItems(List<T> datas) {
        if(datas != null && datas.size() > 0) {
            mList.clear();
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void addItem(T data) {
        if(mList != null) {
            mList.add(data);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> datas) {
        if(datas != null && datas.size() > 0) {
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void clearAll(boolean refresh) {
        mList.clear();
        if(refresh) {
            notifyDataSetChanged();
        }
    }

    public List<T> getList() {
        return mList;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}
