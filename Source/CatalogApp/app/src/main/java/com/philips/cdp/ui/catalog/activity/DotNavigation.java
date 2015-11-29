package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.dotnavigation.ViewPagerAdaptor;
import com.philips.cdp.uikit.customviews.CircleIndicator;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DotNavigation extends CatalogActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dot_navigation);

        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdaptor(getSupportFragmentManager()));

        final CircleIndicator mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
