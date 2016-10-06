
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsValidator {

    public static boolean isValidName(String name) {
        if (name == null)
            return false;
        if (name.length() > 0)
            return true;

        return false;
    }

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        if (email.length() == 0)
            return false;
        if (email.length() != email.trim().length())
            return false;
        if (email.contains(" "))
            return false;

        String emailPattern = "^(?!.\\-\\_{2,}.)(?!.*?[._-]{2})[a-zA-Z0-9][a-zA-Z0-9._%+-]{0,61}[^`~,.<>;':\"\\/\\[\\]\\|{}()=" +
                "_+\\?*&\\^%$#@!\\\\-]@((?!.\\-\\_{2,}.)(?!.*?[._-]{2})[^`~,.<>;':\"\\/\\[\\]\\|{}()=_+\\?*&\\^%$#@!\\\\-][-a-zA-Z0-9_.]+" +
                "[^`~,.<>;':\"\\/\\[\\]\\|{}()=_+\\?*&\\^%$#@!\\\\-]{0,}\\.[a-zA-z]+(?!.*?[0-9])[^`~,.<>;':\"\\/\\[\\]\\|{}()=_+" +
                "\\?*&\\^%$#@!\\\\-])$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;

        if (!isPasswordLengthMeets(password)) {
            return false;
        }


        int passwordValidatorCheckCount = 0;

        if (FieldsValidator.isAlphabetPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (FieldsValidator.isNumberPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (passwordValidatorCheckCount == 2) {
            return true;
        }

        if (FieldsValidator.isSymbolsPresent(password)) {
            passwordValidatorCheckCount++;
        }

        if (passwordValidatorCheckCount >= 2) {
            return true;
        }

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

    public static boolean isAlphabetPresent(String string) {
        if (string == null)
            return false;

        if (string.length() == 0)
            return false;

        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(string);

        return matcher.find();
    }

    public static boolean isNumberPresent(String string) {
        if (string == null)
            return false;

        if (string.length() == 0)
            return false;

        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public static boolean isSymbolsPresent(String string) {
        if (string == null)
            return false;

        if (string.length() == 0)
            return false;

        Pattern pattern = Pattern.compile("[_.@$]");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public static boolean isValidSerialNo(String serialNo) {
        if (serialNo == null)
            return false;

        if (serialNo.length() < 14)
            return false;

        Pattern pattern = Pattern.compile("[a-zA-Z]{2}[\\d]{12}");
        Matcher matcher = pattern.matcher(serialNo);
        return matcher.matches();
    }

    public static boolean isValidMobileNumber(String mobile) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phNumberProto = null;
        if (mobile.length() > 0) {
            if (android.util.Patterns.PHONE.matcher(mobile).matches()) {

                try {
                    // You can find your country code here
                    phNumberProto = phoneUtil.parse(mobile, "US"); //replace Iso code here  check with sim
                } catch (NumberParseException e) {
                    // ineed to rlog

                }
                return phoneUtil.isValidNumber(phNumberProto);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
