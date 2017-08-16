/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
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

class KeyBagHelper {

    private final KeyBagLib keyBagLib;
    private JSONObject rootJsonObject;
    private AppInfra mAppInfra;

    KeyBagHelper(AppInfra mAppInfra) {
        keyBagLib = new KeyBagLib();
        this.mAppInfra = mAppInfra;
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

    void init(Context mContext) throws KeyBagJsonFileNotFoundException {
        initKeyBagProperties(mContext);
    }

    private void mapDeObfuscatedValue(Map<String, ServiceDiscoveryService> urlMap, ArrayList<AIKMService> aikmServices) {
        int i = 0;
        for (Object object : urlMap.entrySet()) {
            Map.Entry pair = (Map.Entry) object;
            ServiceDiscoveryService value = (ServiceDiscoveryService) pair.getValue();
            AIKMService aikmService = aikmServices.get(i);
            String serviceId = aikmService.getServiceId();
            String keyBagHelperIndex = getKeyBagIndex(value.getConfigUrls());
            mapAndValidateKey(value, aikmService, serviceId, keyBagHelperIndex);
            i = i + 1;
        }
    }

    private void mapAndValidateKey(ServiceDiscoveryService value, AIKMService aikmService, String serviceId, String keyBagHelperIndex) {
        if (serviceId != null && !TextUtils.isEmpty(keyBagHelperIndex)) {
            int index = Integer.parseInt(keyBagHelperIndex);
            Object propertiesForKey = getPropertiesForKey(serviceId);
            if (propertiesForKey instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) propertiesForKey;
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                    aikmService.setKeyBag(mapData(jsonObject, index, serviceId));
                } catch (JSONException e) {
                    aikmService.setmError("Key bag index not matched");
                    e.printStackTrace();
                }
            } else {
                aikmService.setmError("Error in Json Syntax");
            }
        } else {
            aikmService.setmError(value.getmError());
        }
    }

    private void initKeyBagProperties(Context mContext) throws KeyBagJsonFileNotFoundException {
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
                throw new KeyBagJsonFileNotFoundException();
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

    ArrayList<String> getAppendedServiceIds(List<String> serviceIds) {
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


    private void mapServiceDiscoveryResponse(Map<String, ServiceDiscoveryService> urlMap, ArrayList<AIKMService> aiKmServices) {
        for (Object object : urlMap.entrySet()) {
            Map.Entry pair = (Map.Entry) object;
            ServiceDiscoveryService value = (ServiceDiscoveryService) pair.getValue();
            AIKMService aikmService = new AIKMService();
            aikmService.setServiceId((String) pair.getKey());
            aikmService.init(value.getLocale(), value.getConfigUrls());
            aikmService.setmError(value.getmError());
            aiKmServices.add(aikmService);
        }
    }

    @NonNull
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener getKeyBagIndexListener(final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener, final ArrayList<AIKMService> aikmServices) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mapDeObfuscatedValue(urlMap, aikmServices);
                onGetKeyBagMapListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener getServiceUrlMapListener(final ArrayList<String> serviceIds, final ArrayList<AIKMService> aiKmServices,
                                                                                          final AISDResponse.AISDPreference aiSdPreference,
                                                                                          final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mapServiceDiscoveryResponse(urlMap, aiKmServices);
                getServiceDiscoveryUrlMap(getAppendedServiceIds(serviceIds), aiSdPreference, null, getKeyBagIndexListener(onGetKeyBagMapListener, aiKmServices));
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }
}
