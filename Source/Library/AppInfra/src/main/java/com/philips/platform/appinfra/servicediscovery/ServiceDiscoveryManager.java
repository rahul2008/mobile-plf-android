/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class downloads list of URLs from service discovery server,
 * providing the locale and the identity of the application.
 */
public class ServiceDiscoveryManager implements ServiceDiscoveryInterface {


    private abstract class DownloadItemListener {
        public boolean forceRefresh() {
            return false;
        }

        public abstract void onDownloadDone(ServiceDiscovery result);
    }

    private enum AISDURLType {AISDURLTypeProposition, AISDURLTypePlatform}


    private static final String URLTagTest = "apps%2b%2benv%2btest";
    private static final String URLTagDevelopment = "apps%2b%2benv%2bdev";
    private static final String URLTagStaging = "apps%2b%2benv%2bstage";
    private static final String URLTagAcceptance = "apps%2b%2benv%2bacc";
    private static final String URLTagProduction = "apps%2b%2benv%2bprod";

    private static final String baseURLProduction = "www.philips.com";
    private static final String baseURLTesting = "tst.philips.com";
    private static final String baseURLStaging = "dev.philips.com";
    private static final String baseURLAcceptance = "acc.philips.com";

    private static final String stateTesting = "TEST";
    private static final String stateDevelopment = "DEVELOPMENT";
    private static final String stateStaging = "STAGING";
    private static final String stateAccepteance = "ACCEPTANCE";
    private static final String stateProduction = "PRODUCTION";


    private OnGetHomeCountryListener.SOURCE countryCodeSource;
    private final AppInfra mAppInfra;
    private final Context context;
    private ServiceDiscovery serviceDiscovery = null;

    private ServiceDiscovery mPlatformServiceModel = null;
    private ServiceDiscovery mPropositionServiceModel = null;


    private String countryCode;
    private long holdbackTime = 0l;

    private final RequestManager mRequestItemManager;

    //
    private boolean downloadInProgress;
    private ArrayDeque<DownloadItemListener> downloadAwaiters;
    private ReentrantLock downloadLock;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        mRequestItemManager = new RequestManager(context, mAppInfra);
        downloadInProgress = false;
        downloadAwaiters = new ArrayDeque<>();
        downloadLock = new ReentrantLock();
    }

    private void queueResultListener(boolean forcerefresh, DownloadItemListener listener) {
        downloadLock.lock();

        if (forcerefresh)
            serviceDiscovery = null;
        downloadAwaiters.add(listener);

        if (!downloadInProgress) {
            if (new Date().getTime() > holdbackTime) {// if current time is greater then holdback time
                downloadInProgress = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean forceRefresh = false;
                      //  ServiceDiscovery service;
                        ServiceDiscovery
                        ArrayList<DownloadItemListener> stalledAwaiters = new ArrayList<DownloadItemListener>();
                        do {
                            if (forceRefresh)
                                downloadLock.unlock();
                            forceRefresh = false;
                            service = downloadServices();
                            downloadLock.lock();
                            DownloadItemListener d;
                            while ((d = downloadAwaiters.poll()) != null) {
                                if (d.forceRefresh())
                                    forceRefresh = true;
                                stalledAwaiters.add(d);
                            }
                        }
                        while (forceRefresh);
                        downloadInProgress = false;
                        serviceDiscovery = service;
                        final ServiceDiscovery result = service;
                        downloadLock.unlock();

                        for (final DownloadItemListener d : stalledAwaiters) {
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    d.onDownloadDone(result);
                                }
                            });

                            new Handler(Looper.getMainLooper()).post(t);
                        }
                    }
                }).start();
            } else {
                ServiceDiscovery ServiceDiscoveryError = new ServiceDiscovery();
                ServiceDiscoveryError.setError(new ServiceDiscovery.Error(OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server is not reachable at the moment,Please try after some time"));
                //ServiceDiscoveryError.setSuccess(false);
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SD call", "Server is not reachable at the moment,Please try after some time");
                listener.onDownloadDone(ServiceDiscoveryError);
            }
        } else {

            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SD call", "Download already in progress, please wait for response");
        }
        downloadLock.unlock();
    }


    /**
     * Precondition: download lock is acquired
     */
