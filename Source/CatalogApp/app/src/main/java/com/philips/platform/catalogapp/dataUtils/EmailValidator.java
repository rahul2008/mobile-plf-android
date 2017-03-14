package com.philips.platform.catalogapp.dataUtils;

import com.philips.platform.uid.view.widget.InputValidationLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements InputValidationLayout.Validator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(msg);
        return matcher.find();
    }
}