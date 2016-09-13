/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

public class ThemeConfiguration {
    TonalRange tonalRange;
    ColorRange colorRange;
    PrimaryControlType controlType = PrimaryControlType.PRIMARY;
    Context context;

    public ThemeConfiguration(@NonNull final ColorRange colorRange, @NonNull final TonalRange tonalRange, @NonNull final Context context) {
        this.context = context;
        this.tonalRange = tonalRange;
        this.colorRange = colorRange;
    }

    public void setPrimaryControlType(PrimaryControlType controlType) {
        this.controlType = controlType;
    }
}