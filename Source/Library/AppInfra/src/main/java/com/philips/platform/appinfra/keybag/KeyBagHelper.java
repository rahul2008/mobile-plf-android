/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.philips.platform.appinfra.AppInfraLogEventID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

class KeyBagHelper {

    private final KeyBagLib keyBagLib;
    private JSONObject rootJsonObject;

    KeyBagHelper() {
        keyBagLib = new KeyBagLib();
    }

    String getMd5ValueInHex(String data) {
        if (!TextUtils.isEmpty(data)) {
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
        }
        return null;
    }

    String getSeed(String groupId, int index, String key) {
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

    String getKeyBagIndex(String value) {
        String data = "https://www.philips.com/";
        if (!TextUtils.isEmpty(value)) {
            StringTokenizer st = new StringTokenizer(value, data);
            if (st.hasMoreTokens())
                return st.nextToken();
        }
        return null;
    }

    String obfuscate(String data, int seed) {
        char[] chars = keyBagLib.obfuscateDeObfuscate(data.toCharArray(), seed);
        if (chars != null && chars.length > 0) {
            return new String(chars);
        }
        return null;
    }

    void init(Context mContext) throws FileNotFoundException {
        initKeyBagProperties(mContext);
    }

    String getAppendedServiceId(String serviceId) {
        if (!TextUtils.isEmpty(serviceId))
            return serviceId.concat(".kindex");

        return null;
    }


    Map getDeObfuscatedMap(String serviceId, String url) {
        if (serviceId != null) {
            String keyBagHelperIndex = getKeyBagIndex(url);
            int index = Integer.parseInt(keyBagHelperIndex);
            Object propertiesForKey = getPropertiesForKey(serviceId);
            if (propertiesForKey instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) propertiesForKey;
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                    return mapData(jsonObject, index, serviceId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private void initKeyBagProperties(Context mContext) throws FileNotFoundException {
        Log.d(AppInfraLogEventID.AI_KEY_BAG, "Reading keybag Config from app");
        StringBuilder total;
        try {
            final InputStream mInputStream = mContext.getAssets().open("AIKeyBag.json");
            final BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            rootJsonObject = new JSONObject(total.toString());
        } catch (JSONException | IOException e) {
            if (e instanceof IOException)
                throw new FileNotFoundException();
            else
                e.printStackTrace();
        }
    }

    private Map mapData(JSONObject jsonObject, int index, String serviceId) {
        try {
            Iterator<String> keys = jsonObject.keys();
            HashMap<String, String> hashMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) jsonObject.get(key);
                String seed = getSeed(serviceId, index, key);
                hashMap.put(key, obfuscate(convertHexDataToString(value), Integer.parseInt(seed, 16)));
            }
            return hashMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getPropertiesForKey(String serviceId) {
        try {
            return rootJsonObject.get(serviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
