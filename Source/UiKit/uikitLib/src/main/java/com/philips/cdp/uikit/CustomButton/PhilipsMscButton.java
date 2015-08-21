package com.philips.cdp.uikit.CustomButton;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsMscButton extends ImageButton {

    private static final int SQUARE = 0;
    private static final int CIRCLE = 1;
    private Context context;

    public PhilipsMscButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        drawView(attrs);
    }

    private void drawView(final AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhilipsView);
        setImageResource(typedArray.getResourceId(R.styleable.PhilipsView_btnImageDrawable, R.drawable.greater));
        addStates(getNormalStateDrawable(typedArray), getPressedStateDrawable(typedArray));
        typedArray.recycle();
    }

    private GradientDrawable getShapeDrawable(final TypedArray typedArray) {
        GradientDrawable gradientDrawable;
        int shapeValue = typedArray.getInt(R.styleable.PhilipsView_btnShape, SQUARE);
        Resources resources = context.getResources();
        switch (shapeValue) {
            case SQUARE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.square);
                break;
            case CIRCLE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.circle);
                break;
            default:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.square);
                break;
        }
        return gradientDrawable;
    }

    private GradientDrawable getNormalStateDrawable(final TypedArray typedArray) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray);
        int color = typedArray.getColor(R.styleable.PhilipsView_btnBgColor, context.getResources().getColor(R.color.philips_dark_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    private GradientDrawable getPressedStateDrawable(final TypedArray typedArray) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray);
        int color = typedArray.getColor(R.styleable.PhilipsView_btnBgColorPressed, context.getResources().getColor(R.color.philips_bright_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    private void addStates(final GradientDrawable normal, final GradientDrawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                pressed);
        states.addState(new int[]{},
                normal);
        setBackgroundDrawable(states);
    }
}