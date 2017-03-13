package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;

public class TextInputLayout extends LinearLayout {

    private Label validationText;
    private ImageView validationIcon;
    private LinearLayout linearLayout;
    private ValidationEditText validationEditText;
    private boolean isShowingError;

    public TextInputLayout(Context context) {
        super(context);
        initialize(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ValidationEditText) {
            validationEditText = (ValidationEditText) child;
        }
    }

    /*    public ValidationEditText getValidationEditText() {
        return validationEditText;
    }*/

    private void initialize(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uid_inline_validation_input, this, true);

        validationText = (Label) findViewById(R.id.uid_inline_validation_text);
        validationIcon = (ImageView) findViewById(R.id.uid_inline_validation_icon);
        linearLayout = (LinearLayout) findViewById(R.id.uid_inline_validation_message_layout);
        validationEditText = (ValidationEditText) findViewById(R.id.uid_inline_validation_edittext);
    }

    public void setErrorMessage(int resID) {
        if (validationEditText != null) {
            validationEditText.setText(resID);
        }
    }

    public void setErrorMessage(CharSequence errorMsg) {
        if (validationEditText != null) {
            validationEditText.setText(errorMsg);
        }
    }

    public void setErrorDrawable(Drawable drawable) {
        validationIcon.setImageDrawable(drawable);
    }

    public void setErrorDrawable(int resID) {
        validationIcon.setImageResource(resID);
    }

    public void showError() {
        isShowingError = true;
        validationEditText.showError(isShowingError);
        linearLayout.setVisibility(VISIBLE);

    }

    public void hideError() {
        isShowingError = false;
        validationEditText.showError(isShowingError);
        linearLayout.setVisibility(GONE);
    }

    public boolean isShowingError() {
        return isShowingError;
    }
}
