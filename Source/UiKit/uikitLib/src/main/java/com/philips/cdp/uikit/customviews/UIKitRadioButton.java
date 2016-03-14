package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.philips.cdp.uikit.R;

public class UIKitRadioButton extends AppCompatRadioButton {
    Context context;
    private boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    private ColorStateList mTintList;

    public UIKitRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTintList();

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.uikit_custom_radio_button);
        setButtonDrawable(wrap(drawable));
    }

    private void initTintList() {
        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor, R.attr.uikit_LightColor});
        int baseColor = ar.getInt(0, R.attr.uikit_baseColor);

        int overlayColor = Color.argb(127, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
        ar.recycle();
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}}, new int[]{baseColor, overlayColor});
        mTintList = colorStateList;

    }



    private Drawable wrap(Drawable d) {
        if (d == null) return null;

        Drawable wrappedDrawable = DrawableCompat.wrap(d).mutate();
        wrappedDrawable.setBounds(d.getBounds());
        if (wrappedDrawable instanceof DrawableWrapper) {
          //  ((DrawableWrapper) wrappedDrawable).setTintList(mTintList);
          ((DrawableWrapper) wrappedDrawable).setCompatTintList(mTintList);
            ((DrawableWrapper) wrappedDrawable).setCompatTintMode(PorterDuff.Mode.SRC_ATOP);
                   // ((DrawableWrapper) wrappedDrawable).setTintMode(PorterDuff.Mode.SRC_ATOP);
        } else {
            wrappedDrawable.setTintList(mTintList);
           // setSupportButtonTintList(mTintList);
        }
        return wrappedDrawable;
    }
}
