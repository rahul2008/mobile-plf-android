/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.*;

public class EditText extends AppCompatEditText {
    private boolean passwordVisible = false;

    private boolean isClearIconSupported = false;
    private EditTextIconHandler editTextIconHandler;

    public EditText(@NonNull final Context context) {
        this(context, null);
    }

    public EditText(@NonNull final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidEditTextStyle);
    }

    public EditText(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDTextEditBox, defStyleAttr, R.style.UIDEditTextBox);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        Rect paddingRect = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        Drawable backgroundDrawable = UIDInputTextUtils.getLayeredBackgroundDrawable(themedContext, typedArray);
        if (backgroundDrawable != null) {
            setBackground(backgroundDrawable);
        }

        isClearIconSupported = typedArray.getBoolean(R.styleable.UIDTextEditBox_uidInputTextWithClearButton, false);
        setHintTextColors(themedContext, typedArray);
        setTextColors(themedContext, typedArray);
        restorePadding(paddingRect);

        typedArray.recycle();
        initIconHandler();
    }

    private void setHintTextColors(final Context themedContext, final TypedArray typedArray) {
        int hintSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextHintTextSelector, -1);
        if (hintSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(themedContext, hintSelectorID);
            setHintTextColor(selector);
        }
    }

    private void setTextColors(final Context themedContext, final TypedArray typedArray) {
        int textSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextTextSelector, -1);
        if (textSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(themedContext, textSelectorID);
            setTextColor(selector);
        }
    }

    private void restorePadding(final Rect viewPaddings) {
        setPadding(viewPaddings.left, viewPaddings.top, viewPaddings.right, viewPaddings.bottom);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, isPasswordVisible());
    }

    @Override
    public void onRestoreInstanceState(@NonNull Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        passwordVisible = savedState.passwordVisible;
        handlePasswordInputVisibility();
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    private void handlePasswordInputVisibility() {
        if (isPasswordInputType()) {
            if (passwordVisible) {
                setTransformationMethod(null);
            } else {
                setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    /**
     * Method to check is the password is visible
     * @return true if the password is visible
     * @since 3.0.0
     */
    public boolean isPasswordVisible() {
        return getTransformationMethod() == null;
    }

    private void initIconHandler() {
        if (isClearIconSupported) {
            editTextIconHandler = new ClearEditTextIconHandler(this);
        } else if (isPasswordInputType()) {
            editTextIconHandler = new PasswordEditTextIconHandler(this);
        }
        updateActionIcon();
    }

    private void updateActionIcon() {
        if (hasIconClickHandler()) {
            if (hasFocus() && isEnabled() && getText() != null && getText().length() > 0) {
                editTextIconHandler.show();
            } else {
                editTextIconHandler.setIconDisplayed(false);
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }


    /**
     * This is extracted as a method to avoid confusion why null check. This field will only be initialized in case icon is needs on right
     * @since 3.0.0
     */
    private boolean hasIconClickHandler() {
        return editTextIconHandler != null;
    }

    protected boolean isPasswordInputType() {
        final int variation = getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        updateActionIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onTextChanged(@NonNull final CharSequence source, final int start, final int lengthBefore, final int lengthAfter) {
        super.onTextChanged(source, start, lengthBefore, lengthAfter);
        updateActionIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFocusChanged(final boolean focused, final int direction, final Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused && isPasswordInputType() && hasIconClickHandler()) {
            if (isPasswordVisible()) {
                editTextIconHandler.processIconTouch();
            }
        }
        updateActionIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        boolean shouldProcessTouch = false;
        if (hasIconClickHandler() && isEnabled()) { //editTextIconHandler will be null if there is no icon
            shouldProcessTouch = editTextIconHandler.isTouchProcessed(event);
            if (!hasFocus()) {
                requestFocus();
            }
        }

        return shouldProcessTouch || super.onTouchEvent(event);
    }

    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private final boolean passwordVisible;

        private SavedState(Parcelable superState, boolean passwordVisible) {
            super(superState);
            this.passwordVisible = passwordVisible;
        }

        private SavedState(Parcel in) {
            super(in);
            this.passwordVisible = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (this.passwordVisible ? 1 : 0));
        }
    }
}