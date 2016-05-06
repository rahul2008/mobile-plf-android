/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.philips.cdp.uikit.utils.UikitUtils;

import java.util.Arrays;
import java.util.List;


public class StateControls extends LinearLayout {

    public interface OnButtonStateChangeListener {
        void onButtonStateChanged(int value);
    }

    private int buttonWidth, buttonHeight;
    private String[] texts;
    private int count;
    private List<View> buttons;
    private List<View> dividers;
    private boolean isMultipleChoice = false;
    private LinearLayout parentLayout;
    private OnButtonStateChangeListener listener;
    private Context context;
    private int baseColor;
    private LayoutInflater inflater;

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
        CharSequence[] texts = a.getTextArray(R.styleable.Controls_uikit_controlEntries);
        count = a.getInt(R.styleable.Controls_uikit_controlCount, 0);
        isMultipleChoice = a.getBoolean(R.styleable.Controls_uikit_controlMultiChoice, false);
        buttonWidth = (int) a.getDimension(R.styleable.Controls_uikit_controlButtonWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonHeight = (int) a.getDimension(R.styleable.Controls_uikit_controlButtonHeight, ViewGroup.LayoutParams.WRAP_CONTENT);
        a.recycle();

        drawControls(texts, false);
    }

    private void initControls(CharSequence[] texts, boolean enableDefaultSelection) {
        final int textCount = texts != null ? texts.length : 0;
        final int elementCount = Math.max(textCount, count);
        if (elementCount == 0) {
            throw new IllegalArgumentException("Count not set up");
        }
        parentLayout.removeAllViews();
        View[] buttons = new Button[elementCount];
        View[] dividers = new View[elementCount];
        this.buttons = Arrays.asList(buttons);
        this.dividers = Arrays.asList(dividers);
        iterateViews(texts, enableDefaultSelection, buttons, dividers, elementCount, false);
        handleButtonState();
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts                  An array of CharSequences for the buttons
     * @param enableDefaultSelection The default value for the buttons
     */
    public void drawControls(CharSequence[] texts, boolean enableDefaultSelection) {

        initControls(texts, enableDefaultSelection);

    }


    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts                  An array of Strings for the buttons
     * @param enableDefaultSelection The default value for the buttons
     */
    public void drawControls(String[] texts, boolean enableDefaultSelection) {
        initControls(texts, enableDefaultSelection);

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
        parentLayout.removeAllViews();
        View[] buttons = new ImageButton[elementCount];
        View[] dividers = new View[elementCount];
        this.buttons = Arrays.asList(buttons);
        this.dividers = Arrays.asList(dividers);
        iterateViews(drawables, enableDefaultSelection, buttons, dividers, elementCount, true);
        handleButtonState();
    }

    private void iterateViews(final Object[] data, final boolean enableDefaultSelection, final View[] buttons, final View[] dividers, final int elementCount, boolean isDrawable) {
        LayoutParams buttonParams = new LayoutParams(buttonWidth, buttonHeight);
        int baseColorAlpha = UikitUtils.adjustAlpha(baseColor, 0.3f);
        for (int i = 0; i < elementCount; i++) {
            View view, divider, button;
            if (isDrawable) {
                view = inflater.inflate(R.layout.uikit_toggle_image_button, null, false);
                button = view.findViewById(R.id.control_button);
                ImageButton imageButton = (ImageButton) button;
                imageButton.setScaleType(ImageView.ScaleType.CENTER);
                divider = view.findViewById(R.id.divider);
                Drawable drawable = (Drawable) data[i];
                imageButton.setImageDrawable(drawable);
            } else {
                view = inflater.inflate(R.layout.uikit_toggle_button, null, false);
                button = view.findViewById(R.id.control_button);
                divider = view.findViewById(R.id.divider);
                Button buttonCast = (Button) button;
                String dataValue = data != null ? (String) data[i] : "";
                buttonCast.setText(dataValue);
            }
            button.setLayoutParams(buttonParams);
            divider.setBackgroundColor(baseColorAlpha);
            final int position = i;
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }
            });
            parentLayout.addView(view);
            if (isMultipleChoice)
                setButtonState(button, enableDefaultSelection, position);
            else
                setButtonState(button, false, position);
            buttons[i] = button;
            dividers[i] = divider;
        }
    }

    private void handleButtonState() {
        int size = buttons.size();
        for (int i = 0; i < size; i++) {
            if (buttons.get(i) instanceof ImageButton) {
                ImageButton button = (ImageButton) buttons.get(i);
                setDividers(button);
            } else {
                Button button = (Button) buttons.get(i);

                setDividers(button);
            }

        }
    }

    private void setDividers(View button) {
        int size = buttons.size();
        for (int i = 0; i < size; i++) {
            if (i == 0 && !button.isSelected() && size > 1 && !buttons.get(i + 1).isSelected()) {
                dividers.get(i).setVisibility(VISIBLE);
            } else if (i != 0 && !button.isSelected() && i != (size - 1) && (size - 1) > i && !buttons.get(i + 1).isSelected()) {
                dividers.get(i).setVisibility(VISIBLE);
            } else {
                dividers.get(i).setVisibility(GONE);
            }
        }
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
            gradientDrawable.setStroke((int) context.getResources().getDimension(R.dimen.uikit_control_default_stroke), baseColor);
            gradientDrawable.setColor(Color.WHITE);
            button.setBackgroundDrawable(gradientDrawable);
        } else {
            gradientDrawable.setColor(baseColor);
            button.setBackgroundDrawable(gradientDrawable);
        }
        int color = selected ? baseColor : Color.WHITE;
        if (button instanceof Button) {
            ((Button) button).setTextColor(color);
        } else if (button instanceof ImageButton) {
            ((ImageButton) button).getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void handleCorners(GradientDrawable gradientDrawable, int position) {
        Resources resources = context.getResources();
        float dimension = resources.getDimension(R.dimen.uikit_controls_button_corner_rounding);
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
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        baseColor = typedArray.getColor(0, -1);
        typedArray.recycle();
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (parentLayout == null) {
            parentLayout = (LinearLayout) inflater.inflate(R.layout.uikit_controls, this, true);
        }
    }

    private void notifyDataSetChanged() {
        this.removeAllViews();
        drawControls(texts, false);
    }

    private void setListenerValue(int value) {
        if (this.listener != null) {
            listener.onButtonStateChanged(value);
        }
    }

    /**
     * Settings state of button with respect to its position
     *
     * @param position
     */
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

    /**
     * @return Returns array of View
     */
    public List<View> getButtons() {
        return buttons;
    }

    /**
     * @return Returns State of buttons
     */
    public boolean[] getStates() {
        int size = this.buttons == null ? 0 : this.buttons.size();
        boolean[] result = new boolean[size];
        for (int i = 0; i < size; i++) {
            result[i] = this.buttons.get(i).isSelected();
        }
        return result;
    }

    /**
     * Setting the button state
     *
     * @param selected - array of boolean values
     */
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

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable - boolean value to either enable or disable multi choice
     */
    public void enableMultipleChoice(boolean enable) {
        this.isMultipleChoice = enable;
        notifyDataSetChanged();
    }

    /**
     * API to set text's programmatically
     *
     * @param texts - Value of texts as array of Strings
     */
    public void setTexts(String[] texts) {
        this.texts = texts;
        notifyDataSetChanged();
    }

    /**
     * API to set total buttons count
     *
     * @param count - Total count value in integer
     */
    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    /**
     * Callback to be registered when state of buttons are changed
     *
     * @param onButtonStateChangeListener - Listener of type OnButtonStateChangeListener
     */
    public void setOnButtonStateChangedListener(OnButtonStateChangeListener onButtonStateChangeListener) {
        this.listener = onButtonStateChangeListener;
    }

    /**
     * @return Return button width as integer
     */
    public int getButtonWidth() {
        return buttonWidth;
    }

    /**
     * Set button width programmatically
     *
     * @param buttonWidth
     */
    public void setButtonWidth(final int buttonWidth) {
        this.buttonWidth = buttonWidth;
        notifyDataSetChanged();
    }

    /**
     * @return Return button height as integer
     */
    public int getButtonHeight() {
        return buttonHeight;
    }

    /**
     * Set button height programmatically
     *
     * @param buttonHeight
     */
    public void setButtonHeight(final int buttonHeight) {
        this.buttonHeight = buttonHeight;
        notifyDataSetChanged();
    }
}
