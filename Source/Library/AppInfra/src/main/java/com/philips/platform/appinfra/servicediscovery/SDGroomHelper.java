/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;


import android.content.Context;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.aikm.GroomLib;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SDGroomHelper {

    private final GroomLib groomLib;
    private JSONObject rootJsonObject;

    public SDGroomHelper() {
        groomLib = new GroomLib();
    }

    public void init(AppInfra mAppInfra) throws AIKMJsonFileNotFoundException, JSONException {
        if (null == rootJsonObject) {
            InputStream mInputStream = getInputStream(mAppInfra.getAppInfraContext(), "AIKMap.json");
            StringBuilder total;
            try {
                final BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
                total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                rootJsonObject = new JSONObject(total.toString());
            } catch (JSONException | IOException e) {
                if (e instanceof IOException)
                    throw new AIKMJsonFileNotFoundException();
                else
                    throw new JSONException(AIKMService.AIKMapError.INVALID_JSON.name());
            }
        }
    }

    InputStream getInputStream(Context mContext, String fileName) throws AIKMJsonFileNotFoundException {
        try {
            return mContext.getAssets().open(fileName);
        } catch (IOException e) {
            throw new AIKMJsonFileNotFoundException();
        }
    }

    String getAilGroomInHex(String data) throws NoSuchAlgorithmException {
        if (!TextUtils.isEmpty(data)) {
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
        }
        return null;
    }

    String getValue(String groupId, int index, String key) throws NoSuchAlgorithmException {
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(key)) {
            String concatData = groupId.trim().concat(String.valueOf(index).concat(key.trim()));
            String ailGroomInHex = getAilGroomInHex(concatData);
            if (ailGroomInHex != null && ailGroomInHex.length() > 4)
                return ailGroomInHex.substring(0, 4).toUpperCase();
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

    String getGroomIndexWithSplit(String value) {
        String data = "https://philips.com/";
        if (!TextUtils.isEmpty(value)) {
            String[] grooms = value.split(data);
            if (grooms.length > 1)
                return grooms[1];
        }
        return null;
    }

    String ailGroom(String data, int seed) {
        char[] chars = groomLib.ailGroom(data.toCharArray(), seed);
        if (chars != null && chars.length > 0) {
            return new String(chars);
        }
        return null;
    }

    public void validateGroom(String key, String configUrls, ServiceDiscoveryService serviceDiscoveryService) {
        if (!TextUtils.isEmpty(configUrls)) {
            String groomIndex = getGroomIndexWithSplit(configUrls);
            mapAndValidateGroom(serviceDiscoveryService, key, groomIndex);
        } else {
            serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.NO_URL_FOUND);
        }
    }

    void mapAndValidateGroom(ServiceDiscoveryService serviceDiscoveryService, String serviceId, String groomIndex) {
        if (serviceId != null && !TextUtils.isEmpty(groomIndex)) {
            int index;
            try {
                index = Integer.parseInt(groomIndex);
            } catch (NumberFormatException e) {
                serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.INVALID_INDEX_URL);
                return;
            }
            try {
                Object propertiesForKey = getAilGroomProperties(serviceId);
                if (propertiesForKey instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) propertiesForKey;
                    JSONObject jsonObject;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(index);
                    } catch (JSONException e) {
                        serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.INDEX_NOT_FOUND);
                        return;
                    }
                    Map map = mapData(jsonObject, index, serviceId);
                    serviceDiscoveryService.setAIKMap(map);

                } else {
                    serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.INVALID_JSON);
                }
            } catch (Exception e) {
                if (e instanceof JSONException)
                    serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.INVALID_JSON);
                else
                    serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.CONVERT_ERROR);
            }
        } else {
            serviceDiscoveryService.setAIKMapError(ServiceDiscoveryService.AIKMapError.NO_SERVICE_FOUND);
        }
    }

    Map mapData(JSONObject jsonObject, int index, String serviceId) throws Exception {
        Iterator<String> keys = jsonObject.keys();
        HashMap<String, String> hashMap = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = (String) jsonObject.get(key);
            String initialValue = getValue(serviceId, index, key);
            hashMap.put(key, ailGroom(convertData(value), Integer.parseInt(initialValue, 16)));
        }
        return hashMap;
    }

    Object getAilGroomProperties(String serviceId) throws JSONException {
        return rootJsonObject.get(serviceId);
    }
}
