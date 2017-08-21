/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyBagManager implements KeyBagInterface {

    private final AppInfra appInfra;
    private final KeyBagHelper keyBagHelper;

    public KeyBagManager(AppInfra mAppInfra) {
        this.appInfra = mAppInfra;
        keyBagHelper = new KeyBagHelper(mAppInfra);
    }

    @Override
    public void getServicesForServiceIds(@NonNull final ArrayList<String> serviceIds, @NonNull AISDResponse.AISDPreference aiSdPreference,
                                         Map<String, String> replacement,
                                         @NonNull final ServiceDiscoveryInterface.OnGetKeyBagMapListener keyBagMapListener) throws KeyBagJsonFileNotFoundException {

        getKeyBagHelper().init(appInfra.getAppInfraContext(), "AIKeyBag.json");
        final ArrayList<AIKMService> aiKmServices = getAiKmServices();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = fetchGettingServiceDiscoveryUrlsListener(serviceIds, aiKmServices, aiSdPreference, keyBagMapListener);
        getKeyBagHelper().getServiceDiscoveryUrlMap(serviceIds, aiSdPreference, replacement, serviceUrlMapListener);
    }

    @NonNull
    KeyBagHelper getKeyBagHelper() {
        return keyBagHelper;
    }

    @NonNull
    ArrayList<AIKMService> getAiKmServices() {
        return new ArrayList<>();
    }


    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingKeyBagUrlsListener(final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener, final List<AIKMService> aikmServices) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                keyBagHelper.mapDeObfuscatedValue(urlMap, aikmServices);
                onGetKeyBagMapListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingServiceDiscoveryUrlsListener(final List<String> serviceIds, final List<AIKMService> aiKmServices,
                                                                                                  final AISDResponse.AISDPreference aiSdPreference,
                                                                                                  final ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                keyBagHelper.mapServiceDiscoveryResponse(urlMap, aiKmServices);
                ServiceDiscoveryInterface.OnGetServiceUrlMapListener keyBagIndexListener = fetchGettingKeyBagUrlsListener(onGetKeyBagMapListener, aiKmServices);
                ArrayList<String> appendedServiceIds = keyBagHelper.getAppendedServiceIds(serviceIds);
                keyBagHelper.getServiceDiscoveryUrlMap(appendedServiceIds, aiSdPreference, null, keyBagIndexListener);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetKeyBagMapListener.onError(error, message);
            }
        };
    }
}
