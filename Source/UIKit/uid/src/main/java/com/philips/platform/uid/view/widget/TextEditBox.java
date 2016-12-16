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
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.compat.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class TextEditBox extends AppCompatEditText {
    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;
    public static final int ADDITIONAL_TOUCH_AREA = 40;
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

        setPasswordType(theme, attrs, defStyleAttr);
        typedArray.recycle();
    }

    private void setPasswordType(final Resources.Theme theme, final AttributeSet attrs, final int defStyleAttr) {

        final int inputType = getInputType();
        if (inputType == 129) {
            final Drawable showPasswordDrawable = VectorDrawableCompat.create(getResources(), R.drawable.uid_password_show_icon, theme);
            setCompoundDrawablesWithIntrinsicBounds(null, null, showPasswordDrawable, null);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(final View view, final MotionEvent event) {
                    final Drawable[] compoundDrawables = getCompoundDrawables();

                    final Drawable drawable = compoundDrawables[2];
                    if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
                        boolean touchedDrawable = isShowPasswordIconTouched(view, event, drawable);
                        if (touchedDrawable) {
                            if (getTransformationMethod() == null) {
                                setTransformationMethod(PasswordTransformationMethod.getInstance());
                            } else {
                                setTransformationMethod(null);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private boolean isShowPasswordIconTouched(final View view, final MotionEvent event, final Drawable drawable) {
        final float rawX = event.getRawX();
        final float rawY = event.getRawY();
        final Rect bounds = drawable.getBounds();
        final int width = view.getWidth();
        final int height = view.getHeight();
        final int left = view.getLeft();
        final int right = view.getRight();
        return (rawX > (left + width - (drawable.getIntrinsicWidth() + ADDITIONAL_TOUCH_AREA)) &&
                (rawX < (left + width - ADDITIONAL_TOUCH_AREA)) &&
                (rawY > (right - height + (drawable.getIntrinsicHeight() + ADDITIONAL_TOUCH_AREA))) &&
                (rawY < (right + height - ADDITIONAL_TOUCH_AREA)));
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