//    private ServiceDiscovery downloadServices() {
//        String platformURL = getSDURLForType(AISDURLType.AISDURLTypePlatform);
//
//        if (propositionURL != null) {
//            ServiceDiscovery propositionService = new ServiceDiscovery();
//            propositionService = processRequest(propositionURL, propositionService)
//        }
//
//        if (platformURL != null) {
//            ServiceDiscovery platformService = new ServiceDiscovery();
//            processRequest(platformURL, platformService);
//        }
//
//
//        return service;
//    }


    private ServiceDiscovery downloadPlatformService() {
        String platformURL = getSDURLForType(AISDURLType.AISDURLTypePlatform);
        ServiceDiscovery platformService = new ServiceDiscovery();
        if (platformURL != null) {
            platformService = processRequest(platformURL, platformService);
        }
        return platformService;
    }


    private ServiceDiscovery downloadPropositionService() {
        String propositionURL = getSDURLForType(AISDURLType.AISDURLTypeProposition);
        ServiceDiscovery propositionService = new ServiceDiscovery();
        if (propositionURL != null) {
            propositionService = processRequest(propositionURL, propositionService);
        }
        return propositionService;
    }


    private ServiceDiscovery processRequest(String urlBuild, ServiceDiscovery service) {
        ServiceDiscovery SDcache = mRequestItemManager.getServiceDiscoveryFromCache(urlBuild);
        if (null == SDcache) {
            if (!isOnline()) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SD call", "NO_NETWORK");
                service.setError(new ServiceDiscovery.Error(OnErrorListener.ERRORVALUES.NO_NETWORK, "NO_NETWORK"));
                // service.setSuccess(false);
            } else {
                //urlBuild = buildUrl();
                if (urlBuild != null) {
                    service = mRequestItemManager.execute(urlBuild);
                    saveToSecureStore(service.getCountry(), true);

                    if (service.isSuccess()) {
                        holdbackTime = 0;   //remove hold back time
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SD call", "SD Fetched from server");
                    } else {
                        holdbackTime = new Date().getTime() + 10000; // curent time + 10 Seconds
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "SD call", service.getError().toString());
                    }
                } else {
                    service.setError(new ServiceDiscovery.Error(OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, "URL is null"));
                }
            }
        } else {
            service = SDcache;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SD call", "SD Fetched from cache");
        }
        return service;
    }

    private String getSDURLForType(AISDURLType aisdurlType) {
        try {
            String sector = null, micrositeid = null, environment = null;
            String url = null;
            final AppIdentityInterface identityManager = mAppInfra.getAppIdentity();
            final InternationalizationInterface localManager = mAppInfra.getInternationalization();
            final String locale = localManager.getUILocaleString();
            final AppIdentityInterface.AppState state = identityManager.getAppState();
            final String service_environment = identityManager.getServiceDiscoveryEnvironment();

            final String appState = getAppStateStringFromState(state);

            switch (aisdurlType) {
                case AISDURLTypePlatform:
                    sector = "B2C";
                    micrositeid = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                            ("servicediscovery.platformMicrositeId", "appinfra", null);
                    environment = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
                            ("servicediscovery.platformMicrositeId", "appinfra", null);
                    environment = getSDBaseURLForEnvironment(environment);
                    break;

                case AISDURLTypeProposition:
                    sector = identityManager.getSector();
                    micrositeid = identityManager.getMicrositeId();
                    environment = getSDBaseURLForEnvironment(service_environment);
                    break;
            }
            if (sector != null && micrositeid != null &&
                    localManager.getUILocale() != null && appState != null) {

                url = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector()
                        + "/" + identityManager.getMicrositeId() + "?locale=" +
                        locale + "&tags=" + appState;


                String countryHome = getCountry(serviceDiscovery);
                if (countryHome != null) {
                    url += "&country=" + countryHome;
                    saveToSecureStore(countryHome, true);
                }
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "URL", "" + url);
            } else {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Build URL in SD", "" + "Appidentity values are null");
            }
            return url;

        } catch (Exception exception) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ServiceDiscovery",
                    exception.toString());
        }
        return null;
    }

    private String getSDBaseURLForEnvironment(String serviceEnv) {
        String baseUrl;
        if (serviceEnv.equalsIgnoreCase(stateProduction)) {
            baseUrl = baseURLProduction;
        } else if (serviceEnv.equalsIgnoreCase(stateTesting)) {
            baseUrl = baseURLTesting;
        } else if (serviceEnv.equalsIgnoreCase(stateStaging)) {
            baseUrl = baseURLStaging;
        } else if (serviceEnv.equalsIgnoreCase(stateAccepteance)) {
            baseUrl = baseURLAcceptance;
        } else {
            baseUrl = baseURLTesting;
        }
        return baseUrl;
    }


    private String getAppStateStringFromState(AppIdentityInterface.AppState appState) {
        String appstate = null;
        switch (appState) {
            case TEST:
                appstate = URLTagTest;
                break;
            case DEVELOPMENT:
                appstate = URLTagDevelopment;
                break;
            case STAGING:
                appstate = URLTagStaging;
                break;
            case PRODUCTION:
                appstate = URLTagProduction;
                break;
            case ACCEPTANCE:
                appstate = URLTagAcceptance;
                break;
        }
        return appstate;
    }


    /**
     * Gets the country from app according to settings/SIM/GEOIP
     *
     * @param listener callback.
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {

        String country = getCountry(serviceDiscovery);
        String countrySource = fetchFromSecureStorage(false);
        if (countrySource != null && countrySource.equalsIgnoreCase("SIMCARD")) {
            countryCodeSource = OnGetHomeCountryListener.SOURCE.SIMCARD;
        } else if (countrySource != null && countrySource.equalsIgnoreCase("GEOIP")) {
            countryCodeSource = OnGetHomeCountryListener.SOURCE.GEOIP;
        } else if (countrySource != null && countrySource.equalsIgnoreCase("STOREDPREFERENCE")) {
            countryCodeSource = OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE;
        }

        if (country != null) {
            listener.onSuccess(country, countryCodeSource);
        } else {
            queueResultListener(false, new DownloadItemListener() {
                @Override
                public void onDownloadDone(ServiceDiscovery result) {
                    String country = getCountry(result);
                    if (country != null) {
                        listener.onSuccess(country, countryCodeSource);
                    } else {
                        ServiceDiscovery.Error err = result.getError();
                        listener.onError(err.getErrorvalue(), err.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void setHomeCountry(String countryCode) {

        if (countryCode != null && countryCode.length() == 2) {
            if (!countryCode.equals(getCountry(serviceDiscovery))) { // entered country is different then existing
                this.countryCode = countryCode;
                countryCodeSource = OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE;
                saveToSecureStore(countryCode, true);
                saveToSecureStore(countryCodeSource.toString(), false);
                serviceDiscovery = null;  // if there is no internet then also old SD value must be cleared.
                mRequestItemManager.clearCacheServiceDiscovery(); // clear SD cache
                queueResultListener(true, new DownloadItemListener() {
                    @Override
                    public void onDownloadDone(ServiceDiscovery result) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Force Refresh is done", "Force Refresh is done");
                    }
                });
            } else {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "SAME COUNTRY", "Entered Country code is same as old one");
            }
        } else {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Invalid COUNTRY", "Country code is INVALID");
        }
    }


    @Override
    public void getServiceUrlWithLanguagePreference(String serviceId, OnGetServiceUrlListener listener) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, false, null);
    }

    @Override
    public void getServiceUrlWithLanguagePreference(String serviceId, OnGetServiceUrlListener listener, Map<String, String> replacement) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, false, replacement);
    }

    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, true, null);
    }

    @Override
    public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener,
                                                   Map<String, String> replacement) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, true, replacement);
    }

    @Override
    public void getServicesWithLanguagePreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, false, null);
    }

    @Override
    public void getServicesWithLanguagePreference(ArrayList<String> serviceId, OnGetServiceUrlMapListener listener,
                                                  Map<String, String> replacement) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, false, replacement);
    }

    @Override
    public void getServicesWithCountryPreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, true, null);
    }

    @Override
    public void getServicesWithCountryPreference(ArrayList<String> serviceId, OnGetServiceUrlMapListener listener,
                                                 Map<String, String> replacement) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, true, replacement);
    }

    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        ServiceLocaleWithCountryorLanguagePreference(serviceId, listener, false);
    }

    @Override
    public void getServiceLocaleWithCountryPreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        ServiceLocaleWithCountryorLanguagePreference(serviceId, listener, true);
    }

    @Override
    public URL applyURLParameters(URL inputURL, Map<String, String> parameters) {
        String url = inputURL.toString();
        URL output;
        if (parameters != null && parameters.size() > 0) {
            for (Map.Entry<String, String> param : parameters.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                url = url.replace('%' + key + '%', value);
            }
        }
        try {
            output = new URL(url);
            return output;
        } catch (MalformedURLException ex) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery URL error",
                    "Malformed URL");
            return null;
        }
    }

    void serviceURLwithCountryorLanguagePreferences(final String serviceId, final OnGetServiceUrlListener listener,
                                                    final boolean isserviceURLwithCountryPreference,
                                                    final Map<String, String> replacement) {
        if (listener == null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "Service Discovery",
                    "OnGetServiceUrlListener is null initialized");
        } else {
            if (serviceId == null || serviceId.contains(",")) {
                listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
            } else {
                ServiceDiscovery service = serviceDiscovery;
                if (service != null && service.isSuccess()) {
                    if (isserviceURLwithCountryPreference) {
                        filterDataForUrlbyCountry(service, serviceId, listener, replacement);
                    } else {
                        filterDataForUrlbyLang(service, serviceId, listener, replacement);
                    }
                } else {
                    queueResultListener(false, new DownloadItemListener() {
                        @Override
                        public void onDownloadDone(ServiceDiscovery result) {
                            if (result.isSuccess()) {
                                if (isserviceURLwithCountryPreference) {
                                    filterDataForUrlbyCountry(result, serviceId, listener, replacement);
                                } else {
                                    filterDataForUrlbyLang(result, serviceId, listener, replacement);
                                }
                            } else {
                                if (result.getError() != null) {
                                    ServiceDiscovery.Error err = result.getError();
                                    listener.onError(err.getErrorvalue(), err.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        }

    }

    void ServicesWithLanguageorCountryPreference(final ArrayList<String> serviceIds, final OnGetServiceUrlMapListener listener,
                                                 final boolean isserviceswithCountryPreference, final Map<String, String> replacement) {
        if (listener == null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "Service Discovery",
                    "OnGetServiceUrlMapListener is null initialized");
        } else {
            if (serviceIds == null) {
                listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
            } else {
                ServiceDiscovery service = serviceDiscovery;
                if (service != null && service.isSuccess()) {
                    if (isserviceswithCountryPreference) {
                        filterDataForUrlbyCountry(service, serviceIds, listener, replacement);
                    } else {
                        filterDataForUrlbyLang(service, serviceIds, listener, replacement);
                    }
                } else {
                    queueResultListener(false, new DownloadItemListener() {
                        @Override
                        public void onDownloadDone(ServiceDiscovery result) {
                            if (result.isSuccess()) {
                                if (isserviceswithCountryPreference) {
                                    filterDataForUrlbyCountry(result, serviceIds, listener, replacement);
                                } else {
                                    filterDataForUrlbyLang(result, serviceIds, listener, replacement);
                                }
                            } else {
                                ServiceDiscovery.Error err = result.getError();
                                listener.onError(err.getErrorvalue(), err.getMessage());
                            }
                        }
                    });
                }
            }
        }

    }

    void ServiceLocaleWithCountryorLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener,
                                                      final boolean isServiceLocaleWithCountry) {
        if (listener == null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "Service Discovery",
                    "OnGetServiceLocaleListener is null initialized");
        } else {
            if (serviceId == null) {
                listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
            } else {
                ServiceDiscovery service = serviceDiscovery;
                if (service != null && service.isSuccess()) {
                    if (isServiceLocaleWithCountry) {
                        filterDataForLocalByCountry(service, listener);
                    } else {
                        filterDataForLocalByLang(service, listener);
                    }
                } else {
                    queueResultListener(false, new DownloadItemListener() {
                        @Override
                        public void onDownloadDone(ServiceDiscovery result) {
                            if (result.isSuccess()) {
                                if (isServiceLocaleWithCountry) {
                                    filterDataForLocalByCountry(result, listener);
                                } else {
                                    filterDataForLocalByLang(result, listener);
                                }
                            } else {
                                ServiceDiscovery.Error err = result.getError();
                                listener.onError(err.getErrorvalue(), err.getMessage());
                            }
                        }
                    });
                }
            }
        }

    }

    private void filterDataForUrlbyLang(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener,
                                        Map<String, String> replacement) {
        if (onGetServiceUrlListener != null && serviceId != null && service.getMatchByLanguage().getConfigs() != null) {
            if (service.getMatchByLanguage().getLocale() == null) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                        "ServiceDiscovery cannot find the locale");
            } else {
                String BYLANG = "bylang";
                getDataForUrl(service, serviceId, onGetServiceUrlListener, replacement, BYLANG);
            }
        } else if (onGetServiceUrlListener != null) {
            onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        }
    }

    private void filterDataForUrlbyCountry(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener,
                                           Map<String, String> replacement) {
        if (onGetServiceUrlListener != null && serviceId != null && service.getMatchByCountry().getConfigs() != null) {
            if (service.getMatchByCountry().getLocale() == null) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                        "ServiceDiscovery cannot find the locale");
            } else {
                String BYCOUNTRY = "bycountry";
                getDataForUrl(service, serviceId, onGetServiceUrlListener, replacement, BYCOUNTRY);
            }
        } else {
            if (onGetServiceUrlListener != null) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void getDataForUrl(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener
            onGetServiceUrlListener, Map<String, String> replacement, String match) {
        URL url = null;
        try {
            if (match.equals("bycountry")) {
                url = new URL(service.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceId));
            } else if (match.equals("bylang")) {
                url = new URL(service.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceId));
            }

            if (url != null) {
                if (url.toString().contains("%22")) {
                    url = new URL(url.toString().replace("%22", "\""));
                }
                if (replacement != null && replacement.size() > 0) {
                    URL replacedUrl = applyURLParameters(url, replacement);
                    onGetServiceUrlListener.onSuccess(replacedUrl);
                } else {
                    onGetServiceUrlListener.onSuccess(url);
                }
            } else {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                        "ServiceDiscovery cannot find the locale");
            }
        } catch (MalformedURLException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                    e.toString());
            onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        }
    }


    private void filterDataForLocalByLang(ServiceDiscovery service, OnGetServiceLocaleListener
            onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null && service.getMatchByLanguage().getConfigs() != null) {
            if (service.getMatchByLanguage().getLocale() == null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                        "ServiceDiscovery cannot find the locale");
            } else {
                onGetServiceLocaleListener.onSuccess(service.getMatchByLanguage().getLocale());
            }
        } else if (onGetServiceLocaleListener != null) {
            onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        }
    }

    private void filterDataForLocalByCountry(ServiceDiscovery service, OnGetServiceLocaleListener
            onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null && service.getMatchByCountry().getConfigs() != null) {
            if (service.getMatchByCountry().getLocale() == null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                        "ServiceDiscovery cannot find the locale");
            } else {
                onGetServiceLocaleListener.onSuccess(service.getMatchByCountry().getLocale());
            }
        } else {
            if (onGetServiceLocaleListener != null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void filterDataForUrlbyLang(ServiceDiscovery service, ArrayList<String> serviceIds,
                                        OnGetServiceUrlMapListener onGetServiceUrlMapListener, Map<String, String> replacement) {
        String dataByUrl = "urlbylanguage";
        if (onGetServiceUrlMapListener != null && serviceIds != null && service.getMatchByLanguage().getConfigs() != null) {
            final int configSize = service.getMatchByLanguage().getConfigs().size();
            getUrlsMapper(service, configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener, replacement);

        } else if (onGetServiceUrlMapListener != null) {
            onGetServiceUrlMapListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        }
    }

    private void filterDataForUrlbyCountry(ServiceDiscovery service, ArrayList<String> serviceIds,
                                           OnGetServiceUrlMapListener onGetServiceUrlMapListener,
                                           Map<String, String> replacement) {
        String dataByUrl = "urlbycountry";
        if (onGetServiceUrlMapListener != null && serviceIds != null && service.getMatchByCountry().getConfigs() != null) {
            final int configSize = service.getMatchByCountry().getConfigs().size();
            getUrlsMapper(service, configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener, replacement);
        } else {
            if (onGetServiceUrlMapListener != null) {
                onGetServiceUrlMapListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void getUrlsMapper(ServiceDiscovery service, int configSize, String urlByData, ArrayList<String> serviceIds,
                               OnGetServiceUrlMapListener onGetServiceUrlMapListener, Map<String, String> replacement) {
        Map<String, String> urls = null;
        String modelLocale = null;
        final HashMap<String, ServiceDiscoveryService> responseMap = new HashMap<>();
        for (int config = 0; config < configSize; config++) {
            if (urlByData.equalsIgnoreCase("urlbycountry")) {
                modelLocale = service.getMatchByCountry().getLocale();
                urls = service.getMatchByCountry().getConfigs().get(config).getUrls();
            } else if (urlByData.equalsIgnoreCase("urlbylanguage")) {
                modelLocale = service.getMatchByLanguage().getLocale();
                urls = service.getMatchByLanguage().getConfigs().get(config).getUrls();
            }

            for (int i = 0; i < serviceIds.size(); i++) {
                if (urls != null) {
                    for (final String key : urls.keySet()) {
                        if (key.equalsIgnoreCase(serviceIds.get(i).trim())) {
                            String serviceUrlval = urls.get(key);
                            if (serviceUrlval.contains("%22")) {
                                serviceUrlval = serviceUrlval.replace("%22", "\"");
                            }
                            if (replacement != null && replacement.size() > 0) {
                                URL replacedUrl;
                                try {
                                    replacedUrl = applyURLParameters(new URL(serviceUrlval), replacement);
                                    ServiceDiscoveryService sdService = new ServiceDiscoveryService();
                                    sdService.init(modelLocale, replacedUrl.toString());
                                    responseMap.put(key, sdService);
                                } catch (MalformedURLException e) {
                                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                                            "ServiceDiscovery URL error",
                                            "Malformed URL");
                                }
                            } else {
                                ServiceDiscoveryService sdService = new ServiceDiscoveryService();
                                sdService.init(modelLocale, serviceUrlval);
                                responseMap.put(key, sdService);
                            }
                        }
                    }
                } else {
                    onGetServiceUrlMapListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                            "ServiceDiscovery cannot find the locale");
                }
            }
        }
        if (responseMap.isEmpty()) {
            onGetServiceUrlMapListener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
                    "ServiceDiscovery cannot find the locale");
        } else {
            onGetServiceUrlMapListener.onSuccess(responseMap);
        }
    }


    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void refresh(final OnRefreshListener listener) { // TODO RayKlo: refresh only works if we have no data?
        refresh(listener, false);
    }

    @Override
    public void refresh(final OnRefreshListener listener, final boolean forcerefresh) {
        queueResultListener(forcerefresh, new DownloadItemListener() {
            @Override
            public void onDownloadDone(ServiceDiscovery result) {
                if (result.isSuccess()) {
                    listener.onSuccess();
                } else {
                    ServiceDiscovery.Error err = result.getError();
                    listener.onError(err.getErrorvalue(), err.getMessage());
                }
            }

            @Override
            public boolean forceRefresh() {
                return forcerefresh;
            }
        });
    }

    private String getCountry(ServiceDiscovery service) {

        if (countryCode != null) {
            return countryCode;
        }

        countryCode = fetchFromSecureStorage(true);
        String countrySource = fetchFromSecureStorage(false);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Country", countryCode);

        if (countryCode != null) {
            if (countrySource != null && countrySource.equalsIgnoreCase("SIMCARD")) {
                countryCodeSource = OnGetHomeCountryListener.SOURCE.SIMCARD;
            } else if (countrySource != null && countrySource.equalsIgnoreCase("GEOIP")) {
                countryCodeSource = OnGetHomeCountryListener.SOURCE.GEOIP;
            }
//            else {
//                countryCodeSource = OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE;
//            }

            countryCode = countryCode.toUpperCase();
            return countryCode;
        }

        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                countryCode = simCountry.toUpperCase(Locale.US);
                countryCodeSource = OnGetHomeCountryListener.SOURCE.SIMCARD;
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                final String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    countryCode = networkCountry.toUpperCase(Locale.US);
                    countryCodeSource = OnGetHomeCountryListener.SOURCE.SIMCARD;
                }
            }
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery URL error",
                    e.toString());
        }
        if (countryCode == null) {
            if (service != null && service.isSuccess()) {
                countryCode = service.getCountry();
                countryCodeSource = OnGetHomeCountryListener.SOURCE.GEOIP;
            }
        }
        if (countryCode != null) {
            saveToSecureStore(countryCode, true);
            saveToSecureStore(countryCodeSource.toString(), false);
        }
        return countryCode;
    }

    private void saveToSecureStore(String country, boolean isCountry) {
        SecureStorageInterface ssi = mAppInfra.getSecureStorage();
        SecureStorage.SecureStorageError mSecureStorage = new SecureStorage.SecureStorageError();
        if (isCountry) {
            ssi.storeValueForKey("Country", country, mSecureStorage);
        } else {
            ssi.storeValueForKey("COUNTRY_SOURCE", country, mSecureStorage);
        }
    }

    private String fetchFromSecureStorage(boolean isCountry) {
        SecureStorageInterface ssi = mAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        String value;
        if (isCountry) {
            value = ssi.fetchValueForKey("Country", sse);
        } else {
            value = ssi.fetchValueForKey("COUNTRY_SOURCE", sse);
        }
        return value;
    }

}
