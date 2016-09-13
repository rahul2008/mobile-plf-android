/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

public class ThemeUtils {
    public static void setPrimaryControlColors(Resources.Theme theme) {
        theme.applyStyle(R.style.UITPrimaryControl, false);
    }

    public static void setPrimaryAlternateControlColors(Resources.Theme theme) {
        theme.applyStyle(R.style.UITPrimaryControl_AlternatePrimary, false);
    }
    public static void setSecondaryControlColors(Resources.Theme theme) {
        theme.applyStyle(R.style.UITPrimaryControl_Secondary, false);
    }
}
