/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.janrain.android.Jump;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.registration.settings.RegistrationSettings.REGISTRATION_API_PREFERENCE;


public class RegPreferenceUtility {

    public static void storePreference(Context context, String key, String value) {
        String oldVal = Jump.getSecureStorageInterface().fetchValueForKey(key, new SecureStorageInterface.SecureStorageError());
        List<String> oldValArray = stringToList(oldVal);
        oldValArray.add(value);
        String newVal = listToString(oldValArray);
        Jump.getSecureStorageInterface().storeValueForKey(key, newVal, new SecureStorageInterface.SecureStorageError());
    }

    public static boolean getPreferenceValue(Context context, String key, String value) {
        if (getStoredState(context, value)) {
            storePreference(context, key, value);
            deletePreference(value);
            return true;
        } else {
            String prefValue = Jump.getSecureStorageInterface().fetchValueForKey(key,
                    new SecureStorageInterface.SecureStorageError());

            if (prefValue == null) {
                return false;
            }

            prefValue = prefValue.replace("[", "");
            prefValue = prefValue.replace("]", "");

            List<String> valArray = stringToList(prefValue);
            for (int i = 0; i < valArray.size(); i++) {
                if (valArray.get(i).trim().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean getStoredState(Context context, String key) {
        String oldPrefeFile = context.getFilesDir().getParent() + "/shared_prefs/" +
                REGISTRATION_API_PREFERENCE + ".xml";
        String oldPrefeFileBackUp = context.getFilesDir().getParent() + "/shared_prefs/" +
                REGISTRATION_API_PREFERENCE + ".bak";
        if (isFileExists(oldPrefeFile)) {
            migrate(context);
            deleteFile(oldPrefeFile);
        }
        if (isFileExists(oldPrefeFileBackUp)) {
            deleteFile(oldPrefeFileBackUp);
        }
        return Boolean.parseBoolean(Jump.getSecureStorageInterface().
                fetchValueForKey(key, new SecureStorageInterface.SecureStorageError()));
    }

    private static void deleteFile(String fileName) {
        new File(fileName).delete();
    }

    private static void migrate(Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                REGISTRATION_API_PREFERENCE, 0);
        Map<String, ?> allEntries = myPrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Jump.getSecureStorageInterface().storeValueForKey(entry.getKey(),
                    String.valueOf(entry.getValue()),
                    new SecureStorageInterface.SecureStorageError());
        }
        myPrefs.edit().clear().apply();
    }

    private static boolean isFileExists(String fileName) {
        return new File(fileName).exists();
    }

    public static void deletePreference(String key) {
        Jump.getSecureStorageInterface().removeValueForKey(key);
    }

    private static String listToString(List<String> list) {
        return Arrays.toString(list.toArray());
    }

    private static List<String> stringToList(String args) {
        List<String> myList;
        if (args == null) {
            myList = new ArrayList<String>();
        } else {
            myList = new ArrayList<String>(Arrays.asList(args.split(",")));
        }
        return myList;
    }
}
