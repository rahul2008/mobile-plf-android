/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;

/**
 * This is the interface which each dynamic configuration app needs to
 * <br>provide to be able to update the theme
 */
public interface ThemeConfig {
    void injectStyle(Resources.Theme theme);
}
