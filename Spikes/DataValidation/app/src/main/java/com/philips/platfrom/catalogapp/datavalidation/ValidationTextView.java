package com.philips.platfrom.catalogapp.datavalidation;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class ValidationTextView extends AppCompatTextView {
    ValidationPopUp validationPopUp;
    public ValidationTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        validationPopUp = new ValidationPopUp(context);
    }

    public void showValidationError() {
        validationPopUp.showError(this);
    }
}