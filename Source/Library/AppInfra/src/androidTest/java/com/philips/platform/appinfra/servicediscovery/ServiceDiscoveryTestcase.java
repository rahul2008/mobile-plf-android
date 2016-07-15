package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.net.URL;

/**
 * Created by 310238655 on 6/28/2016.
 */
public class ServiceDiscoveryTestcase extends MockitoTestCase{

    ServiceDiscoveryInterface mServiceDiscoveryManager=null;
    AppInfra mAppInfra;

    String mServiceId = "userreg.janrain.cdn";
    // Context context = Mockito.mock(Context.class);

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryManager = mAppInfra.getServiceDiscovery();
        assertNotNull(mServiceDiscoveryManager);
    }
    public void testgetServiceUrlWithLanguagePreference()throws Exception{
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
    public void testgetServiceUrlWithCountryPreference()throws Exception{
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
    public void testgetServiceLocaleWithCountryPreference()throws Exception{
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
    public void testgetServiceLocaleWithLanguagePreference()throws Exception{
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
