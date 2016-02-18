package com.philips.cdp.di.iap.address;

import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Validator {

    public boolean validateEmail(View editText) {
        String EMAIL_PATTERN =
                "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";

        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();

        return matches;
    }

    public boolean validatePhoneNumber(View editText){
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }

        return true;
    }
}
