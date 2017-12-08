/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * This interface allows to inject a SharedPreferences objects into the BlueLib specifying the way to store information.
 * Make sure that there are no long blocking calls done during storing or retrieving of data. For instance, opening a
 * connection to a back end server is considered a long blocking call. For best performance use local storage or local cashing
 * in case of remote storage.
 *
 * @publicApi
 */
public interface SharedPreferencesProvider {
    /**
     * see {@link android.content.Context#getSharedPreferences}
     */
    @NonNull
    SharedPreferences getSharedPreferences(String name, int mode);

    /**
     * The shared preferences prefix is added to the key for each request to getSharedPreferences.
     * It is up to the implementation of this interface to make sure that each SharedPreferenceProvider
     * returns a unique non null and non empty string.
     * @return a unique non null non empty string.
     */
    @NonNull
    String getSharedPreferencesPrefix();
}
