package com.philips.cdp.registration.ui.utils;

import com.philips.platform.uid.view.widget.InputValidationLayout;


public class NameValidator implements InputValidationLayout.Validator {

    @Override
    public boolean validate(CharSequence msg) {

        if (msg.length() > 0) {
            return true;
        }
        return false;
    }
}