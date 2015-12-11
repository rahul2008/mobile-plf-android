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
     * Same behavior as base Image View
     */
    public static int STYLE_NONE = 0;
    /**
     * Default color is white, pressed color will be base color
     */
    public static int STYLE_THEME = 1;
    /**
     * Default color is base color, pressed color will be white color
     */
    public static int STYLE_INVERTED = 2;
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
        colorStyle = typedArray.getInt(R.styleable.TintableImageView_themeStyle, 0);
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
        if (colorStyle == STYLE_NONE) return;

        int[] state = getDrawableState();
        boolean isPressed = false;
        //Check if it's pressed
        for (int i : state) {
            if (i == android.R.attr.state_pressed) {
                isPressed = true;
                break;
            }
        }
        if (colorStyle == STYLE_THEME) {
            if (isPressed) {
                setColorFilter(baseColor);
            } else {
                clearColorFilter();
            }
        } else if (colorStyle == STYLE_INVERTED) {
            if (isPressed) {
                clearColorFilter();
            } else {
                setColorFilter(baseColor);
            }
        }
    }

    private void initBaseColor() {
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        baseColor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
    }

    /**
     * Describes how the tint should be applied to the image.
     *
     * @param themeStyle One of {@link #STYLE_NONE}, {@link #STYLE_THEME}, {@link #STYLE_INVERTED}
     */
    public void setStyleTheme(int themeStyle) {
        if (themeStyle > STYLE_INVERTED || themeStyle < STYLE_NONE) {
            throw new RuntimeException("Wrong theme applied");
        }
        colorStyle = themeStyle;
        invalidate();
    }
}