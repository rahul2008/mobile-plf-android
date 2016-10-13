/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

public class ThemeConfiguration {
    ContentTonalRange contentTonalRange;
    ColorRange colorRange;
    PrimaryControlType controlType;
    final NavigationColor navigationColor;
    Context context;

    public ThemeConfiguration(@NonNull final ColorRange colorRange, @NonNull final ContentTonalRange contentTonalRange,
                              final NavigationColor navigationColor, @NonNull final Context context) {
        this.context = context;
        this.contentTonalRange = contentTonalRange;
        this.navigationColor = navigationColor;
        this.colorRange = colorRange;
    }

    public void setPrimaryControlType(PrimaryControlType controlType) {
        this.controlType = controlType;
    }
}