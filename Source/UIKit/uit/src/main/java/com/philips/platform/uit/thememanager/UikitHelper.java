/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.support.annotation.NonNull;

public class UikitHelper {

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        themeConfiguration.colorRange.injectColorRange(themeConfiguration.context.getTheme());

        themeConfiguration.tonalRange.injectTonalRange(themeConfiguration.context.getTheme());
    }
}
