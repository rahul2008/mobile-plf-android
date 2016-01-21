package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
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

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor, R.attr.LightColor});
        int   baseColor = ar.getInt(0, R.attr.brightColor);
        int lightColor=ar.getInt(1, R.attr.LightColor);
        // int red=getResources().getColor(R.color.uikit_enricher_red);
        ar.recycle();
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{


                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {


                        baseColor //enabled

                }
        );

        //     this.setButtonTintList(colorStateList);
        //   setBackgroundTintList(colorStateList);

        //setBackgroundTintList(colorStateList);
      setSupportButtonTintList(colorStateList);
        setButtonDrawable(R.drawable.uikit_radio_button);
        // setCompoundDrawableTintList(colorStateList);
        // this.getButtonDrawable().setColorFilter();

    }
}
