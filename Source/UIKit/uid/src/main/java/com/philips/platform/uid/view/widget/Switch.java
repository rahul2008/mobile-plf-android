/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

public class Switch extends SwitchCompat {
    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidSwitchStyle);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDSwitch, defStyleAttr, R.style.UIDSwitchStyle);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        applyThumbTint(themedContext, typedArray);
        applyTrackTint(themedContext, typedArray);
        applyRippleTint(themedContext, typedArray);
        typedArray.recycle();
    }

    private void applyTrackTint(final Context themedContext, final TypedArray typedArray) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchTrackColorList, -1);
        if (textColorStateID != -1) {
            ColorStateList trackTintList = ThemeUtils.buildColorStateList(themedContext, textColorStateID);
            setTrackDrawable(DrawableCompat.wrap(getTrackDrawable()));
            setTrackTintList(trackTintList);
        }
    }

    private void applyThumbTint(final Context themedContext, final TypedArray typedArray) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchThumbColorList, -1);
        if (textColorStateID != -1) {
            setThumbDrawable(DrawableCompat.wrap(getThumbDrawable()));
            setThumbTintList(ThemeUtils.buildColorStateList(themedContext, textColorStateID));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyRippleTint(final Context themedContext, final TypedArray typedArray) {
        int borderColorStateID = typedArray.getResourceId(R.styleable.UIDSwitch_uidSwitchBorderColorList, -1);
        if (UIDUtils.isMinLollipop() && (borderColorStateID > -1) && (getBackground() instanceof RippleDrawable)) {
            ((RippleDrawable) getBackground()).setColor(ThemeUtils.buildColorStateList(themedContext, borderColorStateID));
            int radius = getResources().getDimensionPixelSize(R.dimen.uid_switch_border_ripple_radius);
            UIDUtils.setRippleMaxRadius(getBackground(), radius);
        }
    }
}