package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public interface EditTextIconHandler {

    public static final int RIGHT_DRAWABLE_INDEX = 2;

    public void show();

    public void handleTouch(final Drawable drawable, final MotionEvent event);
}
