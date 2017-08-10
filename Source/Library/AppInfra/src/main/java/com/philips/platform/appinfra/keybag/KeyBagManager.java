/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class KeyBagManager implements KeyBagInterface {

    private final AppInfra appInfra;
    private KeyBagHelper keyBagHelper;


    //TODO - variables below need to be removed
    private ServiceDiscoveryManagerCSV sdmCSV;

    public KeyBagManager(AppInfra mAppInfra) throws KeyBagJsonFileNotFoundException {
        this.appInfra = mAppInfra;
        keyBagHelper = new KeyBagHelper(mAppInfra);
        keyBagHelper.init(mAppInfra.getAppInfraContext());

        //TODO - need to remove invoking below api
        initServiceDiscovery();
    }

    @Override
    public void getServicesForServiceIds(@NonNull final ArrayList<String> serviceIds, @NonNull AISDResponse.AISDPreference aiSdPreference, Map<String, String> replacement,
                                         @NonNull final ServiceDiscoveryInterface.OnGetKeyBagMapListener keyBagMapListener) {

        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = getServiceUrlMapListener(serviceIds, aiKmServices, aiSdPreference, keyBagMapListener);

        if (replacement != null) {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                sdmCSV.getServicesWithCountryPreference(serviceIds, serviceUrlMapListener, replacement);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                sdmCSV.getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener, replacement);
        } else {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                sdmCSV.getServicesWithCountryPreference(serviceIds, serviceUrlMapListener);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                sdmCSV.getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener);
        }
    }

    @NonNull
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener getServiceUrlMapListener(final ArrayList<String> serviceIds, final ArrayList<AIKMService> aiKmServices,
                                                                                          final AISDResponse.AISDPreference aiSdPreference,
                                                                                          final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mapServiceDiscoveryResponse(urlMap, aiKmServices);
                mapKeyIndex(aiSdPreference, aiKmServices, serviceIds, onGetKeyBagMapListener);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
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

    private void mapKeyIndex(AISDResponse.AISDPreference aiSdPreference, final ArrayList<AIKMService> aiKmServices, ArrayList<String> serviceIds, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        serviceIds = keyBagHelper.getAppendedServiceIds(serviceIds);
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener keyBagIndexListener = getKeyBagIndexListener(onGetKeyBagMapListener, aiKmServices);
        if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference) {
            sdmCSV.getServicesWithCountryPreference(serviceIds, keyBagIndexListener);
        } else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
            sdmCSV.getServicesWithLanguagePreference(serviceIds, keyBagIndexListener);
    }

    @NonNull
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener getKeyBagIndexListener(final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener, final ArrayList<AIKMService> aikmServices) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                keyBagHelper.mapDeObfuscatedValue(urlMap,aikmServices);
                onGetKeyBagMapListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }

    //TODO - need to remove this once we get keybag url's from DS
    private void initServiceDiscovery() {
        sdmCSV = new ServiceDiscoveryManagerCSV();
        AppInfra.Builder builder = new AppInfra.Builder();
        builder.setServiceDiscovery(sdmCSV);
        sdmCSV.init(appInfra);
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
