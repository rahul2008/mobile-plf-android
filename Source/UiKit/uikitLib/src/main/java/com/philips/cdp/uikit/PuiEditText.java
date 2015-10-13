package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiEditText extends RelativeLayout {

    public interface Validator {
        boolean validate(String inputToBeValidated);
    }

    private EditText editText;
    private ImageView errorImage;
    private TextView errorTextView;
    private int errorTextColor;
    private Drawable errorIcon;
    private Drawable errorBackground;

    private Drawable themeDrawable;

    private Validator validator;

    public PuiEditText(final Context context) {
        super(context);
    }

    public PuiEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.input_text_field, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputTextField);
        String editTextHint = a.getString(R.styleable.InputTextField_hintText);
        String errorText = a.getString(R.styleable.InputTextField_errorText);
        boolean disabled = a.getBoolean(R.styleable.InputTextField_disabled, false);
        errorTextColor = a.getColor(R.styleable.InputTextField_errorTextColor, getResources().getColor(R.color.philips_bright_orange));
        errorIcon = a.getDrawable(R.styleable.InputTextField_errorIcon);
        errorBackground = a.getDrawable(R.styleable.InputTextField_errorBackground);
        a.recycle();

        editText = (EditText) getChildAt(0);
        editText.setHint(editTextHint);
        editText.setFocusable(!disabled);
        editText.setEnabled(!disabled);
        editText.setOnFocusChangeListener(onFocusChangeListener);

        themeDrawable = editText.getBackground();

        errorImage = (ImageView) getChildAt(2);
        errorImage.setImageDrawable(errorIcon);

        errorTextView = (TextView) getChildAt(3);
        errorTextView.setText(errorText);
        errorTextView.setTextColor(errorTextColor);

        errorImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                setErrorMessageVisibilty(View.GONE);
            }
        });
    }

    public PuiEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setEditTextEnabled(boolean enabled) {
        editText.setEnabled(enabled);
    }

    private void showErrorAndChangeEditTextStyle(boolean show) {
        if (show) {
            setErrorMessageVisibilty(View.VISIBLE);
            setErrorTextStyle();
        } else {
            setErrorMessageVisibilty(View.GONE);
            editText.setTextColor(getResources().getColor(R.color.philips_very_dark_blue));
            setBackgroundAsPerAPILevel(themeDrawable);
        }
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            if (!hasFocus) {
                showErrorAndChangeEditTextStyle(!(validator == null || validator.validate(editText.getText().toString())));
            } else {
                editText.setTextColor(getResources().getColor(R.color.philips_very_dark_blue));
            }
        }
    };

    private void setErrorTextStyle() {
        editText.setTextColor(errorTextColor);
        setBackgroundAsPerAPILevel(errorBackground);
    }

    private void setErrorMessageVisibilty(int visibility) {
        errorTextView.setVisibility(visibility);
        errorImage.setVisibility((visibility));
    }

    private void setBackgroundAsPerAPILevel(final Drawable backgroundDrawable) {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(backgroundDrawable);
        } else {
            editText.setBackgroundDrawable(backgroundDrawable);
        }
    }
}
