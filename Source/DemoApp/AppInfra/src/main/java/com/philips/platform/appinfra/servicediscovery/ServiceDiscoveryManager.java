/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.os.AsyncTask;

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
    String URL = "https://tst.philips.com/api/v1/discovery/b2c/12345?locale=en&country=IN";
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

    @Override
    public void getservice(String url) {
        String urlBuild;

        if(!url.contains("country")){
            urlBuild=url;
        }else{
            urlBuild= buildUrl();
        }

        if(url!= null){
            new RequestManager(context).execute(urlBuild);
        }

    }
    private String  buildUrl(){
        AppIdentityManager idntityManager = new AppIdentityManager(mAppInfra);
        LocalManager locamManager= new LocalManager(mAppInfra);
//        URL = "https://tst.philips.com/api/v1/discovery/"+idntityManager.getSector()+"/"+idntityManager.getMicrositeId()+"?locale="+ locamManager.getlanguage()+"&country="+ locamManager.getCountry();
        return URL;
    }

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

    @Override
    public void setHomeCountry(String countryCode) {
        this.countryCode =countryCode;
    }

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
            }),URL);
        }

    }

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
            }),URL);
        }

    }

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
            }),URL);
        }

    }

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
            }),URL);
        }

    }

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
            }),URL);
        }

    }

    @Override
    public void getServicesWithCountryPreference(final String serviceIds, final OnGetServicesListener listener) {
        mmServiceUrlWithCountryPreferenceMultiple = true;

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
            }),URL);
        }

    }

    private void filteresDataServicesWithCountryPreference(String serviceIds, OnGetServiceLocaleListener mOnGetServiceLocaleListener, OnGetServiceUrlListener mOnGetServiceUrlListener, OnGetHomeCountryListener mOnGetHomeCountryListener, OnGetServicesListener mOnGetServicesListener){


        Map<String,ServiceUrlandLocale> responseMap= new HashMap<String,ServiceUrlandLocale>();

       if(mServiceUrlWithLanguagePreference){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mServiceUrlWithCountryPreference){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mServiceLocaleWithLanguagePreference){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale());

        }else if(mServiceLocaleWithCountryPreference){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
        }else if(mServicesWithLanguagePreferenceMultiple){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds));

        }else if(mmServiceUrlWithCountryPreferenceMultiple){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds));
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


    @Override
    public void refresh(final OnRefreshListener listener, String url) {
        final String URL=url;
        new AsyncTask<String, String, String>(){

            @Override
            protected String doInBackground(String... params) {
                getservice(URL);
                return null;
            }
            protected void onProgressUpdate(String... progress) {
            }
            protected void onPostExecute(String result) {
                if(RequestManager.mServiceDiscovery != null){
                    if(RequestManager.mServiceDiscovery.isSuccess()){
                        isDataAvailable = true;
                        listener.onSuccess();
                    }else{
//                    if(RequestManager.mServiceDiscovery.getHttpStatus())
//                    listener.onError();
                    }
                }


            }
                }.execute(URL,"","");
    }
}
