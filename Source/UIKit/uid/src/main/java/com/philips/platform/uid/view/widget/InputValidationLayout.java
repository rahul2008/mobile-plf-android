package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;

public class InputValidationLayout extends LinearLayout {

    private Label errorText;
    private ImageView errorIcon;
    private ViewGroup errorLayout;
    private ValidationEditText validationEditText;
    private boolean isShowingError;

    public InputValidationLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public InputValidationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    public InputValidationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputValidationLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        if (errorText != null) {
            errorText.setText(resID);
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
        ensureErrorLayout();
        isShowingError = true;
        validationEditText.showError(isShowingError);
        errorLayout.setVisibility(VISIBLE);

    }

    private void ensureErrorLayout() {
        if (errorLayout == null) {
            errorLayout = (ViewGroup) View.inflate(getContext(), R.layout.uid_inline_validation_input, null);
            errorText = (Label) errorLayout.findViewById(R.id.uid_inline_validation_text);
            errorIcon = (ImageView) errorLayout.findViewById(R.id.uid_inline_validation_icon);
            addView(errorLayout);
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
