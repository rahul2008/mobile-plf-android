package com.philips.pins.shinelib.dicommsupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

final class GsonProvider {

    private final static Gson INSTANCE = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    static Gson getGson() {
        return INSTANCE;
    }
}
