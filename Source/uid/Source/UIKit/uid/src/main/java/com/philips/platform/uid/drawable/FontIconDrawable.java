/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.View;

/**
 * Embed an icon into a Drawable that can be used as TextView icons, or ActionBar icons.
 * <p>
 * new IconDrawable(context, IconValue.icon_star)
 * .colorRes(R.color.white)
 * .actionBarSize();
 * <p>
 * If you don't set the size of the drawable, it will use default. Note that in an ActionBar, if you don't
 * set the size explicitly it uses 0, so please use actionBarSize().
 */

public class FontIconDrawable extends Drawable {

    private Context context;

    private String icon;

    private TextPaint paint;

    private int size = -1;

    private Rect mRect;

    private Rect tempDrawingRect = new Rect();

    private FontIconState mState;

    private boolean mMutated;

    private ColorStateList colorStateList;

    /**
     * Create an IconDrawable.
     *
     * @param context Your activity or application context.
     * @param icon    The icon you want this drawable to display.
     * @param typeface typeface to be applied on drawable
     *                 @since 3.0.0
     */
    public FontIconDrawable(@NonNull Context context, @NonNull String icon, @NonNull Typeface typeface) {
        this.context = context;
        this.icon = icon;
        paint = new TextPaint();
        mRect = new Rect();
        colorStateList = ColorStateList.valueOf(Color.WHITE);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setUnderlineText(false);
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        paint.setAntiAlias(true);
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     *
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable actionBarSize() {
        int ANDROID_ACTIONBAR_ICON_SIZE_DP = 24;
        return sizeDp(ANDROID_ACTIONBAR_ICON_SIZE_DP);
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable sizeRes(@DimenRes int dimenRes) {
        return sizePx(context.getResources().getDimensionPixelSize(dimenRes));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable sizeDp(int size) {
        return sizePx(dpToPx(context.getResources(), size));
    }

    /**
     * Dp to px.
     *
     * @param res the res
     * @param dp  the dp
     * @return the int
     * @since 3.0.0
     */
    private static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    private FontIconDrawable sizePx(int size) {
        this.size = size;

        paint.setTextSize(size);
        paint.getTextBounds(icon, 0, icon.length(), mRect);
        fitRect(mRect, size);
        setBounds(mRect);
        invalidateSelf();
        return this;
    }

    private static void fitRect(Rect rect, int size) {
        int w = rect.width();
        if (size > w) {
            int d1 = (size - w) / 2;
            int d2 = size - w - d1;
            rect.left -= d1;
            rect.right += d2;
        }

        int h = rect.height();
        if (size > h) {
            int d1 = (size - h) / 2;
            int d2 = size - h - d1;
            rect.top -= d1;
            rect.bottom += d2;
        }
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable color(@ColorInt int color) {
        colorStateList = ColorStateList.valueOf(color);
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        invalidateSelf();
        return this;
    }

    /**
     * Set the ColorStateList of the drawable.
     *
     * @param colorStateList The ColorStateList, usually from android.content.res.ColorStateList.
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable colorStateList(@NonNull ColorStateList colorStateList) {
        this.colorStateList = colorStateList;
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        invalidateSelf();
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable colorRes(@ColorRes int colorRes) {
        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes));
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        invalidateSelf();
        return this;
    }

    /**
     * Set the alpha of this drawable.
     *
     * @param alpha The alpha, between 0 (transparent) and 255 (opaque).
     * @return The current IconDrawable for chaining.
     * @since 3.0.0
     */
    public FontIconDrawable alpha(int alpha) {
        setAlpha(alpha);
        invalidateSelf();
        return this;
    }

    @Override
    public int getIntrinsicHeight() {
        return mRect.height();
    }

    @Override
    public int getIntrinsicWidth() {
        return mRect.width();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        paint.getTextBounds(icon, 0, 1, tempDrawingRect);
        float textBottom = (getBounds().height() - tempDrawingRect.height()) / 2f + tempDrawingRect.height() - tempDrawingRect.bottom;
        canvas.drawText(icon, getBounds().width() / 2f, textBottom, paint);
    }

    @Override
    protected boolean onStateChange(int[] state) {
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        invalidateSelf();
        return true;
    }

    /**
     * Indicates whether this drawable will change its appearance based on
     * state.
     *
     * @return True if this drawable changes its appearance based on state,
     * false otherwise.
     * @since 3.0.0
     */
    @Override
    public boolean isStateful() {
        return true;
    }

    /**
     * Set the alpha of this drawable.
     *
     * @param alpha The alpha to be applied to the drawable.
     *              @since 3.0.0
     */
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    /**
     * Set the colorFilter of this drawable.
     *
     * @param cf The colorFilter to be applied to the drawable.
     *           @since 3.0.0
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    /**
     * Clear the colorFilter of this drawable.
     * @since 3.0.0
     *
     */
    @Override
    public void clearColorFilter() {
        paint.setColorFilter(null);
    }

    /**
     * Get the Opacity of this drawable.
     *
     *@return The current opacity.
     * @since 3.0.0
     */
    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     *              @since 3.0.0
     */
    public void setStyle(@NonNull Paint.Style style) {
        paint.setStyle(style);
    }

    /**
     * Call this function to get a parcelable instance of the drawable. Synonymous with onSaveInstance of Activity and should be called manually.
     *
     * @return Parcelable instance of the drawable to be used for saving.
     * @since 3.0.0
     */
    public Parcelable onSaveInstanceState() {
        final SavedState ss = new SavedState(AbsSavedState.EMPTY_STATE);

        ss.text = icon;
        ss.textSize = size;
        ss.paint = paint;
        ss.colorStateList = colorStateList;
        ss.mRect = mRect;

        return ss;
    }

    /**
     * Call this function to restore drawable instance to a new drawable. Synonymous with onRestoreInstance of Activity and should be called manually.
     *
     * @param savedState Saved state of the drawable obtained in the {@link #onSaveInstanceState()}
     *                   @since 3.0.0
     */
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof SavedState) {
            SavedState ss = (SavedState) savedState;

            icon = ss.text;
            size = ss.textSize;
            paint = ss.paint;
            colorStateList = ss.colorStateList;
            mRect = ss.mRect;
            invalidateSelf();
        }
    }

    private static class SavedState extends View.BaseSavedState {
        private String text;
        private TextPaint paint;
        private int textSize;
        private ColorStateList colorStateList;
        private Rect mRect;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@SuppressWarnings("NullableProblems") Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeString(text);
            out.writeParcelableArray(new Parcelable[]{(Parcelable) paint, colorStateList, mRect}, flags);
            out.writeFloat(textSize);
        }
    }

    @Override
    public ConstantState getConstantState() {
        mState.mChangingConfigurations = super.getChangingConfigurations();
        return mState;
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mState.mChangingConfigurations;
    }

    @Override
    public
    @NonNull
    Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mState = new FontIconState(mState);
            mMutated = true;
        }
        return this;
    }

    final static class FontIconState extends ConstantState {
        private int mChangingConfigurations;
        private String icon;
        private TextPaint paint;
        private ColorStateList colorStateList;
        private int size;
        private Rect mRect;

        FontIconState(FontIconState orig) {
            if (orig != null) {
                mChangingConfigurations = orig.mChangingConfigurations;
                icon = orig.icon;
                paint = orig.paint;
                size = orig.size;
                colorStateList = orig.colorStateList;
                mRect = orig.mRect;
            }
        }

        @Override
        public
        @NonNull
        Drawable newDrawable() {
            return new FontIconDrawable(this);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return newDrawable();
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

    }

    private FontIconDrawable(@NonNull FontIconState state) {
        mState = new FontIconState(state);
    }
}