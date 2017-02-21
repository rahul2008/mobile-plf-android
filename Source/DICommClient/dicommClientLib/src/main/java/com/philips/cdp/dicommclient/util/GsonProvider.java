/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */
package com.philips.cdp.dicommclient.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider {
    private final static Gson INSTANCE = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    public static final Gson get() {
        return INSTANCE;
    }

    public static final String EMPTY_JSON_OBJECT_STRING = get().toJson(new Object());
}
