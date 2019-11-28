
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import com.google.i18n.phonenumbers.*;

import java.util.regex.*;

public class FieldsValidator {

    private static Phonenumber.PhoneNumber numberProto;
    private static final String TAG = "RegistrationSettingsURL";


    public static boolean isValidName(String email) {
        if (email == null)
            return false;
        if (email.length() == 0)
            return false;
        if (email.length() != email.trim().length())
            return false;
        if (email.contains(" "))
            return false;

        email = email.toLowerCase();
        String emailPattern = "^(?!.*(?i)(\\p{C}|<|>|\\.com|\\.co\\.|\\.do|\\.ru|\\.it|\\.de|\\.at|\\.ch|\\.nl|\\.be|\\.fr|\\.org|\\.to|\\.do|:\\/\\/|www\\.)(?-i)).*$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
        
        email = email.toLowerCase();
        String emailPattern = "^[A-Za-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?$";
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
    public static boolean isAlphabetUpperCasePresent(String string) {
        if (string == null)
            return false;

        if (string.length() == 0)
            return false;

        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(string);

        return matcher.find();
    }
    public static boolean isAlphabetLowerCasePresent(String string) {
        if (string == null)
            return false;

        if (string.length() == 0)
            return false;

        Pattern pattern = Pattern.compile("[a-z]");
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
        if(mobile == null){
            return false;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (mobile.length() > 0) {
            if (android.util.Patterns.PHONE.matcher(mobile).matches()) {

                try {
                    // You can find your country code here
                    numberProto = phoneUtil.parse(mobile, "CN");
                    RLog.d(TAG,"isValidMobileNumber " + phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
                } catch (NumberParseException e) {
                    RLog.d(TAG,"isValidMobileNumber Exception NumberParseException " +e.getMessage());
                    return false;
                }

                return phoneUtil.isValidNumber(numberProto);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getMobileNumber(String mobileNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (mobileNumber.length() > 0) {
            if (android.util.Patterns.PHONE.matcher(mobileNumber).matches()) {
                try {
                    numberProto = phoneUtil.parse(mobileNumber, "CN");
                    mobileNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                    mobileNumber = mobileNumber.replace("+","");
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
                RLog.d(TAG,"Validated MobileNumber"+ mobileNumber);
            }
        }
        return mobileNumber;
    }

    public static String getVerifiedMobileNumber(String uuid, String sms) {
        String uuid_a = uuid.replaceAll("[0|i|o|l|1|-]", "");
        String resultStr = sms;
        while (resultStr.length() < 32) {
            resultStr += uuid_a;
        }
        resultStr = resultStr.substring(0, Math.min(resultStr.length(), 32));
        return resultStr;
    }

}
