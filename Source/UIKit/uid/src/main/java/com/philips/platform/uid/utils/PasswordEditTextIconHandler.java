package com.philips.platform.uid.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

public class PasswordEditTextIconHandler implements EditTextIconHandler {
    private EditText editText;
    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;

    public PasswordEditTextIconHandler(final EditText editText) {
        this.editText = editText;
    }

    @Override
    public void show() {
        final Drawable[] compoundDrawables = editText.getCompoundDrawables();
        setPasswordDrawables(compoundDrawables);
        editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[3]);
    }

    @Override
    public void handleTouch(final Drawable drawable, final MotionEvent event) {
        final Drawable[] compoundDrawables = editText.getCompoundDrawables();
        setPasswordDrawables(compoundDrawables);
        editText.setTransformationMethod(getToggledTransformationMethod());
    }

    @Nullable
    private TransformationMethod getToggledTransformationMethod() {
        return editText.isPasswordVisible() ? PasswordTransformationMethod.getInstance() : null;
    }

    private void setPasswordDrawables(final Drawable[] compoundDrawables) {
        compoundDrawables[RIGHT_DRAWABLE_INDEX] = editText.isPasswordVisible() ? getHidePasswordDrawable(editText.getContext().getTheme()) :
                getShowPasswordDrawable(editText.getContext().getTheme());
    }

    private Drawable getShowPasswordDrawable(final Resources.Theme theme) {
        if (showPasswordDrawable == null) {
            showPasswordDrawable = getPasswordDrawable(theme, R.drawable.uid_texteditbox_show_password_icon);
        }
        return showPasswordDrawable;
    }

    private VectorDrawableCompat getPasswordDrawable(final Resources.Theme theme, final int drawableResourceId) {
        return VectorDrawableCompat.create(editText.getResources(), drawableResourceId, theme);
    }

    private Drawable getHidePasswordDrawable(final Resources.Theme theme) {
        if (hidePasswordDrawable == null) {
            hidePasswordDrawable = getPasswordDrawable(theme, R.drawable.uid_texteditbox_hide_password_icon);
        }
        return hidePasswordDrawable;
    }
}
