/*
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.philips.cdp.dicommclient.networknode.WiFiNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WiFiNetworkPortResponseDeserializer implements JsonDeserializer<List<WiFiNode>> {

    private static final String INVALID_SSID_PREFIX = "\u0000";
    private static final String NULL_SSID = "null";

    @NonNull
    private final Gson gson;

    WiFiNetworkPortResponseDeserializer() {
        this.gson = new Gson();
    }

    @Override
    public List<WiFiNode> deserialize(final JsonElement jsonElement, final Type typeOfT,
                                      final JsonDeserializationContext context) throws JsonParseException {
        final JsonArray jsonArray = jsonElement.getAsJsonArray();
        final List<WiFiNode> list = new ArrayList<>(jsonArray.size());

        for (JsonElement element : jsonArray) {
            if (!(element instanceof JsonNull)) {
                WiFiNode wiFiNode = gson.fromJson(element, WiFiNode.class);

                if (!TextUtils.isEmpty(wiFiNode.SSID)
                        && !wiFiNode.SSID.equals(NULL_SSID)
                        && !wiFiNode.SSID.startsWith(INVALID_SSID_PREFIX)) {
                    list.add(wiFiNode);
                }
            }
        }
        return list;
    }
}