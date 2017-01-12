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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDUtils;

public class RecyclerViewDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.divider, android.R.attr.dividerHeight, R.attr.uidSeparatorColor, R.attr.uidSeparatorAlpha};

    private Drawable divider;
    private int height = 1;

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private int orientation = HORIZONTAL;

    public RecyclerViewDividerItemDecoration(Context context, int orientation) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        final int color = styledAttributes.getResourceId(0, 0);
        this.height = (int) styledAttributes.getDimension(1, 1);
        final int color1 = styledAttributes.getColor(2, ContextCompat.getColor(context, R.color.uid_orange_level_75));
        final Float alpha = styledAttributes.getFloat(3, 1);
        final int modulateColorAlpha = UIDUtils.modulateColorAlpha(color1, 1);
        this.divider = new ColorDrawable(modulateColorAlpha);
        styledAttributes.recycle();
        setOrientation(orientation);
    }

    private void setOrientation(final int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (divider == null) {
            super.onDrawOver(c, parent);
            return;
        }
        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();

        for (int i = 1; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int size = divider.getIntrinsicHeight();
            final int top = child.getTop() - params.topMargin;
            final int bottom = top + size;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    private void drawHorizontal(final Canvas c, final RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() - params.bottomMargin - divider.getIntrinsicHeight();
            int bottom = top + height;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (orientation == VERTICAL) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        }
    }
}
