/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;

/**
 * <p>Provides an implementation for a customized RadioButton.
 * </p>
 * <p>In order to customize the RadioButton to your own needs, it is recommended to create a new style and use UIDRadioButton style as your parent style. <br>
 * It is recommended to use customized UID RadioGroup class instead of Android RadioGroup class. <br>
 * The provided background is used for the ripple effect from lollipop onwards.</p>
 */
public class RadioButton extends AppCompatRadioButton{

    public RadioButton(final Context context) {
        this(context, null);
    }

    public RadioButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidRadioButtonStyle);
    }

    public RadioButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        setTextColorFromResourceID(context, this, attrs);
    }

    private void setTextColorFromResourceID(@NonNull Context themedContext, @NonNull View view, @NonNull final AttributeSet attr) {
            TypedArray textColorArray = themedContext.obtainStyledAttributes(attr, new int[]{android.R.attr.textColor});
            int resourceId = textColorArray.getResourceId(0, -1);
            if (resourceId != -1) {
                setTextColor(resourceId);
            } else{
                setTextColor(ThemeUtils.buildColorStateList(themedContext, R.color.uid_radiobutton_text_selector));
            }
            textColorArray.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), ViewCompat.getMeasuredHeightAndState(this));
        final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        if(gravity == Gravity.CENTER_VERTICAL && getLineCount() >1) {
            setGravity(Gravity.TOP);
        }
    }
}