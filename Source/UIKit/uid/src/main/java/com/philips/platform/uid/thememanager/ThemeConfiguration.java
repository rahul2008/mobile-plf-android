/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

public class ThemeConfiguration {
    final ContentColor contentColor;
    final ColorRange colorRange;
    final NavigationColor navigationColor;
    PrimaryControlType controlType;
    Context context;

    @StyleRes
    int colorPaletteID = -1;

    public ThemeConfiguration(@NonNull final ColorRange colorRange, @NonNull final ContentColor contentColor,
                              final NavigationColor navigationColor, @NonNull final Context context) {
        this.context = context;
        this.contentColor = contentColor;
        this.navigationColor = navigationColor;
        this.colorRange = colorRange;
    }

    public void setPrimaryControlType(PrimaryControlType controlType) {
        this.controlType = controlType;
    }

    /**
     * API to set the color palette programmatically.
     * No need to call this api, if the activity is launched with DLS theme.
     * If both DLS and non-DLS UIKit are used together in same activity, this must be set in configuration object.
     * @param colorPaletteID Color palette resourceID (example:R.style.Theme.DLS.Blue.UltraLight)
     */
    public void setDLSColorPalette(@StyleRes int colorPaletteID) {
        this.colorPaletteID = colorPaletteID;
    }
}