package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;

import java.util.Arrays;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class StateControls extends LinearLayout {

    public interface OnButtonStateChangeListener {
        void onButtonStateChanged(int value);
    }
    private float buttonWidth, buttonHeight;
    private boolean isSelected;
    private CharSequence[] texts;
    private int count;
    private List<View> buttons;
    private List<View> dividers;
    private boolean isMultipleChoice = false;
    private LinearLayout parentLayout;
    private OnButtonStateChangeListener listener;
    private Context context;
    private int baseColor;

    public StateControls(Context context) {
        super(context, null);
        if (this.isInEditMode()) {
            return;
        }
    }

    public StateControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
        if (this.isInEditMode()) {
            return;
        }
        init();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Controls, 0,
                R.style.Controls_Style);
        texts = a.getTextArray(R.styleable.Controls_controlEntries);
        count = a.getInt(R.styleable.Controls_controlCount, 0);
        isSelected = a.getBoolean(R.styleable.Controls_controlSelected, false);
        isMultipleChoice = a.getBoolean(R.styleable.Controls_controlMultiChoice, false);
        buttonWidth = a.getDimension(R.styleable.Controls_controlButtonWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonHeight = a.getDimension(R.styleable.Controls_controlButtonHeight, ViewGroup.LayoutParams.WRAP_CONTENT);
        a.recycle();

        drawControls(texts, isSelected, count);
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts                  An array of CharSequences for the buttons
     * @param enableDefaultSelection The default value for the buttons
     */
    public void drawControls(CharSequence[] texts, boolean enableDefaultSelection, int count) {
        final int textCount = texts != null ? texts.length : 0;
        final int elementCount = Math.max(textCount, count);
        if (elementCount == 0) {
            throw new IllegalArgumentException("Count not set up");
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (parentLayout == null) {
            parentLayout = (LinearLayout) inflater.inflate(R.layout.uikit_controls, this, true);
        }
        parentLayout.removeAllViews();
        View[] buttons = new Button[elementCount];
        View[] dividers = new View[elementCount];
        this.buttons = Arrays.asList(buttons);
        this.dividers = Arrays.asList(dividers);

        int baseColorAlpha = adjustAlpha(baseColor, 0.3f);
        LayoutParams buttonParams = new LayoutParams((int) buttonWidth, (int) buttonHeight);
        for (int i = 0; i < elementCount; i++) {
            View view, divider;
            Button button;
            view = inflater.inflate(R.layout.uikit_toggle_button, null, false);
            button = (Button) view.findViewById(R.id.control_button);
            divider = view.findViewById(R.id.divider);
            divider.setBackgroundColor(baseColorAlpha);
            button.setLayoutParams(buttonParams);
            button.setText(texts != null ? texts[i] : "");
            if (isMultipleChoice && i != elementCount - 1) {
                divider.setVisibility(View.VISIBLE);
            }
            final int position = i;
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }
            });
            parentLayout.addView(view);
            if (position == 0)
                setButtonState(button, true, position);
            else if (isMultipleChoice)
                setButtonState(button, enableDefaultSelection, position);
            else
                setButtonState(button, false, position);
            buttons[i] = button;
            dividers[i] = divider;
        }
        handleButtonState();
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param drawables              An array of Drawable's for the buttons
     * @param enableDefaultSelection The default value for the buttons
     */
    public void drawControls(Drawable[] drawables, boolean enableDefaultSelection) {
        final int elementCount = drawables != null ? drawables.length : 0;
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

        View[] buttons = new ImageButton[elementCount];
        View[] dividers = new View[elementCount];
        this.buttons = Arrays.asList(buttons);
        this.dividers = Arrays.asList(dividers);

        int baseColorAlpha = adjustAlpha(baseColor, 0.3f);
        LayoutParams buttonParams = new LayoutParams((int) buttonWidth, (int) buttonHeight);
        for (int i = 0; i < elementCount; i++) {
            ImageButton button;
            View view, divider;
            view = inflater.inflate(R.layout.uikit_toggle_image_button, null, false);
            button = (ImageButton) view.findViewById(R.id.control_button);
            button.setLayoutParams(buttonParams);
            button.setScaleType(ImageView.ScaleType.CENTER);
            divider = view.findViewById(R.id.divider);
            divider.setBackgroundColor(baseColorAlpha);
            button.setImageDrawable(drawables[i]);
            if (isMultipleChoice && i != elementCount - 1) {
                divider.setVisibility(View.VISIBLE);
            }
            final int position = i;
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }
            });
            parentLayout.addView(view);
            if (position == 0)
                setButtonState(button, true, position);
            else if (isMultipleChoice)
                setButtonState(button, enableDefaultSelection, position);
            else
                setButtonState(button, false, position);
            buttons[i] = button;
            dividers[i] = divider;
        }
        handleButtonState();
    }

    private void handleButtonState() {
        int size = buttons.size();
        for (int i = 0; i < size; i++) {
            if (buttons.get(i) instanceof ImageButton) {
                ImageButton button = (ImageButton) buttons.get(i);
                if (i == 0 && button.isSelected() && size > 1 && buttons.get(i + 1).isSelected()) {
                    dividers.get(i).setVisibility(VISIBLE);
                } else if (i != 0 && button.isSelected() && i != (size - 1) && (size - 1) > i && buttons.get(i + 1).isSelected()) {
                    dividers.get(i).setVisibility(VISIBLE);
                } else {
                    dividers.get(i).setVisibility(GONE);
                }
            } else {
                Button button = (Button) buttons.get(i);
                if (i == 0 && button.isSelected() && size > 1 && buttons.get(i + 1).isSelected()) {
                    dividers.get(i).setVisibility(VISIBLE);
                } else if (i != 0 && button.isSelected() && i != (size - 1) && (size - 1) > i && buttons.get(i + 1).isSelected()) {
                    dividers.get(i).setVisibility(VISIBLE);
                } else {
                    dividers.get(i).setVisibility(GONE);
                }
            }
        }
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to setBackgroundColor sticking with deprecated API for now
    private void setButtonState(View button, boolean selected, int position) {
        if (button == null) {
            return;
        }
        button.setSelected(selected);
        GradientDrawable gradientDrawable = new GradientDrawable();
        handleCorners(gradientDrawable, position);
        if (selected) {
            gradientDrawable.setColor(baseColor);
            button.setBackgroundDrawable(gradientDrawable);
        } else {
            gradientDrawable.setStroke((int) context.getResources().getDimension(R.dimen.uikit_control_default_stroke), baseColor);
            gradientDrawable.setColor(Color.WHITE);
            button.setBackgroundDrawable(gradientDrawable);
        }

        if (button instanceof Button) {
            int textColor = selected ? Color.WHITE : baseColor;
            ((Button) button).setTextColor(textColor);
        }
    }

    private void handleCorners(GradientDrawable gradientDrawable, int position) {
        Resources resources = context.getResources();
        float dimension = resources.getDimension(R.dimen.uikit_button_corner_rounding);
        int lastPosition = this.buttons.size() - 1;

        if (position == 0) {
            gradientDrawable.setCornerRadii(new float[]{dimension, dimension, 0, 0, 0, 0, dimension, dimension});
        } else if (position == lastPosition) {
            gradientDrawable.setCornerRadii(new float[]{0, 0, dimension, dimension, dimension, dimension, 0, 0});
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to getColor sticking with deprecated API for now
    private void init() {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        baseColor = typedArray.getColor(0, -1);
        typedArray.recycle();
    }

    private void notifyDataSetChanged() {
        this.removeAllViews();
        drawControls(texts, isSelected, count);
    }

    public void setValue(int position) {
        for (int i = 0; i < this.buttons.size(); i++) {
            if (isMultipleChoice) {
                if (i == position) {
                    View b = buttons.get(i);
                    if (b != null) {
                        setButtonState(b, !b.isSelected(), i);
                    }
                }
            } else {
                if (i == position) {
                    setButtonState(buttons.get(i), true, i);
                } else if (!isMultipleChoice) {
                    setButtonState(buttons.get(i), false, i);
                }
            }
        }
        handleButtonState();
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
        for (int i = 0; i < this.buttons.size(); i++) {
            setButtonState(this.buttons.get(i), selected[count], i);
            count++;
        }
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        notifyDataSetChanged();
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable
     */
    public void enableMultipleChoice(boolean enable) {
        this.isMultipleChoice = enable;
        notifyDataSetChanged();
    }

    public void setTexts(CharSequence[] texts) {
        this.texts = texts;
        notifyDataSetChanged();
    }

    public void setListenerValue(int value) {
        if (this.listener != null) {
            listener.onButtonStateChanged(value);
        }
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setOnButtonStateChangedListener(OnButtonStateChangeListener onButtonStateChangeListener) {
        this.listener = onButtonStateChangeListener;
    }
}
