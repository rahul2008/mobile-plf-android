package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

public class ClearEditTextIconHandler implements EditTextIconHandler {

    private EditText editText;
    private Drawable drawable;

    public ClearEditTextIconHandler(final EditText editText) {
        this.editText = editText;
    }

    @Override
    public void show() {
        final Drawable[] compoundDrawables = editText.getCompoundDrawables();
        compoundDrawables[RIGHT_DRAWABLE_INDEX] = getClearDrawable();
        editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[3]);
    }

    @Override
    public void handleTouch(final Drawable drawable, final MotionEvent event) {
        editText.setText("");
        editText.setHint(editText.getHint());
    }

    private Drawable getClearDrawable() {
        if (drawable == null) {
            drawable = VectorDrawableCompat.create(editText.getResources(), R.drawable.uid_texteditbox_clear_icon, editText.getContext().getTheme());
        }
        return drawable;
    }
}
