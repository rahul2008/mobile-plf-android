package com.philips.cdp.ui.catalog.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.dotnavigation.ViewPagerAdaptor;
import com.philips.cdp.uikit.costumviews.ImageIndicator;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ImageNavigation extends CatalogActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_navigation);

        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdaptor(getSupportFragmentManager()));

        final ImageIndicator mIndicator = (ImageIndicator) findViewById(R.id.indicator);
        Drawable drawables[] = {ResourcesCompat.getDrawable(getResources(), R.drawable.apple, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.alarm, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.barchart, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.gear, null)};
        mIndicator.setImages(drawables);
        mIndicator.setViewPager(mPager);
    }
}
