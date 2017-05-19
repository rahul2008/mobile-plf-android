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
 *
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {
    /**
     * sets the indicator to a ViewPager.
     *
     * @param view
     */
    void setViewPager(ViewPager view);

    /**
     * sets the indicator to a ViewPager.
     *
     * @param view
     * @param initialPosition
     */
    void setViewPager(ViewPager view, int initialPosition);

    /**
     * <p>sets the current page of both the ViewPager and indicator.</p>
     * <p>
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * Set a page change listener which will receive callback when page changed.
     *
     * @param listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    /**
     * refresh the indicator that the pager list has changed.
     */
    void refresh();

    /**
     * Move to next item,
     */
    void showNext();

    /**
     * Move to previous item,
     */
    void showPrevious();
}
