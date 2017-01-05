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
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
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


    private OnGetHomeCountryListener.SOURCE countryCodeSource;
    private AppInfra mAppInfra;
    private Context context;
    ServiceDiscovery serviceDiscovery = null;
    private String countryCode;

    private RequestItemManager mRequestItemManager;

    //
    private boolean downloadInProgress;
    private ArrayDeque<DownloadItemListener> downloadAwaiters;
    private ReentrantLock downloadLock;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        mRequestItemManager = new RequestItemManager(context, mAppInfra);
        downloadInProgress = false;
        downloadAwaiters = new ArrayDeque<>();
        downloadLock = new ReentrantLock();
//        OnRefreshListener mOnRefreshListener = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

//        refresh(new OnRefreshListener() {
//            @Override
//            public void onSuccess() {
//                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "refresh ", "refresh" );
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "refresh", "refresh" );
//            }
//        });
    }

    private void queueResultListener(boolean forcerefresh, DownloadItemListener listener) {
        downloadLock.lock();

        if (forcerefresh)
            serviceDiscovery = null;
        downloadAwaiters.add(listener);

        if (!downloadInProgress) {
            downloadInProgress = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean forceRefresh = false;
                    ServiceDiscovery service;
                    ArrayList<DownloadItemListener> stalledAwaiters = new ArrayList<DownloadItemListener>();
                    do {
                        if (forceRefresh == true)
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
        }
        downloadLock.unlock();
    }


    /**
     * Precondition: download lock is acquired
     */
    private ServiceDiscovery downloadServices() {
        String urlBuild = buildUrl();

        ServiceDiscovery service = new ServiceDiscovery();
        service = mRequestItemManager.getServiceDiscoveryFromCache(urlBuild);
        if (null == service) {
            if (!isOnline()) {
                service.setError(new ServiceDiscovery.Error(OnErrorListener.ERRORVALUES.NO_NETWORK, "NO_NETWORK"));
                service.setSuccess(false);
            } else {
                //urlBuild = buildUrl();
                if (urlBuild != null) {
                    service = mRequestItemManager.execute(urlBuild);
                    Log.i("Request Call", "Request Call");
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Request Call", "Request Call");
                } else {
                    // TODO RayKlo ???
                }
            }
        }
        return service;
    }

    private String buildUrl() {
        try {
            final AppIdentityInterface identityManager = mAppInfra.getAppIdentity(); // TODO RayKlo don't recreate existing instances
            final InternationalizationInterface localManager = mAppInfra.getInternationalization();
            Locale localeForURL = localManager.getUILocale();
            final AppIdentityInterface.AppState state = identityManager.getAppState();
            String service_environment = identityManager.getServiceDiscoveryEnvironment();
            String tags = null;
            String environment = null;
            if (state != null && service_environment != null) {
                switch (state) {
                    case DEVELOPMENT:
                        tags = "apps%2b%2benv%2bdev";
                        break;
                    case STAGING:
                        tags = "apps%2b%2benv%2bstage";
                        break;
                    case ACCEPTANCE:
                        tags = "apps%2b%2benv%2bacc";
                        break;
                    case TEST:
                        tags = "apps%2b%2benv%2btest";
                        break;
                    case PRODUCTION:
                        tags = "apps%2b%2benv%2bprod";
                        break;
                    default:
                        tags = "apps%2b%2benv%2btest";
                }

                if (service_environment.equalsIgnoreCase("PRODUCTION")) {
                    environment = "www.philips.com";
                } else if (service_environment.equalsIgnoreCase("TEST")) {
                    environment = "tst.philips.com";
                } else if (service_environment.equalsIgnoreCase("STAGING")) {
                    environment = "dev.philips.com";
                } else if (service_environment.equalsIgnoreCase("ACCEPTANCE")) {
                    environment = "acc.philips.com";
                } else {
                    environment = "tst.philips.com";
                }
            } else {
                // TODO RayKlo ??
            }

            String url = null;

            if (identityManager.getSector() != null && identityManager.getMicrositeId() != null &&
                    localManager.getUILocale() != null && tags != null && environment != null) {
                url = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localeForURL.getLanguage() + "_" + localeForURL.getCountry() + "&tags=" + tags;

                String countryHome = getCountry(serviceDiscovery);
                if (countryHome != null) {
                    url += "&country=" + countryHome;
                }
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "build URL", "" + url);
            } else {
                // TODO RayKlo ??
            }
            return url;
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "ServiceDiscovery", e.toString());
        }
        return null;
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

        if (countryCode != null && countryCode.length()==2 ) {
            if( !countryCode.equals(getCountry(serviceDiscovery))) { // entered country is different then existing
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
            }else{
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
        ServicesWithLanguageorCountryPreference(serviceId, listener, false, replacement);
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
                            if (listener != null && result.getError() != null) {
                                ServiceDiscovery.Error err = result.getError();
                                listener.onError(err.getErrorvalue(), err.getMessage());
                            }
                        }
                    }
                });
            }
        }
    }

    void ServicesWithLanguageorCountryPreference(final ArrayList<String> serviceIds, final OnGetServiceUrlMapListener listener,
                                                 final boolean isserviceswithCountryPreference, final Map<String, String> replacement) {

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

    void ServiceLocaleWithCountryorLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener,
                                                      final boolean isServiceLocaleWithCountry) {

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

    private void filterDataForUrlbyLang(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener,
                                        Map<String, String> replacement) {
        if (onGetServiceUrlListener != null && serviceId != null && service.getMatchByLanguage().getConfigs() != null) {
            try {
                URL url = new URL(service.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceId));
                if (url.toString().contains("%22")) {
                    url = new URL(url.toString().replace("%22", "\""));
                }

                if (replacement != null && replacement.size() > 0) {
                    URL replacedUrl = applyURLParameters(url, replacement);
                    onGetServiceUrlListener.onSuccess(replacedUrl);
                } else {
                    onGetServiceUrlListener.onSuccess(url);
                }

            } catch (MalformedURLException e) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        } else if (onGetServiceUrlListener != null) {
            onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        }
    }

    private void filterDataForUrlbyCountry(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener,
                                           Map<String, String> replacement) {
        if (onGetServiceUrlListener != null && serviceId != null && service.getMatchByCountry().getConfigs() != null) {
            try {
                URL url = new URL(service.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceId));
                if (url.toString().contains("%22")) {
                    url = new URL(url.toString().replace("%22", "\""));
                }
                if (replacement != null && replacement.size() > 0) {
                    URL replacedUrl = applyURLParameters(url, replacement);
                    onGetServiceUrlListener.onSuccess(replacedUrl);
                } else {
                    onGetServiceUrlListener.onSuccess(url);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        } else {
            if (onGetServiceUrlListener != null) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void filterDataForLocalByLang(ServiceDiscovery service, OnGetServiceLocaleListener
            onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null && service.getMatchByLanguage().getConfigs() != null) {
            if (service.getMatchByLanguage().getLocale() == null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
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
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
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
                                           OnGetServiceUrlMapListener onGetServiceUrlMapListener, Map<String, String> replacement) {
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
        final HashMap<String, ServiceDiscoveyService> responseMap = new HashMap<>();
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
                                    ServiceDiscoveyService sdService = new ServiceDiscoveyService();
                                    sdService.init(modelLocale, replacedUrl.toString());
                                    responseMap.put(key, sdService);
                                } catch (MalformedURLException e) {
                                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                                            "ServiceDiscovery URL error",
                                            "Malformed URL");
                                }
                            } else {
                                ServiceDiscoveyService sdService = new ServiceDiscoveyService();
                                sdService.init(modelLocale, serviceUrlval);
                                responseMap.put(key, sdService);
                            }
                        }
                    }
                }
            }
        }
        if (responseMap.isEmpty()) {
            onGetServiceUrlMapListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
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
        mRequestItemManager.clearCacheServiceDiscovery(); // clear SD cache
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

    String getCountry(ServiceDiscovery service) {

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
