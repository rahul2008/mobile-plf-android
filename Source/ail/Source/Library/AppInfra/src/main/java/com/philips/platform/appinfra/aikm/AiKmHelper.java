/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm;


import android.content.Context;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.model.AIKMResponse;
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

class AiKmHelper {

    private final GroomLib groomLib;
    private JSONObject rootJsonObject;
    private AppInfra mAppInfra;

    AiKmHelper(AppInfra appInfra) {
        groomLib = new GroomLib();
        this.mAppInfra = appInfra;
    }

    void init(AppInfra mAppInfra) throws AIKMJsonFileNotFoundException, JSONException {
        this.mAppInfra = mAppInfra;
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

    // TODO: Deepthi please change the strings and revisit the logic 
//           Will consider above TODO in 17.5
    String getGroomIndex(String value) {
        String data = "https://philips.com/";
        if (!TextUtils.isEmpty(value)) {
            StringTokenizer st = new StringTokenizer(value, data);
            if (st.hasMoreTokens())
                return st.nextToken();
        }
        return null;
    }

    //TODO - will be introduced in 17.5
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

    void mapResponse(Map<String, ServiceDiscoveryService> map, List<AIKMService> aiKmServices, Map<String, ServiceDiscoveryService> serviceDiscoveryUrlMap) {
        for (Map.Entry<String, ServiceDiscoveryService> entry : serviceDiscoveryUrlMap.entrySet()) {
            String key = entry.getKey();
            ServiceDiscoveryService value = entry.getValue();
            AIKMService aikmService = new AIKMService();
            aikmService.setServiceId(entry.getKey());
            aikmService.init(value.getLocale(), value.getConfigUrls());
            aikmService.setmError(value.getmError());

            ServiceDiscoveryService serviceDiscoveryService = map.get(key.concat(".kindex"));
            if (serviceDiscoveryService != null) {
                validateGroom(entry.getKey(), aikmService, serviceDiscoveryService);
            } else {
                aikmService.setAIKMapError(AIKMService.AIKMapError.NO_SERVICE_FOUND);
            }
            aiKmServices.add(aikmService);
        }
    }

    private void validateGroom(String key, AIKMService aikmService, ServiceDiscoveryService serviceDiscoveryService) {
        String configUrls = serviceDiscoveryService.getConfigUrls();
        if (!TextUtils.isEmpty(configUrls)) {
            String groomIndex = getGroomIndex(configUrls);
            mapAndValidateGroom(aikmService, key, groomIndex);
        } else {
            aikmService.setAIKMapError(AIKMService.AIKMapError.NO_URL_FOUND);
        }
    }

    void mapAndValidateGroom(AIKMService aikmService, String serviceId, String groomIndex) {
        if (serviceId != null && !TextUtils.isEmpty(groomIndex)) {
            int index;
            try {
                index = Integer.parseInt(groomIndex);
            } catch (NumberFormatException e) {
                aikmService.setAIKMapError(AIKMService.AIKMapError.INVALID_INDEX_URL);
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
                        aikmService.setAIKMapError(AIKMService.AIKMapError.INDEX_NOT_FOUND);
                        return;
                    }
                    Map<?,?> map = mapData(jsonObject, index, serviceId);
                    aikmService.setAIKMap(map);

                } else {
                    aikmService.setAIKMapError(AIKMService.AIKMapError.INVALID_JSON);
                }
            } catch (Exception e) {
                if (e instanceof JSONException)
                    aikmService.setAIKMapError(AIKMService.AIKMapError.INVALID_JSON);
                else
                    aikmService.setAIKMapError(AIKMService.AIKMapError.CONVERT_ERROR);
            }
        } else {
            aikmService.setAIKMapError(AIKMService.AIKMapError.NO_SERVICE_FOUND);
        }
    }

    Map<?,?> mapData(JSONObject jsonObject, int index, String serviceId) throws Exception {
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

    ArrayList<String> getAppendedGrooms(List<String> serviceIds) {
        ArrayList<String> appendedServiceIds = new ArrayList<>();
        for (String serviceId : serviceIds) {
            if (!TextUtils.isEmpty(serviceId))
                appendedServiceIds.add(serviceId.concat(".kindex"));
        }
        return appendedServiceIds;
    }

    AIKMResponse getServiceExtension(String serviceId, String url, AIKMResponse aikmResponse) {
        return validateGroom(serviceId, url, aikmResponse);
    }

    private AIKMResponse validateGroom(String key, String configUrls, AIKMResponse aikmResponse) {
        if (!TextUtils.isEmpty(configUrls)) {
            String groomIndex = getGroomIndexWithSplit(configUrls);
            mapAndValidateGroom(key, groomIndex, aikmResponse);
        } else {
            aikmResponse.setkError(AIKManager.KError.NO_KINDEX_URL_FOUND);
        }
        return aikmResponse;
    }

    AIKMResponse mapAndValidateGroom(String serviceId, String groomIndex, AIKMResponse aikmResponse) {
        if (!TextUtils.isEmpty(groomIndex)) {
            int index;
            try {
                index = Integer.parseInt(groomIndex);
            } catch (NumberFormatException e) {
                aikmResponse.setkError(AIKManager.KError.INVALID_INDEX_URL);
                return aikmResponse;
            }
            try {
                Object propertiesForKey = getAilGroomProperties(serviceId);
                if (propertiesForKey instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) propertiesForKey;
                    JSONObject jsonObject;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(index);
                    } catch (JSONException e) {
                        aikmResponse.setkError(AIKManager.KError.DATA_NOT_FOUND);
                        return aikmResponse;
                    }
                    Map<?,?> map = mapData(jsonObject, index, serviceId);
                    aikmResponse.setkMap(map);
                } else {
                    aikmResponse.setkError(AIKManager.KError.INVALID_JSON);
                }
            } catch (Exception e) {
                if (e instanceof JSONException)
                    aikmResponse.setkError(AIKManager.KError.INVALID_JSON);
                else
                    aikmResponse.setkError(AIKManager.KError.CONVERT_ERROR);
            }
        } else {
            aikmResponse.setkError(AIKManager.KError.INVALID_INDEX_URL);
        }
        return aikmResponse;
    }
}
