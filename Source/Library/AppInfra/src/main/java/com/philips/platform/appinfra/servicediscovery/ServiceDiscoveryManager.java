/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ServiceDiscoveryManager implements ServiceDiscoveryInterface, ServiceDiscoveryInterface.OnRefreshListener {

    AppInfra mAppInfra;
    Context context;
    boolean isDataAvailable = false;
    String countryCode;
    String URL = null;
    boolean mServiceUrlWithLanguagePreference = false;
    boolean mServiceUrlWithCountryPreference= false;
    boolean mServiceLocaleWithLanguagePreference= false;
    boolean mServiceLocaleWithCountryPreference= false;
    boolean mServicesWithLanguagePreferenceMultiple= false;
    boolean mmServiceUrlWithCountryPreferenceMultiple= false;


    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

        refresh((OnRefreshListener)this);

    }


    public String getservice(OnRefreshListener listener) {
        String urlBuild = null;
//        if(mAppInfra.getTagging()!=null){
//            mAppInfra.getTagging().trackActionWithInfo("ServiceDiscoveryPage", "KeyServiceDiscovery", "ValueServiceDiscovery");
//        }

        InternationalizationManager locamManager= new InternationalizationManager(mAppInfra);
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
        idntityManager.loadJSONFromAsset();
        InternationalizationManager localmanager= new InternationalizationManager(mAppInfra);
        localmanager.getUILocal();
        String mState = idntityManager.getAppState();
        String mEnvironment = idntityManager.getServiceDiscoveryEnvironment();
        String tags = null;
        String environment = null;
if(mState!=null && mEnvironment!=null){
    if(mState.equalsIgnoreCase("DEVELOPMENT")){
        tags="apps%2b%2benv%2bdev";
    }
    else if(mState.equalsIgnoreCase("TEST")){
        tags="apps%2b%2benv%2btest";
    }
    else if(mState.equalsIgnoreCase("STAGING")){
        tags="apps%2b%2benv%2bstage";
    }
    else if(mState.equalsIgnoreCase("ACCEPTANCE")){
        tags="apps%2b%2benv%2bacc";
    }
    else if(mState.equalsIgnoreCase("PRODUCTION")){
        tags="apps%2b%2benv%2bprod";
    }
    if(mEnvironment.equalsIgnoreCase("PRODUCTION") ){
        environment = "www.philips.com";
    }
    else if(mEnvironment.equalsIgnoreCase("TEST")){
        environment = "tst.philips.com";
    }
    else if(mEnvironment.equalsIgnoreCase("STAGING")){
        environment = "dev.philips.com";
    }
    else if(mEnvironment.equalsIgnoreCase("ACCEPTANCE")){
        environment = "acc.philips.com";
    }
}

        if(idntityManager.getSector() != null && idntityManager.getMicrositeId() != null && localmanager.getUILocal() != null && tags!=null && environment!=null ){
            if(localmanager.getCountry() == null){
                URL = "https://"+environment+"/api/v1/discovery/"+idntityManager.getSector()+"/"+idntityManager.getMicrositeId()+"?locale="+ localmanager.getUILocal()+"&tags="+tags;
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev";
            }
            if(localmanager.getCountry() != null ){
                URL = "https://"+environment+"/api/v1/discovery/"+idntityManager.getSector()+"/"+idntityManager.getMicrositeId()+"?locale="+ localmanager.getUILocal()+"&tags="+tags+"&country="+ localmanager.getCountry();
//                URL = "https://tst.philips.com/api/v1/discovery/B2C/12345?locale=en_US&tags=apps%2b%2benv%2bdev&country=US";
            }
        }
        return URL;
    }

    private void environmentSetUp(String state, String environment){

        if(state!=null){

        }
        if(environment != null){

        }

    }

    /**
     * Gets the country from app according to settings/SIM/GEOIP
     * @param listener
     */
    @Override
    public void getHomeCountry(final OnGetHomeCountryListener listener) {
        String country = null;
        InternationalizationManager locamManager= new InternationalizationManager(mAppInfra);
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
            filteredDataForServices(serviceId, null, listener, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceId, null, listener, null, null);
                }
            }));
        }

    }


    @Override
    public void getServiceUrlWithCountryPreference(final String serviceId, final OnGetServiceUrlListener listener) {
        mServiceUrlWithCountryPreference = true;
        if(isDataAvailable){
            filteredDataForServices(serviceId, null, listener, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceId,null,  listener, null, null);
                }
            }));
        }

    }


    @Override
    public void getServiceLocaleWithLanguagePreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        mServiceLocaleWithLanguagePreference = true;

        if(isDataAvailable){
            filteredDataForServices(serviceId,listener, null, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceId, listener,null, null, null);
                }
            }));
        }

    }


    @Override
    public void getServiceLocaleWithCountryPreference(final String serviceId, final OnGetServiceLocaleListener listener) {
        mServiceLocaleWithCountryPreference = true;
        if(isDataAvailable){
            filteredDataForServices(serviceId, listener, null, null, null);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceId, listener, null, null, null);
                }
            }));
        }

    }



    @Override
    public void getServicesWithLanguagePreference(final String serviceIds, final OnGetServicesListener listener) {
        mServicesWithLanguagePreferenceMultiple = true;
        if(isDataAvailable){
            filteredDataForServices(serviceIds,null, null, null, listener);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {

                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceIds,null, null, null, listener);
                }
            }));
        }

    }


    @Override
    public void getServicesWithCountryPreference(final String serviceIds, final OnGetServicesListener listener) {
        mmServiceUrlWithCountryPreferenceMultiple = true;

        if(isDataAvailable){
            filteredDataForServices(serviceIds,null, null, null, listener);
        }else{
            refresh((new OnRefreshListener() {
                @Override
                public void onError(ERRORVALUES error, String message) {
                    //Log.i("onError", ""+"refresh  Error");
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"Service Discovery onError","refresh  Error");
                }

                @Override
                public void onSuccess() {
                    filteredDataForServices(serviceIds,null, null, null, listener);
                }
            }));
        }

    }

    private void filteredDataForServices(String serviceIds, OnGetServiceLocaleListener mOnGetServiceLocaleListener, OnGetServiceUrlListener mOnGetServiceUrlListener, OnGetHomeCountryListener mOnGetHomeCountryListener, OnGetServicesListener mOnGetServicesListener){


        Map<String,ServiceUrlandLocale> responseMap= new HashMap<String,ServiceUrlandLocale>();

       if(mOnGetServiceUrlListener != null){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds)));
