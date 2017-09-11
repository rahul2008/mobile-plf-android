/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.*;

import com.janrain.android.*;
import com.philips.platform.appinfra.securestorage.*;

import java.io.*;
import java.util.*;

import static com.philips.cdp.registration.settings.RegistrationSettings.*;


public class RegPreferenceUtility {

    public static void storePreference(Context context, String key, boolean value) {
        Jump.getSecureStorageInterface().storeValueForKey(key, String.valueOf(value), new SecureStorageInterface.SecureStorageError());
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
        myPrefs.edit().clear().commit();
    }

    private static boolean isFileExists(String fileName) {
        return new File(fileName).exists();
    }
}
