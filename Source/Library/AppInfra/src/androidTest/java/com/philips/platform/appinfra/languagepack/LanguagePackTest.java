package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 3/14/17.
 */

public class LanguagePackTest extends MockitoTestCase {

    Method method;
    AppConfigurationInterface mConfigInterface = null;
    LanguagePackInterface mLanguagePackInterface = null;
    LanguagePackManager mLanguagePackManager =null;
    ServiceDiscoveryInterface mServiceDiscoveryInterface =null;
    private  final String LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY = "LANGUAGEPACK.SERVICEID";
    private  final String LANGUAGE_PACK_CONFIG_SERVICE_ID = "appinfra.languagePack";

    private  final String LANGUAGE_PACK_OVERVIEW_URL = "https://hashim-rest.herokuapp.com/sd/tst/en_IN/appinfra/lp.json";

    private Context context;
    private AppInfra mAppInfra;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        //mConfigInterface = mAppInfra.getConfigInterface();
        mLanguagePackManager = new LanguagePackManager(mAppInfra);
        // overriding ConfigManager to get Test JSON data, as AppInfra library does not have uApp configuration file
        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        assertNotNull(mConfigInterface);


        // override service discovery getServiceUrlWithCountryPreference to verify correct service id is being passed to SD
        mServiceDiscoveryInterface = new ServiceDiscoveryManager(mAppInfra){
            @Override
            public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener) {
                if(serviceId!=null&& serviceId.equals(LANGUAGE_PACK_CONFIG_SERVICE_ID)){
                    try {
                        URL url = new URL(LANGUAGE_PACK_OVERVIEW_URL);
                        listener.onSuccess(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else{
                    listener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,"Invalid ServiceID");
                }
            }
        };
        assertNotNull(mServiceDiscoveryInterface);

        mLanguagePackInterface = mAppInfra.getLanguagePack();
        assertNotNull(mLanguagePackInterface);



    }


public void testFetchSefrviceIDfromConfigAndURLfromSD() {
    // fetch language pack service id from config
    AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
    String languagePackServiceId = (String) mConfigInterface.getPropertyForKey(LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY, "APPINFRA", configError);
    assertEquals(LANGUAGE_PACK_CONFIG_SERVICE_ID, languagePackServiceId);

    //fetch overview json file url from service discovery
    ServiceDiscoveryInterface.OnGetServiceUrlListener listener= new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
        @Override
        public void onSuccess(URL url) {
            try {
                URL expectedURL = new URL(LANGUAGE_PACK_OVERVIEW_URL);
                Log.i("SD TEST","OVERVIEW URL: "+url.toString());
                assertEquals(expectedURL,url);
            } catch (MalformedURLException e) {
                fail();
                e.printStackTrace();
            }
        }

        @Override
        public void onError(ERRORVALUES error, String message) {
            fail();
            assertNotNull(message);
        }
    };
    mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(languagePackServiceId,listener);




}

    public void testRefresh(){
        try {
            Method method= mLanguagePackManager.getClass().getDeclaredMethod("refresh", LanguagePackInterface.OnRefreshListener.class);
            method.setAccessible(true);

            LanguagePackInterface.OnRefreshListener listener=  new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {

                }

                @Override
                public void onSuccess(AILPRefreshResult result) {

                }
            };
            method.invoke(mLanguagePackInterface, listener);


        } catch (NoSuchMethodException | InvocationTargetException |  IllegalAccessException e ) {
            e.printStackTrace();
        }


    }

   /* public void testFetchOverViewJsonURLfromServiceDiscovery(){


        final ServiceDiscoveryInterface serviceDiscoveryInterfaceMock= mock(ServiceDiscoveryInterface.class);
        ServiceDiscoveryInterface.OnGetServiceUrlListener listener= new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SD TEST","OVERVIEW URL: "+url.toString());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(message);
            }
        };

        serviceDiscoveryInterfaceMock.getServiceUrlWithCountryPreference(LANGUAGE_PACK_CONFIG_SERVICE_ID, listener);

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {

                Object[] args = invocation.getArguments();
                ServiceDiscoveryInterface.OnGetServiceUrlListener listenerCallback = (ServiceDiscoveryInterface.OnGetServiceUrlListener)args[1];
                URL url=null;
                try {
                     url = new URL(LANGUAGE_PACK_OVERVIEW_URL);
                    Log.i("SD TEST","OVERVIEW URL: "+url.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                listenerCallback.onSuccess(url);

                return null;
            }
        }).when(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(LANGUAGE_PACK_CONFIG_SERVICE_ID, listener);



    }*/
}
