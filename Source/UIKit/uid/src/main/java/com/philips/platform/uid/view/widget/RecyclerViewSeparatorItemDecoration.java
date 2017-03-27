/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.philips.platform.uid.drawable.SeparatorDrawable;

/**
 * The type Recycler view divider item decoration.
 * This class can be used to set the divider as per DLS specification which is 1dp/1px in height
 * Color specification is as per below table
 * Theme        Ultralight          VeryLight           Light               Bright           Very dark
 * <p>
 * Color        10% textPrimary     15% textPrimary     20% textPrimary     15% textPrimary  15% textPrimary
 * (gray 75)           color 75            white               white            white
 */
public class RecyclerViewSeparatorItemDecoration extends RecyclerView.ItemDecoration {

    final SeparatorDrawable divider;

    /**
     * Instantiates a new Recycler view divider item decoration.
     * <p>
     * Usage
     * recyclerview.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
     *
     * @param context the context
     */
    public RecyclerViewSeparatorItemDecoration(@NonNull Context context) {
        this.divider = new SeparatorDrawable(context);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider != null) {
            drawHorizontalDivider(canvas, parent);
        }
        super.onDraw(canvas, parent, state);
    }

    private void drawHorizontalDivider(@NonNull final Canvas canvas, @NonNull final RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int top = child.getBottom() - divider.getHeight();
            int bottom = child.getBottom();
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    @VisibleForTesting
    public SeparatorDrawable getDivider() {
        return divider;
    }
}
