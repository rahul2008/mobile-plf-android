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
                Map<String, Object> map = keyBagHelper.jsonToMap(keyBagModel);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    ArrayList arrayList = (ArrayList) entry.getValue();
                    iterateArray(arrayList, key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void iterateArray(ArrayList arrayList, String groupId) {
        for (int index = 0; index < arrayList.size(); index++) {
            iterateJson(arrayList.get(index).toString(), groupId, index);
        }
    }

    private void iterateJson(String jsonData, String groupId, int index) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) jsonObject.get(key);
                String seed = keyBagHelper.getSeed(groupId, key, index);
                String deObfuscatedData = obfuscate(keyBagHelper.convertHexDataToString(value), Integer.parseInt(seed, 16));
                Log.d("Testing deObfuscation -", "for key-" + key + "=" + deObfuscatedData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
