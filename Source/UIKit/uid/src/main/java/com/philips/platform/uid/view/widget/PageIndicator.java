/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.support.v4.view.ViewPager;

/**
 * This interface for pager to communicate with indicator
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {
    /**
     * sets the viewPager to indicator.
     *
     * @param view ViewPager to be set
     */
    void setViewPager(ViewPager view);

    /**
     * sets the indicator to a ViewPager.
     *
     * @param viewPager ViewPager to be set
     * @param initialPosition with initialPosition selected/highlited
     */
    void setViewPager(ViewPager viewPager, int initialPosition);

    /**
     * <p>sets the current page of both the ViewPager and indicator.</p>
     * <p>
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param position to be shown on pager and corresponding dot to be highlighted
     */
    void setCurrentItem(int position);

    /**
     * Set a page change listener which will receive callback when page changed.
     *
     * @param listener ViewPager.OnPageChangeListener to be set
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);
}
