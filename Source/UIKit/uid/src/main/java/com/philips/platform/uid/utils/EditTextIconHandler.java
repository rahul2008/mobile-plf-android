package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.MotionEvent;

import com.philips.platform.uid.view.widget.EditText;

public abstract class EditTextIconHandler {

    public static final int LEFT_DRAWABLE_INDEX = 0;
    public static final int TOP_DRAWABLE_INDEX = 1;
    public static int RIGHT_DRAWABLE_INDEX = 2;
    public static final int BOTTOM_DRAWABLE_INDEX = 3;
    public boolean isIconDisplayed;
    protected EditText editText;

    protected EditTextIconHandler(@NonNull final EditText editText) {
        this.editText = editText;
//        if (editText.getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
//            RIGHT_DRAWABLE_INDEX = 0;
//        }
    }

    public void show() {
        if (!isIconDisplayed) {
            isIconDisplayed = true;
            final Drawable[] compoundDrawables = editText.getCompoundDrawables();
            compoundDrawables[RIGHT_DRAWABLE_INDEX] = getIconDrawable();
            editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[LEFT_DRAWABLE_INDEX], compoundDrawables[TOP_DRAWABLE_INDEX], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[BOTTOM_DRAWABLE_INDEX]);
        }
    }

    @NonNull
    protected VectorDrawableCompat getDrawable(final int drawableResourceId) {
        return VectorDrawableCompat.create(editText.getResources(), drawableResourceId, editText.getContext().getTheme());
    }

    public abstract void processIconTouch(@NonNull final Drawable drawable, @NonNull final MotionEvent event);

    public void setIconDisplayed(boolean iconDisplayed) {
        this.isIconDisplayed = iconDisplayed;
    }

    @NonNull
    public abstract Drawable getIconDrawable();
}
