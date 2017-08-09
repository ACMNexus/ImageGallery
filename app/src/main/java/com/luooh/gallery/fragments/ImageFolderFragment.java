package com.luooh.gallery.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.luooh.gallery.GalleryApplication;
import com.luooh.gallery.R;
import com.luooh.gallery.activitys.ImageFolderDetailActivity;
import com.luooh.gallery.adapter.ImageFolderAdapter;
import com.luooh.gallery.bean.BucketInfo;

import java.util.ArrayList;
import java.util.List;

public class ImageFolderFragment extends Fragment implements Handler.Callback, AbsListView.OnItemClickListener, Runnable {

    private Context mContext;
    private View mContentView;
    private GridView mGridView;
    private Handler mHandler;

    private static final String PROJECTION_BUCKET[] = {
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATA,
    };

    private ImageFolderAdapter mAdapter;

    public static ImageFolderFragment newInstance() {
        return new ImageFolderFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = GalleryApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_image_folder, container, false);
        initView();
        loadData();
        return mContentView;
    }

    private void initView() {
        mGridView = (GridView) mContentView.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);
    }

    private void loadData() {
        mHandler = new Handler(this);
        mAdapter = new ImageFolderAdapter(mContext);
        mGridView.setAdapter(mAdapter);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BucketInfo bucketInfo = mAdapter.getItem(position);
        Intent selectImageIntent = new Intent(mContext, ImageFolderDetailActivity.class);
        selectImageIntent.putExtra("name", bucketInfo.getBucketName());
        selectImageIntent.putExtra("image", true);
        selectImageIntent.putExtra("isFromBucket", true);
        selectImageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(selectImageIntent);
    }

    @Override
    public void run() {
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        List<BucketInfo> list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, null, null, orderBy + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    BucketInfo entry = new BucketInfo();
                    entry.setBucketId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)));
                    entry.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                    entry.setBucketName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                    entry.setBucketUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                    if (!list.contains(entry)) {
                        list.add(entry);
                    }
                }
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }

        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.obj = list;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                List<BucketInfo> list = (List<BucketInfo>) msg.obj;
                if (list != null && list.size() > 0) {
                    mAdapter.setItems(list);
                } else {
                    Toast.makeText(mContext, "当前没有数据!!!", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }
}
