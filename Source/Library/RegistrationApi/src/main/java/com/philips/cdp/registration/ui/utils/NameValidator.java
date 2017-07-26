package com.philips.cdp.registration.ui.utils;

import static com.philips.platform.uid.view.widget.InputValidationLayout.Validator;


public class NameValidator implements Validator {

    @Override
    public boolean validate(CharSequence msg) {

        if (msg.length() > 0) {
            return true;
        }
        return false;
    }
}