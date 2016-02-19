package com.philips.cdp.di.iap.address;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Validator {

    public boolean isValidEmail(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
         else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(stringToBeValidated).matches();
        }
    }

    public boolean isValidPostalCode(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
         else {
            Pattern postalCodePattern = Pattern.compile("^[A-Z0-9]*$");
            Matcher match = postalCodePattern.matcher(stringToBeValidated);
            return match.matches();
        }
    }

    public boolean isValidPhoneNumber(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        } else {
            return PhoneNumberUtils.isGlobalPhoneNumber(stringToBeValidated.toString());
        }
    }

    public boolean isValidFirstName(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        } else {
            Pattern firstNamePattern = Pattern.compile("^[\\p{IsAlphabetic}]+( [\\p{IsAlphabetic}]+)*$");
            Matcher match = firstNamePattern.matcher(stringToBeValidated);
            return match.matches();
        }
    }

    public boolean isValidLastName(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        return true;
    }

    public boolean isValidAddress(View editText) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        return true;
    }

    public boolean isValidTown(View editText){
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        return true;
    }

    public boolean isValidCountry(View editText){
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        return true;
    }


}
