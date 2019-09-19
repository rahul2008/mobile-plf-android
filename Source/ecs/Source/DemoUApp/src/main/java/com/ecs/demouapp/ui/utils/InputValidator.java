package com.ecs.demouapp.ui.utils;


import com.ecs.demouapp.ui.address.Validator;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidator extends Validator implements InputValidationLayout.Validator {

    private final Pattern valid_regex_pattern;

    public InputValidator(Pattern valid_regex_pattern) {
        this.valid_regex_pattern = valid_regex_pattern;
    }

    @Override
    public boolean isValidName(String stringToBeValidated) {
        super.isValidName(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidEmail(String stringToBeValidated) {
        super.isValidEmail(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidPostalCode(String stringToBeValidated) {
        super.isValidPostalCode(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidPhoneNumber(String stringToBeValidated) {
        super.isValidPhoneNumber(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidAddress(String stringToBeValidated) {
        super.isValidAddress(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidTown(String stringToBeValidated) {
        super.isValidTown(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean isValidCountry(String stringToBeValidated) {
        super.isValidCountry(stringToBeValidated);
        return validate(stringToBeValidated);
    }

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = valid_regex_pattern.matcher(msg);
        return matcher.find();
    }

}