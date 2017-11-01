package com.philips.cdp2.ews.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

/**
 * Created by andrewbortnichuk on 01/11/2017.
 */

public class ColorsUtil {
    public static int getAttributeColor(Context context, int attribute) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attribute, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
}
