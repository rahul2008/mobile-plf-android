/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.philips.platform.uit.R;
import com.philips.platform.uit.thememanager.ThemeUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Button extends AppCompatButton {

    private ColorStateList drawableColorlist;
    private int drawableWidth;
    private int drawableHeight;

    public Button(Context context) {
        this(context, null);
    }

    public Button(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(@NonNull Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITButton, defStyleAttr, R.style.UITDefaultButton);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        assignDrawableProperties(typedArray, theme, attrs);
        applyBackgroundTinting(typedArray, theme);
        applyTextColorTinting(typedArray, theme);
        applyDrawable(typedArray);
        typedArray.recycle();
    }

    private void applyTextColorTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int textColorStateID = typedArray.getResourceId(R.styleable.UITButton_uitButtonTextColorList, -1);
        if (textColorStateID != -1) {
            setTextColor(getColorStateList(textColorStateID, theme));
        }
    }

    private void applyBackgroundTinting(@NonNull TypedArray typedArray, final Resources.Theme theme) {
        int backGroundListID = typedArray.getResourceId(R.styleable.UITButton_uitButtonBackgroundColorList, -1);
        if (backGroundListID != -1 && getBackground() != null) {
            setSupportBackgroundTintList(getColorStateList(backGroundListID, theme));
        }
    }

    // TODO: 22/09/16 Switch back to ThemeUtils once we update sdk to 24.
    private ColorStateList getColorStateList(final int backgroundColorStateID, final @NonNull Resources.Theme theme) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ColorStateList.createFromXml(getResources(), getResources().getXml(backgroundColorStateID), theme);
                //ThemeUtils.buildColorStateList(getContext().getResources(), theme, backgroundColorStateID);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method can be used to set the drawable on left inside button
     *
     * @param drawable
     */
    public void setImageDrawable(Drawable drawable) {
        if (drawableColorlist != null && drawable != null) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            drawable.invalidateSelf();
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, drawableColorlist);
        }
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }

    private void applyDrawable(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.UITButton_uitButtonImageDrawableSrc, -1);
//        We allow setting drawable programmatically too, which can be case for vectors.
        if (resourceId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId).mutate();
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            final Drawable[] compoundDrawables = getCompoundDrawables();
            setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        }
    }

    private void assignDrawableProperties(TypedArray typedArray, final Resources.Theme theme, final AttributeSet attr) {
        drawableWidth = (int) typedArray.getDimension(R.styleable.UITButton_uitButtonImageDrawableWidth, 24.0f);
        drawableHeight = (int) typedArray.getDimension(R.styleable.UITButton_uitButtonImageDrawableHeight, 24.0f);
        //Store the color state list
        int resourceId = typedArray.getResourceId(R.styleable.UITButton_uitButtonDrawableColorList, -1);
        if (resourceId != -1) {
            drawableColorlist = getColorStateList(resourceId, theme);
        }
    }
}
