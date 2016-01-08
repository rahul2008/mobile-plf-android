package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MultiStateControls extends LinearLayout {

    private static final String TAG = MultiStateControls.class.getSimpleName();
    private List<View> buttons;
    private boolean isMultipleChoice = false;
    private LinearLayout parentLayout;
    private OnButtonStateChangeListener listener;
    private Context context;
    private int baseColor;
    private GradientDrawable unSelectedDrawable;
    private int whiteColor;

    public MultiStateControls(Context context) {
        super(context, null);
        if (this.isInEditMode()) {
            return;
        }
    }

    public MultiStateControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
        if (this.isInEditMode()) {
            return;
        }
        init();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Controls, 0,
                R.style.Controls_Style);
        CharSequence[] texts = a.getTextArray(R.styleable.Controls_controlEntries);
        int count = a.getInt(R.styleable.Controls_controlCount, 1);
        boolean isSelected = a.getBoolean(R.styleable.Controls_controlSelected, false);
        a.recycle();

        drawControls(texts, isSelected, count);
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts    An array of CharSequences for the buttons
     * @param enableDefaultSelection The default value for the buttons
     */
    public void drawControls(CharSequence[] texts, boolean enableDefaultSelection, int count) {
        final int textCount = texts != null ? texts.length : 0;
        final int elementCount = Math.max(textCount, count);
        if (elementCount == 0) {
            throw new IllegalArgumentException("neither texts nor count are setup");
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (parentLayout == null) {
            parentLayout = (LinearLayout) inflater.inflate(R.layout.uikit_controls, this, true);
        }
        parentLayout.removeAllViews();

        this.buttons = new ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            Button button;
            button = (Button) inflater.inflate(R.layout.uikit_toggle_button, parentLayout, false);
            button.setText(texts != null ? texts[i] : "");
            final int position = i;
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }

            });
            parentLayout.addView(button);
            setButtonState(button, enableDefaultSelection);
            this.buttons.add(button);
        }
    }

    private void setUnSelectedState() {
        unSelectedDrawable = new GradientDrawable();
        unSelectedDrawable.setStroke(2, baseColor);
        unSelectedDrawable.setColor(Color.WHITE);

    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to setBackgroundColor sticking with deprecated API for now
    public void setButtonState(View button, boolean selected) {
        if (button == null) {
            return;
        }
        button.setSelected(selected);
        if (selected) {
            button.setBackgroundColor(baseColor);
        } else {
            button.setBackgroundDrawable(unSelectedDrawable);
        }
        int style = selected ? R.style.WhiteText : R.style.baseText;
        ((Button) button).setTextAppearance(this.context, style);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to getColor sticking with deprecated API for now
    private void init() {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        baseColor = typedArray.getColor(0, -1);
        whiteColor = context.getResources().getColor(R.color.uikit_white);
        typedArray.recycle();
        setUnSelectedState();
    }

    public int getValue() {
        for (int i = 0; i < this.buttons.size(); i++) {
            if (buttons.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setValue(int position) {
        for (int i = 0; i < this.buttons.size(); i++) {
            if (isMultipleChoice) {
                if (i == position) {
                    View b = buttons.get(i);
                    if (b != null) {
                        setButtonState(b, !b.isSelected());
                    }
                }
            } else {
                if (i == position) {
                    setButtonState(buttons.get(i), true);
                } else if (!isMultipleChoice) {
                    setButtonState(buttons.get(i), false);
                }
            }
        }
        setListenerValue(position);
    }

    public List<View> getButtons() {
        return buttons;
    }

    public boolean[] getStates() {
        int size = this.buttons == null ? 0 : this.buttons.size();
        boolean[] result = new boolean[size];
        for (int i = 0; i < size; i++) {
            result[i] = this.buttons.get(i).isSelected();
        }
        return result;
    }

    public void setStates(boolean[] selected) {
        if (this.buttons == null || selected == null ||
                this.buttons.size() != selected.length) {
            return;
        }
        int count = 0;
        for (View view : this.buttons) {
            setButtonState(view, selected[count]);
            count++;
        }
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable
     */
    public void enableMultipleChoice(boolean enable) {
        this.isMultipleChoice = enable;
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param buttons the array of button views to use
     */
    public void setUserDefinedButtons(View[] buttons) {
        final int elementCount = buttons.length;
        if (elementCount == 0) {
            throw new IllegalArgumentException("neither texts nor images are setup");
        }

        boolean defaultSelection = false;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (parentLayout == null) {
            parentLayout = (LinearLayout) inflater.inflate(R.layout.uikit_controls, this, true);
        }
        parentLayout.removeAllViews();

        this.buttons = new ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            View b = buttons[i];
            final int position = i;
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }

            });
            parentLayout.addView(b);
            if (defaultSelection) {
                setButtonState(b, defaultSelection);
            }
            this.buttons.add(b);
        }
    }

    public void setListenerValue(int value) {
        if (this.listener != null) {
            listener.onButtonStateChanged(value);
        }
    }

    public void setOnButtonStateChangedListener(OnButtonStateChangeListener onButtonStateChangeListener) {
        this.listener = onButtonStateChangeListener;
    }

    public interface OnButtonStateChangeListener {
        void onButtonStateChanged(int value);
    }


}
