package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
    AppConfigurationInterface mConfigInterface = null;
    LanguagePackInterface mLanguagePackInterface = null;
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

        mLanguagePackInterface = mAppInfra.getLanguagePack();
        assertNotNull(mLanguagePackInterface);
    }


public void testFetchSefrviceIDfromConfig() {
    // fetch language pack service id from config
    AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
    String languagePackServiceId = (String) mConfigInterface.getPropertyForKey(LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY, "APPINFRA", configError);
    assertEquals(LANGUAGE_PACK_CONFIG_SERVICE_ID, languagePackServiceId);


    //fetch overview json file url from service discovery


   // when(ServiceDiscoveryInterfaceMock.getServiceUrlWithCountryPreference("",null)).thenReturn(true);


}

    public void testFetchOverViewJsonURLfromServiceDiscovery(){


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

        serviceDiscoveryInterfaceMock.getServiceUrlWithCountryPreference("", listener);

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



    }
}
