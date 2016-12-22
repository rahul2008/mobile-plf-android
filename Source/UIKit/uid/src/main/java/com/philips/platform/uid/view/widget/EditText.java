/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
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
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.philips.platform.uid.R;
import com.philips.platform.uid.compat.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class EditText extends AppCompatEditText {
    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;
    private static final int RIGHT_DRAWABLE_INDEX = 2;

    private ColorStateList strokeColorStateList;
    private ColorStateList fillColorStateList;

    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;
    private boolean passwordVisible;

    public EditText(final Context context) {
        this(context, null);
    }

    public EditText(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public EditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDTextEditBox, defStyleAttr, R.style.UIDTextEditBox);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        Rect paddingRect = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        Drawable backgroundDrawable = getLayeredBackgroundDrawable(typedArray, theme);
        if (backgroundDrawable != null) {
            setBackground(getLayeredBackgroundDrawable(typedArray, theme));
        }

        setHintTextColors(typedArray, theme);
        setTextColors(typedArray, theme);
        restorePadding(paddingRect);

        typedArray.recycle();
        showIcon();
    }

    private void setTextColors(final TypedArray typedArray, final Resources.Theme theme) {
        int textSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextTextSelector, -1);
        if (textSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(getContext().getResources(), theme, textSelectorID);
            setTextColor(selector);
        }
    }

    private void setHintTextColors(final TypedArray typedArray, final Resources.Theme theme) {
        int hintSelectorID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextHintTextSelector, -1);
        if (hintSelectorID != -1) {
            ColorStateList selector = ThemeUtils.buildColorStateList(getContext().getResources(), theme, hintSelectorID);
            setHintTextColor(selector);
        }
    }

    private void restorePadding(final Rect viewPaddings) {
        setPadding(viewPaddings.left, viewPaddings.top, viewPaddings.right, viewPaddings.bottom);
    }

    private Drawable getLayeredBackgroundDrawable(@NonNull TypedArray typedArray,
                                                  final Resources.Theme theme) {
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
        return new SavedState(superState, passwordVisible, String.valueOf(getText()));
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        passwordVisible = savedState.isPasswordVisible();
        handlePasswordInputVisibility();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        showIcon();
    }

    @Override
    protected void onTextChanged(final CharSequence source, final int start, final int lengthBefore, final int lengthAfter) {
        super.onTextChanged(source, start, lengthBefore, lengthAfter);

        showIcon();
    }

    private void showIcon() {
        final Drawable[] compoundDrawables = getCompoundDrawables();
        if (isEnabled() && getText() != null && getText().length() > 0) {
            if (isPasswordInputType()) {
                setPasswordDrawables(compoundDrawables);
            } else {
//                setClearDrawable(compoundDrawables);
            }
        } else {
            compoundDrawables[RIGHT_DRAWABLE_INDEX] = null;
        }

        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[RIGHT_DRAWABLE_INDEX], compoundDrawables[3]);
    }

    private void setPasswordDrawables(final Drawable[] compoundDrawables) {
        compoundDrawables[RIGHT_DRAWABLE_INDEX] = getShowHidePasswordDrawable(getContext().getTheme());
    }

    private Drawable getShowHidePasswordDrawable(final Resources.Theme theme) {
        return isPasswordVisible() ? getHidePasswordDrawable(theme) : getShowPasswordDrawable(theme);
    }

    private Drawable getShowPasswordDrawable(final Resources.Theme theme) {
        if (showPasswordDrawable == null) {
            showPasswordDrawable = getPasswordDrawable(theme, R.drawable.uid_texteditbox_show_password_icon);
        }
        return showPasswordDrawable;
    }

    private Drawable getHidePasswordDrawable(final Resources.Theme theme) {
        if (hidePasswordDrawable == null) {
            hidePasswordDrawable = getPasswordDrawable(theme, R.drawable.uid_texteditbox_hide_password_icon);
        }
        return hidePasswordDrawable;
    }

    private void setClearDrawable(final Drawable[] compoundDrawables) {
        compoundDrawables[RIGHT_DRAWABLE_INDEX] = VectorDrawableCompat.create(getResources(), R.drawable.uid_texteditbox_clear_icon, getContext().getTheme());
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return processOnTouch(event);
    }

    private boolean processOnTouch(final MotionEvent event) {
        final Drawable[] compoundDrawables = getCompoundDrawables();

        final boolean isRtl = (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
        final Drawable drawable = compoundDrawables[RIGHT_DRAWABLE_INDEX];
        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null && isEnabled()) {
            if (isShowPasswordIconTouched(event, drawable)) {
                if (isPasswordInputType()) {
                    setPasswordDrawables(compoundDrawables);
                    final int selectionStart = getSelectionStart();
                    final int selectionEnd = getSelectionEnd();
                    setTransformationMethod(getPasswordTransaformationMethod());
                    setSelection(selectionStart, selectionEnd);
                } else {
                    setText("");
                    setHint(getHint());
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //Code from TextView of android to check the input type is numberPassword or textPassword
    private boolean isPasswordInputType() {
        final int variation =
                getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private boolean isShowPasswordIconTouched(final MotionEvent event, final Drawable drawable) {
        final int passwordDrawableTouchArea = getContext().getResources().getDimensionPixelSize(R.dimen.uid_texteditbox_password_drawable_touch_area);
        return (event.getRawX() >= (getRight() + getPaddingRight() + drawable.getBounds().width() - (passwordDrawableTouchArea + getCompoundDrawablePadding())));
    }

    @Nullable
    private TransformationMethod getPasswordTransaformationMethod() {
        return isPasswordVisible() ? PasswordTransformationMethod.getInstance() : null;
    }

    private boolean isPasswordVisible() {
        return passwordVisible = (getTransformationMethod() == null);
    }

    private VectorDrawableCompat getPasswordDrawable(final Resources.Theme theme, final int drawableResourceId) {
        return VectorDrawableCompat.create(getResources(), drawableResourceId, theme);
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

    protected static class SavedState extends BaseSavedState {

        private final boolean mPasswordVisible;

        private SavedState(Parcelable superState, boolean showingIcon, final String text) {
            super(superState);
            this.mPasswordVisible = showingIcon;
        }

        private SavedState(Parcel in) {
            super(in);
            mPasswordVisible = in.readByte() != 0;
        }

        public boolean isPasswordVisible() {
            return mPasswordVisible;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (mPasswordVisible ? 1 : 0));
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}