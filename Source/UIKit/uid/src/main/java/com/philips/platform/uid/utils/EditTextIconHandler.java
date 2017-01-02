package com.philips.platform.uid.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.Editable;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.EditText;

public abstract class EditTextIconHandler {

    static final int LEFT_DRAWABLE_INDEX = 0;
    static final int TOP_DRAWABLE_INDEX = 1;
    static int RIGHT_DRAWABLE_INDEX = 2;
    static final int BOTTOM_DRAWABLE_INDEX = 3;
    private boolean isIconActionUpDetected;
    private boolean isIconActionDownDetected;
    private boolean isIconDisplayed;
    protected EditText editText;

    protected EditTextIconHandler(@NonNull final EditText editText) {
        this.editText = editText;
//        if (editText.getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
//            RIGHT_DRAWABLE_INDEX = 0;
//        }
    }

    public boolean isTouchProcessed(final MotionEvent event) {
        final Drawable[] compoundDrawables = editText.getCompoundDrawables();
        final Drawable drawable = compoundDrawables[RIGHT_DRAWABLE_INDEX];
        if (drawable != null && editText.isEnabled() && isShowPasswordIconTouched(event, drawable)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isIconActionDownDetected = true;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isIconActionUpDetected = true;
            }
            if (isIconActionDownDetected && isIconActionUpDetected) {
                final Editable editableText = editText.getEditableText();
                if (editableText != null && editableText.length() > 0) {
                    resetIconTouch();
                    processIconTouch();
                    return true;
                }
            }
        } else {
            resetIconTouch();
        }
        return false;
    }

    private void resetIconTouch() {
        isIconActionDownDetected = false;
        isIconActionUpDetected = false;
    }

    private boolean isShowPasswordIconTouched(@NonNull final MotionEvent event, @NonNull final Drawable drawable) {
        final int passwordDrawableTouchArea = editText.getContext().getResources().getDimensionPixelSize(R.dimen.uid_texteditbox_password_drawable_touch_area);
        return (event.getRawX() >= (editText.getRight() + editText.getPaddingRight() + drawable.getBounds().width() - (passwordDrawableTouchArea + editText.getCompoundDrawablePadding())));
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
    protected Drawable getDrawable(final int drawableResourceId) {
        return VectorDrawableCompat.create(editText.getResources(), drawableResourceId, editText.getContext().getTheme());
    }

    public abstract void processIconTouch();

    public void setIconDisplayed(boolean iconDisplayed) {
        this.isIconDisplayed = iconDisplayed;
    }

    @NonNull
    public abstract Drawable getIconDrawable();
}
