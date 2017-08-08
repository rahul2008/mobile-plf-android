/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private AppInfra mAppInfra;
    private Map<String, ServiceDiscoveryService> urlMap;

    //TODO - variables below need to be removed
    private ServiceDiscoveryManagerCSV sdmCSV;

    public KeyBagManager(AppInfra appInfra) throws FileNotFoundException {
        mAppInfra = appInfra;
        keyBagHelper = new KeyBagHelper();
        keyBagHelper.init(mAppInfra.getAppInfraContext());
        //TODO - need to remove invoking below api
        initServiceDiscovery();
    }

    @Override
    public void getValueForServiceId(final ArrayList<String> serviceIds, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        final ArrayList<Map> keyBags = new ArrayList<>();
        final boolean[] fetchIndexSuccess = new boolean[1];
        final boolean[] fetchUrlSuccess = new boolean[1];
        for (final String serviceId : serviceIds) {
            String appendedServiceId = keyBagHelper.getAppendedServiceId(serviceId);
//        mAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(appendedServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            //TODO - need to take from app-infra defined service discovery
            sdmCSV.getServiceUrlWithCountryPreference(appendedServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    Log.d(getClass().getSimpleName(), "fetching keybag index " + url.toString());
                    keyBags.add(keyBagHelper.getDeObfuscatedMap(serviceId, url.toString()));
                    fetchIndexSuccess[0] = true;
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    Log.d(getClass().getSimpleName(), "error in getting keybag url " + message);
                    fetchIndexSuccess[0] = false;
                }
            });
        }
        sdmCSV.getServicesWithCountryPreference(serviceIds, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                KeyBagManager.this.urlMap = urlMap;
                fetchUrlSuccess[0] = true;
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                fetchUrlSuccess[0] = false;
            }
        });
        if(fetchIndexSuccess[0] && fetchUrlSuccess[0])
            onGetKeyBagMapListener.onSuccess(KeyBagManager.this.urlMap, keyBags);
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
