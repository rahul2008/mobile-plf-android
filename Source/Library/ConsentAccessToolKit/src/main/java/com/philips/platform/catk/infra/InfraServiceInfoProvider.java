package com.philips.platform.catk.infra;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ServiceInfoProvider;

import java.util.ArrayList;
import java.util.Map;

public class InfraServiceInfoProvider implements ServiceInfoProvider {

    private static final String CSS_SERVICE_DISCOVERY_KEY = "ds.consentservice";

    public void retrieveInfo(ServiceDiscoveryInterface serviceDiscovery, ResponseListener responseListener) {
        DiscoveryListener serviceUrlListener = new DiscoveryListener(responseListener);
        ArrayList<String> services = new ArrayList<>();
        services.add(CSS_SERVICE_DISCOVERY_KEY);
        serviceDiscovery.getServicesWithLanguagePreference(services, serviceUrlListener);
    }

    private static class DiscoveryListener implements ServiceDiscoveryInterface.OnGetServiceUrlMapListener {

        public ResponseListener listener;

        private DiscoveryListener (ResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(Map<String, ServiceDiscoveryService> map) {
            ArrayList<ServiceDiscoveryService>  services = new ArrayList<>(map.values());
            if (services.size() > 0) {
                listener.onResponse(new AppInfraInfo(services.get(0).getConfigUrls()));
            }
        }

        @Override
        public void onError(ERRORVALUES errorvalues, String s) {
            listener.onError(s);
        }
    }


}

