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

    ServiceDiscoveryInterface mServiceDiscoveryManager = null;
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
        mServiceDiscoveryManager = mAppInfra.getServiceDiscovery();
        assertNotNull(mServiceDiscoveryManager);
    }

    public void testgetServiceUrlWithLanguagePreference() throws Exception {
        mServiceDiscoveryManager.getServiceUrlWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }

    public void testgetServiceUrlWithLanguageMapUrl() throws Exception {
        mServiceDiscoveryManager.getServiceUrlWithLanguagePreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map urlMap) {
                assertNotNull(urlMap);
                assertFalse(urlMap.size()<0);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

            }
        });
    }


    public void testgetServiceUrlWithCountryPreference() throws Exception {
        mServiceDiscoveryManager.getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }

    public void testgetServiceUrlWithCountryMapUrl() throws Exception {
        mServiceDiscoveryManager.getServiceUrlWithCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map urlMap) {
                assertNotNull(urlMap);
                assertFalse(urlMap.size()<0);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

            }
        });
    }

    public void testgetServiceLocaleWithCountryPreference() throws Exception {
        mServiceDiscoveryManager.getServiceLocaleWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }

    public void testgetServiceLocaleWithLanguagePreference() throws Exception {
        mServiceDiscoveryManager.getServiceLocaleWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {


            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }


}
