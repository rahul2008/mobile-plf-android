/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.util;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import javax.inject.Inject;

public final class SecureStorageUtility {

    public static final String CPP_ID = "cppID";
    public static final String APPLIANCE_PIN = "appliancePin";

    private static final SecureStorageInterface.SecureStorageError ANY_ERRORR =
            new SecureStorageInterface.SecureStorageError();

    @NonNull
    private final SecureStorageInterface secureStorage;

    @Inject
    public SecureStorageUtility(@NonNull SecureStorageInterface secureStorage) {
        this.secureStorage = secureStorage;
    }

    public void storeString(@NonNull String key, String value) {
        secureStorage.storeValueForKey(key, value, ANY_ERRORR);
    }

    public String loadString(@NonNull String key, String defaultValue){
        String loadedString = secureStorage.fetchValueForKey(key, ANY_ERRORR);
        if (loadedString == null) {
            return defaultValue;
        }
        return loadedString;
    }

}
