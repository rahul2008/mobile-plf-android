package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

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

    OnGetServicesListener mOnGetServicesListener;

    public ServiceDiscoveryManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        getservice();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public void getservice() {
//        ServiceDiscoveryMain demo = new ServiceDiscoveryMain(this);
        new RequestManager(context).execute("https://tst.philips.com/api/v1/discovery/b2c/12345?locale=en&country=IN");
    }

    @Override
    public void getHomeCountry(OnGetHomeCountryListener listener) {

    }

    @Override
    public void setHomeCountry(String countryCode) {

    }

    @Override
    public void getServiceUrlWithLanguagePreference(String serviceId, OnGetServiceUrlListener listener) {

    }

    @Override
    public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener) {

    }

    @Override
    public void getServiceLocaleWithLanguagePreference(String serviceId, OnGetServiceLocaleListener listener) {

    }

    @Override
    public void getServiceLocaleWithCountryPreference(String serviceId, OnGetServiceLocaleListener listener) {

    }

    @Override
    public void getServicesWithLanguagePreference(String[] serviceIds, OnGetServicesListener listener) {

    }

    @Override
    public void getServicesWithCountryPreference(String serviceIds, OnGetServicesListener listener) {

        Map<String,ServiceUrlandLocale> responseMap= new HashMap<String,ServiceUrlandLocale>();
        for(int configCount=0;configCount<RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().size();configCount++) {
            HashMap<String, String> urls = RequestManager.mServiceDiscovery.getMatchByCountry().getConfigs().get(configCount).getUrls();
            String serviceUrlval = null;
            for (String key : urls.keySet()) {
                if (key.contains(serviceIds)) {
                    serviceUrlval = urls.get(key);
                    URL serviceURL = null;
                    try {
                        serviceURL = new URL(serviceUrlval);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    ServiceUrlandLocale serviceUrlandLocale = new ServiceUrlandLocale(serviceURL, RequestManager.mServiceDiscovery.getMatchByCountry().getLocale());
                    responseMap.put(key,serviceUrlandLocale );
                }
            }

        }
        listener.onSuccess(responseMap);

    }

    @Override
    public void refresh(OnRefreshListener listener) {

    }
}
