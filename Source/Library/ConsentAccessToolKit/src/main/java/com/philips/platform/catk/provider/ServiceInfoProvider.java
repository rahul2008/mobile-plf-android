package com.philips.platform.catk.provider;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Map;

public interface ServiceInfoProvider {

    void retrieveInfo(ServiceDiscoveryInterface serviceDiscovery, ResponseListener responseListener);

    interface ResponseListener {
        void onResponse(AppInfraInfo info);
        void onError(String message);
    }

}

