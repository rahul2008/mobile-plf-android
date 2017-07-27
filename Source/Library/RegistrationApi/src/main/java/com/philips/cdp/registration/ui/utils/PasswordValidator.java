package com.philips.cdp.registration.ui.utils;

import com.philips.platform.uid.view.widget.InputValidationLayout;


public class PasswordValidator implements InputValidationLayout.Validator {


    ValidPassword passwordStrength;


    public PasswordValidator(ValidPassword passwordStrength) {
        this.passwordStrength = passwordStrength;
    }

    @Override
    public boolean validate(CharSequence str) {
        String password = str.toString();
        int passwordValidatorCheckCount = 0;

        if (password.length() > 0) {
            passwordValidatorCheckCount++;
        }

        if (!isPasswordLengthMeets(password)) {
            passwordStrength.getStrength(passwordValidatorCheckCount);
            return false;
        }

        if (FieldsValidator.isAlphabetPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (FieldsValidator.isNumberPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (FieldsValidator.isSymbolsPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (passwordValidatorCheckCount > 2) {
            passwordStrength.getStrength(passwordValidatorCheckCount);
            return true;
        }
        passwordStrength.getStrength(passwordValidatorCheckCount);
        return false;
    }

    public static boolean isPasswordLengthMeets(String string) {
        if (string == null) {
            return false;
        }

        int length = string.length();
        if (length == 0) {
            return false;
        }

        if (length < 8 || length > 32) {
            return false;
        }

        return true;
    }


}