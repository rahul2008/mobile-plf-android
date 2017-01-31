package com.philips.platform.appinfra.servicediscovery.model;

import java.net.MalformedURLException;
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

        if (getPropositionURLs() != null) {
            try {
                return getPropositionURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (getPlatformURLs() != null) {
            try {
                return getPlatformURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public HashMap<String, ServiceDiscoveryService> getServicesUrl(ArrayList<String> serviceIds,
                                                                   AISDResponse.AISDPreference preference,
                                                                   Map<String, String> replacement) {
        if (getPropositionURLs() != null) {
            return getPropositionURLs().getServicesWithServiceID(serviceIds, preference, replacement);
        }

        if (getPlatformURLs() != null) {

            return getPlatformURLs().getServicesWithServiceID(serviceIds, preference, replacement);
        }

        return null;
    }

    public String getLocaleWithPreference(AISDPreference preference) {

        if (getPropositionURLs() != null) {
            return getPropositionURLs().getLocaleWithPreference(preference);
        }

        if (getPlatformURLs() != null) {
            return getPlatformURLs().getLocaleWithPreference(preference);
        }
        return null;
    }

    public String getCountryCode() {
        if (getPropositionURLs() != null) {
            return getPropositionURLs().getCountry();
        }
        if (getPlatformURLs() != null) {
            return getPlatformURLs().getCountry();
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
