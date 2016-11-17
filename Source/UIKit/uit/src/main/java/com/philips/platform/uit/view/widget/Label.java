/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.philips.platform.uit.R;
import com.philips.platform.uit.utils.UIDUtils;

public class Label extends AppCompatTextView {
    public Label(final Context context) {
        this(context, null);
    }

    public Label(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public Label(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UIDLabel, defStyleAttr, R.style.UIDLabel);
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.UIDLabel);
        int textColor = getDefaultLabelColor(attrsArray, themeArray);
        float textAlpha = getDefaultLabelAlpha(attrsArray, themeArray);
        setTextColor(UIDUtils.modulateColorAlpha(textColor, textAlpha));
        setLabelTextSize(context, attrsArray);
        attrsArray.recycle();
        themeArray.recycle();
    }

    //Try to find first in theme array before looking in attributes array
    private int getDefaultLabelColor(final TypedArray attrsArray, final TypedArray themeArray) {
        int color = themeArray.getColor(R.styleable.UIDLabel_uidLabelTextColor, Color.WHITE);
        return attrsArray.getColor(R.styleable.UIDLabel_uidLabelTextColor, color);
    }

    //Try to find first in theme array before looking in attributes array
    private float getDefaultLabelAlpha(final TypedArray attrsArray, final TypedArray themeArray) {
        float alpha = themeArray.getFloat(R.styleable.UIDLabel_uidLabelTextAlpha, 1.0f);
        return attrsArray.getFloat(R.styleable.UIDLabel_uidLabelTextAlpha, alpha);
    }

    //set text size from attributes
    private void setLabelTextSize(Context context, final TypedArray attrsArray) {
        int  textSize = attrsArray.getDimensionPixelSize(R.styleable.UIDLabel_uidLabelTextSize, -1);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}