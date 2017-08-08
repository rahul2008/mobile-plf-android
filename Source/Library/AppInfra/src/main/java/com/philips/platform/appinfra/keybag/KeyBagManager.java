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

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class KeyBagManager implements KeyBagInterface {

    private KeyBagHelper keyBagHelper;
    private Map<String, ServiceDiscoveryService> urlMap;

    public KeyBagManager(AppInfra mAppInfra) throws FileNotFoundException {
        keyBagHelper = new KeyBagHelper(mAppInfra);
        keyBagHelper.init(mAppInfra.getAppInfraContext());
    }

    @Override
    public void getValueForServiceId(final ArrayList<String> serviceIds, AIKMServiceDiscoveryPreference aikmServiceDiscoveryPreference, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        final ArrayList<Map> keyBags = new ArrayList<>();
        final boolean[] fetchIndexSuccess = new boolean[1];
        final boolean[] fetchUrlSuccess = new boolean[1];
        for (final String serviceId : serviceIds) {
            String appendedServiceId = keyBagHelper.getAppendedServiceId(serviceId);
            handleGettingKeyIndex(aikmServiceDiscoveryPreference, keyBags, fetchIndexSuccess, serviceId, appendedServiceId);
        }

        handleGettingUrl(serviceIds, aikmServiceDiscoveryPreference, fetchUrlSuccess);
        if(fetchIndexSuccess[0] && fetchUrlSuccess[0])
            onGetKeyBagMapListener.onSuccess(KeyBagManager.this.urlMap, keyBags);
    }

    private void handleGettingUrl(ArrayList<String> serviceIds, AIKMServiceDiscoveryPreference aikmServiceDiscoveryPreference, final boolean[] fetchUrlSuccess) {
        keyBagHelper.getServiceDiscoveryUrlMap(serviceIds, aikmServiceDiscoveryPreference, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
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
    }

    private void handleGettingKeyIndex(AIKMServiceDiscoveryPreference aikmServiceDiscoveryPreference, final ArrayList<Map> keyBags, final boolean[] fetchIndexSuccess, final String serviceId, String appendedServiceId) {
        keyBagHelper.getIndexFromServiceDiscovery(appendedServiceId, aikmServiceDiscoveryPreference, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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


}
