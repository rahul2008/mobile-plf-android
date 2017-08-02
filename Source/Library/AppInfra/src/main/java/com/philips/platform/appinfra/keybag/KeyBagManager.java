/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private AppInfra mAppInfra;


    //TODO - variables below need to be removed
    private ServiceDiscoveryManagerCSV sdmCSV;

    public KeyBagManager(AppInfra appInfra) {
        mAppInfra = appInfra;
    }

    @Override
    public void init() throws FileNotFoundException {
        keyBagHelper = new KeyBagHelper();
        keyBagHelper.init(mAppInfra.getAppInfraContext());
        //TODO - need to remove invoking below api
        initServiceDiscovery();
    }

    @Override
    public Map getMapForServiceId(final String serviceId) {
        String appendedServiceId = keyBagHelper.getAppendedServiceId(serviceId);
        final Map[] maps = new Map[1];
//        mAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(appendedServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
        //TODO - need to take from app-infra defined service discovery
        sdmCSV.getServiceUrlWithCountryPreference(appendedServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.d(getClass().getSimpleName(), "fetching keybag index " + url.toString());
                maps[0] = getDataMap(serviceId, url.toString());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.d(getClass().getSimpleName(), "error in getting keybag url " + message);
            }
        });

        return maps[0];
    }

    private Map getDataMap(String serviceId, String url) {
        if (serviceId != null) {
            String keyBagHelperIndex = keyBagHelper.getIndex(url);
            int index = Integer.parseInt(keyBagHelperIndex);
            Object propertiesForKey = keyBagHelper.getPropertiesForKey(serviceId);
            if (propertiesForKey instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) propertiesForKey;
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                    return keyBagHelper.addToHashMapData(jsonObject, index, serviceId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //TODO - need to remove this once we get keybag url's from DS
    private void initServiceDiscovery() throws FileNotFoundException {
        sdmCSV = new ServiceDiscoveryManagerCSV();
        AppInfra.Builder builder = new AppInfra.Builder();
        builder.setServiceDiscovery(sdmCSV);
        sdmCSV.init(mAppInfra);
        sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                Log.d(TAG, "Error Response from Service Discovery CSV :" + s);
            }
        });
    }

}
