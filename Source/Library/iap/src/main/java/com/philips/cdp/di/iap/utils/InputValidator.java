package com.philips.cdp.di.iap.utils;

import com.philips.platform.uid.view.widget.InputValidationLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidator implements InputValidationLayout.Validator {

    private final Pattern valid_regex_pattern;
    public InputValidator(Pattern valid_regex_pattern) {
        this.valid_regex_pattern = valid_regex_pattern;
    }

    @Override
    public boolean validate(CharSequence msg) {
        if(msg.toString().trim().equalsIgnoreCase("")){
            return false;
        }
        Matcher matcher = valid_regex_pattern.matcher(msg);
        return matcher.find();
    }
}