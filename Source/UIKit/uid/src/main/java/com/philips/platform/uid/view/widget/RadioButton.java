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
import com.philips.platform.uid.utils.UIDLocaleHelper;

/**
 * <p>Provides an implementation for a customized RadioButton.
 * </p>
 * <p>In order to customize the RadioButton to your own needs, it is recommended to create a new style and use UIDRadioButton style as your parent style. <br>
 * The provided background is used for the ripple effect from lollipop onwards.</p>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidRadioButtonPaddingStart</td>
 * <td rowspan="1">Padding used in front of the RadioButton</td>
 * </tr>
 * <tr>
 * <td rowspan="1">paddingStart</td>
 * <td rowspan="1">Padding used between the RadioButton and label</td>
 * </tr>
 * <tr>
 * <td rowspan="1">paddingEnd</td>
 * <td rowspan="1">Padding used after the label</td>
 * </tr>
 * <tr>
 * <td rowspan="1">fontPath</td>
 * <td rowspan="1">Path used to specify your custom font</td>
 * </tr>
 * </tbody>
 * <p>
 * </table>
 */
public class RadioButton extends AppCompatRadioButton{

    private int radioButtonStartPadding = 0;

    public RadioButton(final Context context) {
        this(context, null);
    }

    public RadioButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidRadioButtonStyle);
    }

    public RadioButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        setTextColorFromResourceID(context, this, attrs, theme);
    }

    private void setTextColorFromResourceID(@NonNull Context context, @NonNull View view, @NonNull final AttributeSet attr, @NonNull final Resources.Theme theme) {
        if (view instanceof TextView) {
            TypedArray textColorArray = context.obtainStyledAttributes(attr, new int[]{android.R.attr.textColor});
            int resourceId = textColorArray.getResourceId(0, -1);
            if (resourceId != -1) {
                ((TextView) view).setTextColor(resourceId);
            } else{
                setTextColor(ThemeUtils.buildColorStateList(context.getResources(), theme, R.color.uid_radiobutton_text_selector));
            }
            textColorArray.recycle();
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredWidth = getMeasuredWidth() + radioButtonStartPadding;
        setMeasuredDimension(measuredWidth, ViewCompat.getMeasuredHeightAndState(this));
        final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        if(gravity == Gravity.CENTER_VERTICAL && getLineCount() >1) {
            setGravity(Gravity.TOP);
        }
    }
}