//                mServiceUrlWithLanguagePreference = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mOnGetServiceUrlListener!=null){
            try {
                mOnGetServiceUrlListener.onSuccess(new URL(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds)));
//                mServiceUrlWithCountryPreference = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(mOnGetServiceLocaleListener!=null){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getLocale());
//           mServiceLocaleWithLanguagePreference = false;

        }else if(mOnGetServiceLocaleListener!=null){
           mOnGetServiceLocaleListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
//           mServiceLocaleWithCountryPreference = false;
        }else if(mOnGetServicesListener!=null){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByLanguage().getConfigs().get(0).getUrls().get(serviceIds));
//           mServicesWithLanguagePreferenceMultiple = false;

        }else if(mOnGetServicesListener!=null){
            mOnGetServicesListener.onSuccess(RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(0).getUrls().get(serviceIds));
//           mmServiceUrlWithCountryPreferenceMultiple = false;
        }


        //this is future implementaion

//        for(int configCount=0;configCount<RequestManager.mServiceDiscovery.getMatchByCountryOrLanguage().getConfigs().size();configCount++) {
//            HashMap<String, String> urls = RequestManager.mServiceDiscovery.getMatchByCountryOrLanguage().getConfigs().get(configCount).getUrls();
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
//                    ServiceUrlandLocale serviceUrlandLocale = new ServiceUrlandLocale(serviceURL, RequestManager.mServiceDiscovery.getMatchByCountryOrLanguage().getLocale());
//                    responseMap.put(key,serviceUrlandLocale );
//                }
//            }
//
//        }

    }


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

    @Override
    public void onSuccess() {
        if(RequestManager.mServiceDiscovery != null){
            if(RequestManager.mServiceDiscovery.isSuccess()){
                isDataAvailable = true;
            }
        }else{
            getservice((OnRefreshListener)this);
        }
    }

    @Override
    public void onError(ERRORVALUES error, String message) {

    }
}
