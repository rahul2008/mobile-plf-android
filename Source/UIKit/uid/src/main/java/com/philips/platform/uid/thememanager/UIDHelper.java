/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.philips.platform.uid.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UIDHelper {

    public static final String CONTENT_TONAL_RANGE = "CONTENT_TONAL_RANGE";
    public static final String COLOR_RANGE = "COLOR_RANGE";
    public static final String NAVIGATION_RANGE = "NAVIGATION_RANGE";

    public static void injectCalligraphyFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/centralesansbook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        Resources.Theme theme = themeConfiguration.context.getTheme();
        themeConfiguration.colorRange.injectColorRange(theme);
        themeConfiguration.contentColor.injectTonalRange(theme);
        if (themeConfiguration.controlType != null) {
            themeConfiguration.controlType.injectPrimaryControlColors(theme);
        }
        themeConfiguration.navigationColor.injectNavigationColor(theme);
    }

    /**
     * This API will setup toolbar with title navigation icon
     * Be-aware that this API will override the title from toolbar to make title center aligned
     *
     * @param activity       Reference of activity you want to setup toolbar
     * @param navigationIcon Icon resource to be displayed as navigation(hamburger or back button)
     * @param toolbarId      Resource id for toolbar from your activity layout
     */
    public static void setupToolbar(@NonNull final AppCompatActivity activity, @DrawableRes final int navigationIcon, @IdRes final int toolbarId) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        if (toolbar == null) {
            final String formattedException = String.format("Please include a layout with view android.support.v7.widget.toolbar containing id %id in  you layout", toolbar);
            throw new RuntimeException(formattedException);
        }
        toolbar.setNavigationIcon(navigationIcon);
        toolbar.setTitleMarginStart(activity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        toolbar.setTitleMarginEnd(activity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        toolbar.setTitle(null);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
