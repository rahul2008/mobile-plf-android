package com.philips.cdp.ui.catalog.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.dotnavigation.ViewPagerAdaptor;
import com.philips.cdp.uikit.customviews.ImageIndicator;
import com.philips.cdp.uikit.drawable.VectorDrawable;

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
        Drawable drawables[] = {VectorDrawable.create(this, R.drawable.uikit_cart),
                VectorDrawable.create(this, R.drawable.uikit_coffee),
                VectorDrawable.create(this, R.drawable.uikit_stats),
                VectorDrawable.create(this, R.drawable.uikit_recycle_bin)};
        mIndicator.setImages(drawables);
        mIndicator.setViewPager(mPager);
    }
}
