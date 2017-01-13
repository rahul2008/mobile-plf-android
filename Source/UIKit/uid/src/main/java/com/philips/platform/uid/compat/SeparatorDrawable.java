/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.compat;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.philips.platform.uid.view.widget.RecyclerViewDividerItemDecoration;

/**
 * The type Divider drawable.
 * This class can be used as utility class for setting divider and and its height
 */
public class SeparatorDrawable {
    private final RecyclerViewDividerItemDecoration recyclerViewDividerItemDecoration;

    /**
     * Instantiates a new Divider drawable.
     *
     * @param context the context
     */
    public SeparatorDrawable(final Context context) {
        recyclerViewDividerItemDecoration = new RecyclerViewDividerItemDecoration(context);
    }

    /**
     * Gets divider drawable which can be set for listview or recycler view.
     * using
     * SeparatorDrawable dividerDrawable = new SeparatorDrawable(getContext());
     * <p>
     * listView.setDivider(dividerDrawable.getDrawable());
     *
     * @return the divider
     */
    public Drawable getDrawable() {
        return recyclerViewDividerItemDecoration.getDivider();
    }

    /**
     * Gets height of divider as per DLS specification.
     * Usage :
     * SeparatorDrawable dividerDrawable = new SeparatorDrawable(getContext());
     * <p>
     * listView.setDivider(dividerDrawable.getDrawable());
     * listView.setDividerHeight(dividerDrawable.getHeight());
     *
     * @return the height
     */
    public int getHeight() {
        return recyclerViewDividerItemDecoration.getHeight();
    }
}
