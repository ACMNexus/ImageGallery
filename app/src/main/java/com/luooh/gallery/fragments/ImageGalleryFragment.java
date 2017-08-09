package com.luooh.gallery.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.luooh.gallery.GalleryApplication;
import com.luooh.gallery.R;
import com.luooh.gallery.adapter.ImageStickGridAdapter;
import com.luooh.gallery.bean.ImageInfo;
import com.luooh.gallery.utils.TimeUtils;
import com.luooh.gallery.view.stickgridview.StickyGridHeadersGridView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageGalleryFragment extends Fragment implements Handler.Callback, AbsListView.OnItemClickListener, Runnable {

    private Context mContext;
    private View mContentView;
    private Handler mHandler;
    private ImageStickGridAdapter mAdapter;
    private String mFormatType = "yyyy-MM-dd";
    private StickyGridHeadersGridView mGridView;

    private static final String PROJECTION_BUCKET[] = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.SIZE,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns._ID
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = GalleryApplication.sInstance;
    }

    public static ImageGalleryFragment newInstance() {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        initView();
        loadData();
        return mContentView;
    }

    private void initView() {
        mGridView = (StickyGridHeadersGridView) mContentView.findViewById(R.id.gridview);
        mGridView.setSelector(android.R.color.transparent);
        mGridView.setOnItemClickListener(this);
        mAdapter = new ImageStickGridAdapter(mContext);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(new PauseOnScrollListener(mAdapter.getImageLoader(), false, false));
    }

    private void loadData() {
        mHandler = new Handler(this);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position < 0) return;
        ImageInfo imageInfo = mAdapter.getItem(position);
        mAdapter.getList().get(position).setCheck(!imageInfo.isCheck());
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void run() {
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        ContentResolver contentResolver = mContext.getContentResolver();
        List<ImageInfo> imageInfos = null;
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, null, null, orderBy);
        if (cursor != null && cursor.getCount() > 0) {
            ImageInfo imageInfo;
            File file;
            imageInfos = new ArrayList();
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if(TextUtils.isEmpty(filePath)) continue;
                file = new File(filePath);
                if (!file.exists()) continue;
                long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                imageInfo = new ImageInfo();
                imageInfo.setFilePath(filePath);
                imageInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
                imageInfo.setFileSize(fileSize);
                //省略时分秒，只记录日期的秒数
                imageInfo.setDisplaytime(TimeUtils.parseTime(mFormatType, TimeUtils.formatTime(mFormatType, imageInfo.getCreateTime())));
                imageInfos.add(imageInfo);
            }
        }

        if(cursor != null) {
            cursor.close();
        }

        if (imageInfos != null && imageInfos.size() > 1) {
            Collections.sort(imageInfos, new Comparator<ImageInfo>() {
                @Override
                public int compare(ImageInfo imageInfo1, ImageInfo imageInfo2) {
                    if (imageInfo1.getDisplaytime() < imageInfo2.getDisplaytime()) {
                        return 1;
                    } else if (imageInfo1.getDisplaytime() > imageInfo2.getDisplaytime()) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        Message msg = mHandler.obtainMessage();
        if (imageInfos != null && imageInfos.size() > 0) {
            msg.obj = imageInfos;
            msg.what = 1;
            mHandler.sendMessage(msg);
        } else {
            msg.what = -1;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                List<ImageInfo> imageInfos = (List<ImageInfo>) msg.obj;
                mAdapter.setItems(imageInfos);
                break;
            case -1:
                Toast.makeText(mContext, "当前没有数据!!!", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
}
