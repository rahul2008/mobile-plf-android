/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class PagerItem {
    final Drawable drawable;
    final int index;

    public PagerItem(@NonNull final Drawable drawable, final int index) {
        this.drawable = drawable;
        this.index = index;
    }
}
