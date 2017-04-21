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
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.ClearEditTextIconHandler;
import com.philips.platform.uid.utils.EditTextIconHandler;
import com.philips.platform.uid.utils.PasswordEditTextIconHandler;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

public class EditText extends AppCompatEditText {
    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;

    private ColorStateList strokeColorStateList;
    private ColorStateList fillColorStateList;

    private boolean passwordVisible = false;

    private boolean isClearIconSupported = false;
    private EditTextIconHandler editTextIconHandler;

    public EditText(@NonNull final Context context) {
        this(context, null);
    }

    public EditText(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        this(context, attrs, R.attr.uidEditTextStyle);
    }

    public EditText(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDTextEditBox, defStyleAttr, R.style.UIDEditTextBox);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        Rect paddingRect = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        Drawable backgroundDrawable = getLayeredBackgroundDrawable(typedArray, theme);
        if (backgroundDrawable != null) {
            setBackground(backgroundDrawable);
        }

        isClearIconSupported = typedArray.getBoolean(R.styleable.UIDTextEditBox_uidInputTextWithClearButton, false);
        setHintTextColors(typedArray, theme);
        setTextColors(typedArray, theme);
        restorePadding(paddingRect);

        typedArray.recycle();
        initIconHandler();
    }

    private Drawable getLayeredBackgroundDrawable(@NonNull TypedArray typedArray, @NonNull final Resources.Theme theme) {
        Drawable fillDrawable = getFillBackgroundDrawable(typedArray, theme);
        Drawable borderDrawable = getBorderBackground(typedArray, theme);
        Drawable backgroundDrawable = fillDrawable;
        if (fillDrawable != null && borderDrawable != null) {
            backgroundDrawable = new LayerDrawable(new Drawable[]{fillDrawable, borderDrawable});
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_FILL_INDEX, R.id.uid_texteditbox_fill_drawable);
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_STROKE_INDEX, R.id.uid_texteditbox_stroke_drawable);
        }
        return backgroundDrawable;
    }

    private void setHintTextColors(final TypedArray typedArray, final Resources.Theme theme) {
        int hintSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextHintTextSelector, -1);
        if (hintSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(getContext().getResources(), theme, hintSelectorID);
            setHintTextColor(selector);
        }
    }

    private void setTextColors(final TypedArray typedArray, final Resources.Theme theme) {
        int textSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextTextSelector, -1);
        if (textSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(getContext().getResources(), theme, textSelectorID);
            setTextColor(selector);
        }
    }

    private void restorePadding(final Rect viewPaddings) {
        setPadding(viewPaddings.left, viewPaddings.top, viewPaddings.right, viewPaddings.bottom);
    }

    private Drawable getBorderBackground(final @NonNull TypedArray typedArray,
                                         final Resources.Theme theme) {
        int borderDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackground, -1);
        Drawable strokeDrawable = null;
        if (borderDrawableID != -1) {
            strokeDrawable = AppCompatResources.getDrawable(getContext(), borderDrawableID);
            int borderColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackgroundColorList, -1);
            int borderWidth = (int) typedArray.getDimension(R.styleable.UIDTextEditBox_uidInputTextBorderWidth, 0f);
            if (borderColorStateListID != -1) {
                strokeColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, borderColorStateListID);
                strokeDrawable = StrokeCompat.setStroke(strokeDrawable, borderWidth, strokeColorStateList);
            }
        }
        return strokeDrawable;
    }

    private Drawable getFillBackgroundDrawable(final @NonNull TypedArray typedArray,
                                               final Resources.Theme theme) {
        int fillDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackground, -1);
        Drawable fillDrawable = null;
        if (fillDrawableID != -1) {
            fillDrawable = DrawableCompat.wrap(AppCompatResources.getDrawable(getContext(), fillDrawableID));
            int fillColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackgroundColorList, -1);
            if (fillColorStateListID != -1) {
                fillColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, fillColorStateListID);
                DrawableCompat.setTintList(fillDrawable, fillColorStateList);
            }
        }
        return fillDrawable;
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
     * Method to check is the password is visisble
     * @return true if the password is visible
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

    //This is extracted as a method to avoid confusion why null check. This field will only be initialized in case icon is needs on right
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