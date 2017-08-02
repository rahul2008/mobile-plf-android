/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfra;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private AppInfra mAppInfra;

    public KeyBagManager(AppInfra appInfra) {
        mAppInfra = appInfra;
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
//        serviceId = getAppendedServiceId(serviceId);
        ArrayList<HashMap> hashMaps = new ArrayList<>();
        if (serviceId != null) {
            String keyBagHelperIndex = keyBagHelper.getIndex(url.toString());
            int index = Integer.parseInt(keyBagHelperIndex);
            Object propertiesForKey = keyBagHelper.getPropertiesForKey(serviceId);

            if (propertiesForKey instanceof JSONArray) {
                keyBagHelper.addToHashMapArray((JSONArray) propertiesForKey, hashMaps, serviceId);
            } else if (propertiesForKey instanceof JSONObject) {
                keyBagHelper.addToHashMapData((JSONObject) propertiesForKey, hashMaps, index, serviceId);
            }
        }
        return hashMaps;
    }

    private String getAppendedServiceId(String serviceId) {
        if (!TextUtils.isEmpty(serviceId))
            return serviceId.concat(".kindex");

        return null;
    }

}
