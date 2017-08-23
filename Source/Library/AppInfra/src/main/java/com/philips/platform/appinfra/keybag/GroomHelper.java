/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;


import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class GroomHelper {

    private final KeyBagLib keyBagLib;
    private JSONObject rootJsonObject;
    private AppInfra mAppInfra;

    GroomHelper(AppInfra appInfra) {
        keyBagLib = new KeyBagLib();
        this.mAppInfra = appInfra;
    }

    boolean init(AppInfra mAppInfra, InputStream mInputStream) throws KeyBagJsonFileNotFoundException {
        this.mAppInfra = mAppInfra;
        StringBuilder total;
        try {
            final BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            rootJsonObject = new JSONObject(total.toString());
            return true;
        } catch (JSONException | IOException e) {
            if (e instanceof IOException)
                throw new KeyBagJsonFileNotFoundException();
            else
                e.printStackTrace();
        }
        return false;
    }

    String getAilGroomInHex(String data) {
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

    String getValue(String groupId, int index, String key) {
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(key)) {
            String concatData = groupId.trim().concat(String.valueOf(index).concat(key.trim()));
            String md5Value = getAilGroomInHex(concatData);
            if (md5Value != null && md5Value.length() > 4)
                return md5Value.substring(0, 4).toUpperCase();
        }
        return null;
    }

    String convertData(String hex) {
        int l = hex.length();
        char[] data = new char[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (char) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(data);
    }

    String getGroomIndex(String value) {
        String data = "https://www.philips.com/";
        if (!TextUtils.isEmpty(value)) {
            StringTokenizer st = new StringTokenizer(value, data);
            if (st.hasMoreTokens())
                return st.nextToken();
        }
        return null;
    }

    String ailGroom(String data, int seed) {
        char[] chars = keyBagLib.ailGroom(data.toCharArray(), seed);
        if (chars != null && chars.length > 0) {
            return new String(chars);
        }
        return null;
    }

    void mapResponse(Map<String, ServiceDiscoveryService> keyBagUrlMap, List<AIKMService> aiKmServices, Map<String, ServiceDiscoveryService> serviceDiscoveryUrlMap) {
        for (Map.Entry<String, ServiceDiscoveryService> entry : serviceDiscoveryUrlMap.entrySet()) {
            String key = entry.getKey();
            ServiceDiscoveryService value = entry.getValue();
            AIKMService aikmService = new AIKMService();
            aikmService.setServiceId(entry.getKey());
            aikmService.init(value.getLocale(), value.getConfigUrls());
            aikmService.setmError(value.getmError());

            ServiceDiscoveryService keyBagValue = keyBagUrlMap.get(key.concat(".kindex"));
            if (keyBagValue != null) {
                String keyBagHelperIndex = getGroomIndex(keyBagValue.getConfigUrls());
                mapAndValidateGroom(aikmService, entry.getKey(), keyBagHelperIndex);
            }
            aiKmServices.add(aikmService);
        }
    }

    void mapAndValidateGroom(AIKMService aikmService, String serviceId, String keyBagHelperIndex) {
        if (serviceId != null && !TextUtils.isEmpty(keyBagHelperIndex)) {
            int index;
            try {
                index = Integer.parseInt(keyBagHelperIndex);
            } catch (NumberFormatException e) {
                aikmService.setKeyBagError(AIKMService.KEY_BAG_ERROR.INVALID_INDEX_URL);
                e.printStackTrace();
                return;
            }
            Object propertiesForKey = getAilGroomProperties(serviceId);
            if (propertiesForKey instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) propertiesForKey;
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                    Map keyBag = mapData(jsonObject, index, serviceId);
                    aikmService.setKeyBag(keyBag);
                } catch (JSONException e) {
                    aikmService.setKeyBagError(AIKMService.KEY_BAG_ERROR.INDEX_NOT_MAPPED);
                    e.printStackTrace();
                }
            } else {
                aikmService.setKeyBagError(AIKMService.KEY_BAG_ERROR.INVALID_JSON_STRUCTURE);
            }
        } else {
            aikmService.setKeyBagError(AIKMService.KEY_BAG_ERROR.SERVICE_DISCOVERY_RESPONSE_ERROR);
        }
    }

    Map mapData(JSONObject jsonObject, int index, String serviceId) {
        try {
            Iterator<String> keys = jsonObject.keys();
            HashMap<String, String> hashMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) jsonObject.get(key);
                String initialValue = getValue(serviceId, index, key);
                hashMap.put(key, ailGroom(convertData(value), Integer.parseInt(initialValue, 16)));
            }
            return hashMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    Object getAilGroomProperties(String serviceId) {
        try {
            return rootJsonObject.get(serviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<String> getAppendedGrooms(List<String> serviceIds) {
        ArrayList<String> appendedServiceIds = new ArrayList<>();
        for (String serviceId : serviceIds) {
            if (!TextUtils.isEmpty(serviceId))
                appendedServiceIds.add(serviceId.concat(".kindex"));
        }
        return appendedServiceIds;
    }

    void getServiceDiscoveryUrlMap(ArrayList<String> serviceIds, AISDResponse.AISDPreference aiSdPreference,
                                   Map<String, String> replacement,
                                   ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener) {
        if (replacement != null) {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                mAppInfra.getServiceDiscovery().getServicesWithCountryPreference(serviceIds, serviceUrlMapListener, replacement);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                mAppInfra.getServiceDiscovery().getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener, replacement);
        } else {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                mAppInfra.getServiceDiscovery().getServicesWithCountryPreference(serviceIds, serviceUrlMapListener);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                mAppInfra.getServiceDiscovery().getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener);
        }
    }

}
