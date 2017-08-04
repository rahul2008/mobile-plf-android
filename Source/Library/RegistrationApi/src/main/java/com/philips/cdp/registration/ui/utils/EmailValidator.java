package com.philips.cdp.registration.ui.utils;

import java.util.regex.*;

import static com.philips.platform.uid.view.widget.InputValidationLayout.Validator;


public class EmailValidator implements Validator {
     private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    ValidEmail validEmail;

    public EmailValidator(ValidEmail validEmail) {
        this.validEmail = validEmail;
    }

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(msg);
        boolean validMail = matcher.find();

        if (msg.length() == 0) {
            validEmail.isEmpty(true);
        } else {
            validEmail.isEmpty(false);
            validEmail.isValid(validMail);
        }
        return validMail;
    }
}


