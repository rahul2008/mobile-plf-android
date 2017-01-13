package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
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
        VectorDrawableCompat checkedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_enabled, theme);
        VectorDrawableCompat checkedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_checked_disabled, theme);
        VectorDrawableCompat uncheckedDisabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_disabled, theme);
        VectorDrawableCompat uncheckedEnabled = VectorDrawableCompat.create(getResources(), R.drawable.uid_checkbox_unchecked_enabled, theme);

        setButtonDrawable(getStateListDrawable(checkedEnabled, checkedDisabled, uncheckedDisabled, uncheckedEnabled));
        setBackground(null);
    }

    @NonNull
    private StateListDrawable getStateListDrawable(final VectorDrawableCompat checkedEnabled,
                                                   final VectorDrawableCompat checkedDisabled,
                                                   final VectorDrawableCompat uncheckedDisabled,
                                                   final VectorDrawableCompat uncheckedEnabled
                                                   ) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}, checkedEnabled);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}, checkedDisabled);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked}, uncheckedDisabled);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked}, uncheckedEnabled);
        return stateListDrawable;
    }

    @NonNull
    private LayerDrawable getCombinedDrawable(final VectorDrawableCompat tick, final VectorDrawableCompat background) {
        Drawable[] array =  new Drawable[2];
        array[0] = background;
        array[1] = tick;
        return new LayerDrawable(array);
    }
}
