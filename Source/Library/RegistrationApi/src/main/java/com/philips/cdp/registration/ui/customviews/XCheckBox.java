/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XCheckBox extends LinearLayout {

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean checked);
    }

    private View parentView;
    private RelativeLayout textLayoutParent;
    private TextView checkBoxText;
    private boolean isChecked = false;
    private String text;
    private TextView checkBoxTick;
    private OnCheckedChangeListener onCheckedChangeListener;

    public XCheckBox(final Context context) {
        super(context);
    }

    public XCheckBox(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final Resources resources = getResources();
        parentView = LayoutInflater.from(context).inflate(
                R.layout.x_checkbox, null);
        this.removeAllViews();
        this.addView(parentView);
        initView(getContext(), resources, attrs);
    }

    private void initView(final Context context, Resources resources, final AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.reg_baseColor});
        textLayoutParent = (RelativeLayout) parentView.findViewById(R.id.rl_x_checkbox);

        checkBoxText = (TextView) parentView.findViewById(R.id.reg_tv_checkbox);
        checkBoxTick = (TextView) parentView.findViewById(R.id.reg_check);
        FontLoader.getInstance().setTypeface(checkBoxTick, RegConstants.PUIICON_TTF);
        typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Registration_CheckBox, 0, 0);
        isChecked = typedArray.getBoolean(R.styleable.Registration_CheckBox_reg_checked, false);
        text = typedArray.getString(R.styleable.Registration_CheckBox_reg_textValue);
        checkBoxText.setText(text);
        typedArray.recycle();
        changeBackGround();
        setChecked(isChecked);
        textLayoutParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                isChecked = !isChecked;
                setChecked(isChecked);
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(v, isChecked);
            }
        });
    }

    public void setChecked(final boolean isChecked) {
        if (isChecked) {
            checkBoxTick.setVisibility(VISIBLE);
        } else {
            checkBoxTick.setVisibility(GONE);
        }
    }

    public void setEnabled(boolean isChecked) {
        textLayoutParent.setEnabled(isChecked);
    }

    private void changeBackGround() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(2, getResources().getColor(R.color.reg_check_box_border_color));
        textLayoutParent.setBackgroundDrawable(gradientDrawable);
    }


    public void setText(String text) {
        this.checkBoxText.setText(text);
    }

    public TextView getText() {
        return checkBoxText;
    }

    public void setText(int resId) {
        this.checkBoxText.setText(resId);
    }


    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
