package com.philips.platform.catk.infra;

import java.util.ArrayList;
import java.util.Map;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ServiceInfoProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class InfraServiceInfoProvider implements ServiceInfoProvider {

    private static final String CSS_SERVICE_DISCOVERY_KEY = "css.consentservice";
    private static final String CSS_CONTEXT_PATH = "consent";
    @NonNull
    private final DiscoveryListener discoveryListener;

    public InfraServiceInfoProvider() {
        discoveryListener = new DiscoveryListener();
    }

    public void retrieveInfo(ServiceDiscoveryInterface serviceDiscovery, ResponseListener responseListener) {
        discoveryListener.setResponseListener(responseListener);
        ArrayList<String> services = new ArrayList<>();
        services.add(CSS_SERVICE_DISCOVERY_KEY);
        serviceDiscovery.getServicesWithCountryPreference(services, discoveryListener);
    }

    static class DiscoveryListener implements ServiceDiscoveryInterface.OnGetServiceUrlMapListener {

        @Nullable
        public ResponseListener listener;

        private DiscoveryListener() {
        }

        void setResponseListener(ResponseListener responseListener) {
            this.listener = responseListener;
        }

        @Override
        public void onSuccess(Map<String, ServiceDiscoveryService> map) {
            ArrayList<ServiceDiscoveryService> services = new ArrayList<>(map.values());
            if (services.size() > 0) {
                notifySuccess(services.get(0));
            } else {
                notifyError("No Services found");
            }
        }

        @Override
        public void onError(ERRORVALUES errorvalues, String s) {
            notifyError(s);
        }

        private void notifySuccess(ServiceDiscoveryService service) {
            if (listener != null) {
                try {
                    listener.onResponse(new AppInfraInfo(extractConsentUrl(service)));
                } catch (IllegalStateException invalidUrl) {
                    listener.onError(invalidUrl.getMessage());
                }
            }
        }

        private void notifyError(String message) {
            if (listener != null) {
                listener.onError(message);
            }
        }

        @NonNull
        private String extractConsentUrl(ServiceDiscoveryService service) throws IllegalStateException {
            if (service.getmError() == null && service.getConfigUrls() != null) {
                String host = service.getConfigUrls();
                return host + (host.endsWith("/") ? "" : "/") + CSS_CONTEXT_PATH;
            }
            throw new IllegalStateException("Invalid configurl: " + service.getConfigUrls());
        }
    }

}
