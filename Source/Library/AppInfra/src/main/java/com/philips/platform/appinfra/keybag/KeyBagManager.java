/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                JSONArray jsonArray = keyBagModel.getJSONArray("appinfra.languagePack2");
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String clientId = keyBagHelper.getSeed("appinfra.languagePack", "ClientId", 0);
                String clientId_obfuscate = obfuscate(keyBagHelper.getHexStringToString(jsonObject.get("ClientId").toString()), 0XACE1);
                Log.d("Client_id ----", clientId_obfuscate);
                Log.d("secret_key ----", obfuscate(clientId_obfuscate, 0XACE1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
