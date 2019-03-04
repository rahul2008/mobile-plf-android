package com.philips.platform.pim.utilities;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;


public class ServiceDiscoveryWrapper {

    private final ServiceDiscoveryInterface serviceDiscoveryInterface;

    public ServiceDiscoveryWrapper(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        this.serviceDiscoveryInterface = serviceDiscoveryInterface;
    }

    public Single<String> getServiceUrlWithCountryPreferenceSingle(ArrayList<String> serviceId) {
        return Single.create(emitter -> {
            ServiceDiscoveryInterface.OnGetServiceUrlMapListener listener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    if (!emitter.isDisposed())
                        emitter.onSuccess(urlMap.toString());
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    if (!emitter.isDisposed())
                        emitter.onError(new Throwable(message));
                }
            };
            serviceDiscoveryInterface.getServicesWithCountryPreference(serviceId, listener, null);
        });
    }

    public Single<String> getServiceUrlWithLanguagePreferenceSingle(ArrayList<String> serviceId) {
        return Single.create(emitter -> {
            ServiceDiscoveryInterface.OnGetServiceUrlMapListener listener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    if (!emitter.isDisposed())
                        emitter.onSuccess(urlMap.toString());
                }


                @Override
                public void onError(ERRORVALUES errorvalues, String s) {
                    if (!emitter.isDisposed())
                        emitter.onError(new Throwable(s));

                }
            };
            serviceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, listener, null);
        });
    }
}
