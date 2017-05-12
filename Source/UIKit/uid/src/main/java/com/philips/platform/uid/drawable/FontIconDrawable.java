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

    public static int ANDROID_ACTIONBAR_ICON_SIZE_DP = 24;

    private Context context;

    private String icon;

    private TextPaint paint;

    private int size = -1;

    private int alpha = 255;

    private FontIconState mState;

    private boolean mMutated;

    private ColorStateList colorStateList;

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
     */
    public FontIconDrawable colorStateList(ColorStateList colorStateList) {
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
     */
    public FontIconDrawable colorRes(int colorRes) {
        colorStateList = ColorStateList.valueOf(context.getResources().getColor(colorRes));
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
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
    protected boolean onStateChange(int[] state) {
        paint.setColor(colorStateList.getColorForState(getState(), colorStateList.getDefaultColor()));
        invalidateSelf();
        return true;
    }

    @Override
    public boolean isStateful() {
        return true;
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
        ss.colorStateList = colorStateList;

        return ss;
    }

    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof SavedState) {
            SavedState ss = (SavedState) savedState;

            icon = ss.text;
            size = ss.textSize;
            paint = ss.paint;
            colorStateList = ss.colorStateList;
            invalidateSelf();
        }
    }

    static class SavedState extends View.BaseSavedState {
        String text;
        TextPaint paint;
        int textSize;
        ColorStateList colorStateList;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@SuppressWarnings("NullableProblems") Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeString(text);
            out.writeParcelable((Parcelable) paint, flags);
            out.writeParcelable(colorStateList, flags);
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
            paint = in.readParcelable(getClass().getClassLoader());
            colorStateList = in.readParcelable(getClass().getClassLoader());
            textSize = in.readInt();
        }
    }

    @Override
    public ConstantState getConstantState() {
        if (mState.canConstantState()) {
            mState.mChangingConfigurations = getChangingConfigurations();
            return mState;
        }
        return null;
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mState.mChangingConfigurations
                | mState.mDrawable.getChangingConfigurations();
    }

    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mState.mDrawable.mutate();
            mMutated = true;
        }
        return this;
    }

    final static class FontIconState extends ConstantState {
        int mChangingConfigurations;
        Drawable mDrawable;
        String icon;
        TextPaint paint;
        ColorStateList colorStateList;
        int size;
        private int alpha;
        private boolean mCheckedConstantState;
        private boolean mCanConstantState;

        FontIconState(FontIconState orig, Resources res) {
            if (orig != null) {
                mChangingConfigurations = orig.mChangingConfigurations;
                if (res != null) {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable(res);
                } else {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable();
                }
                mDrawable.setBounds(orig.mDrawable.getBounds());
                mDrawable.setLevel(orig.mDrawable.getLevel());
                icon = orig.icon;
                paint = orig.paint;
                size = orig.size;
                alpha = orig.alpha;
                colorStateList = orig.colorStateList;
                mCheckedConstantState = mCanConstantState = true;
            }
        }

        @Override
        public Drawable newDrawable() {
            return new FontIconDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new FontIconDrawable(this, res);
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

        boolean canConstantState() {
            if (!mCheckedConstantState) {
                mCanConstantState = mDrawable.getConstantState() != null;
                mCheckedConstantState = true;
            }
            return mCanConstantState;
        }
    }

    private FontIconDrawable(FontIconState state, Resources res) {
        mState = new FontIconState(state, res);
    }
}