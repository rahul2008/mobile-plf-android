/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.provider;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

public interface ServiceInfoProvider {

    void retrieveInfo(ServiceDiscoveryInterface serviceDiscovery, ResponseListener responseListener);

    interface ResponseListener {
        void onResponse(AppInfraInfo info);
        void onError(String message);
    }

}

