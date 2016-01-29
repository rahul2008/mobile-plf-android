package com.philips.cdp.uikit.utils;

import android.graphics.Color;
import android.view.Menu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UikitUtils {

    public static void menuShowIcon(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static int getResourceID(String resName, Class<?> resourceClass) {
        Field target = null;
        int resourceID = -1;
        try {
            target = resourceClass.getField(resName);
            try {
                resourceID = target.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceID;
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
