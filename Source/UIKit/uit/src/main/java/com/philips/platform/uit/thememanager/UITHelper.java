/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.support.annotation.NonNull;

import com.philips.platform.uit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UITHelper {

    public void injectCalligraphyFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/centralesansbook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        themeConfiguration.colorRange.injectColorRange(themeConfiguration.context.getTheme());

        themeConfiguration.tonalRange.injectTonalRange(themeConfiguration.context.getTheme());
    }
}
