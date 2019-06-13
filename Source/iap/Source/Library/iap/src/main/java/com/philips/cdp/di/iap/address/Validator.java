/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

   /* private static final String NAME = "^[a-zA-Z ]{1,17}$";
    private static final String ADDRESS = "^[a-zA-Z0-9.',\\-_\\s]{1,35}$";
    private static final String TOWN = "^[a-zA-Z\\s]{1,35}$";
    private static final String POSTAL_CODE = "^[A-Za-z0-9 ]{1,10}$";
    private static final String COUNTRY = "^[A-Z]{2,2}$";*/

    private static final String NAME = "^(?!\\s*$).+";
    private static final String ADDRESS = "^(?!\\s*$).+";
    private static final String TOWN = "^(?!\\s*$).+";
    private static final String POSTAL_CODE = "^(?!\\s*$).+";
    private static final String COUNTRY = "^(?!\\s*$).+";

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

    public static Pattern NAME_PATTERN;
    public static Pattern ADDRESS_PATTERN;
    public static Pattern TOWN_PATTERN;
    public static Pattern POSTAL_CODE_PATTERN;
    public static Pattern COUNTRY_PATTERN;
    public static Pattern PHONE_NUMBER_PATTERN;
    public static Pattern EMAIL_PATTERN;

    static {
        NAME_PATTERN = Pattern.compile(NAME);
        EMAIL_PATTERN = Pattern.compile(EMAIL);
        ADDRESS_PATTERN = Pattern.compile(ADDRESS);
        POSTAL_CODE_PATTERN = Pattern.compile(POSTAL_CODE);
        PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER);
        TOWN_PATTERN = Pattern.compile(TOWN);
        COUNTRY_PATTERN = Pattern.compile(COUNTRY);
    }

    private boolean isValidString(String stringToBeValidated) {
        return stringToBeValidated != null && !stringToBeValidated.trim().equalsIgnoreCase("");
    }

    public boolean isValidName(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = NAME_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidEmail(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = EMAIL_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidPostalCode(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = POSTAL_CODE_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidPhoneNumber(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = PHONE_NUMBER_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidAddress(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = ADDRESS_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidTown(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = TOWN_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public boolean isValidCountry(String stringToBeValidated) {
        if (isValidString(stringToBeValidated)) {
            Matcher matcher = COUNTRY_PATTERN.matcher(stringToBeValidated);
            return matcher.matches();
        } else {
            return false;
        }
    }
}
