/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;

public class CanvasCompat {

    @SuppressWarnings({"deprecation", "UnusedReturnValue"})
    public static boolean clipOutRect(Canvas canvas, Rect rect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return canvas.clipOutRect(rect);
        }
        return canvas.clipRect(rect, Region.Op.DIFFERENCE);
    }
}