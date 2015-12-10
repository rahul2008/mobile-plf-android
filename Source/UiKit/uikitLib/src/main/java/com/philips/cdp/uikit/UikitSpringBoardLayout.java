package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.customviews.LayerListDrawable;

/**
 * Created by 310213373 on 12/7/2015.
 */
public class UikitSpringBoardLayout extends LinearLayout {

    private Drawable selector;
    public static int STYLE_THEME = 1;
    int baseColor;
    int colorStyle=1;
    int overlayColor = 0;
    Context mContext;
    public UikitSpringBoardLayout(Context context) {
        super(context);
    }

    public UikitSpringBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UikitSpringBoardLayout);
        colorStyle = typedArray.getInt(R.styleable.UikitSpringBoardLayout_opacityStyle, 0);
        typedArray.recycle();

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.verydarkBaseColor});
        baseColor = ar.getInt(0, R.attr.baseColor);
        if(colorStyle==0) {
            overlayColor = ar.getInt(1, R.attr.verydarkBaseColor);
        }
        else {

            overlayColor = ar.getInt(1, R.attr.verydarkBaseColor);
            overlayColor = Color.argb(89, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor));
        }
        selector = getBackgroundSelector();
        ar.recycle();
        //ToDO: Initialize ur seelctor
    }

    public UikitSpringBoardLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        selector = getBackgroundSelector();
        int version = Build.VERSION.SDK_INT;
        if(version<16) {
            child.setBackgroundDrawable(selector);
        }
        else {
            child.setBackground(selector);
        }

    }


    private Drawable getBackgroundSelector() {
        GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
     /*  Drawable d = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable
               .uikit_springboard_layout_shape, null);

        GradientDrawable gd =(GradientDrawable) d;
                gd.setColor(baseColor);*/
        d.setColor(baseColor);
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
        background.addState(new int[]{}, d);

        return background;
    }

    private Drawable getPressedDrawable() {
        Drawable[] d = new Drawable[2];
       //  d[0] = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable
          //      .uikit_springboard_layout_shape, null);

            d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
        ((GradientDrawable)d[0]).setColor(baseColor);
       //  d[1] = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable
         //       .uikit_springboard_layout_shape,null);

       d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
        ((GradientDrawable)d[1]).setColor(overlayColor);
        return new LayerListDrawable(d);
    }
}
