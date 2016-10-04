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

    public static void injectCalligraphyFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/centralesansbook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        Resources.Theme theme = themeConfiguration.context.getTheme();
        themeConfiguration.colorRange.injectColorRange(theme);
        themeConfiguration.tonalRange.injectTonalRange(theme);
        themeConfiguration.controlType.injectPrimaryControlColors(theme);
    }
}
