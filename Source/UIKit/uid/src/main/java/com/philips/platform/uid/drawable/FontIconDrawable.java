package com.philips.platform.uid.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.View;

/** Embed an icon into a Drawable that can be used as TextView icons, or ActionBar icons.
 *
 * new IconDrawable(context, IconValue.icon_star)
 *           .colorRes(R.color.white)
 *           .actionBarSize();
 *
 * If you don't set the size of the drawable, it will use default. Note that in an ActionBar, if you don't
 * set the size explicitly it uses 0, so please use actionBarSize().
 */

public class FontIconDrawable extends Drawable {

    public static int ANDROID_ACTIONBAR_ICON_SIZE_DP = 24;

    private final Context context;

    private String icon;

    private TextPaint paint;

    private int size = -1;

    private int alpha = 255;

    /**
     * Create an IconDrawable.
     *
     * @param context Your activity or application context.
     * @param icon    The icon you want this drawable to display.
     */
    public FontIconDrawable(Context context, String icon, Typeface typeface) {
        this.context = context;
        this.icon = icon;
        paint = new TextPaint();
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setUnderlineText(false);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     *
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable actionBarSize() {
        return sizeDp(ANDROID_ACTIONBAR_ICON_SIZE_DP);
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable sizeRes(int dimenRes) {
        return sizePx(context.getResources().getDimensionPixelSize(dimenRes));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     * @return The current IconDrawable for chaining.
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
     */
    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable sizePx(int size) {
        this.size = size;
        setBounds(0, 0, size, size);
        invalidateSelf();
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable color(int color) {
        paint.setColor(ColorStateList.valueOf(color).getDefaultColor());
        invalidateSelf();
        return this;
    }

    /**
     * Set the ColorStateList of the drawable.
     *
     * @param colorStateList The ColorStateList, usually from android.content.res.ColorStateList.
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable colorStateList(ColorStateList colorStateList) {
        paint.setColor(colorStateList.getColorForState(getState(),colorStateList.getDefaultColor()));
        invalidateSelf();
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable colorRes(int colorRes) {
        paint.setColor(context.getResources().getColor(colorRes));
        invalidateSelf();
        return this;
    }

    /**
     * Set the alpha of this drawable.
     *
     * @param alpha The alpha, between 0 (transparent) and 255 (opaque).
     * @return The current IconDrawable for chaining.
     */
    public FontIconDrawable alpha(int alpha) {
        setAlpha(alpha);
        invalidateSelf();
        return this;
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setTextSize(getBounds().height());
        Rect textBounds = new Rect();
        String textValue = icon;
        paint.getTextBounds(textValue, 0, 1, textBounds);
        float textBottom = (getBounds().height() - textBounds.height()) / 2f + textBounds.height() - textBounds.bottom;
        canvas.drawText(textValue, getBounds().width() / 2f, textBottom, paint);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean setState(int[] stateSet) {
        int oldValue = paint.getAlpha();
        int newValue = isEnabled(stateSet) ? alpha : alpha / 2;
        paint.setAlpha(newValue);
        return oldValue != newValue;
    }

    /**
     * Checks if is enabled.
     *
     * @param stateSet the state set
     * @return true, if is enabled
     */
    public static boolean isEnabled(int[] stateSet) {
        for (int state : stateSet)
            if (state == android.R.attr.state_enabled)
                return true;
        return false;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public void clearColorFilter() {
        paint.setColorFilter(null);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     */
    public void setStyle(Paint.Style style) {
        paint.setStyle(style);
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(AbsSavedState.EMPTY_STATE);

        ss.text = icon;
        ss.textSize = size;
        ss.paint = paint;

        return ss;
    }

    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof SavedState) {
            SavedState ss = (SavedState) savedState;

            icon = ss.text;
            size = ss.textSize;
            paint = ss.paint;
            invalidateSelf();
        }
    }

    static class SavedState extends View.BaseSavedState {
        String text;
        TextPaint paint;
        int textSize;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@SuppressWarnings("NullableProblems") Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeString(text);
            out.writeParcelable((Parcelable) paint, flags);
            out.writeFloat(textSize);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        protected SavedState(Parcel in) {
            super(in);

            text = in.readString();
            paint = in.readParcelable(null);
            textSize = in.readInt();
        }
    }
}