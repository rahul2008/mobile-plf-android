/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.io.IOException;
import java.io.InputStream;
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
                                         @NonNull final ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener) throws KeyBagJsonFileNotFoundException {

        InputStream inputStream = getInputStream(appInfra.getAppInfraContext(), "AIKeyBag.json");
        getKeyBagHelper().init(appInfra, inputStream);
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = fetchGettingServiceDiscoveryUrlsListener(serviceIds, aiKmServices, aiSdPreference, onGetServicesListener);
        getKeyBagHelper().getServiceDiscoveryUrlMap(serviceIds, aiSdPreference, replacement, serviceUrlMapListener);
    }


    InputStream getInputStream(Context mContext, String fileName) throws KeyBagJsonFileNotFoundException {
        try {
            return mContext.getAssets().open(fileName);
        } catch (IOException e) {
            throw new KeyBagJsonFileNotFoundException();
        }
    }

    @NonNull
    KeyBagHelper getKeyBagHelper() {
        return keyBagHelper;
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingKeyBagUrlsListener(final ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener, final List<AIKMService> aikmServices, final Map<String, ServiceDiscoveryService> serviceDiscoveryUrlMap) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                keyBagHelper.mapResponse(urlMap, aikmServices, serviceDiscoveryUrlMap);
                onGetServicesListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetServicesListener.onError(error, message);
            }
        };
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener fetchGettingServiceDiscoveryUrlsListener(final List<String> serviceIds, final List<AIKMService> aiKmServices,
                                                                                                  final AISDResponse.AISDPreference aiSdPreference,
                                                                                                  final ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                ServiceDiscoveryInterface.OnGetServiceUrlMapListener keyBagIndexListener = fetchGettingKeyBagUrlsListener(onGetServicesListener, aiKmServices, urlMap);
                ArrayList<String> appendedServiceIds = keyBagHelper.getAppendedServiceIds(serviceIds);
                keyBagHelper.getServiceDiscoveryUrlMap(appendedServiceIds, aiSdPreference, null, keyBagIndexListener);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetServicesListener.onError(error, message);
            }
        };
    }
}
