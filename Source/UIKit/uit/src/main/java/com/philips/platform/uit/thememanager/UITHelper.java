/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.philips.platform.uit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UITHelper {

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
        themeConfiguration.contentTonalRange.injectTonalRange(theme);
        if (themeConfiguration.controlType != null) {
            themeConfiguration.controlType.injectPrimaryControlColors(theme);
        }
    }
}
