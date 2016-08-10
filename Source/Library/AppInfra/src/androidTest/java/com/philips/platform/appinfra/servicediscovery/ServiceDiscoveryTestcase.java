package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by 310238655 on 6/28/2016.
 */
public class ServiceDiscoveryTestcase extends MockitoTestCase {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    AppInfra mAppInfra;

    String mServiceId = "userreg.janrain.cdn";
    // Context context = Mockito.mock(Context.class);
    ArrayList<String> mServicesId = new ArrayList<String>(
            Arrays.asList("userreg.janrain.cdn", "userreg.janrain.cdn", "userreg.landing.emailverif"));


    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
    }

    public void testgetServiceUrlWithLanguagePreference() throws Exception {
        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }

    public void testgetServiceUrlWithLanguageMapUrl() throws Exception {
        mServiceDiscoveryInterface.getServicesWithLanguagePreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map urlMap) {
                assertNotNull(urlMap);
                assertFalse(urlMap.size()<0);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }
        });
    }


    public void testgetServiceUrlWithCountryPreference() throws Exception {
        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }

    public void testgetServiceUrlWithCountryMapUrl() throws Exception {
        mServiceDiscoveryInterface.getServicesWithCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map urlMap) {
                assertNotNull(urlMap);
                assertFalse(urlMap.size()<0);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }
        });
    }

    public void testgetServiceLocaleWithCountryPreference() throws Exception {
        mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }

    public void testgetServiceLocaleWithLanguagePreference() throws Exception {
        mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {


            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }
    public void testgetHomeCountry(){
        mServiceDiscoveryManager.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String countryCode, SOURCE source) {
                assertNotNull(countryCode);
                assertNotNull(source);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

}
