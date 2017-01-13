/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * The type Recycler view divider item decoration.
 * This class can be used to set the divider as per DLS specification which is 1dp/1px in height
 * Color specification is as per below table
 * Theme        Ultralight          VeryLight           Light               Bright           Very dark
 * <p>
 * Color        10% textPrimary     15% textPrimary     20% textPrimary     15% textPrimary  15% textPrimary
 * (gray 75)           color 75            white               white            white
 */
public class RecyclerViewDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.dividerHeight, R.attr.uidSeparatorColor, R.attr.uidSeparatorAlpha};
    public static final int HEIGHT_ATTR_INDEX = 0;
    public static final int SEPARATOR_ATT_INDEX = 1;
    public static final int SEPARATOR_ALPHA_ATTR_INDEX = 2;
    private final Drawable divider;
    private final int height;

    /**
     * Instantiates a new Recycler view divider item decoration.
     * <p>
     * Usage
     * recyclerview.addItemDecoration(new RecyclerViewDividerItemDecoration(getContext()));
     *
     * @param context the context
     */
    public RecyclerViewDividerItemDecoration(@NonNull Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        this.height = (int) styledAttributes.getDimension(HEIGHT_ATTR_INDEX, 1);
        final int color = styledAttributes.getColor(SEPARATOR_ATT_INDEX, ContextCompat.getColor(context, R.color.uid_gray_level_75));
        final Float alpha = styledAttributes.getFloat(SEPARATOR_ALPHA_ATTR_INDEX, 0);
        final int modulateColorAlpha = UIDUtils.modulateColorAlpha(color, alpha);

        this.divider = new ColorDrawable(modulateColorAlpha);
        styledAttributes.recycle();
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
    public Drawable getDivider() {
        return divider;
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
        return height;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider == null) {
            super.onDrawOver(canvas, parent);
            return;
        }
        drawHorizontalDivider(canvas, parent);
    }

    private void drawHorizontalDivider(@NonNull final Canvas canvas, @NonNull final RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() - params.bottomMargin - divider.getIntrinsicHeight();
            int bottom = top + height;
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
        outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
    }
}
