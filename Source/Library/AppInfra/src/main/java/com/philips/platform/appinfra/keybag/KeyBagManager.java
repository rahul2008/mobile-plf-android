/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private AppInfra mAppInfra;

    public KeyBagManager(AppInfra ai) {
        mAppInfra = ai;
    }

    @Override
    public String obfuscate(final String data, final int seed) {
        return keyBagHelper.obfuscate(data, seed);
    }

    @Override
    public void init() throws FileNotFoundException {
        keyBagHelper = new KeyBagHelper();
        keyBagHelper.init(mAppInfra.getAppInfraContext());
    }

    @Override
    public ArrayList<HashMap> getMapForServiceId(String serviceId, URL url) {
        ArrayList<HashMap> hashMaps = new ArrayList<>();
        Object propertiesForKey = keyBagHelper.getPropertiesForKey(serviceId);

        if (propertiesForKey instanceof JSONArray) {
            addToHashMapArray((JSONArray) propertiesForKey, hashMaps);
        } else if (propertiesForKey instanceof JSONObject) {
            addToHashMapData((JSONObject) propertiesForKey, hashMaps, 0);
        }
        return hashMaps;
    }

    private void addToHashMapArray(JSONArray jsonArray, ArrayList<HashMap> hashMapData) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);
                if (value instanceof JSONObject) {
                    addToHashMapData((JSONObject) value, hashMapData, i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addToHashMapData(JSONObject jsonObject, ArrayList<HashMap> hashMaps, int index) {
        try {
            Iterator<String> keys = jsonObject.keys();
            HashMap<String, String> hashMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) jsonObject.get(key);
                String seed = keyBagHelper.getSeed("appinfra.localtesting", key, index);
                hashMap.put(key, obfuscate(keyBagHelper.convertHexDataToString(value), Integer.parseInt(seed, 16)));
            }
            hashMaps.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
