/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.Editable;
import android.view.MotionEvent;
import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

import static android.view.View.LAYOUT_DIRECTION_RTL;

public abstract class EditTextIconHandler {

    private static final int LEFT_DRAWABLE_INDEX = 0;
    private static final int TOP_DRAWABLE_INDEX = 1;
    private static final int RIGHT_DRAWABLE_INDEX = 2;
    private static final int BOTTOM_DRAWABLE_INDEX = 3;
    private final int passwordDrawableTouchArea;
    protected EditText editText;
    private boolean isIconActionUpDetected;
    private boolean isIconActionDownDetected;
    private boolean isIconDisplayed;
    private Drawable icon;

    protected EditTextIconHandler(@NonNull final EditText editText) {
        this.editText = editText;
        passwordDrawableTouchArea = editText.getContext().getResources().getDimensionPixelSize(R.dimen.uid_edittext_password_drawable_touch_area);
    }

    public abstract void processIconTouch();

    public abstract Drawable getIconDrawable();

    public boolean isTouchProcessed(final MotionEvent event) {
        if (icon == null || !isIconTouched(event, icon)) {
            resetIconTouch();
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isIconActionDownDetected = true;
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isIconActionUpDetected = true;
        }
        return processTouch();
    }

    private boolean processTouch() {
        if (isIconActionDownDetected && isIconActionUpDetected) {
            final Editable editableText = editText.getEditableText();
            if (editableText != null && editableText.length() > 0) {
                resetIconTouch();
                processIconTouch();
                return true;
            }
        }
        return false;
    }

    private void resetIconTouch() {
        isIconActionDownDetected = false;
        isIconActionUpDetected = false;
    }

    private boolean isIconTouched(@NonNull final MotionEvent event, @NonNull final Drawable drawable) {
        if (isRightToLeft()) {
            return (event.getRawX() <= (editText.getRight() - editText.getWidth() + passwordDrawableTouchArea + editText.getCompoundDrawablePadding()));
        }
        return (event.getRawX() >= (editText.getRight() + editText.getPaddingRight() + drawable.getBounds().width() - (passwordDrawableTouchArea + editText.getCompoundDrawablePadding())));
    }

    public void show() {
        if (!isIconDisplayed) {
            isIconDisplayed = true;
            final Drawable[] compoundDrawables = editText.getCompoundDrawables();
            icon = getIconDrawable();
            compoundDrawables[getDrawableIndexBasedOnLayoutDirection()] =icon;
            editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[LEFT_DRAWABLE_INDEX], compoundDrawables[TOP_DRAWABLE_INDEX], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[BOTTOM_DRAWABLE_INDEX]);
        }
    }

    private int getDrawableIndexBasedOnLayoutDirection() {
        return isRightToLeft() ? LEFT_DRAWABLE_INDEX : RIGHT_DRAWABLE_INDEX;
    }

    private boolean isRightToLeft() {
        return editText.getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    public void setIconDisplayed(boolean iconDisplayed) {
        this.isIconDisplayed = iconDisplayed;
    }

    protected Drawable getDrawable(@DrawableRes final int drawableResourceId) {
        return VectorDrawableCompat.create(editText.getResources(), drawableResourceId, editText.getContext().getTheme());
    }
}
