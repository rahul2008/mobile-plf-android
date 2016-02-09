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

public class XCheckBox extends LinearLayout {

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
        backGroundColor = context.getResources().getColor(R.color.reg_layout_bg);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        baseColor = typedArray.getColor(0, resources.getColor(R.color.reg_text_heading_one_color));//reg_text_heading_one_color/reg_check_box_color
        textLayoutParent = (RelativeLayout) parentView.findViewById(R.id.rl_x_checkbox);
        checkBoxText = (TextView) parentView.findViewById(R.id.reg_tv_checkbox);
        checkBoxTick = (TextView) parentView.findViewById(R.id.reg_check);
        typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Registration_CheckBox, 0, 0);
        isChecked = typedArray.getBoolean(R.styleable.Registration_CheckBox_checked, false);
        text = typedArray.getString(R.styleable.Registration_CheckBox_textValue);
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

    private void changeBackGround() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(2, baseColor);
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
    public boolean isChecked(){
        return isChecked;
    }
}
