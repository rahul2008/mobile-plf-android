package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.utils.UikitUtils;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiCheckBox extends LinearLayout {

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean checked);
    }

    private View parentView;
    private int baseColor;
    private int backGroundColor;
    private RelativeLayout textLayoutParent;
    private TextView checkBoxText;
    private boolean isChecked = false;
    private String text;
    private TextView checkBoxTick;
    private OnCheckedChangeListener onCheckedChangeListener;

    public PuiCheckBox(final Context context) {
        super(context);
    }

    public PuiCheckBox(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final Resources resources = getResources();
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_checkbox, null);
        this.removeAllViews();
        this.addView(parentView);
        initView(getContext(), resources, attrs);
    }

    private void initView(final Context context, Resources resources, final AttributeSet attrs) {
        backGroundColor = Color.WHITE;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        baseColor = typedArray.getColor(0, resources.getColor(R.color.uikit_philips_blue));
        textLayoutParent = (RelativeLayout) parentView.findViewById(R.id.uikit_checkbox_text_layout);
        checkBoxText = (TextView) parentView.findViewById(R.id.uikit_checkbox_text);
        checkBoxTick = (TextView) parentView.findViewById(R.id.uikit_tick);
        typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.UiKit_CheckBox, 0, 0);
        isChecked = typedArray.getBoolean(R.styleable.UiKit_CheckBox_uikit_checked, false);
        text = typedArray.getString(R.styleable.UiKit_CheckBox_uikit_textValue);
        checkBoxText.setText(text);
        typedArray.recycle();
        changeBackGround(resources);
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

    private void changeBackGround(final Resources resources) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke((int) resources.getDimension(R.dimen.uikit_controls_checkbox_stroke), UikitUtils.adjustAlpha(baseColor, 0.5f));
        gradientDrawable.setColor(backGroundColor);
        textLayoutParent.setBackgroundDrawable(gradientDrawable);
    }

    private void notifyDataSetChanged() {

    }

    public void setStrokeColor(int color) {
        this.baseColor = color;
    }

    public void setFilledColor(int color) {
        this.backGroundColor = color;
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

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    private void setChecked(final boolean isChecked) {
        if (isChecked) {
            checkBoxTick.setVisibility(VISIBLE);
        } else {
            checkBoxTick.setVisibility(GONE);
        }
    }
}
