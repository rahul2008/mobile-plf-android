/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyBagManager implements KeyBagInterface {


    private KeyBagLib keyBagLib = new KeyBagLib();
    private KeyBagHelper keyBagHelper = new KeyBagHelper();

    @Override
    public String obfuscate(final String data, final int seed) {

        char[] chars = keyBagLib.obfuscateDeObfuscate(data.toCharArray(), seed);
        if (chars != null && chars.length > 0) {
            return new String(chars);
        }
        return null;
    }

    @Override
    public void init(String rawData) {
        try {
            if (!TextUtils.isEmpty(rawData)) {
                JSONObject keyBagModel = new JSONObject(rawData);
                Iterator<String> keys = keyBagModel.keys();
                Map<String, Object> map = keyBagHelper.jsonToMap(keyBagModel);
                while (keys.hasNext()) {
                    String key = keys.next();
                    ArrayList arrayList = (ArrayList) map.get(key);
                    iterateArrayList(arrayList, key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void iterateArrayList(ArrayList arrayList, String groupId) {
        for (int i = 0; i < arrayList.size(); i++) {
            iterateHashMap((HashMap) arrayList.get(i), groupId, i);
        }
    }

    private void iterateHashMap(HashMap<String,Object> hashMap, String groupId, int index) {
        for (Map.Entry<String,Object> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            String seed = keyBagHelper.getSeed(groupId, key, index);
            String deObfuscatedData = obfuscate(keyBagHelper.convertHexDataToString(value), Integer.parseInt(seed, 16));
            Log.d("Testing deObfuscation -", "for key-" + key + "=" + deObfuscatedData);
        }
    }


}
