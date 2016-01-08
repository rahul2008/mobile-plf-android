package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Controls extends LinearLayout {

    private final View parentView;
    private int baseColor;
    private int whiteColor;
    private int controlsCount = 2;

    public Controls(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.controlStyleRef);
    }

    public Controls(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_controls, null);
        createDynamicButtons(getParentLayout());
//        this.addView(parentView);
    }

    private void createDynamicButtons(LinearLayout parentLayout) {
        for (int i = 0; i < controlsCount; i++) {
            Button button = new Button(getContext());
            button.setText("Option " + (i + 1));
            parentLayout.addView(button);
            addStatesForBackground(button);
            addStatesForTextColor(button);
        }

    }

    private void handleClicks() {
//        Button button1 = (Button) parentView.findViewById(R.id.control_button1);
//        Button button2 = (Button) parentView.findViewById(R.id.control_button2);
//        Button button3 = (Button) parentView.findViewById(R.id.control_button3);
//        addStatesForBackground(button1);
//        addStatesForTextColor(button1);
    }

    @NonNull
    View.OnClickListener onClick(final Button button) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.isSelected()) {
                    button.setSelected(false);
                    button.setBackgroundColor(baseColor);
                    button.setTextColor(Color.WHITE);
                } else {
                    button.setSelected(true);
                    button.setBackgroundColor(Color.WHITE);
                    button.setTextColor(baseColor);
                }
            }
        };
    }

    private void init() {
        Context context = getContext();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.controlCount});
        baseColor = typedArray.getColor(0, -1);
        controlsCount = typedArray.getInt(1, 2);
        whiteColor = context.getResources().getColor(R.color.uikit_white);
        typedArray.recycle();
        setLayoutParams();

    }

    private void setLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(layoutParams);
    }

    private void addStatesForBackground(Button button) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(whiteColor));
        states.addState(new int[]{},
                new ColorDrawable(baseColor));
        button.setBackgroundDrawable(states);
    }

    private void addStatesForTextColor(Button button) {
        button.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        baseColor,
                        whiteColor
                }
        ));
    }

    @NonNull
    private LinearLayout getParentLayout() {
        this.removeAllViews();
        this.addView(parentView);
        final LinearLayout linearLayout = (LinearLayout) parentView.findViewById(R.id.uikit_linear);
        linearLayout.removeAllViews();

        return linearLayout;
    }

}
