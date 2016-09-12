/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.concurrent.locks.Lock;
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

    public static final String COUNTRY_PRREFERENCE = "COUNTRY_PRREFERENCE";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String COUNTRY_SOURCE = "COUNTRY_SOURCE";

    private OnGetHomeCountryListener.SOURCE countryCodeSource;

    public static final String CANCELLED_REQUEST = "Request cancelled";

    private AppInfra mAppInfra;
    private Context context;
    private ServiceDiscovery serviceDiscovery = null;
    boolean isDataAvailable = false;
    public static boolean isDownloadInProgress = false;
    private String countryCode;
    private String mUrl = null;
    String mCountry;

    OnErrorListener.ERRORVALUES mErrorValues = null;
    String errorMessage = null;

    RequestItemManager mRequestItemManager;

    //
    private boolean downloadInProgress;
    private ArrayDeque<DownloadItemListener> downloadAwaiters;
    private ReentrantLock downloadLock ;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        mRequestItemManager = new RequestItemManager(context,mAppInfra);
        downloadInProgress = false;
        downloadAwaiters = new ArrayDeque<DownloadItemListener>();
        downloadLock =new ReentrantLock();
//        OnRefreshListener mOnRefreshListener = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

//        refresh(null);
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
        String urlBuild = null;
        ServiceDiscovery service = new ServiceDiscovery();

        if (!isOnline()) {
            service.setError(new ServiceDiscovery.Error(OnErrorListener.ERRORVALUES.NO_NETWORK, "NO_NETWORK"));
            service.setSuccess(false);
        } else {
            urlBuild = buildUrl();
            if (urlBuild != null) {
                service = mRequestItemManager.execute(urlBuild);
                Log.i("Request Call", "Request Call");
            } else {
                // TODO RayKlo ???
            }
        }
        return service;
    }

