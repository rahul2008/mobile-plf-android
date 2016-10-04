/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;

import org.apache.commons.lang3.reflect.MethodUtils;

public class UITTestUtils {
    public static int getAttributeColor(Context context, int attribute) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{attribute});
        int color = Color.MAGENTA;
        if (typedArray != null) {
            color = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();

        }
        return color;
    }

    public static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }

    public static Drawable getDrawableWithReflection(Object object, String funcName) {
        Drawable drawable = null;
        try {
            drawable = (Drawable) MethodUtils.invokeMethod(object, funcName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return drawable;
    }


    public static ColorStateList getColorStateListWithReflection(Object object, String funcName) {
        ColorStateList colorList;
        try {
            colorList = (ColorStateList) MethodUtils.invokeMethod(object, funcName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return colorList;
    }

    /**
     * Avoid using until really necessary to call it.
     * We can't avoid waiting in few situations, where we need actual view must be drawn before we start asserting.
     * @param object
     * @param milliSecs
     */
    public static void waitFor(Object object, int milliSecs) {
        Thread thread = Thread.currentThread();
        synchronized (object) {
            try {
                object.wait(milliSecs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}