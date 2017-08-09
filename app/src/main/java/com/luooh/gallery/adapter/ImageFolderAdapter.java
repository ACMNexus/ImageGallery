package com.luooh.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luooh.gallery.R;
import com.luooh.gallery.bean.BucketInfo;

/**
 * Created by Luooh on 2017/8/3.
 */
public class ImageFolderAdapter extends CommonAdapter<BucketInfo> {

    private int mWidth;
    private LayoutInflater mInflater;

    public ImageFolderAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(mContext);
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BucketInfo bucketInfo = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_image_folder_item, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.bucket_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.bucket_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) holder.imageView.getLayoutParams();
        imageParams.width  = mWidth/2;
        imageParams.height = mWidth/2;
        holder.imageView.setLayoutParams(imageParams);

        holder.nameTextView.setText(bucketInfo.getBucketName());
        mImageLoader.displayImage("file://" + bucketInfo.getBucketUrl(), holder.imageView, mOptions);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
    }
}
