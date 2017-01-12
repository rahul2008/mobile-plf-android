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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDUtils;

public class RecyclerViewDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.dividerHeight, R.attr.uidSeparatorColor, R.attr.uidSeparatorAlpha};

    private Drawable divider;
    private int height = 1;

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    public RecyclerViewDividerItemDecoration(@NonNull Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        this.height = (int) styledAttributes.getDimension(0, 1);
        final int color1 = styledAttributes.getColor(1, ContextCompat.getColor(context, R.color.uid_gray_level_75));
        final Float alpha = styledAttributes.getFloat(2, 1);
        final int modulateColorAlpha = UIDUtils.modulateColorAlpha(color1, alpha);
        this.divider = new ColorDrawable(modulateColorAlpha);
        styledAttributes.recycle();
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
