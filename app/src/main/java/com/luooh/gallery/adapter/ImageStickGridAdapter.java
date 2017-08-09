package com.luooh.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luooh.gallery.R;
import com.luooh.gallery.bean.ImageInfo;
import com.luooh.gallery.utils.DimentionUtils;
import com.luooh.gallery.utils.TimeUtils;
import com.luooh.gallery.view.stickgridview.StickyGridHeadersSimpleAdapter;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Luooh on 2016/7/8.
 */
public class ImageStickGridAdapter extends CommonAdapter<ImageInfo> implements StickyGridHeadersSimpleAdapter
{
    private int mWidthPixels;
    private AnimaterFirstDisplayListener mAnimateListener;

    public ImageStickGridAdapter(Context context) {
        super(context);
        mWidthPixels = (DimentionUtils.getScreenWidth(context) - DimentionUtils.dp2px(context, 12))/3;
        mAnimateListener = new AnimaterFirstDisplayListener();
        mBuilder.showImageForEmptyUri(R.drawable.ic_media_picture);
        mBuilder.showImageOnLoading(R.drawable.ic_media_picture);
        mBuilder.showImageOnFail(R.drawable.ic_media_picture);
        mOptions = mBuilder.build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ImageInfo imageInfo = getItem(position);
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.layout_stickimage_child, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.imageChecked = (ImageView) convertView.findViewById(R.id.imageChecked);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.width = mWidthPixels;
        params.height = mWidthPixels;
        holder.imageView.setLayoutParams(params);

        mImageLoader.displayImage("file://" + imageInfo.getFilePath(), holder.imageView, mOptions, mAnimateListener);
        if(imageInfo.isCheck()) {
            holder.imageChecked.setVisibility(View.VISIBLE);
            Drawable drawable = holder.imageView.getDrawable();
            if(drawable != null) {
                drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
        }else {
            holder.imageChecked.setVisibility(View.GONE);
            Drawable drawable = holder.imageView.getDrawable();
            if(drawable != null) {
                drawable.mutate().clearColorFilter();
            }
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mList.get(position).getDisplaytime();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderHolder holder;
        if(convertView == null) {
            holder = new HeaderHolder();
            convertView = View.inflate(mContext, R.layout.layout_stickimage_header, null);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.grouptitle);
            convertView.setTag(holder);
        }else {
            holder = (HeaderHolder) convertView.getTag();
        }
        holder.titleTextView.setText(TimeUtils.judgeTime(mContext, getHeaderId(position)));
        return convertView;
    }

    private static class AnimaterFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayImages.contains(imageUri);
                if(firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 1000);
                    displayImages.add(imageUri);
                }
            }
        }
    }

    class ViewHolder {
        ImageView imageView;
        ImageView imageChecked;
    }

    class HeaderHolder {
        TextView titleTextView;
    }
}
