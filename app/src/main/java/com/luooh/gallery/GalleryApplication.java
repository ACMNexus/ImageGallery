package com.luooh.gallery;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Luooh on 2017/8/9.
 */
public class GalleryApplication extends Application {

    public static GalleryApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565);
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(sInstance)
                .defaultDisplayImageOptions(builder.build())
                .threadPoolSize(3).build();
        ImageLoader.getInstance().init(configuration);
    }

    public static GalleryApplication getInstance() {
        return sInstance;
    }
}
