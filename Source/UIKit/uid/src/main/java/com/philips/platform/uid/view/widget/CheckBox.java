package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class CheckBox extends android.widget.CheckBox {
    public CheckBox(final Context context) {
        this(context, null);
    }

    public CheckBox(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.checkboxStyle);
    }

    public CheckBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyStyling(context, attrs);
    }

    private void applyStyling(Context context, AttributeSet attrs) {
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        VectorDrawableCompat checked = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked, theme);
        VectorDrawableCompat unchecked = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked, theme);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checked);
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, unchecked);

        setButtonDrawable(stateListDrawable);
    }
}
