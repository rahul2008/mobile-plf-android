/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.internationalization.InternationalizationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ServiceDiscoveryManager implements ServiceDiscoveryInterface, ServiceDiscoveryInterface.OnRefreshListener {

    AppInfra mAppInfra;
    Context context;
    boolean isDataAvailable = false;
    boolean isDownloadInProgress = false;
    String countryCode;
    String URL = null;
    OnRefreshListener mOnRefreshListener;
    String mCountry;
    SharedPreferences pref;


    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        mOnRefreshListener = this;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

        refresh(mOnRefreshListener);

    }


    private String getService(OnRefreshListener listener) {
        String urlBuild = null;
        String country = getCountry();
        if (null != country) {
            urlBuild = buildUrl();
            if (urlBuild != null) {
                new RequestManager(context).execute(urlBuild, listener);
            }
        } else {
            urlBuild = buildUrl();
            if (urlBuild != null)
                new RequestManager(context).execute(urlBuild = buildUrl(), listener);
        }


        return urlBuild;
    }

    private String buildUrl() {
        AppIdentityManager identityManager = new AppIdentityManager(mAppInfra);
        identityManager.loadJSONFromAsset();
        InternationalizationManager localManager = new InternationalizationManager(mAppInfra);
        localManager.getUILocale();
        AppIdentityInterface.AppState mState = identityManager.getAppState();
        String mEnvironment = identityManager.getServiceDiscoveryEnvironment();
        String tags = null;
        String environment = null;
        if (mState != null && mEnvironment != null) {
            switch (mState) {
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

            if (mEnvironment.equalsIgnoreCase("PRODUCTION")) {
                environment = "www.philips.com";
            } else if (mEnvironment.equalsIgnoreCase("TEST")) {
                environment = "tst.philips.com";
            } else if (mEnvironment.equalsIgnoreCase("STAGING")) {
                environment = "dev.philips.com";
            } else if (mEnvironment.equalsIgnoreCase("ACCEPTANCE")) {
                environment = "acc.philips.com";
            }
        }

        if (identityManager.getSector() != null && identityManager.getMicrositeId() != null && localManager.getUILocale() != null && tags != null && environment != null) {
            if (getCountry() == null) {
                URL = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localManager.getUILocale() + "&tags=" + tags;
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev";
                Log.i("URL", "" + URL);
            }
            if (getCountry() != null) {
                URL = "https://" + environment + "/api/v1/discovery/" + identityManager.getSector() + "/" + identityManager.getMicrositeId() + "?locale=" + localManager.getUILocale() + "&tags=" + tags + "&country=" + getCountry();
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev&country=US";
                Log.i("URL", "" + URL);
            }
        }
        return URL;
    }

    /**
     * Gets the country from app according to settings/SIM/GEOIP
     *
     * @param listener
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {
        String country = null;
        country = getCountry();
        if (country != null) {
            setHomeCountry(country);
            listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
        }
    }

    @Override
    public void setHomeCountry(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public void getServiceUrlWithLanguagePreference(final String serviceId, final OnGetServiceUrlListener listener) {
        if (isDataAvailable) {
            filterDataForURLbyLang(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForURLbyLang(serviceId, listener);
                    }
                }));
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }

    }

    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        if (isDataAvailable) {
            filterDataForURLbyCountry(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForURLbyCountry(serviceId, listener);
                    }
                }));
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }

    }

    @Override
    public void getServiceUrlWithLanguagePreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        if (isDataAvailable) {
            filterDataForURLbyLang(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForURLbyLang(serviceId, listener);
                    }
                }));
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }
    }

    @Override
    public void getServiceUrlWithCountryPreference(final ArrayList<String> serviceId, final OnGetServiceUrlMapListener listener) {
        if (isDataAvailable) {
            filterDataForURLbyCountry(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForURLbyCountry(serviceId, listener);
                    }
                }));
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }
    }


    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {

        if (isDataAvailable) {
            filterDataForLocalByLang(serviceId, listener);
        } else {
            if (!isDownloadInProgress) {
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForLocalByLang(serviceId, listener);
                    }
                }));
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
                refresh((new OnRefreshListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        listener.onError(error, message);
                    }

                    @Override
                    public void onSuccess() {
                        filterDataForLocalByCountry(serviceId, listener);
                    }
                }));
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }
        }
    }


    private void filterDataForURLbyLang(String serviceIds, OnGetServiceUrlListener mOnGetServiceUrlListener) {
        if (mOnGetServiceUrlListener != null && serviceIds != null) {
            try {
                URL url = new URL(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds));
                if (url == null) {
                    mOnGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                } else {
                    mOnGetServiceUrlListener.onSuccess(url);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                mOnGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }
    }

    private void filterDataForURLbyLang(ArrayList<String> serviceIds, OnGetServiceUrlMapListener mOnGetServiceUrlMapListener) {
        String dataByUrl = "urlbylanguage";
        if (mOnGetServiceUrlMapListener != null && serviceIds != null) {
            int configSize = RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().size();
            getUrlsMapper(configSize, dataByUrl, serviceIds, mOnGetServiceUrlMapListener);
        }
    }


    private void filterDataForURLbyCountry(String serviceIds, OnGetServiceUrlListener mOnGetServiceUrlListener) {
        if (mOnGetServiceUrlListener != null && serviceIds != null) {
            try {
                URL url = new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds));
                if (url == null) {
                    mOnGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
                } else {
                    mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds)));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                mOnGetServiceUrlListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            }

        }
    }

    private void filterDataForURLbyCountry(ArrayList<String> serviceIds, OnGetServiceUrlMapListener mOnGetServiceUrlMapListener) {
        String dataByUrl = "urlbycountry";
        if (mOnGetServiceUrlMapListener != null && serviceIds != null) {
            int configSize = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().size();
            getUrlsMapper(configSize, dataByUrl, serviceIds, mOnGetServiceUrlMapListener);
        }
    }

    private void getUrlsMapper(int configSize, String urlByData, ArrayList<String> serviceIds, OnGetServiceUrlMapListener mOnGetServiceUrlMapListener) {
        Map<String, String> urls = null;
        HashMap<String, String> responseMap = new HashMap<String, String>();
        for (int config = 0; config < configSize; config++) {
            if (urlByData.equalsIgnoreCase("urlbycountry")) {
                urls = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(config).getUrls();
            } else if (urlByData.equalsIgnoreCase("urlbylanguage")) {
                urls = RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(config).getUrls();
            }

            for (int i = 0; i < serviceIds.size(); i++) {
                if (urls != null) {
                    for (String key : urls.keySet()) {
                        if (key.equalsIgnoreCase(serviceIds.get(i).trim())) {
                            String serviceUrlval = urls.get(key);
                            Log.d("SERVICE DISCOVERY", serviceUrlval);
                            responseMap.put(key, serviceUrlval);
                        }
                    }
                }
            }
        }
        if (responseMap.size() == 0) {
            mOnGetServiceUrlMapListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
        } else {
            mOnGetServiceUrlMapListener.onSuccess(responseMap);
        }
    }


    private void filterDataForLocalByLang(String serviceIds, OnGetServiceLocaleListener mOnGetServiceLocaleListener) {
        if (mOnGetServiceLocaleListener != null) {
            if (RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale() == null) {
                mOnGetServiceLocaleListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            } else {
                mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale());
            }


        }
    }

    private void filterDataForLocalByCountry(String serviceIds, OnGetServiceLocaleListener mOnGetServiceLocaleListener) {
        if (mOnGetServiceLocaleListener != null) {
            if (RequestManager.mServiceDiscovery.getMatchByCountry().getLocale() == null) {
                mOnGetServiceLocaleListener.onError(ERRORVALUES.INVALID_RESPONSE, "INVALID_RESPONSE");
            } else {
                mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
            }
        }
    }


    @Override
    public void refresh(final OnRefreshListener listener) {

        if (RequestManager.mServiceDiscovery != null) {
            if (RequestManager.mServiceDiscovery.isSuccess()) {
                isDataAvailable = true;
                isDownloadInProgress = false;
                listener.onSuccess();
            } else {
                listener.onError(ERRORVALUES.INVALID_RESPONSE, "ERROR");
                isDownloadInProgress = false;
            }
        } else {
            getService(listener);
            isDownloadInProgress = true;
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
        isDownloadInProgress = false;
    }

    private String getCountry() {
        pref = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);
        if (mCountry == null) {
            mCountry = pref.getString(RequestManager.COUNTRY_NAME, null);
            // Log.i("Country", " "+mCountry);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "Country", mCountry);
            if (mCountry != null) {
                if (mAppInfra.getTagging() != null) {
                    mAppInfra.getTagging().trackActionWithInfo("InternationalizationPage", "KeyCountry", "ValueCountry");
                }
                return mCountry.toUpperCase();
            }
        }
        if (mCountry == null) {
            SharedPreferences.Editor editor = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE).edit();
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    mCountry = simCountry.toUpperCase(Locale.US);

                    editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                    editor.commit();
                    if (mCountry != null)
                        return mCountry.toUpperCase();
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        mCountry = networkCountry.toUpperCase(Locale.US);
                        editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                        editor.commit();
                        if (mCountry != null)
                            return mCountry.toUpperCase();
                    }
                }
            } catch (Exception e) {
            }
        }

        return mCountry;
    }

}
