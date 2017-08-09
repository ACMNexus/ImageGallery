package com.luooh.gallery.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luooh.gallery.R;
import com.luooh.gallery.fragments.ImageFolderFragment;
import com.luooh.gallery.fragments.ImageGalleryFragment;

/**
 * Created by Luooh on 2017/8/3.
 */
public class ImageLoaderAdapter extends FragmentPagerAdapter {

    private String mTitles[];

    public ImageLoaderAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImageLoaderAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitles = context.getResources().getStringArray(R.array.imageloader_title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ImageFolderFragment.newInstance();
            case 1:
                return ImageGalleryFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
