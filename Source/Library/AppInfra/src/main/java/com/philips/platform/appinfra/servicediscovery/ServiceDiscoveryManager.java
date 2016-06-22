/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.AppInfra;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class ServiceDiscoveryManager implements ServiceDiscoveryInterface {

    AppInfra mAppInfra;
    Context context;
    Context localContext;
    boolean isDataAvailable = false;
    String countryCode;
    String URL = null;
    boolean mHomeCountry = false;
    boolean mServiceUrlWithLanguagePreference = false;
    boolean mServiceUrlWithCountryPreference= false;
    boolean mServiceLocaleWithLanguagePreference= false;
    boolean mServiceLocaleWithCountryPreference= false;
    boolean mServicesWithLanguagePreferenceMultiple= false;
    boolean mmServiceUrlWithCountryPreferenceMultiple= false;

    OnGetServicesListener mOnGetServicesListener;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }


    public String getservice(OnRefreshListener listener) {
        String urlBuild = null;
        LocalManager locamManager= new LocalManager(mAppInfra);
        String country= locamManager.getCountry();
        if(null!=country){
            urlBuild= buildUrl();
            if(urlBuild!= null){
                new RequestManager(context).execute(urlBuild, listener);
            }
        }else{
            urlBuild= buildUrl();
            if(urlBuild!= null)
            new RequestManager(context).execute(urlBuild= buildUrl(), listener);
        }



    return urlBuild;
    }
    private String  buildUrl(){
        AppIdentityManager idntityManager = new AppIdentityManager(mAppInfra);
//        idntityManager.loadJSONFromAsset();
        LocalManager localmanager= new LocalManager(mAppInfra);
        localmanager.getlocal();
        String mState = idntityManager.getAppState();
        String tags = null;
        String environment = null;

        if(mState.contains("DEVELOPMENT")){
            tags="apps%2b%2benv%2bdev";
            environment = "tst";
        }
        else if(mState.contains("TEST")){
            tags="apps%2b%2benv%2btst";
            environment = "tst";
        }
        else if(mState.contains("STAGING")){
            tags="apps%2b%2benv%2bstag";
            environment = "acc";
        }
        else if(mState.contains("ACCEPTANCE")){
            tags="apps%2b%2benv%2bacc";
            environment = "acc";
        }
        else if(mState.contains("PRODUCTION")){
            tags="apps%2b%2benv%2bprd";
            environment = "www";
        }
       if(localmanager.getCountry() == null && idntityManager.getSector() != null && idntityManager.getMicrositeId() != null && localmanager.getlocal() != null && tags!=null && environment!=null){
            URL = "https://"+environment+".philips.com/api/v1/discovery/"+idntityManager.getSector()+"/"+idntityManager.getMicrositeId()+"?locale="+ localmanager.getlocal()+"&tags="+tags;
        }
        if(localmanager.getCountry() != null && idntityManager.getSector() != null && idntityManager.getMicrositeId() != null && localmanager.getlocal() != null && tags!=null && environment!=null ){
            URL = "https://"+environment+".philips.com/api/v1/discovery/"+idntityManager.getSector()+"/"+idntityManager.getMicrositeId()+"?locale="+ localmanager.getlocal()+"&tags="+tags+"&country="+ localmanager.getCountry();
        }

        return URL;
    }

    /**
     * Gets the country from app according to settings/SIM/GEOIP
     * @param listener
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {
        String country = null;
        LocalManager locamManager= new LocalManager(mAppInfra);
        country=locamManager.getCountry();
        if(country == null && country.contains("")){

        }else{
            setHomeCountry(country);
            listener.onSuccess(country, OnGetHomeCountryListener.SOURCE.GEOIP);
        }

    }

    /**
     * Persistently store Home country, overwrites any existing country value.
     * Changing the country automatically clears the cached service list and triggers a refresh.
     * @param countryCode country code to persistently store, code must be according to ISO 3166-1
     */
    @Override
    public void setHomeCountry(String countryCode) {
        this.countryCode =countryCode;
    }

    /**
     * Returns the URL for a specific service with a preference for the current language.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the URL of the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServiceUrlWithLanguagePreference(final String serviceId, final OnGetServiceUrlListener listener) {
        mServiceUrlWithLanguagePreference = true;
        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceId, null, listener, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceId, null, listener, null, null);
                }
            }));
        }

    }

    /**
     * Returns the URL for a specific service with a preference for the current home country.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the URL of the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        mServiceUrlWithCountryPreference = true;
        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceId, null, listener, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceId,null,  listener, null, null);
                }
            }));
        }

    }

    /**
     * Returns the locale to be used for a specific service with a preference for the current language.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the recommended locale for the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        mServiceLocaleWithLanguagePreference = true;

        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceId,listener, null, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceId, listener,null, null, null);
                }
            }));
        }

    }

    /**
     * Returns the locale to be used for a specific service with a preference for the current home country.
     * @param serviceId name of the service for which the URL is to be retrieved
     * @param listener asynchronously returns using onSuccess the recommended locale for the requested service;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServiceLocaleWithCountryPreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        mServiceLocaleWithCountryPreference = true;
        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceId, listener, null, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceId, listener, null, null, null);
                }
            }));
        }

    }


    /**
     * Returns the URLs + locales for a set of services with a preference for the current language.
     * @param serviceIds list of service names for which the URL + locale are to be retrieved
     * @param listener asynchronously returns using onSuccess a map containing the requested service names (key)
     *                 and their URL + locale (value), the value will be null if the service is unknown;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServicesWithLanguagePreference(final String serviceIds, final OnGetServicesListener listener) {
        mServicesWithLanguagePreferenceMultiple = true;
        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceIds,null, null, null, listener);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceIds,null, null, null, listener);
                }
            }));
        }

    }

    /**
     * Returns the URLs + locales for a set of services with a preference for the current home country.
     * @param serviceIds list of service names for which the URL + locale are to be retrieved
     * @param listener asynchronously returns using onSuccess a map containing the requested service names (key)
     *                 and their URL + locale (value), the value will be null if the service is unknown;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void getServicesWithCountryPreference(final String serviceIds, final OnGetServicesListener listener) {
        mmServiceUrlWithCountryPreferenceMultiple = true;

        if(isDataAvailable){
            filteresDataServicesWithCountryPreference(serviceIds,null, null, null, listener);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {
                    Log.i("onError", ""+"refresh  Error");
                }

                @Override
                public void onSuccess() {
                    filteresDataServicesWithCountryPreference(serviceIds,null, null, null, listener);
                }
            }));
        }

    }

    private void filteresDataServicesWithCountryPreference(String serviceIds, OnGetServiceLocaleListener mOnGetServiceLocaleListener, OnGetServiceUrlListener mOnGetServiceUrlListener, OnGetHomeCountryListener mOnGetHomeCountryListener, OnGetServicesListener mOnGetServicesListener){


        Map<String,ServiceUrlandLocale> responseMap= new HashMap<String,ServiceUrlandLocale>();

       if(mServiceUrlWithLanguagePreference){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds)));
                mServiceUrlWithLanguagePreference = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mServiceUrlWithCountryPreference){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds)));
                mServiceUrlWithCountryPreference = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mServiceLocaleWithLanguagePreference){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale());
           mServiceLocaleWithLanguagePreference = false;

        }else if(mServiceLocaleWithCountryPreference){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
           mServiceLocaleWithCountryPreference = false;
        }else if(mServicesWithLanguagePreferenceMultiple){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds));
           mServicesWithLanguagePreferenceMultiple = false;

        }else if(mmServiceUrlWithCountryPreferenceMultiple){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds));
           mmServiceUrlWithCountryPreferenceMultiple = false;
        }


        //this is future implementaion

//        for(int configCount=0;configCount<RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().size();configCount++) {
//            HashMap<String, String> urls = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(configCount).getUrls();
//            String serviceUrlval = null;
//            for (String key : urls.keySet()) {
//                if (key.contains(serviceIds)) {
//                    serviceUrlval = urls.get(key);
//                    URL serviceURL = null;
//                    try {
//                        serviceURL = new URL(serviceUrlval);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//
//                    ServiceUrlandLocale serviceUrlandLocale = new ServiceUrlandLocale(serviceURL, RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
//                    responseMap.put(key,serviceUrlandLocale );
//                }
//            }
//
//        }

    }

    /**
     * Start negotiation with cloud service for the list of service for this application. List is based on sector, microsite, home country, and language.
     * The retrieved list is cached internally (not persistently).
     * The cached list is automatically refreshed every 24hours.
     * A refresh is only required, to ensure the very first service request after app start can be processed from the cache quickly, or when a previous sync failed.
     * @param listener asynchronously returns using onSuccess when retrieval was successful;
     *                 or returns onError the error code when retrieval failed.
     */
    @Override
    public void refresh(final OnRefreshListener listener) {

        if(RequestManager.mServiceDiscovery != null){
                    if(RequestManager.mServiceDiscovery.isSuccess()){
                        isDataAvailable = true;
                        listener.onSuccess();
                    }
                }else{
            getservice(listener);
        }


    }
}
