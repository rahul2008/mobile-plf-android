package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.customviews.LayerListDrawable;

/**
 * Created by 310213373 on 12/7/2015.
 */
public class SpringBoardItemSelector extends LinearLayout {

    private Drawable selector;
    int baseColor;
    int overlayColor = 0;

    public SpringBoardItemSelector(Context context) {
        super(context);
    }

    public SpringBoardItemSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.verydarkBaseColor});
        baseColor = ar.getInt(0, R.attr.baseColor);
        overlayColor = ar.getInt(1, R.attr.verydarkBaseColor);
        overlayColor = Color.argb(89, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor));
        selector = getBackgroundSelector();
        ar.recycle();
        //ToDO: Initialize ur seelctor
    }

    public SpringBoardItemSelector(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        selector = getBackgroundSelector();
        child.setBackgroundDrawable(selector);

    }

    private void initViews(Context context, AttributeSet attrs) {

    }

    private Drawable getBackgroundSelector() {
        GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
        d.setColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
        background.addState(new int[]{}, d);

        return background;
    }

    private Drawable getPressedDrawable() {
        Drawable[] d = new Drawable[2];
        d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
        ((GradientDrawable)d[0]).setColor(baseColor);

        d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
        ((GradientDrawable)d[1]).setColor(overlayColor);
        return new LayerListDrawable(d);
    }
}
