package com.philips.platform.appinfra.servicediscovery.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310243577 on 1/27/2017.
 */

public class AISDResponse {

    public enum AISDPreference {AISDLanguagePreference, AISDCountryPreference}

    private ServiceDiscovery platformURLs = null;
    private ServiceDiscovery propositionURLs = null;


    public ServiceDiscovery getPlatformURLs() {
        return platformURLs;
    }

    public void setPlatformURLs(ServiceDiscovery platformURLs) {
        this.platformURLs = platformURLs;
    }

    public ServiceDiscovery getPropositionURLs() {
        return propositionURLs;
    }

    public void setPropositionURLs(ServiceDiscovery propositionURLs) {
        this.propositionURLs = propositionURLs;
    }

    public URL getServiceURL(String serviceId, AISDPreference preference,
                             Map<String, String> replacement) {
        URL url;
        if (getPropositionURLs() != null) {
            url = getPropositionURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
            if (url != null) {
                return url;
            }
        }
        if (getPlatformURLs() != null) {
            url = getPlatformURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    public HashMap<String, ServiceDiscoveryService> getServicesUrl(ArrayList<String> serviceIds,
                                                                   AISDResponse.AISDPreference preference,
                                                                   Map<String, String> replacement) {
        HashMap<String, ServiceDiscoveryService> response;

        if (getPropositionURLs() != null) {
            response = getPropositionURLs().getServicesWithServiceID(serviceIds, preference, replacement);
            if (response != null && response.size() > 0) {
                return response;
            }
        }

        if (getPlatformURLs() != null) {
            response = getPlatformURLs().getServicesWithServiceID(serviceIds, preference, replacement);
            if (response != null && response.size() > 0) {
                return response;
            }
        }

        return null;
    }

    public String getLocaleWithPreference(AISDPreference preference) {
        String locale;

        if (getPropositionURLs() != null) {
            locale = getPropositionURLs().getLocaleWithPreference(preference);
            if (locale != null) {
                return locale;
            }
        }

        if (getPlatformURLs() != null) {
            locale = getPlatformURLs().getLocaleWithPreference(preference);
            if (locale != null) {
                return locale;
            }
        }
        return null;
    }

    public String getCountryCode() {
        String country;
        if (getPropositionURLs() != null) {
            country = getPropositionURLs().getCountry();
            if (country != null) {
                return country;
            }
        }
        if (getPlatformURLs() != null) {
            country = getPlatformURLs().getCountry();
            if (country != null) {
                return country;
            }
        }
        return null;
    }

    public ServiceDiscovery.Error getError() {
        if (getPropositionURLs() != null) {
            return getPropositionURLs().getError();
        }
        if (getPlatformURLs() != null) {
            return getPlatformURLs().getError();
        }
        return null;
    }

    public boolean isSuccess() {

        if (getPropositionURLs() != null) {
            return getPropositionURLs().isSuccess();
        }
        return getPlatformURLs() != null && getPlatformURLs().isSuccess();
    }
}
