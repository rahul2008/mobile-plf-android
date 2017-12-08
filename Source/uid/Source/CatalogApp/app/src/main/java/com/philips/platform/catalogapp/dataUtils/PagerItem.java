/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.catalogapp.dataUtils;

import android.graphics.drawable.Drawable;

public class PagerItem {
    public Drawable resourceId;
    public int number;

    public PagerItem(final Drawable resourceId, final int number) {
        this.resourceId = resourceId;
        this.number = number;
    }
}
