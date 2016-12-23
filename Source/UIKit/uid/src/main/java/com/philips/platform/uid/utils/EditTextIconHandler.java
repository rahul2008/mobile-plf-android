package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.philips.platform.uid.view.widget.EditText;

public abstract class EditTextIconHandler {

    public static final int LEFT_DRAWABLE_INDEX = 0;
    public static final int TOP_DRAWABLE_INDEX = 1;
    public static final int RIGHT_DRAWABLE_INDEX = 2;
    public static final int BOTTOM_DRAWABLE_INDEX = 3;
    public boolean shown;
    private EditText editText;

    protected EditTextIconHandler(final EditText editText) {
        this.editText = editText;
    }

    public void show() {
        if (!shown) {
            shown = true;
            final Drawable[] compoundDrawables = editText.getCompoundDrawables();
            compoundDrawables[RIGHT_DRAWABLE_INDEX] = getIconDrawable();
            editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[LEFT_DRAWABLE_INDEX], compoundDrawables[TOP_DRAWABLE_INDEX], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[BOTTOM_DRAWABLE_INDEX]);
        }
    }

    public abstract void handleTouch(final Drawable drawable, final MotionEvent event);

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public abstract Drawable getIconDrawable();
}
