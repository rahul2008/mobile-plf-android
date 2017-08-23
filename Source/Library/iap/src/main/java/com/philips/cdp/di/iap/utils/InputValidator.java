package com.philips.cdp.di.iap.utils;

import com.philips.platform.uid.view.widget.InputValidationLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidator implements InputValidationLayout.Validator {

    private final InputValidationLayout inputValidationLayout;
    private final String errorMessage;
    private final Pattern valid_regex_pattern;

    public InputValidator(InputValidationLayout inputValidationLayout, String errorMessage, Pattern valid_regex_pattern) {
        this.inputValidationLayout = inputValidationLayout;
        this.errorMessage = errorMessage;
        this.valid_regex_pattern = valid_regex_pattern;
    }

    @Override
    public boolean validate(CharSequence msg) {
        Matcher matcher = valid_regex_pattern.matcher(msg);
        if(!matcher.find()){
            inputValidationLayout.setErrorMessage(errorMessage);
        }
        return matcher.find();
    }
}