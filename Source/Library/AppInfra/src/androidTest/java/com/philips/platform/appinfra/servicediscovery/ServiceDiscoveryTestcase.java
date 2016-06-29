package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appidentity.AppIdentityManager;

import java.net.URL;

/**
 * Created by 310238655 on 6/28/2016.
 */
public class ServiceDiscoveryTestcase extends MockitoTestCase{

    ServiceDiscoveryManager mServiceDiscoveryManager=null;
    // Context context = Mockito.mock(Context.class);

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        assertNotNull(mServiceDiscoveryManager);
    }
    public void testgetServiceUrlWithLanguagePreference()throws Exception{
        mServiceDiscoveryManager.getServiceUrlWithLanguagePreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }
    public void testgetServiceUrlWithCountryPreference()throws Exception{
        mServiceDiscoveryManager.getServiceUrlWithCountryPreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(URL url) {
                assertNotNull(url.toString());
            }
        });
    }
    public void testgetServiceLocaleWithCountryPreference()throws Exception{
        mServiceDiscoveryManager.getServiceLocaleWithCountryPreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }
    public void testgetServiceLocaleWithLanguagePreference()throws Exception{
        mServiceDiscoveryManager.getServiceLocaleWithLanguagePreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {


            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String locale) {
                assertNotNull(locale);
            }
        });
    }
    public void testgetServicesWithLanguagePreference()throws Exception{
        mServiceDiscoveryManager.getServicesWithLanguagePreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServicesListener() {


            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String services) {
                assertNotNull(services);
            }
        });
    }
    public void testgetServicesWithCountryPreference()throws Exception{
        mServiceDiscoveryManager.getServicesWithCountryPreference("ugrow.terms", new ServiceDiscoveryInterface.OnGetServicesListener() {


            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess(String services) {
                assertNotNull(services);
            }
        });
    }
}
