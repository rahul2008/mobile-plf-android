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
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private AppInfra mAppInfra;


    //TODO - variables below need to be removed
    private ServiceDiscoveryManagerCSV sdmCSV;
    private boolean isRefreshed = false;

    public KeyBagManager(AppInfra appInfra) {
        mAppInfra = appInfra;
    }

    @Override
    public void init() throws FileNotFoundException {
        keyBagHelper = new KeyBagHelper();
        keyBagHelper.init(mAppInfra.getAppInfraContext());
        initServiceDiscovery();
    }

    @Override
    public ArrayList<HashMap> getMapForServiceId(String serviceId) {
        String appendedServiceId = keyBagHelper.getAppendedServiceId(serviceId);
        final String[] urlData = new String[1];
        sdmCSV.getServiceUrlWithCountryPreference(appendedServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.d(getClass().getSimpleName(), "testing keybag index " + url.toString());
                urlData[0] = url.toString();

            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.d(getClass().getSimpleName(), "error in getting keybag url " + message);
            }
        });
        ArrayList<HashMap> hashMaps = new ArrayList<>();
        if (serviceId != null) {
            String keyBagHelperIndex = keyBagHelper.getIndex(urlData[0]);
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

    //TODO - need to remove this once we get keybag url's from DS
    private void initServiceDiscovery() throws FileNotFoundException {
        sdmCSV = new ServiceDiscoveryManagerCSV();
        AppInfra.Builder builder = new AppInfra.Builder();
        builder.setServiceDiscovery(sdmCSV);
        sdmCSV.init(mAppInfra);
        sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {
                isRefreshed = true;
            }

            @Override
            public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                Log.d(TAG, "Error Response from Service Discovery CSV :" + s);
                isRefreshed = false;
            }
        });
    }

}
