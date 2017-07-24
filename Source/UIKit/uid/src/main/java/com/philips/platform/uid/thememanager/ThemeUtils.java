/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDContextWrapper;


public final class ThemeUtils {

    //    private static final String TAG = "ThemeUtils";
    static final String ACCENT = "Accent";
    private static final String TR_VD = "VeryDark";
    private static final String TR_B = "Bright";

    /**
     * Build themed list as per the context.
     *
     * @param context context which will be used to provide theme
     * @param resId   resourceID of color resource
     * @return Colorstate list as per theme.
     */
    @Nullable
    public static ColorStateList buildColorStateList(Context context, @ColorRes int resId) {
        return AppCompatResources.getColorStateList(context, resId);
    }

    //Accent has higher priority over component type. It must be given preference while injecting themes.
    public static Resources.Theme getTheme(@NonNull final Context context, @NonNull final AttributeSet attributeSet) {
        final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.uidComponentType});

        final TypedArray accentArray = context.obtainStyledAttributes(attributeSet, R.styleable.UIDAccentType);
        boolean isAccent = accentArray.getBoolean(0, false);
        accentArray.recycle();

        int resourceId = 0;
        if (isAccent) {
            resourceId = R.style.BaseAccent;
        } else {
            resourceId = getResourceIdBasedComponentType(typedArray);
        }

        typedArray.recycle();

        if (resourceId == 0) {
            return context.getTheme();
        }
        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(resourceId, true);

        return theme;
    }

    static int getAccentResourceID(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String accentRange = themeArray.getString(R.styleable.PhilipsUID_uidAccentRange);
        String tonalRange = themeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        String accentThemeName = String.format("%s%s.%s", ACCENT, accentRange, tonalRange);
        themeArray.recycle();

        return context.getResources().getIdentifier(accentThemeName, "style", context.getPackageName());
    }

    private static int getResourceIdBasedComponentType(@NonNull final TypedArray typedArray) {
        int resourceId = typedArray.getInt(0, 0);
        if (resourceId == 1) {
            resourceId = R.style.UIDPrimaryControl_Secondary;
        }
        return resourceId;
    }


    static String getColorRangeName(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String colorRange = themeArray.getString(R.styleable.PhilipsUID_uidColorRange);
        themeArray.recycle();
        return colorRange;
    }

    static String getTonalRangeName(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String tonalRange = themeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        themeArray.recycle();
        return tonalRange;
    }

    static Context getPopupThemedContext(Context context) {
        String tonalRange = getTonalRangeName(context);
        if (!(tonalRange.equals(TR_VD) || tonalRange.equals(TR_B))) {
            return context;
        }

        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(getPopupThemeResourceID(context), true);

        return UIDContextWrapper.getThemedContext(context, theme);
    }

    private static int getPopupThemeResourceID(Context context) {
        String colorRange = getColorRangeName(context);
        String themeName = String.format("Popup%sTheme", colorRange);
        return context.getResources().getIdentifier(themeName, "style", context.getPackageName());
    }
}
