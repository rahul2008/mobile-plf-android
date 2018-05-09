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

    //Tonal ranges
    private static final String TR_UL = "UltraLight";
    private static final String TR_VL = "VeryLight";
    private static final String TR_B = "Bright";
    private static final String TR_VD = "VeryDark";

    private static final String NO_COLOR_RANGE = "";
    static final String ACCENT = "Accent%s.%s";
    private static final String THEME_ULTRA_LIGHT = "Theme.DLS.%s.UltraLight";
    private static final String NAVIGATION = "UIDNavigationbar%s";
    private static final String NAVIGATION_TOP = "Navigation%s%sTopTheme";

    /**
     * Build themed list as per the context.
     *
     * @param context context which will be used to provide theme
     * @param resId   resourceID of color resource
     * @return Colorstate list as per theme.
     * @since 3.0.0
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

    public static int getAccentResourceID(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String accentRange = themeArray.getString(R.styleable.PhilipsUID_uidAccentRange);
        String tonalRange = themeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        String accentThemeName = String.format(ACCENT, accentRange, tonalRange);
        themeArray.recycle();

        return context.getResources().getIdentifier(accentThemeName, "style", context.getPackageName());
    }

    public static void restoreAccentAndNavigation(@NonNull Context context) {
        TypedArray appThemeArray = context.getApplicationContext().getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String colorRange = appThemeArray.getString(R.styleable.PhilipsUID_uidColorRange);
        String tonalRange = appThemeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        String navigationRange = appThemeArray.getString(R.styleable.PhilipsUID_uidNavigationRange);
        String accentRange = appThemeArray.getString(R.styleable.PhilipsUID_uidAccentRange);

        String navigationTheme = String.format(NAVIGATION, navigationRange);
        String navigationTopColorTheme = String.format(NAVIGATION_TOP, colorRange, navigationRange);
        String accentTheme = String.format(ACCENT, accentRange, tonalRange);
        appThemeArray.recycle();

        String packageName = context.getPackageName();
        Resources.Theme activityTheme = context.getTheme();

        int statusBarID = isLightStatusBar(navigationRange) ? R.style.UIDStatusBarLight:R.style.UIDStatusBarDark;
        int navID = context.getResources().getIdentifier(navigationTheme, "style", packageName);
        int navTopID = context.getResources().getIdentifier(navigationTopColorTheme, "style", packageName);
        int accentID = context.getResources().getIdentifier(accentTheme, "style", packageName);

        activityTheme.applyStyle(statusBarID, true);
        activityTheme.applyStyle(navID, true);
        activityTheme.applyStyle(navTopID, true);
        activityTheme.applyStyle(accentID, true);

    }

    static int getNavigationTopResourceID(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String colorRange = themeArray.getString(R.styleable.PhilipsUID_uidColorRange);
        String navigationRange = themeArray.getString(R.styleable.PhilipsUID_uidNavigationRange);
        String navigationTopColorTheme = String.format(NAVIGATION_TOP, colorRange, navigationRange);
        themeArray.recycle();

        return context.getResources().getIdentifier(navigationTopColorTheme, "style", context.getPackageName());
    }

    public static int getThemeResourceID(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String colorRange = themeArray.getString(R.styleable.PhilipsUID_uidColorRange);
        String tonalRange = themeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        String themeName = String.format("Theme.DLS.%s.%s", colorRange, tonalRange);
        themeArray.recycle();
        return context.getResources().getIdentifier(themeName, "style", context.getPackageName());
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
        return colorRange == null ? NO_COLOR_RANGE : colorRange;
    }

    static String getTonalRangeName(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String tonalRange = themeArray.getString(R.styleable.PhilipsUID_uidTonalRange);
        themeArray.recycle();
        return tonalRange;
    }

    static Context getPopupThemedContext(Context context) {
        String tonalRange = getTonalRangeName(context);
        if (!(tonalRange.equals(TR_VL) || tonalRange.equals(TR_B))) {
            return context;
        }

        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(getPopupThemeResourceID(context), true);

        return UIDContextWrapper.getThemedContext(context, theme);
    }

    private static int getPopupThemeResourceID(Context context) {
        String colorRange = getColorRangeName(context);
        String themeName = String.format(THEME_ULTRA_LIGHT, colorRange);
        return context.getResources().getIdentifier(themeName, "style", context.getPackageName());
    }

    private static boolean isLightStatusBar(String tonalRange) {
        return TR_UL.equals(tonalRange) || TR_VL.equals(tonalRange);
    }

    private static int getNavigationFullThemeResourceID(@NonNull Context context) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.PhilipsUID);
        String colorRange = themeArray.getString(R.styleable.PhilipsUID_uidColorRange);
        String navigationRange = themeArray.getString(R.styleable.PhilipsUID_uidNavigationRange);
        String themeName = String.format("Theme.DLS.%s.%s", colorRange, navigationRange);
        themeArray.recycle();
        return context.getResources().getIdentifier(themeName, "style", context.getPackageName());
    }

    static Context getContentThemedContext(Context context) {
        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(ThemeUtils.getThemeResourceID(context), true);
        return UIDContextWrapper.getThemedContext(context, theme);
    }

    static Context getNavigationThemedContext(Context context) {
        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(getNavigationFullThemeResourceID(context), true);
        return UIDContextWrapper.getThemedContext(context, theme);
    }
}
