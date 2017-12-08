package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.settings.*;

import java.util.regex.*;

import static com.philips.platform.uid.view.widget.InputValidationLayout.*;


public class LoginIdValidator implements Validator {
     private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    ValidLoginId validLoginId;

    public LoginIdValidator(ValidLoginId validLoginId) {
        this.validLoginId = validLoginId;
    }

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(msg);
        boolean validMail = matcher.find();

        if (!validMail && RegistrationHelper.getInstance().isMobileFlow()) {
           validMail = FieldsValidator.isValidMobileNumber(msg.toString());
        }

        if (msg.length() == 0) {
            validLoginId.isEmpty(true);
        } else {
            validLoginId.isEmpty(false);
            validLoginId.isValid(validMail);
        }
        return validMail;
    }
}


