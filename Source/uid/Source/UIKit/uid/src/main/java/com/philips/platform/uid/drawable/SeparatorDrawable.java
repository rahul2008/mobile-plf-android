/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import com.philips.platform.uid.R;

/**
 * The type Divider drawable.
 * This class can be used as utility class for setting divider and and its height
 */
public class SeparatorDrawable extends Drawable {

    private static final int HEIGHT_ATTR_INDEX = 0;
    private static final int SEPARATOR_ATT_INDEX = 1;
    private static final int[] ATTRS = new int[]{android.R.attr.dividerHeight, R.attr.uidSeparatorContentNormalBackgroundColor};
    private final Paint paint;
    private int height;
    public SeparatorDrawable(@NonNull final Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        this.height = styledAttributes.getDimensionPixelSize(HEIGHT_ATTR_INDEX, 1);
        final int color = styledAttributes.getColor(SEPARATOR_ATT_INDEX, ContextCompat.getColor(context, R.color.uid_gray_level_75));
        paint = new Paint();
        paint.setColor(color);
        styledAttributes.recycle();
    }

    @Override
    public void setBounds(final int left, final int top, final int right, final int bottom) {
        super.setBounds(left, top, right, top + height);
    }

    public int getHeight() {
        return height;
    }

    @VisibleForTesting
    public int getColor() {
        return paint.getColor();
    }

    /**
     * This API can be used to set color for separator
     *
     * @param color Color to be set to drawable
     */
    public void setColor(@ColorInt int color) {
        paint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull final Canvas canvas) {
        canvas.drawRect(getBounds(), paint);
    }

    @Override
    public void setAlpha(final int alpha) {

    }

    @Override
    public void setColorFilter(final ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
