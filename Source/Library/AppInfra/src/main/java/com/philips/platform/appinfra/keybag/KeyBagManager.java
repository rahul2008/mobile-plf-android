/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class KeyBagManager implements KeyBagInterface {

    private final AppInfra appInfra;
    private KeyBagHelper keyBagHelper;


    //TODO - variables below need to be removed
    private ServiceDiscoveryManagerCSV sdmCSV;

    public KeyBagManager(AppInfra mAppInfra) throws FileNotFoundException {
        this.appInfra = mAppInfra;
        keyBagHelper = new KeyBagHelper(mAppInfra);
        keyBagHelper.init(mAppInfra.getAppInfraContext());

        //TODO - need to remove invoking below api
        initServiceDiscovery();
    }

    @Override
    public void getValueForServiceId(final ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference, Map<String, String> replacement,
                                     final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {

        final ArrayList<AIKMService> aikmServices = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener listener = getKeyBagIndexListener(serviceIds, aikmServices, aisdPreference, onGetKeyBagMapListener);

        if (replacement != null) {
            if (aisdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                sdmCSV.getServicesWithCountryPreference(serviceIds, listener, replacement);
            else if (aisdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                sdmCSV.getServicesWithLanguagePreference(serviceIds, listener, replacement);
        } else {
            if (aisdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                sdmCSV.getServicesWithCountryPreference(serviceIds, listener);
            else if (aisdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                sdmCSV.getServicesWithLanguagePreference(serviceIds, listener);
        }
    }

    @NonNull
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener getKeyBagIndexListener(final ArrayList<String> serviceIds, final ArrayList<AIKMService> aikmServices, final AISDResponse.AISDPreference aisdPreference, final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mapServiceDiscoveryResponse(urlMap, aikmServices);
                mapKeyIndex(aisdPreference, aikmServices, serviceIds, onGetKeyBagMapListener);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }

   /* private void mapServiceDiscoveryUrl(ArrayList<String> serviceIds, AIKMServiceDiscoveryPreference aikmServiceDiscoveryPreference, final boolean[] fetchUrlSuccess, final ArrayList<AIKMService> aikmServices) {
        keyBagHelper.getServiceDiscoveryUrlMap(serviceIds, aikmServiceDiscoveryPreference, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                mapServiceDiscoveryResponse(urlMap,aikmServices);
                fetchUrlSuccess[0] = true;
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                fetchUrlSuccess[0] = false;
            }
        });
    }*/

    private void mapServiceDiscoveryResponse(Map<String, ServiceDiscoveryService> urlMap, ArrayList<AIKMService> aikmServices) {
        for (Object object : urlMap.entrySet()) {
            Map.Entry pair = (Map.Entry) object;
            ServiceDiscoveryService value = (ServiceDiscoveryService) pair.getValue();
            AIKMService aikmService = new AIKMService();
            aikmService.setServiceId((String) pair.getKey());
            aikmService.init(value.getLocale(), value.getConfigUrls());
            aikmService.setmError(value.getmError());
            aikmServices.add(aikmService);
        }
    }

    private void mapKeyIndex(AISDResponse.AISDPreference aisdPreference, final ArrayList<AIKMService> aikmServices, ArrayList<String> serviceIds, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        serviceIds = keyBagHelper.getAppendedServiceIds(serviceIds);
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener listener = getServiceUrlMapListener(onGetKeyBagMapListener,aikmServices,serviceIds);
        if (aisdPreference == AISDResponse.AISDPreference.AISDCountryPreference) {

            sdmCSV.getServicesWithCountryPreference(serviceIds, listener);
        }
        else if (aisdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
            sdmCSV.getServicesWithLanguagePreference(serviceIds, listener);
    }

    @NonNull
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener getServiceUrlMapListener(final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener, final ArrayList<AIKMService> aikmServices, final ArrayList<String> serviceIds) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                keyBagHelper.mapDeObfuscatedValue(urlMap,aikmServices);
                onGetKeyBagMapListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

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