//    String getService(final OnRefreshListener listener) {
//        final String urlBuild;
//        final String country = getCountry();
//
//        if (null != country) {
//            urlBuild = buildUrl(country);
//            if (urlBuild != null) {
//                new RequestManager(context, mAppInfra).execute(urlBuild, listener);
//
//            }
//        } else {
//            urlBuild = buildUrl(country);
//            if (urlBuild != null) {
//                new RequestManager(context, mAppInfra).execute(urlBuild, listener);
//
//            }
//        }
//        return urlBuild;
//    }

    String buildUrl() {
        final AppIdentityInterface identityManager = mAppInfra.getAppIdentity(); // TODO RayKlo don't recreate existing instances
        final InternationalizationInterface localManager = mAppInfra.getInternationalization();
        localManager.getUILocale();
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
            url = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localManager.getUILocale() + "&tags=" + tags;

            String countryHome = getCountry(serviceDiscovery);
            if (countryHome != null) {
                url += "&country=" + countryHome;
            }
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "URL", "" + url);
        } else {
            // TODO RayKlo ??
        }
        return url;
    }

    /**
     * Gets the country from app according to settings/SIM/GEOIP
     *
     * @param listener
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {
//        String country = getCountry();
//        String countrySource = fetchFromSecureStorage(false);
//
//        if (country != null) {
//            setHomeCountry(country);
//            if (countrySource != null && countrySource.equalsIgnoreCase("SIMCARD")) {
//                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.SIMCARD);
//            } else if (countrySource != null && countrySource.equalsIgnoreCase("GEOIP")) {
//                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
//            } else if(countrySource != null && countrySource.equalsIgnoreCase("STOREDPREFERENCE")) {
//                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE);
//            }
//
//        } else {
//            refresh(new OnRefreshListener() {
//                @Override
//                public void onError(ERRORVALUES error, String message) {
//                    listener.onError(error, message);
//                }
//
//                @Override
//                public void onSuccess() {
//                    String country = getCountry();
//                    listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
//                }
//            });
//        }

        String country = getCountry(serviceDiscovery);

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
        this.countryCode = countryCode;
        if(countryCode != null){
            countryCodeSource = OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE;
            saveToSecureStore(countryCode, true);
            saveToSecureStore(countryCodeSource.toString(), false);
            queueResultListener(true, new DownloadItemListener() {
                @Override
                public void onDownloadDone(ServiceDiscovery result) {

                }
            });
        }
    }

    @Override
    public void getServiceUrlWithLanguagePreference(String serviceId, OnGetServiceUrlListener listener) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, false);
    }

    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        serviceURLwithCountryorLanguagePreferences(serviceId, listener, true);
    }

    @Override
    public void getServicesWithLanguagePreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, false);
    }

    @Override
    public void getServicesWithCountryPreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        ServicesWithLanguageorCountryPreference(serviceId, listener, true);
    }

    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {

        ServiceLocaleWithCountryorLanguagePreference(serviceId, listener, false);
    }

    @Override
    public void getServiceLocaleWithCountryPreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        ServiceLocaleWithCountryorLanguagePreference(serviceId, listener, true);
    }

    void serviceURLwithCountryorLanguagePreferences(final String serviceId, final OnGetServiceUrlListener listener, final boolean isserviceURLwithCountryPreference) {
//        if ( serviceId == null || serviceId.contains(",")) {
//            listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
//        } else {
//            if (isDataAvailable) {
//                if (isserviceURLwithCountryPreference) {
//                    filterDataForUrlbyCountry(serviceId, listener);
//                } else {
//                    filterDataForUrlbyLang(serviceId, listener);
//                }
//            } else {
//                if (!isDownloadInProgress) {
//                    refresh(new OnRefreshListener() {
//                        @Override
//                        public void onError(ERRORVALUES error, String message) {
//                            listener.onError(error, message);
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            if (isserviceURLwithCountryPreference) {
//                                filterDataForUrlbyCountry(serviceId, listener);
//                            } else {
//                                filterDataForUrlbyLang(serviceId, listener);
//                            }
//                        }
//                    });
//                } else if(listener != null) {
//                    listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
//                }else{
//                    Log.i("null interface found", "null interface found");
//                }
//
//            }
//        }

        if (serviceId.contains(",") || serviceId == null) {
            listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        } else {
            ServiceDiscovery service = serviceDiscovery;
            if (service != null && service.isSuccess()) {
                if (isserviceURLwithCountryPreference) {
                    filterDataForUrlbyCountry(service, serviceId, listener);
                } else {
                    filterDataForUrlbyLang(service, serviceId, listener);
                }
            } else {
                queueResultListener(false, new DownloadItemListener() {
                    @Override
                    public void onDownloadDone(ServiceDiscovery result) {
                        if (result.isSuccess()) {
                            if (isserviceURLwithCountryPreference) {
                                filterDataForUrlbyCountry(result, serviceId, listener);
                            } else {
                                filterDataForUrlbyLang(result, serviceId, listener);
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

    void ServicesWithLanguageorCountryPreference(final ArrayList<String> serviceIds, final OnGetServiceUrlMapListener listener, final boolean isserviceswithCountryPreference) {
//        if (serviceId == null) {
//            listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
//        } else {
//            if (isDataAvailable) {
//                if (isserviceswithCountryPreference) {
//                    filterDataForUrlbyCountry(serviceId, listener);
//                } else {
//                    filterDataForUrlbyLang(serviceId, listener);
//                }
//            } else {
//                if (!isDownloadInProgress) {
//                    refresh(new OnRefreshListener() {
//                        @Override
//                        public void onError(ERRORVALUES error, String message) {
//                            listener.onError(error, message);
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            if (isserviceswithCountryPreference) {
//                                filterDataForUrlbyCountry(serviceId, listener);
//                            } else {
//                                filterDataForUrlbyLang(serviceId, listener);
//                            }
//                        }
//                    });
//                } else if(listener != null) {
//                    listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
//                }else{
//                    Log.i("null interface found", "null interface found");
//                }
//            }
//        }

        if (serviceIds == null) {
            listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        } else {
            ServiceDiscovery service = serviceDiscovery;
            if (service != null && service.isSuccess()) {
                if (isserviceswithCountryPreference) {
                    filterDataForUrlbyCountry(service, serviceIds, listener);
                } else {
                    filterDataForUrlbyLang(service, serviceIds, listener);
                }
            } else {
                queueResultListener(false, new DownloadItemListener() {
                    @Override
                    public void onDownloadDone(ServiceDiscovery result) {
                        if (result.isSuccess()) {
                            if (isserviceswithCountryPreference) {
                                filterDataForUrlbyCountry(result, serviceIds, listener);
                            } else {
                                filterDataForUrlbyLang(result, serviceIds, listener);
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

    void ServiceLocaleWithCountryorLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener, final boolean isServiceLocaleWithCountry) {
//        if (isDataAvailable) {
//            if (isServiceLocaleWithCountry) {
//                filterDataForLocalByCountry(serviceId, listener);
//            } else {
//                filterDataForLocalByLang(serviceId, listener);
//            }
//
//        } else {
//            if (!isDownloadInProgress) {
//                refresh(new OnRefreshListener() {
//                    @Override
//                    public void onError(ERRORVALUES error, String message) {
//                        listener.onError(error, message);
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        if (isServiceLocaleWithCountry) {
//                            filterDataForLocalByCountry(serviceId, listener);
//                        } else {
//                            filterDataForLocalByLang(serviceId, listener);
//                        }
//                    }
//                });
//            } else if(listener != null) {
//                listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
//            }else{
//                Log.i("null interface found", "null interface found");
//            }
//        }

        if (serviceId == null) {
            listener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        } else {
            ServiceDiscovery service = serviceDiscovery;
            if (service != null && service.isSuccess()) {
                if (isServiceLocaleWithCountry) {
                    filterDataForLocalByCountry(service, serviceId, listener);
                } else {
                    filterDataForLocalByLang(service, serviceId, listener);
                }
            } else {
                queueResultListener(false, new DownloadItemListener() {
                    @Override
                    public void onDownloadDone(ServiceDiscovery result) {
                        if (result.isSuccess()) {
                            if (isServiceLocaleWithCountry) {
                                filterDataForLocalByCountry(result, serviceId, listener);
                            } else {
                                filterDataForLocalByLang(result, serviceId, listener);
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

    private void filterDataForUrlbyLang(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener) {
        if (onGetServiceUrlListener != null && serviceId != null) {
            try {
                final URL url = new URL(service.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceId));
                if (url == null) {
                    onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
                } else {
                    onGetServiceUrlListener.onSuccess(url);
                }
            } catch (MalformedURLException e) {
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void filterDataForUrlbyCountry(ServiceDiscovery service, String serviceId, OnGetServiceUrlListener onGetServiceUrlListener) {
        if (onGetServiceUrlListener != null && serviceId != null) {
            try {
                URL url = new URL(service.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceId));
                if (url == null) {
                    onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
                } else {
                    onGetServiceUrlListener.onSuccess(new URL(service.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceId)));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                onGetServiceUrlListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }

        }
    }

    private void filterDataForLocalByLang(ServiceDiscovery service, String serviceId, OnGetServiceLocaleListener onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null) {
            if (service.getMatchByLanguage().getLocale() == null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            } else {
                onGetServiceLocaleListener.onSuccess(service.getMatchByLanguage().getLocale());
            }
        }
    }

    private void filterDataForLocalByCountry(ServiceDiscovery service, String serviceId, OnGetServiceLocaleListener onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null) {
            if (service.getMatchByCountry().getLocale() == null) {
                onGetServiceLocaleListener.onError(OnErrorListener.ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            } else {
                onGetServiceLocaleListener.onSuccess(service.getMatchByCountry().getLocale());
            }
        }
    }

    private void filterDataForUrlbyLang(ServiceDiscovery service, ArrayList<String> serviceIds, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        String dataByUrl = "urlbylanguage";
        if (onGetServiceUrlMapListener != null && serviceIds != null) {
            final int configSize = service.getMatchByLanguage().getConfigs().size();
            getUrlsMapper(service, configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener);
        }
    }

    private void filterDataForUrlbyCountry(ServiceDiscovery service, ArrayList<String> serviceIds, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        String dataByUrl = "urlbycountry";
        if (onGetServiceUrlMapListener != null && serviceIds != null) {
            final int configSize = service.getMatchByCountry().getConfigs().size();
            getUrlsMapper(service, configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener);
        }
    }

    private void getUrlsMapper(ServiceDiscovery service, int configSize, String urlByData, ArrayList<String> serviceIds,
                               OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        Map<String, String> urls = null;
        String modelLocale = null;
        final HashMap<String, ServiceDiscoveyService> responseMap = new HashMap<String, ServiceDiscoveyService>();
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
                            final String serviceUrlval = urls.get(key);
                            ServiceDiscoveyService sdService = new ServiceDiscoveyService();
                            sdService.init(modelLocale, serviceUrlval);
                            responseMap.put(key, sdService);
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

        if (countryCode != null)
            return countryCode;


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
        String value = null;
        if (isCountry) {
            value = ssi.fetchValueForKey("Country", sse);
        } else {
            value = ssi.fetchValueForKey("COUNTRY_SOURCE", sse);
        }
        return value;
    }

}
