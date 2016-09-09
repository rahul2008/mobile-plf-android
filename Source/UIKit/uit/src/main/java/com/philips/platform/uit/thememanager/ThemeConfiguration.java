package com.philips.platform.uit.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ThemeConfiguration {
    TonalRange tonalRange;
    ColorRange colorRange;
    Context context;

    public ThemeConfiguration(@NonNull final ColorRange colorRange, @NonNull final TonalRange tonalRange, @NonNull final Context context) {
        this.context = context;
        this.tonalRange = tonalRange;
        this.colorRange = colorRange;
    }
}
