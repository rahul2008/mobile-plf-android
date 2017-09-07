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
import com.philips.cdp.registration.settings.*;
import com.philips.platform.appinfra.securestorage.*;

import java.util.*;


public class RegPreferenceUtility {

    public static void storePreference(Context context, String key, boolean value) {
        Jump.getSecureStorageInterface().storeValueForKey(key,String.valueOf(value),new SecureStorageInterface.SecureStorageError());
    }

    public static boolean getStoredState(Context context, String key) {
        migrate(context);
        return Boolean.parseBoolean(Jump.getSecureStorageInterface().fetchValueForKey(key,new SecureStorageInterface.SecureStorageError()));
    }

    private static void migrate(Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                 RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
        Map<String, ?> allEntries = myPrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Jump.getSecureStorageInterface().storeValueForKey(entry.getKey(),String.valueOf(entry.getValue()),new SecureStorageInterface.SecureStorageError());
        }
        myPrefs.edit().clear().commit();



            

    }


}
