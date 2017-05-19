/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class DotNavigationIcon extends AppCompatImageButton {
    private int iconSpacing;

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

        int backGroundListID = typedArray.getResourceId(R.styleable.UIDDotNavigation_uidDotNavigationIconColorList, -1);
        int backgroundDrawable = typedArray.getResourceId(R.styleable.UIDDotNavigation_uidDotNavigationDrawable, -1);
        //Need to check if this works even for normal drawables and icons along with vector
        setBackground(ContextCompat.getDrawable(getContext(), backgroundDrawable));
        iconSpacing = typedArray.getDimensionPixelOffset(R.styleable.UIDDotNavigation_uidDotNavigationDotsSpacing, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            ViewCompat.setBackgroundTintList(this, ThemeUtils.buildColorStateList(getContext().getResources(), getContext().getTheme(), backGroundListID));
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();

        layoutParams.leftMargin = iconSpacing;
        layoutParams.rightMargin = iconSpacing;
        setLayoutParams(layoutParams);
    }

    protected int getIconSpacing() {
        return iconSpacing;
    }
}
