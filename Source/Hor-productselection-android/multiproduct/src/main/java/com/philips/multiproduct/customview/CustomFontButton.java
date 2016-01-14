package com.philips.multiproduct.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.philips.multiproduct.R;


public class CustomFontButton extends Button {
    public CustomFontButton(Context context) {
        super(context);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(this, context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(this, context, attrs);
    }

    private void applyAttributes(Button view, Context context,
                                 AttributeSet attrs) {

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.CustomFontTextView);
            final String typeface = a
                    .getString(R.styleable.CustomFontTextView_digitalcareFontAssetName);
            a.recycle();

            // set the font using class CustomFontLoader
            CustomFontLoader.getInstance().setTypeface(view, typeface);
        }
    }

    public void setTypeface(String typeface) {
        CustomFontLoader.getInstance().setTypeface(this, typeface);
    }
}
