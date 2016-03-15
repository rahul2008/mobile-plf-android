package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 * <p/>
 * <br>
 * Helper ImageView class which provides differnt color for pressed state and normal state <br>
 * Use themeStyle (none, theme, inverted) attribute in xml to apply the proper color on
 * pressed state.
 *
 * @attr ref com.philips.cdp.uikit.R.attr.TintableImageView
 */
public class TintableImageView extends ImageView {

    /**
     * Style NONE has same behavior as base Image View
     */

    /**
     *  Style THEME Default color is white, pressed color will be base color
     */
     /**
     * Style Inverted Default color is base color, pressed color will be white color
     */

        public enum STYLE {
        NONE(0), THEME(1), INVERTED(2);
        private int value;

        STYLE(int value) {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    ;


    Context context;
    int baseColor;
    private ColorStateList tint;
    private int colorStyle;

    public TintableImageView(Context context) {
        super(context);
        this.context = context;
        initBaseColor();
    }

    //this is the constructor that causes the exception
    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initBaseColor();
        //Set the color style
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TintableImageView);
        colorStyle = typedArray.getInt(R.styleable.TintableImageView_uikit_themeStyle, 0);
        typedArray.recycle();
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        //Same as bsae ImageView
        if (colorStyle == STYLE.NONE.getValue()) return;

        int[] state = getDrawableState();
        boolean isPressed = false;
        //Check if it's pressed
        for (int i : state) {
            if (i == android.R.attr.state_pressed) {
                isPressed = true;
                break;
            }
        }
        if (colorStyle == STYLE.THEME.getValue()) {
            if (isPressed) {
                setColorFilter(baseColor);
            } else {
                clearColorFilter();
            }
        } else if (colorStyle == STYLE.INVERTED.getValue()) {
            if (isPressed) {
                clearColorFilter();
            } else {
                setColorFilter(baseColor);
            }
        }
    }

    private void initBaseColor() {
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        baseColor = array.getColor(R.styleable.PhilipsUIKit_uikit_baseColor, 0);
        array.recycle();
    }

    /**
     * Describes how the tint should be applied to the image.
     *
     * @param themeStyle One of { STYLE}
     */
    public void setStyleTheme(int themeStyle) {
        if (themeStyle > STYLE.INVERTED.getValue() || themeStyle < STYLE.NONE.getValue()) {
            throw new RuntimeException("Wrong theme applied");
        }
        colorStyle = themeStyle;
        invalidate();
    }
}