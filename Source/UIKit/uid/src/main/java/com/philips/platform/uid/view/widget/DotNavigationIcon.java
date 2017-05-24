/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class DotNavigationIcon extends AppCompatImageButton {
    private int iconLeftSpacing;
    private int iconRightSpacing;

    public DotNavigationIcon(@NonNull final Context context) {
        this(context, null);
    }

    public DotNavigationIcon(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNavigationIcon(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDDotNavigation, defStyleAttr, R.style.UIDDotNavigation);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Drawable backgroundDrawable = typedArray.getDrawable(R.styleable.UIDDotNavigation_uidDotNavigationDrawable);
        //Need to check if this works even for normal drawables and icons along with vector
        setBackground(backgroundDrawable);

        iconLeftSpacing = typedArray.getDimensionPixelOffset(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingLeft, -1);
        iconRightSpacing = typedArray.getDimensionPixelOffset(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingRight, -1);

        applyBackgroundTinting(typedArray, theme);
        typedArray.recycle();
    }

    protected Drawable getBackgroundDrawable(final int backgroundDrawable) {
        return ContextCompat.getDrawable(getContext(), backgroundDrawable);
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, @NonNull final Resources.Theme theme) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UIDDotNavigation_uidDotNavigationIconColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setSupportBackgroundTintList(ThemeUtils.buildColorStateList(getResources(), theme, backGroundListID));
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();

        layoutParams.leftMargin = getIconLeftSpacing();
        layoutParams.rightMargin = getIconRightSpacing();
        setLayoutParams(layoutParams);
    }

    protected int getIconLeftSpacing() {
        return iconLeftSpacing;
    }

    protected int getIconRightSpacing() {
        return iconRightSpacing;
    }
}
