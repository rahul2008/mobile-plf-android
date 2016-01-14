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
public class UIKitRadioButton extends AppCompatRadioButton{
    Context context;
    public UIKitRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor, R.attr.LightColor});
      int   baseColor = ar.getInt(0, R.attr.brightColor);
        int lightColor=ar.getInt(1,R.attr.LightColor);
        int red=getResources().getColor(R.color.uikit_enricher_red);
        ar.recycle();
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{android.R.attr.state_pressed}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {

                        red //disabled
                        , baseColor //enabled

                }
        );

   //     this.setButtonTintList(colorStateList);
          //setBackgroundTintList(colorStateList)

      //  setBackgroundTintList(colorStateList);
   //     setSupportButtonTintList(colorStateList);
       // setCompoundDrawableTintList(colorStateList);
       // this.getButtonDrawable().setColorFilter();
    }
}
