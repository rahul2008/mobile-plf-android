package com.philips.platform.catk.mock;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ServiceInfoProvider;

public class ServiceInfoProviderMock implements ServiceInfoProvider {

    public ServiceDiscoveryInterface serviceDiscovery;
    public ResponseListener responseListener;
    public AppInfraInfo retrievedInfo;

    @Override
    public void retrieveInfo(ServiceDiscoveryInterface serviceDiscovery, ResponseListener responseListener) {
        this.serviceDiscovery = serviceDiscovery;
        this.responseListener = responseListener;
        responseListener.onResponse(retrievedInfo);
    }

}
