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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDUtils;

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
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
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