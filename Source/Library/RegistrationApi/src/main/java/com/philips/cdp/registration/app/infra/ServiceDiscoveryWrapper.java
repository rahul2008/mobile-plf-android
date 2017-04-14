package com.philips.cdp.registration.app.infra;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;

import io.reactivex.Single;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnGetServiceUrlListener;

public class ServiceDiscoveryWrapper {

    private final ServiceDiscoveryInterface serviceDiscoveryInterface;

    public ServiceDiscoveryWrapper(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        this.serviceDiscoveryInterface = serviceDiscoveryInterface;
    }

    public Single<String> getServiceUrlWithCountryPreferenceSingle(String serviceId) {
        return Single.create(emitter -> {
            OnGetServiceUrlListener listener = new OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    emitter.onSuccess(url.toString());
                }

                @Override
                public void onError(ERRORVALUES errorvalues, String s) {
                    emitter.onError(null);
                }
            };
            serviceDiscoveryInterface.getServiceUrlWithCountryPreference(serviceId, listener);
        });
    }
}
