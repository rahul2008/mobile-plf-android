package com.philips.cdp.productselection.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cdp.productselection.R;

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(this, context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(this, context, attrs);
    }

    private void applyAttributes(TextView view, Context context,
                                 AttributeSet attrs) {

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.MultiProductFontTextView);
            final String typeface = a
                    .getString(R.styleable.MultiProductFontTextView_multiproductFontAssetName);
            a.recycle();

            CustomFontLoader.getInstance().setTypeface(view, typeface);
        }
    }

    public void setTypeface(String typeface) {
        CustomFontLoader.getInstance().setTypeface(this, typeface);
    }
}