package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.settings.RegistrationHelper;

import static com.philips.platform.uid.view.widget.InputValidationLayout.Validator;


public class LoginIdValidator implements Validator {

    ValidLoginId validLoginId;

    public LoginIdValidator(ValidLoginId validLoginId) {
        this.validLoginId = validLoginId;
    }

    @Override
    public boolean validate(CharSequence msg) {
        boolean validMail = FieldsValidator.isValidEmail(msg.toString());

        if (!validMail && RegistrationHelper.getInstance().isMobileFlow()) {
           validMail = FieldsValidator.isValidMobileNumber(msg.toString());
        }else{
            validMail = FieldsValidator.isValidEmail(msg.toString());
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


