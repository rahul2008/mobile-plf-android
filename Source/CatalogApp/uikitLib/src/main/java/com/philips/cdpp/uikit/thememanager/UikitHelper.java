package com.philips.cdpp.uikit.thememanager;

import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UikitHelper {

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        themeConfiguration.colorRange.injectColorRange(themeConfiguration.context.getTheme());

        themeConfiguration.tonalRange.injectTonalRange(themeConfiguration.context.getTheme());
    }
}
