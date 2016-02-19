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

    /**
     * For checking the Town/City Address validity
     */
    private static final String TOWN_PATTERN =
            "^[a-zA-Z\\s]{1,35}$";

    private static final String FIRST_NAME = "^[a-zA-Z]{1,17}$";
    private static final String LAST_NAME = "^[a-zA-Z]{1,17}$";
    private static final String ADDRESS = "^[a-zA-Z0-9(.,#/\\-)_\\s]{1,35}$";
    private static final String POSTAL_CODE = "^[A-Z0-9]{1,10}$";
    private static final String COUNTRY = "^[A-Z]{2,2}$";


    private boolean isValidString(CharSequence stringToBeValidated){
        if (TextUtils.isEmpty(stringToBeValidated)) {
            return false;
        }
        return true;
    }
    public boolean isValidEmail(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }
         else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(stringToBeValidated).matches();
        }
    }

    public boolean isValidPostalCode(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }
         else {
            Pattern pattern = Pattern.compile(POSTAL_CODE);
            Matcher matcher = pattern.matcher(stringToBeValidated);
            boolean matches = matcher.matches();
            return matches;
        }
    }

    public boolean isValidPhoneNumber(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        } else {
            return PhoneNumberUtils.isGlobalPhoneNumber(stringToBeValidated.toString());
        }
    }

    public boolean isValidFirstName(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(FIRST_NAME);
            Matcher matcher = pattern.matcher(stringToBeValidated);
            boolean matches = matcher.matches();
            return matches;
        }
    }

    public boolean isValidLastName(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }
        Pattern pattern = Pattern.compile(LAST_NAME);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean isValidAddress(View editText) {
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }
        Pattern pattern = Pattern.compile(ADDRESS);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean isValidTown(View editText){
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }

        Pattern pattern = Pattern.compile(TOWN_PATTERN);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean isValidCountry(View editText){
        CharSequence stringToBeValidated = ((EditText) editText).getText();
        if (!isValidString(stringToBeValidated)) {
            return false;
        }
        Pattern pattern = Pattern.compile(COUNTRY);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();
        return matches;
    }
}
