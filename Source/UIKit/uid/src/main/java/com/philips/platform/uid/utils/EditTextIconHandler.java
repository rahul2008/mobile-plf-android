package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public interface EditTextIconHandler {

    public static final int LEFT_DRAWABLE_INDEX = 0;
    public static final int TOP_DRAWABLE_INDEX = 1;
    public static final int RIGHT_DRAWABLE_INDEX = 2;
    public static final int BOTTOM_DRAWABLE_INDEX = 3;

    public void show();

    public void handleTouch(final Drawable drawable, final MotionEvent event);
}
