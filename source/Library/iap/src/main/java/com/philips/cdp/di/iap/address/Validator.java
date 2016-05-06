/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String NAME = "^[a-zA-Z ]{1,17}$";
    private static final String ADDRESS = "^[a-zA-Z0-9(.',#/\\-)_\\s]{1,35}$";
    private static final String TOWN = "^[a-zA-Z\\s]{1,35}$";
    private static final String POSTAL_CODE = "^[A-Za-z0-9 ]{1,10}$";
    private static final String COUNTRY = "^[A-Z]{2,2}$";
    private static final String PHONE_NUMBER =
            "(\\+[0-9]+[\\- \\.]*)?"
                    + "(\\([0-9]+\\)[\\- \\.]*)?"
                    + "([0-9][0-9\\- \\.]+[0-9])";
    private static final String EMAIL =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+";

    private Pattern mNamePattern;
    private Pattern mAddressPattern;
    private Pattern mTownPattern;
    private Pattern mPostalCodePattern;
    private Pattern mCountryPattern;
    private Pattern mPhoneNumberPattern;
    private Pattern mEmailPattern;

    public Validator() {
        mNamePattern = Pattern.compile(NAME);
        mEmailPattern = Pattern.compile(EMAIL);
        mAddressPattern = Pattern.compile(ADDRESS);
        mPostalCodePattern = Pattern.compile(POSTAL_CODE);
        mPhoneNumberPattern = Pattern.compile(PHONE_NUMBER);
        mTownPattern = Pattern.compile(TOWN);
        mCountryPattern = Pattern.compile(COUNTRY);
    }

    private boolean isValidString(String stringToBeValidated) {
        return stringToBeValidated != null && !stringToBeValidated.trim().equalsIgnoreCase("");
    }

    public boolean isValidName(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mNamePattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidEmail(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mEmailPattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidPostalCode(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mPostalCodePattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidPhoneNumber(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mPhoneNumberPattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidAddress(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mAddressPattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidTown(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mTownPattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidCountry(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = mCountryPattern.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }
}
