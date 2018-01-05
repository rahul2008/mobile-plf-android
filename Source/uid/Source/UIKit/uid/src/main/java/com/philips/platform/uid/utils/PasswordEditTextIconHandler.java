/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.thememanager.UIDHelper;
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
            showPasswordDrawable = new FontIconDrawable(editText.getContext(), editText.getContext().getString(R.string.dls_password_show), Typeface.createFromAsset(editText.getContext().getAssets(), "fonts/iconfont.ttf"))
            .color(UIDHelper.getColorFromAttribute(editText.getContext().getTheme(), R.attr.uidTextBoxDefaultNormalShowHideIconColor, Color.WHITE))
            .sizeDp(24);
        }
        return showPasswordDrawable;
    }

    private Drawable getHidePasswordDrawable() {
        if (hidePasswordDrawable == null) {
            hidePasswordDrawable = new FontIconDrawable(editText.getContext(), editText.getContext().getString(R.string.dls_password_hide), Typeface.createFromAsset(editText.getContext().getAssets(), "fonts/iconfont.ttf"))
                    .color(UIDHelper.getColorFromAttribute(editText.getContext().getTheme(), R.attr.uidTextBoxDefaultNormalShowHideIconColor, Color.WHITE))
                    .sizeDp(24);
        }
        return hidePasswordDrawable;
    }
}
