package com.luooh.gallery.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.luooh.gallery.R;
import com.luooh.gallery.adapter.ImageLoaderAdapter;
import com.luooh.gallery.view.PagerSlidingTabStrip;

public class ImageLoaderActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagestrip);
        ImageLoaderAdapter adapter = new ImageLoaderAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(mViewPager);
    }
}
