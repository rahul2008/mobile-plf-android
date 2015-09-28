package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

    private EditText editText;
    private ImageView errorImage;
    private TextView errorTextView;

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
        a.recycle();

        editText = (EditText) getChildAt(0);
        editText.setHint(editTextHint);
        themeDrawable = editText.getBackground();
        editText.setOnFocusChangeListener(onFocusChangeListener);
        errorImage = (ImageView) getChildAt(2);
        errorTextView = (TextView) getChildAt(3);
        errorTextView.setText(errorText);

        errorImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
               showError(false);
            }
        });

    }

    public PuiEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void showError(boolean show) {
        if(show) {
            setErrorMessageVisibilty(View.VISIBLE);
            setErrorTextStyle();
        } else {
            setErrorMessageVisibilty(View.GONE);
            editText.setTextColor(getResources().getColor(R.color.philips_dark_blue));
            editText.setBackground(themeDrawable);
        }
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            //If out of focus, validate the entered text
            //If (!validate) change border and text colour to bright orange
            //else apply theme colours
            if(!hasFocus) {
                if(validator == null || validator.validate(editText.getText().toString())) {
                    showError(false);
                } else {
                    showError(true);
                }
            }
        }
    };

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    private void setErrorTextStyle() {
        editText.setBackgroundResource(R.drawable.edittext_error_bg);
        editText.setTextColor(getResources().getColor(R.color.philips_bright_orange));
    }

    private void setErrorMessageVisibilty(int visibility) {
        errorTextView.setVisibility(visibility);
        errorImage.setVisibility((visibility));
    }

    public interface Validator {
        boolean validate(String inputToBeValidated);
    }
}
