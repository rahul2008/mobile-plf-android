/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;


import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class KeyBagHelper {

    String getMd5ValueInHex(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte byteArray[] = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte byteData : byteArray) {
                String hex = Integer.toHexString(0xff & byteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getSeed(String groupId, String key, int index) {
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(key)) {
            String concatData = groupId.trim().concat(String.valueOf(index).concat(key.trim()));
            String md5Value = getMd5ValueInHex(concatData);
            if (md5Value != null && md5Value.length() > 4)
                return md5Value.substring(0, 4).toUpperCase();
        }
        return null;
    }

    String convertHexDataToString(String hex) {
        int l = hex.length();
        char[] data = new char[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (char) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(data);
    }

    Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();
        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private String data = "https://www.philips.com/";

    String getIndex(String indexData) {
        if (!TextUtils.isEmpty(indexData)) {
            String[] split = indexData.split(data);
            if (split.length > 1 && split[1] != null)
                return split[1];
        }
        return null;
    }
}
