package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.ColorFilterStateListDrawable;

public class UIKitRadioButton extends AppCompatRadioButton {
    Context context;
    private int selectedColor;
    private int enabledColor;
    private ColorFilter enabledFilter;
    private ColorFilter selectedFilter;

    public UIKitRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSelectionColors();
        initIconColorFilters();
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.uikit_custom_radio_button);
        setButtonDrawable(getButtonSelector(drawable));
    }


    private void initSelectionColors() {

        TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor, R.attr.uikit_LightColor});
        selectedColor = ar.getInt(0, R.attr.uikit_baseColor);

        enabledColor = Color.argb(127, Color.red(selectedColor), Color.green(selectedColor), Color.blue(selectedColor));
        ar.recycle();
    }

    private void initIconColorFilters() {
        enabledFilter = new PorterDuffColorFilter(enabledColor, PorterDuff.Mode.SRC_ATOP);
        selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
    }

    private Drawable getButtonSelector(Drawable drawable) {
        Drawable enabled = drawable.getConstantState().newDrawable().mutate();
        Drawable selected = drawable.getConstantState().newDrawable().mutate();

        ColorFilterStateListDrawable selector = new ColorFilterStateListDrawable();
        selector.addState(new int[]{android.R.attr.state_checked}, selected, selectedFilter);
        selector.addState(new int[]{}, enabled, enabledFilter);

        return selector;
    }
}
