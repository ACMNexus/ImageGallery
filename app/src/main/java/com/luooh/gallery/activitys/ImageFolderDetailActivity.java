package com.luooh.gallery.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.luooh.gallery.R;
import com.luooh.gallery.adapter.ImageStickGridAdapter;
import com.luooh.gallery.bean.MediaInfo;
import com.luooh.gallery.utils.TimeUtils;
import com.luooh.gallery.view.stickgridview.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.List;

public class ImageFolderDetailActivity extends AppCompatActivity implements Runnable, Handler.Callback {

    private Handler mHandler;
    private String mBucketName;
    private ImageStickGridAdapter mAdapter;
    private String mFormatType = "yyyy-MM-dd";
    private StickyGridHeadersGridView mGridView;
    private String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder_detail);

        initView();
        loadData();
    }

    private void initView() {
        mGridView = (StickyGridHeadersGridView) findViewById(R.id.gridview);
        mAdapter = new ImageStickGridAdapter(this);
        mGridView.setAdapter(mAdapter);
    }

    private void loadData() {
        Intent intent = getIntent();
        mBucketName = intent.getStringExtra("name");
        mHandler = new Handler(this);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Cursor cursor = null;
        List<MediaInfo> list = null;
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams;
            String bucket = mBucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, searchParams, null, orderBy + " DESC");
            list = getDataFromCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.obj = list;
        mHandler.sendMessage(msg);
    }

    private List<MediaInfo> getDataFromCursor(Cursor cursor) {

        List<MediaInfo> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            MediaInfo mediaInfo;
            while (cursor.moveToNext()) {
                mediaInfo = new MediaInfo();
                mediaInfo.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow((MediaStore.Images.Media.DATA))));
                mediaInfo.setDisplaytime(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)));
                mediaInfo.setDisplaytime(TimeUtils.parseTime(mFormatType, TimeUtils.formatTime(mFormatType, mediaInfo.getDisplaytime())));
                list.add(mediaInfo);
            }
        }
        return list;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                List list = (List) msg.obj;
                if (list != null && list.size() > 0) {
                    mAdapter.setItems(list);
                } else {
                    Toast.makeText(this, "数据为空!!!", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }
}
