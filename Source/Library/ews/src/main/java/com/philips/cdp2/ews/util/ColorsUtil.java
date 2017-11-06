/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;


public final class ColorsUtil {

    public static int getAttributeColor(@NonNull Context context, int attribute) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attribute, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
    
}
