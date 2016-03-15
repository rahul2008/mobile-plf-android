
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 *
 *  Layout class which provides different color for pressed state and normal state <br>
 *
 *  Use opacityStyle ( darker , darker_with_opacity) attribute in xml to apply the proper color on
 * pressed state.
 *
 * Darker is just one shade darker of the current color
 * darker_with_opacity is one shade darker of current base color with opacity of 35 %
 *
 * @attr ref com.philips.cdp.uikit.R.attr.uikit_opacityStyle
 *
 *
 */

public class UikitSpringBoardLayout extends LinearLayout {


    int baseColor;
    int colorStyle = 1;
    int overlayColor = 0;
    Context mContext;
    private Drawable selector;

    public UikitSpringBoardLayout(Context context) {
        super(context);
    }

    public UikitSpringBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UikitSpringBoardLayout);
        colorStyle = typedArray.getInt(R.styleable.UikitSpringBoardLayout_uikit_opacityStyle, 0);
        typedArray.recycle();

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor, R.attr.uikit_darkerColor});
        baseColor = ar.getInt(0, R.attr.uikit_baseColor);
        if (colorStyle == 0) {
            overlayColor = ar.getInt(1, R.attr.uikit_darkerColor);
        } else {

            overlayColor = ar.getInt(1, R.attr.uikit_darkerColor);
            overlayColor = Color.argb(89, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor));
        }
        selector = getBackgroundSelector();
        ar.recycle();

    }

    public UikitSpringBoardLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        selector = getBackgroundSelector();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            child.setBackgroundDrawable(selector);
        } else {
            child.setBackground(selector);
        }

    }


    private Drawable getBackgroundSelector() {

        if (colorStyle == 0) {
            GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();

            d.setColor(baseColor);
            StateListDrawable background = new StateListDrawable();
            background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
            background.addState(new int[]{}, d);

            return background;
        } else {
            GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();

            d.setColor(baseColor);
            StateListDrawable background = new StateListDrawable();
            background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
            background.addState(new int[]{}, d);

            return background;
        }
    }

    private Drawable getPressedDrawable() {
        if (colorStyle == 0) {
            Drawable[] d = new Drawable[2];
            d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();
            ((GradientDrawable) d[0]).setColor(baseColor);

            d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();
            ((GradientDrawable) d[1]).setColor(overlayColor);
            return new LayerDrawable(d);

        } else {
            {
                Drawable[] d = new Drawable[2];
                d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
                ((GradientDrawable) d[0]).setColor(baseColor);

                d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
                ((GradientDrawable) d[1]).setColor(overlayColor);
                return new LayerDrawable(d);

            }
        }

    }
}