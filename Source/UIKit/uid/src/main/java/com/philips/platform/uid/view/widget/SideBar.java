/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID Sidebar
 * <p>
 * <P> UID Sidebar is custom DrawerLayout as per DLS design.
 * <p>
 * <P> Use Sidebar in your xml layout file similar to DrawerLayout.
 * <p>
 *
 * @see <a href="https://confluence.atlas.philips.com/display/MU/How+to+integrate+Sidebar">https://confluence.atlas.philips.com/display/MU/How+to+integrate+Sidebar</a>
 */

public class SideBar extends DrawerLayout {

    private Context context;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUID);
        int shadowColor = array.getColor(R.styleable.PhilipsUID_uidDimLayerSubtleBackgroundColor, Color.TRANSPARENT);
        array.recycle();
        setScrimColor(shadowColor);
        float elevation = context.getResources().getDimensionPixelSize(R.dimen.uid_sidebar_elevation);
        setDrawerElevation(elevation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Let super sort out the height
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            if (isDrawerView(child)) {
                int width = Math.min(child.getMeasuredWidth(), getMaxWidth());
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }
    }

    private int getMaxWidth() {
        int defaultWidth = UIDUtils.getDeviceWidth(context) - UIDUtils.getActionBarSize(context);
        int recommendedMaxWidth = context.getResources().getDimensionPixelSize(R.dimen.uid_sidebar_max_width);
        return Math.min(defaultWidth, recommendedMaxWidth);
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        if ((absGravity & Gravity.LEFT) != 0 ) {
            // This child is a left-edge drawer
            return true;
        }
        if ((absGravity & Gravity.RIGHT) != 0) {
            // This child is a right-edge drawer
            return true;
        }
        return false;
    }
}
