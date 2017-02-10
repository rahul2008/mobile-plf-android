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
    private final String SDEmptyURL = "https://delete.delete";


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
                if (url.toString().equalsIgnoreCase(SDEmptyURL))
                    return null;
                else
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
        HashMap<String, ServiceDiscoveryService> response = new HashMap<>();
        HashMap<String, ServiceDiscoveryService> propositionResponse = null, platformResponse = null;

        if (getPropositionURLs() != null) {
            propositionResponse = getPropositionURLs().getServicesWithServiceID(serviceIds, preference, replacement);
        }

        if (getPlatformURLs() != null) {
            platformResponse = getPlatformURLs().getServicesWithServiceID(serviceIds, preference, replacement);
        }

        for (String serviceId : serviceIds) {
            if (propositionResponse != null) {
                ServiceDiscoveryService propositionService = propositionResponse.get(serviceId);
                if (propositionService != null && propositionService.getConfigUrls() != null) {
                    if (propositionService.getConfigUrls().equalsIgnoreCase(SDEmptyURL)) {
                        propositionService.setConfigUrl(null);
                        propositionService.setmError("ServiceDiscovery cannot find the URL for serviceID" + serviceId);
                    }
                    response.put(serviceId, propositionService);
                } else {
                    if(platformResponse != null) {
                        ServiceDiscoveryService platformService = platformResponse.get(serviceId);
                        if(platformService != null) {
                            response.put(serviceId,platformService);
                        }
                    }
                }
            }
        }
        return response;
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
