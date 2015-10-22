package com.philips.cdp.uikit.costumviews;

import android.support.v4.view.ViewPager;
import android.view.View;

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

    void onClickUnSelectedCircle(View view, int position);

}
