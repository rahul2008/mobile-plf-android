package com.philips.platform.uid.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

public class PasswordEditTextIconHandler extends EditTextIconHandler {
    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;

    public PasswordEditTextIconHandler(@NonNull final EditText editText) {
        super(editText);
        this.editText.setTextIsSelectable(false);
    }

    @Override
    public void processIconTouch(@NonNull final Drawable drawable, @NonNull final MotionEvent event) {
        setIconDisplayed(false);
        editText.setTransformationMethod(editText.isPasswordVisible() ? PasswordTransformationMethod.getInstance() : null);
        show();
    }

    @Override
    public Drawable getIconDrawable() {
        return editText.isPasswordVisible() ? getHidePasswordDrawable(editText.getContext().getTheme()) :
                getShowPasswordDrawable();
    }

    private Drawable getShowPasswordDrawable() {
        if (showPasswordDrawable == null) {
            showPasswordDrawable = getDrawable(R.drawable.uid_texteditbox_show_password_icon);
        }
        return showPasswordDrawable;
    }

    private Drawable getHidePasswordDrawable(@NonNull final Resources.Theme theme) {
        if (hidePasswordDrawable == null) {
            hidePasswordDrawable = getDrawable(R.drawable.uid_texteditbox_hide_password_icon);
        }
        return hidePasswordDrawable;
    }
}
