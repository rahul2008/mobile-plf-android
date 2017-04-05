/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

public class ThemeConfiguration {
    final ContentColor contentColor;
    final NavigationColor navigationColor;
    PrimaryControlType controlType;
    Context context;

    public ThemeConfiguration(@NonNull final ContentColor contentColor,
                              final NavigationColor navigationColor, @NonNull final Context context) {
        this.context = context;
        this.contentColor = contentColor;
        this.navigationColor = navigationColor;
    }

    public void setPrimaryControlType(PrimaryControlType controlType) {
        this.controlType = controlType;
    }
}