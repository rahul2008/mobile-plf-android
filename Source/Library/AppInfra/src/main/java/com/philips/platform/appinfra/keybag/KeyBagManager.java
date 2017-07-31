/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper = new KeyBagHelper();

    @Override
    public String obfuscate(final String data, final int seed) {
        return keyBagHelper.obfuscate(data,seed);
    }

    @Override
    public void init(String rawData) {
        try {
            if (!TextUtils.isEmpty(rawData)) {
                JSONObject keyBagModel = new JSONObject(rawData);
                Map<String, Object> map = keyBagHelper.jsonToMap(keyBagModel);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    ArrayList arrayList = (ArrayList) entry.getValue();
                    keyBagHelper.iterateArray(arrayList, key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, String> getMapForServiceId(String serviceId,URL url) {
        String seed = keyBagHelper.getSeed(serviceId, "", Integer.parseInt(keyBagHelper.getIndex(url.toString())));
        String deObfuscatedData = obfuscate("",Integer.parseInt(seed));
        return null;
    }
}
