package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 1/20/2016.
 */
public class UIKITCheckBox extends AppCompatCheckBox {
    Context context;
    public UIKITCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.LightColor});
        int   baseColor = ar.getInt(0, R.attr.baseColor);
        int lightColor=ar.getInt(1, R.attr.LightColor);
        int white=getResources().getColor(R.color.uikit_white);
        ar.recycle();
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {


                        lightColor,
                         white//enabled

                }
        );


setBackgroundColor(white);
        setTextColor(lightColor);

    //setSupportButtonTintMode(PorterDuff.Mode.SRC_ATOP,colorStateList);

    }
}
