/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

public class PasswordEditTextIconHandler extends EditTextIconHandler {
    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;

    public PasswordEditTextIconHandler(@NonNull final EditText editText) {
        super(editText);
        editText.setTextIsSelectable(false);
    }

    @Override
    public void processIconTouch() {
        setIconDisplayed(false);
        final int selectionStart = editText.getSelectionStart();
        final int selectionend = editText.getSelectionEnd();
        editText.setTransformationMethod(editText.isPasswordVisible() ? PasswordTransformationMethod.getInstance() : null);
        show();
        Selection.setSelection(editText.getText(), selectionStart, selectionend);
    }

    @Override
    public Drawable getIconDrawable() {
        return editText.isPasswordVisible() ? getHidePasswordDrawable() :
                getShowPasswordDrawable();
    }

    private Drawable getShowPasswordDrawable() {
        if (showPasswordDrawable == null) {
            showPasswordDrawable = getDrawable(R.drawable.uid_texteditbox_show_password_icon);
        }
        return showPasswordDrawable;
    }

    private Drawable getHidePasswordDrawable() {
        if (hidePasswordDrawable == null) {
            hidePasswordDrawable = getDrawable(R.drawable.uid_texteditbox_hide_password_icon);
        }
        return hidePasswordDrawable;
    }
}
