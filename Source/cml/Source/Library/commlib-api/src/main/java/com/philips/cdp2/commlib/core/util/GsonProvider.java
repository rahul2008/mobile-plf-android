/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider {
    private final static Gson INSTANCE = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    public static Gson get() {
        return INSTANCE;
    }

    public static final String EMPTY_JSON_OBJECT_STRING = get().toJson(new Object());
}
