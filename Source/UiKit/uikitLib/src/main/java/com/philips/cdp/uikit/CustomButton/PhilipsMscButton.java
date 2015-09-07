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

    public PhilipsMscButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.miscButtonStyle);
    }

    public PhilipsMscButton(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MiscButton, defStyle,
                R.style.Misc_button);
        drawView(typedArray, context.getResources());
    }

    private void drawView(final TypedArray typedArray, final Resources resources) {
        setImageResource(typedArray.getResourceId(R.styleable.MiscButton_miscButtonImageDrawable, 0));
        addStates(getNormalStateDrawable(typedArray, resources), getPressedStateDrawable(typedArray, resources));
        typedArray.recycle();
        setScaleType(ScaleType.CENTER);
    }

    private GradientDrawable getShapeDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable;
        int shapeValue = typedArray.getInt(R.styleable.MiscButton_miscButtonShape, SQUARE);
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

    private GradientDrawable getNormalStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.MiscButton_miscButtonBgColor, resources.getColor(R.color.philips_bright_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    private GradientDrawable getPressedStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.MiscButton_miscButtonBgColorPressed, resources.getColor(R.color.philips_light_blue));
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