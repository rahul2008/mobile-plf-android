package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.ui.traditional.CreateAccountFragment;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements InputValidationLayout.Validator {
     private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    CreateAccountFragment.ValidEmail isValidEmail;

    public EmailValidator(CreateAccountFragment.ValidEmail isValidEmail) {
        this.isValidEmail = isValidEmail;
    }

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(msg);
        boolean validMail = matcher.find();

        if (msg.length() == 0) {
            isValidEmail.isEmpty(true);
        } else if (!validMail) {
            isValidEmail.isEmpty(false);
            isValidEmail.isValid(validMail);
        }
        return validMail;
    }
}


