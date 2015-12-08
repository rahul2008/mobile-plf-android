package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.customviews.LayerListDrawable;

/**
 * Created by 310213373 on 12/7/2015.
 */
public class CustomLinearLayout extends LinearLayout {

    private Drawable selector;
    private int baseColor;
    int overlayColor = 0;
    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.verydarkBaseColor});
        baseColor  = ar.getInt(0, R.attr.baseColor);
        overlayColor = ar.getInt(1, R.attr.verydarkBaseColor);
        overlayColor = Color.argb(89,Color.red(overlayColor),Color.green(overlayColor),Color.blue(overlayColor));
        getBackgroundSelector();
        selector = getBackgroundSelector();
        ar.recycle();
        //ToDO: Initialize ur seelctor
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        selector = getBackgroundSelector();
        child.setBackground(selector);

    }

    private void initViews(Context context, AttributeSet attrs) {

    }

    private Drawable getBackgroundSelector() {
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
        background.addState(new int[]{}, new ColorDrawable(baseColor)); //
        return background;
    }

    private Drawable getPressedDrawable() {

      //  int baseColor = 0;
     //   int overlayColor = 0; //35%

        Drawable[] d = new Drawable[2];
        d[0] = new ColorDrawable(baseColor);
        d[1] = new ColorDrawable(overlayColor);

        return new LayerListDrawable(d);
    }
}
