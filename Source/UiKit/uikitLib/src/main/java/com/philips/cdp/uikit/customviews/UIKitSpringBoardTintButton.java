package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UIKitSpringBoardTintButton extends AppCompatButton {

    private ColorStateList mTintList;
    public UIKitSpringBoardTintButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initTintColor();
    }

    public void setCompoundDrawableTintList(final ColorStateList tint) {
        mTintList = tint;
        invalidate();
    }

    @Override
    public void invalidate() {
        updateFixedTint();
        super.invalidate();
    }

    private void initTintColor() {
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.darkerColor});
        if (a != null) {
            int normalColor = a.getColor(0, Color.WHITE);
            int pressedColor = a.getColor(1, Color.WHITE);

            int [][] states = {{android.R.attr.state_pressed},{}};
            int [] colors = {pressedColor,normalColor};

            mTintList = new ColorStateList(states,colors);
            a.recycle();
        }
    }

    private void updateFixedTint() {
        if (getCompoundDrawables() != null) {
            Drawable []drawables = getCompoundDrawables();
            if(drawables[0] != null) {
                DrawableCompat.setTintList(drawables[0], mTintList);
            }

            if(drawables[1] != null) {
                DrawableCompat.setTintList(drawables[1], mTintList);
            }

            if(drawables[2] != null) {
                DrawableCompat.setTintList(drawables[2], mTintList);
            }

            if(drawables[3] != null) {
                DrawableCompat.setTintList(drawables[3], mTintList);
            }
        }
    }
}
