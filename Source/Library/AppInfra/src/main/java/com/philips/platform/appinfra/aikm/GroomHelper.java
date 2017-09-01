/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm;


import android.text.TextUtils;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
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

    private final GroomLib groomLib;
    private JSONObject rootJsonObject;
    private AppInfra mAppInfra;

    GroomHelper(AppInfra appInfra) {
        groomLib = new GroomLib();
        this.mAppInfra = appInfra;
    }

    boolean init(AppInfra mAppInfra, InputStream mInputStream) throws AIKMJsonFileNotFoundException {
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
                throw new AIKMJsonFileNotFoundException();
            else
                Log.e("error"," while mapping local Groom data");
        }
        return false;
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
                aikmService.setMapError(AIKMService.MapError.NO_SERVICE_FOUND);
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
            aikmService.setMapError(AIKMService.MapError.EMPTY_ARGUMENT_URL);
        }
    }

    void mapAndValidateGroom(AIKMService aikmService, String serviceId, String groomIndex) {
        if (serviceId != null && !TextUtils.isEmpty(groomIndex)) {
            int index;
            try {
                index = Integer.parseInt(groomIndex);
            } catch (NumberFormatException e) {
                aikmService.setMapError(AIKMService.MapError.INVALID_INDEX_URL);
                return;
            }
            try {
                Object propertiesForKey = getAilGroomProperties(serviceId);
                if (propertiesForKey instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) propertiesForKey;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(index);
                    } catch (JSONException e) {
                        aikmService.setMapError(AIKMService.MapError.BEYOND_BOUND_ERROR);
                        return;
                    }
                    Map map = mapData(jsonObject, index, serviceId);
                    aikmService.setMap(map);

                } else {
                    aikmService.setMapError(AIKMService.MapError.INVALID_JSON);
                }
            } catch (Exception e) {
                if (e instanceof JSONException)
                    aikmService.setMapError(AIKMService.MapError.INVALID_JSON);
                else
                    aikmService.setMapError(AIKMService.MapError.CONVERT_ERROR);
            }
        } else {
            aikmService.setMapError(AIKMService.MapError.NO_SERVICE_FOUND);
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
