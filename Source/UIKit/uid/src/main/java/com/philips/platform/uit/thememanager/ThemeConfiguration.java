/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

public class ThemeConfiguration {
    final ContentColor contentColor;
    final ColorRange colorRange;
    PrimaryControlType controlType;
    final NavigationColor navigationColor;
    Context context;

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
}