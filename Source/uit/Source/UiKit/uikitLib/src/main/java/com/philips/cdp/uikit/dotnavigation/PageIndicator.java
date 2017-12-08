package com.philips.cdp.uikit.dotnavigation;

import android.support.v4.view.ViewPager;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {

    void setViewPager(ViewPager view);

    void setViewPager(ViewPager view, int initialPosition);

    void setCurrentItem(int item);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    void notifyDataSetChanged();

}
