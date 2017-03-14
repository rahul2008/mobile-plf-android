package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;

public class InputValidationLayout extends LinearLayout {

    private Label errorLabel;
    private ImageView errorIcon;
    private ViewGroup errorLayout;
    private ValidationEditText validationEditText;
    private boolean isShowingError;

    private int errorDrawableID;
    private int errorMessageID;
    private Validator validator;

    public interface Validator {
        boolean validate(CharSequence msg);
    }


    private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean focussed) {
            if (!focussed && validator != null)  {
                boolean result = validator.validate(validationEditText.getText());
                if(!result) {
                    showError();
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (validator != null && isShowingError && validator.validate(charSequence)) {
                hideError();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public InputValidationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, new int[]{R.attr.uidTextBoxValidationErrorDrawable, R.attr.uidTextBoxValidationErrorText});
        errorDrawableID = typedArray.getResourceId(0, -1);
        errorMessageID = typedArray.getResourceId(1, -1);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ValidationEditText) {
            validationEditText = (ValidationEditText) child;
            validationEditText.setOnFocusChangeListener(focusChangeListener);
            validationEditText.addTextChangedListener(textWatcher);
            ensureErrorLayout();
        }
    }

    public void setErrorMessage(int resID) {
        if (errorLabel != null) {
            errorLabel.setText(resID);
            errorLabel.setVisibility(VISIBLE);
        }
    }

    public void setErrorMessage(CharSequence errorMsg) {
        if (errorLabel != null) {
            errorLabel.setText(errorMsg);
            errorLabel.setVisibility(VISIBLE);
        }
    }

    public void setErrorDrawable(Drawable drawable) {
        errorIcon.setImageDrawable(drawable);
        errorIcon.setVisibility(VISIBLE);
    }

    public void setErrorDrawable(int resID) {
        errorIcon.setImageResource(resID);
        errorIcon.setVisibility(VISIBLE);
    }

    public void showError() {
        isShowingError = true;
        validationEditText.showError(isShowingError);
        errorLayout.setVisibility(VISIBLE);

    }

    private void ensureErrorLayout() {
        if (errorLayout == null) {
            errorLayout = (ViewGroup) View.inflate(getContext(), R.layout.uid_inline_validation_input, null);
            errorLabel = (Label) errorLayout.findViewById(R.id.uid_inline_validation_text);
            if (errorMessageID != -1) {
                errorLabel.setText(errorMessageID);
                errorLabel.setVisibility(VISIBLE);
            }

            errorIcon = (ImageView) errorLayout.findViewById(R.id.uid_inline_validation_icon);
            if (errorDrawableID != -1) {
                errorIcon.setImageResource(errorDrawableID);
                errorIcon.setVisibility(VISIBLE);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.uid_inline_validation_message_margin_top);
            layoutParams.setMargins(0, topMargin, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;

            addView(errorLayout, layoutParams);
        }
    }

    public void hideError() {
        isShowingError = false;
        validationEditText.showError(isShowingError);
        errorLayout.setVisibility(GONE);
    }

    public boolean isShowingError() {
        return isShowingError;
    }

    public Label getErrorView() {
        return errorLabel;
    }

    public ImageView getErrorIconView() {
        return errorIcon;
    }

    public ViewGroup getErrorLayout() {
        return errorLayout;
    }

}
