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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.philips.platform.uid.R;
import com.philips.platform.uid.compat.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class TextEditBox extends AppCompatEditText {
    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;

    private ColorStateList strokeColorStateList;
    private ColorStateList fillColorStateList;

    public TextEditBox(final Context context) {
        this(context, null);
    }

    public TextEditBox(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public TextEditBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
        processPasswordInputType(theme);
        typedArray.recycle();
    }

    private void processPasswordInputType(final Resources.Theme theme) {
        if (isPasswordInputType()) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, getPasswordDrawable(theme, R.drawable.uid_texteditbox_password_show_icon), null);
            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View view, final MotionEvent event) {
                    final Drawable[] compoundDrawables = getCompoundDrawables();

                    final Drawable drawable = compoundDrawables[2];
                    if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
                        boolean touchedDrawable = isShowPasswordIconTouched(view, event);
                        if (touchedDrawable) {
                            setCompoundDrawablesWithIntrinsicBounds(null, null, getShowHidePasswordDrawable(theme), null);
                            setTransformationMethod(getPasswordTransaformationMethod());
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Nullable
    private PasswordTransformationMethod getPasswordTransaformationMethod() {
        return isPasswordHidden() ? PasswordTransformationMethod.getInstance() : null;
    }

    private VectorDrawableCompat getShowHidePasswordDrawable(final Resources.Theme theme) {
        return isPasswordHidden() ?
                getPasswordDrawable(theme, R.drawable.uid_texteditbox_password_show_icon) :
                getPasswordDrawable(theme, R.drawable.uid_texteditbox_password_hide_icon);
    }

    private boolean isPasswordHidden() {
        return getTransformationMethod() == null;
    }

    private VectorDrawableCompat getPasswordDrawable(final Resources.Theme theme, final int drawableResourceId) {
        return VectorDrawableCompat.create(getResources(), drawableResourceId, theme);
    }

    //Code from TextView of android to check the input type is numberPassword or textPassword
    private boolean isPasswordInputType() {
        final int variation = getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        final boolean passwordInputType = (variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD));
        final boolean numberPasswordInputType = (variation == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD));
        return passwordInputType || numberPasswordInputType;
    }

    private boolean isShowPasswordIconTouched(final View view, final MotionEvent event) {
        final int passwordDrawableTouchArea = view.getContext().getResources().getDimensionPixelSize(R.dimen.uid_texteditbox_password_drawable_touch_area);

        return isTouchIsInBoundAroundXaxis(view, passwordDrawableTouchArea, event.getRawX()) && isTouchInBoundAroundYaxis(view, passwordDrawableTouchArea, event.getRawY());
    }

    private boolean isTouchInBoundAroundYaxis(final View view, final int passwordDrawableTouchArea, final float touchYcoordinate) {
        return (touchYcoordinate > (view.getTop() + view.getHeight() - passwordDrawableTouchArea));
    }

    private boolean isTouchIsInBoundAroundXaxis(final View view, final int passwordDrawableTouchArea, final float touchXcoordinate) {
        return (touchXcoordinate > (view.getLeft() + view.getWidth() - passwordDrawableTouchArea)) && (touchXcoordinate < view.getLeft() + view.getWidth());
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
}