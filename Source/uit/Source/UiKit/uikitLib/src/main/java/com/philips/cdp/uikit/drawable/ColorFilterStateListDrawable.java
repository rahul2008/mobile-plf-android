package com.philips.cdp.uikit.drawable;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.SparseArray;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ColorFilterStateListDrawable extends StateListDrawable {

    private SparseArray<ColorFilter> filterArray;
    private int selectedIndex;
    private int stateCount;

    private boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

    public ColorFilterStateListDrawable() {
        super();
        filterArray = new SparseArray<ColorFilter>();
    }

    public void addState(int[] stateSet, Drawable drawable, ColorFilter filter) {
        if (isPreLollipop) {
            filterArray.put(stateCount, filter);
        } else {
            drawable.setColorFilter(filter);
        }
        addState(stateSet, drawable);
    }

    @Override
    public void addState(final int[] stateSet, final Drawable drawable) {
        super.addState(stateSet, drawable);
        stateCount++;
    }

    @Override
    public boolean selectDrawable(int idx) {
        if (isPreLollipop && selectedIndex != idx) {
            selectedIndex = idx;
            if (filterArray != null && filterArray.indexOfKey(idx) >= 0) {
                setColorFilter(filterArray.get(idx));
            } else {
                clearColorFilter();
            }
        }
        return super.selectDrawable(idx);
    }
}