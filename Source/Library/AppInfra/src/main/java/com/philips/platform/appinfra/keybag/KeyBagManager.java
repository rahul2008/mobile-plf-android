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

import java.util.ArrayList;
import java.util.Map;

public class KeyBagManager implements KeyBagInterface {

    private final AppInfra appInfra;

    public KeyBagManager(AppInfra mAppInfra) {
        this.appInfra = mAppInfra;
    }

    @Override
    public void getServicesForServiceIds(@NonNull final ArrayList<String> serviceIds, @NonNull AISDResponse.AISDPreference aiSdPreference,
                                         Map<String, String> replacement,
                                         @NonNull final ServiceDiscoveryInterface.OnGetKeyBagMapListener keyBagMapListener) throws KeyBagJsonFileNotFoundException {

        KeyBagHelper keyBagHelper = new KeyBagHelper(appInfra);
        keyBagHelper.init(appInfra.getAppInfraContext());
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = keyBagHelper.getServiceUrlMapListener(serviceIds, aiKmServices, aiSdPreference, keyBagMapListener);
        keyBagHelper.getServiceDiscoveryUrlMap(serviceIds, aiSdPreference, replacement, serviceUrlMapListener);
    }
}
