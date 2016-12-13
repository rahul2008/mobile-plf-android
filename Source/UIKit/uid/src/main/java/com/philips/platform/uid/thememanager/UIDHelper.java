/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
     * This API will setup toolbar
     * Be-aware that this API will override the title from toolbar to make title center aligned, <br>Make sure to include uid_toolbar_layout in your activity.
     * <br> If you have used your own toobar then you can setup toolbar on your own using activity.setSupportActionBar(toolbar);
     *
     * @param activity Reference of activity you want to setup toolbar
     */
    public static void setupToolbar(@NonNull final AppCompatActivity activity) {
        final View toolbarView = activity.findViewById(com.philips.platform.uid.R.id.uid_toolbar);
        if (toolbarView instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) toolbarView;
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            return;
        }
        throw new RuntimeException("Please include a uid_toolbar_layout in your main activity");
    }
}
