/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import java.net.URL;
import java.util.Map;

/**
 * Created by 310238655 on 6/7/2016.
 */
public interface ServiceDiscoveryInterface {

    public void getservice(String url);

    public class ServiceUrlandLocale {
        public final URL    serviceUrl;
        public final String serviceLocale;

        public ServiceUrlandLocale(URL serviceUrl, String serviceLocale) {
            this.serviceUrl = serviceUrl;
            this.serviceLocale = serviceLocale;
        }
    }

    interface OnErrorListener {
        enum ERRORVALUES {NO_NETWORK, CONNECTION_TIMEOUT, SERVER_ERROR, SECURITY_ERROR, INVALID_RESPONSE};
        void onError(ERRORVALUES error, String message);
    }
    interface OnGetHomeCountryListener extends OnErrorListener {
        enum SOURCE {STOREDPREFERENCE, SIMCARD, GEOIP};
        void onSuccess(String countryCode, SOURCE source);
    }
    interface OnGetServiceUrlListener extends OnErrorListener {
        void onSuccess(URL url);
    }
    interface OnGetServiceLocaleListener extends OnErrorListener {
        void onSuccess(String locale);
    }
    interface OnGetServicesListener extends OnErrorListener {
//        void onSuccess(Map<String, ServiceUrlandLocale> services);
            void onSuccess(String services);
    }
    interface OnRefreshListener extends OnErrorListener {
        void onSuccess();
    }
    void getHomeCountry(OnGetHomeCountryListener listener);
    /**
     * Persistently store Home country, overwrites any existing country value.
     * Changing the country automatically clears the cached service list and triggers a refresh.
     * @param countryCode country code to persistently store, code must be according to ISO 3166-1
     */
    void setHomeCountry(String countryCode);

    /**
     * Returns the URL for a specific service with a preference for the current language.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the URL of the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServiceUrlWithLanguagePreference(String serviceId, OnGetServiceUrlListener listener);
    /**
     * Returns the URL for a specific service with a preference for the current home country.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the URL of the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener);

    /**
     * Returns the locale to be used for a specific service with a preference for the current language.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the recommended locale for the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServiceLocaleWithLanguagePreference(String serviceId, OnGetServiceLocaleListener listener);
    /**
     * Returns the locale to be used for a specific service with a preference for the current home country.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the recommended locale for the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServiceLocaleWithCountryPreference(String serviceId, OnGetServiceLocaleListener listener);

    /**
     * Returns the URLs + locales for a set of services with a preference for the current language.
     * @param serviceIds list of service names for which the URL + locale are to be retrieved
     * @param listener asynchronously returns using onSuccess a map containing the requested service names (key)
     *                 and their URL + locale (value), the value will be null if the service is unknown;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServicesWithLanguagePreference(String serviceIds, OnGetServicesListener listener);
    /**
     * Returns the URLs + locales for a set of services with a preference for the current home country.
     * @param serviceIds list of service names for which the URL + locale are to be retrieved
     * @param listener asynchronously returns using onSuccess a map containing the requested service names (key)
     *                 and their URL + locale (value), the value will be null if the service is unknown;
     *                 or returns onError the error code when retrieval failed.
     */
    void getServicesWithCountryPreference(String serviceIds, OnGetServicesListener listener);

    /**
     * Start negotiation with cloud service for the list of service for this application. List is based on sector, microsite, home country, and language.
     * The retrieved list is cached internally (not persistently).
     * The cached list is automatically refreshed every 24hours.
     * A refresh is only required, to ensure the very first service request after app start can be processed from the cache quickly, or when a previous sync failed.
     * @param listener asynchronously returns using onSuccess when retrieval was successful;
     *                 or returns onError the error code when retrieval failed.
     */
    void refresh(OnRefreshListener listener, String url);
}
