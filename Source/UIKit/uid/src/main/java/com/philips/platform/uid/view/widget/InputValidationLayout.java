package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

    public InputValidationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, new int[]{R.attr.uidTextBoxValidationText, R.attr.uidTextBoxValidationErrorDrawable});
        errorMessageID = typedArray.getResourceId(0, -1);
        errorDrawableID = typedArray.getResourceId(1, -1);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ValidationEditText) {
            validationEditText = (ValidationEditText) child;
            ensureErrorLayout();
        }
    }

    public void setErrorMessage(int resID) {
        if (errorLabel != null) {
            errorLabel.setText(resID);
        }
    }

    public void setErrorMessage(CharSequence errorMsg) {
        if (validationEditText != null) {
            validationEditText.setText(errorMsg);
        }
    }

    public void setErrorDrawable(Drawable drawable) {
        errorIcon.setImageDrawable(drawable);
    }

    public void setErrorDrawable(int resID) {
        errorIcon.setImageResource(resID);
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
            if(errorMessageID != -1) {
                errorLabel.setText(errorMessageID);
            }

            errorIcon = (ImageView) errorLayout.findViewById(R.id.uid_inline_validation_icon);
            if (errorDrawableID != -1) {
                errorIcon.setImageResource(errorDrawableID);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.uid_inline_validation_message_padding_top);
            layoutParams.setMargins(0, topMargin, 0, 0);

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
}
