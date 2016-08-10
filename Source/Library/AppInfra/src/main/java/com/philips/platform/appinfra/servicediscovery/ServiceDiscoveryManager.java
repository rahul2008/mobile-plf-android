/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.internationalization.InternationalizationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.model.Error;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class downloads list of URLs from service discovery server,
 * providing the locale and the identity of the application.
 */
public class ServiceDiscoveryManager implements ServiceDiscoveryInterface, ServiceDiscoveryInterface
        .OnRefreshListener {

    private AppInfra mAppInfra;
    private Context context;
    private boolean isDataAvailable = false;
    private boolean isDownloadInProgress = false;
    private String countryCode;
    private String mUrl = null;
    private String mCountry;
    SharedPreferences pref = null;

    ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES mErrorValues = null;
    String errorMessage = null;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        OnRefreshListener mOnRefreshListener = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

        refresh(mOnRefreshListener);

    }


    private String getService(OnRefreshListener listener) {
        String urlBuild;
        final String country = getCountry();
        if (null != country) {
            urlBuild = buildUrl();
            if (urlBuild != null) {
                new RequestManager(context).execute(urlBuild, listener);
            }
        } else {
            urlBuild = buildUrl();
            if (urlBuild != null) {
                new RequestManager(context).execute(urlBuild = buildUrl(), listener);
            }
        }
        return urlBuild;
    }

    private String buildUrl() {
        final AppIdentityManager identityManager = new AppIdentityManager(mAppInfra);
        identityManager.loadJSONFromAsset();
        final InternationalizationManager localManager = new InternationalizationManager(mAppInfra);
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
            }

            if (service_environment.equalsIgnoreCase("PRODUCTION")) {
                environment = "www.philips.com";
            } else if (service_environment.equalsIgnoreCase("TEST")) {
                environment = "tst.philips.com";
            } else if (service_environment.equalsIgnoreCase("STAGING")) {
                environment = "dev.philips.com";
            } else if (service_environment.equalsIgnoreCase("ACCEPTANCE")) {
                environment = "acc.philips.com";
            }
        }

        if (identityManager.getSector() != null && identityManager.getMicrositeId() != null &&
                localManager.getUILocale() != null && tags != null && environment != null) {
            if (getCountry() == null) {
                mUrl = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localManager.getUILocale() + "&tags=" + tags;
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev";
                Log.i("URL", "" + mUrl);
            }
            if (getCountry() != null) {
                mUrl = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localManager.getUILocale() + "&tags=" + tags + "&country=" + getCountry();
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev&country=US";
                Log.i("URL", "" + mUrl);
            }
        }
        return mUrl;
    }

    /**
     * Gets the country from app according to settings/SIM/GEOIP
     *
     * @param listener
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {
        String country = getCountry();
        String countrySource = pref.getString(RequestManager.COUNTRY_SOURCE, null);

        if (country != null) {
            setHomeCountry(country);
            if (countrySource != null && countrySource.equalsIgnoreCase("SIMCARD")) {
                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.SIMCARD);
            } else if (countrySource != null && countrySource.equalsIgnoreCase("GEOIP")) {
                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
            } else {
                listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE);
            }

        } else {
            refresh(new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {
                    listener.onError(error, message);
                }

                @Override
                public void onSuccess() {
                    String country = getCountry();
                    listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
                }
            });
        }
    }

    @Override
    public void setHomeCountry(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public void getServiceUrlWithLanguagePreference(final String serviceId, final OnGetServiceUrlListener listener) {
        if(serviceId.contains(",") || serviceId == null){
            listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        }else{
            if (isDataAvailable) {
                filterDataForUrlbyLang(serviceId, listener);
            } else {
                if (!isDownloadInProgress) {
                    refresh(new OnRefreshListener() {
                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            listener.onError(error, message);
                        }

                        @Override
                        public void onSuccess() {
                            filterDataForUrlbyLang(serviceId, listener);
                        }
                    });
                } else {
                    listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                }

            }
        }

    }

    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        if(serviceId.contains(",")|| serviceId == null){
            listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        }else {
            if (isDataAvailable) {
                filterDataForUrlbyCountry(serviceId, listener);
            } else {
                if (!isDownloadInProgress) {
                    refresh(new OnRefreshListener() {
                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            listener.onError(error, message);
                        }

                        @Override
                        public void onSuccess() {
                            filterDataForUrlbyCountry(serviceId, listener);
                        }
                    });
                } else {
                    listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                }

            }
        }

    }

    @Override
    public void getServiceUrlWithLanguagePreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {

        if(serviceId == null){
            listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        }else{
            if (isDataAvailable) {
                filterDataForUrlbyLang(serviceId, listener);
            } else {
                if (!isDownloadInProgress) {
                    refresh(new OnRefreshListener() {
                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            listener.onError(error, message);
                        }

                        @Override
                        public void onSuccess() {
                            filterDataForUrlbyLang(serviceId, listener);
                        }
                    });
                } else {
                    listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                }
            }
        }

    }

    @Override
    public void getServiceUrlWithCountryPreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        if(serviceId == null){
            listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_INPUT");
        }else{
            if (isDataAvailable) {
                filterDataForUrlbyCountry(serviceId, listener);
            } else {
                if (!isDownloadInProgress) {
                    refresh(new OnRefreshListener() {
                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            listener.onError(error, message);
                        }

                        @Override
                        public void onSuccess() {
                            filterDataForUrlbyCountry(serviceId, listener);
                        }
                    });
                } else {
                    listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                }
            }
        }

    }


    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {

        if (isDataAvailable) {
            filterDataForLocalByLang(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh(new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForLocalByLang(serviceId, listener);
                    }
                });
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }
        }
    }

    @Override
    public void getServiceLocaleWithCountryPreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        if (isDataAvailable) {
            filterDataForLocalByCountry(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh(new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForLocalByCountry(serviceId, listener);
                    }
                });
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }
        }
    }


    private void filterDataForUrlbyLang(String serviceIds, OnGetServiceUrlListener onGetServiceUrlListener) {
        if (onGetServiceUrlListener != null && serviceIds != null) {
            try {
                final URL url = new URL(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds));
                if (url == null) {
                    onGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
                } else {
                    onGetServiceUrlListener.onSuccess(url);
                }
            } catch (MalformedURLException e) {
                onGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }
        }
    }

    private void filterDataForUrlbyLang(ArrayList<String> serviceIds, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        String dataByUrl = "urlbylanguage";
        if (onGetServiceUrlMapListener != null && serviceIds != null) {
            final int configSize = RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().size();
            getUrlsMapper(configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener);
        }
    }


    private void filterDataForUrlbyCountry(String serviceIds, OnGetServiceUrlListener onGetServiceUrlListener) {
        if (onGetServiceUrlListener != null && serviceIds != null) {
            try {
                URL url = new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds));
                if (url == null) {
                    onGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
                } else {
                    onGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds)));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                onGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            }

        }
    }

    private void filterDataForUrlbyCountry(ArrayList<String> serviceIds, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        String dataByUrl = "urlbycountry";
        if (onGetServiceUrlMapListener != null && serviceIds != null) {
            final int configSize = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().size();
            getUrlsMapper(configSize, dataByUrl, serviceIds, onGetServiceUrlMapListener);
        }
    }

    private void getUrlsMapper(int configSize, String urlByData, ArrayList<String> serviceIds,
                               OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        Map<String, String> urls = null;
        final HashMap<String, String> responseMap = new HashMap<String, String>();
        for (int config = 0; config < configSize; config++) {
            if (urlByData.equalsIgnoreCase("urlbycountry")) {
                urls = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(config).getUrls();
            } else if (urlByData.equalsIgnoreCase("urlbylanguage")) {
                urls = RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(config).getUrls();
            }

            for (int i = 0; i < serviceIds.size(); i++) {
                if (urls != null) {
                    for (final String key : urls.keySet()) {
                        if (key.equalsIgnoreCase(serviceIds.get(i).trim())) {
                            final String serviceUrlval = urls.get(key);
                            Log.d("SERVICE DISCOVERY", serviceUrlval);
                            responseMap.put(key, serviceUrlval);
                        }
                    }
                }
            }
        }
        if (responseMap.isEmpty()) {
            onGetServiceUrlMapListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
        } else {
            onGetServiceUrlMapListener.onSuccess(responseMap);
        }
    }


    private void filterDataForLocalByLang(String serviceIds, OnGetServiceLocaleListener onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null) {
            if (RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale() == null) {
                onGetServiceLocaleListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            } else {
                onGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale());
            }
        }
    }

    private void filterDataForLocalByCountry(String serviceIds, OnGetServiceLocaleListener onGetServiceLocaleListener) {
        if (onGetServiceLocaleListener != null) {
            if (RequestManager.mServiceDiscovery.getMatchByCountry().getLocale() == null) {
                onGetServiceLocaleListener.onError(ERRORVALUES.INVALID_RESPONSE, "NO VALUE FOR KEY");
            } else {
                onGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void refresh(final OnRefreshListener listener) {

        if (isOnline()) {
            if (RequestManager.mServiceDiscovery != null) {
                if (RequestManager.mServiceDiscovery.isSuccess()) {
                    isDataAvailable = true;
                    isDownloadInProgress = false;
                    listener.onSuccess();
                } else {
                    if (mErrorValues != null && errorMessage != null) {
                        listener.onError(mErrorValues, errorMessage);
                    } else {
                        listener.onError(ERRORVALUES.SERVER_ERROR, "SERVER_ERROR");
                    }

                    isDownloadInProgress = false;
                }
            } else {
                getService(listener);
                isDownloadInProgress = true;
            }
        } else {
            listener.onError(ERRORVALUES.NO_NETWORK, "NO_NETWORK");
        }


    }

    @Override
    public void onSuccess() {
        Log.i("Refresh Success", "Refresh Success");
        if (RequestManager.mServiceDiscovery != null) {
            if (RequestManager.mServiceDiscovery.isSuccess()) {
                isDataAvailable = true;
                isDownloadInProgress = false;
            }
        }
    }

    @Override
    public void onError(ERRORVALUES error, String message) {
        Log.i("Refresh Failed" + error, "Refresh error " + message);
        mErrorValues = error;
        errorMessage = message;
        isDownloadInProgress = false;
    }

    private String getCountry() {
        pref = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);

        if (mCountry == null) {
            mCountry = pref.getString(RequestManager.COUNTRY_NAME, null);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Country", mCountry);
            if (mCountry != null) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(RequestManager.COUNTRY_NAME, mCountry.toUpperCase());
                editor.putString(RequestManager.COUNTRY_SOURCE, OnGetHomeCountryListener.SOURCE.STOREDPREFERENCE.toString());
                editor.commit();

                return mCountry.toUpperCase(); // Return Country
            }
        }
        if (mCountry == null) {

            try {
                SharedPreferences.Editor editor = pref.edit();
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    mCountry = simCountry.toUpperCase(Locale.US);
                    editor.putString(RequestManager.COUNTRY_NAME, mCountry.toUpperCase());
                    editor.putString(RequestManager.COUNTRY_SOURCE, OnGetHomeCountryListener.SOURCE.SIMCARD.toString());
                    editor.commit();
                    if (mCountry != null)
                        return mCountry.toUpperCase();
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                    final String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        mCountry = networkCountry.toUpperCase(Locale.US);
                        editor.putString(RequestManager.COUNTRY_NAME, mCountry.toUpperCase());
                        editor.putString(RequestManager.COUNTRY_PRREFERENCE, OnGetHomeCountryListener.SOURCE.SIMCARD.toString());
                        editor.commit();
                        if (mCountry != null) {
                            return mCountry.toUpperCase();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return mCountry;
    }

}
