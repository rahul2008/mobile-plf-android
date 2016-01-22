package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 1/13/2016.
 */
public class UIKitRadioButton extends AppCompatRadioButton {
    Context context;

    public UIKitRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor, R.attr.LightColor});
        int baseColor = ar.getInt(0, R.attr.baseColor);
        int overlayColor = Color.argb(127, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
        ar.recycle();
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_checked},
                         //disabled
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.checked}//enabled
                },
                new int[]{

                        overlayColor //disabled
                        ,
                        overlayColor //disabled
                        ,baseColor,
                         baseColor//enabled

                }
        );


        setSupportButtonTintList(colorStateList);

    }
}
