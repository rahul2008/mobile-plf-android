package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.languagepack.model.LanguageList;
import com.philips.platform.appinfra.languagepack.model.LanguageModel;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
    LanguagePackManager mLanguagePackManager = null;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    private final String LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY = "LANGUAGEPACK.SERVICEID";
    private final String LANGUAGE_PACK_CONFIG_SERVICE_ID = "appinfra.languagePack";

    private final String LANGUAGE_PACK_OVERVIEW_URL = "https://hashim-rest.herokuapp.com/sd/tst/en_IN/appinfra/lp.json";

    private Context context;
    private AppInfra mAppInfra;
    private LanguagePackUtil languagePackUtil;


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
        mServiceDiscoveryInterface = new ServiceDiscoveryManager(mAppInfra) {
            @Override
            public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener) {
                if (serviceId != null && serviceId.equals(LANGUAGE_PACK_CONFIG_SERVICE_ID)) {
                    try {
                        URL url = new URL(LANGUAGE_PACK_OVERVIEW_URL);
                        listener.onSuccess(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "Invalid ServiceID");
                }
            }
        };
        assertNotNull(mServiceDiscoveryInterface);

        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).setServiceDiscovery(mServiceDiscoveryInterface).build(context);
        mLanguagePackInterface = mAppInfra.getLanguagePack();
        assertNotNull(mLanguagePackInterface);

        languagePackUtil = new LanguagePackUtil(mAppInfra.getAppInfraContext());
        assertNotNull(languagePackUtil);

    }


    public void testFetchSefrviceIDfromConfigAndURLfromSD() {
        // fetch language pack service id from config
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        String languagePackServiceId = (String) mConfigInterface.getPropertyForKey(LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY, "APPINFRA", configError);
        assertEquals(LANGUAGE_PACK_CONFIG_SERVICE_ID, languagePackServiceId);

        //fetch overview json file url from service discovery
        ServiceDiscoveryInterface.OnGetServiceUrlListener listener = new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                try {
                    URL expectedURL = new URL(LANGUAGE_PACK_OVERVIEW_URL);
                    Log.i("SD TEST", "OVERVIEW URL: " + url.toString());
                    assertEquals(expectedURL, url);
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
        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(languagePackServiceId, listener);


    }

    public void testRefresh() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("refresh", LanguagePackInterface.OnRefreshListener.class);
            method.setAccessible(true);

            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {

                }

                @Override
                public void onSuccess(AILPRefreshResult result) {

                }
            };
            method.invoke(mLanguagePackInterface, listener);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testPostRefreshSuccess(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("postRefreshSuccess", LanguagePackInterface.OnRefreshListener.class, LanguagePackInterface.OnRefreshListener.AILPRefreshResult.class);
            method.setAccessible(true);

            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {

                }

                @Override
                public void onSuccess(AILPRefreshResult result) {

                }
            };
            LanguagePackInterface.OnRefreshListener.AILPRefreshResult result = LanguagePackInterface.OnRefreshListener.AILPRefreshResult.REFRESHED_FROM_SERVER;
            method.invoke(mLanguagePackInterface, listener, result);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testPostRefreshError(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("postRefreshSuccess", LanguagePackInterface.OnRefreshListener.class, LanguagePackInterface.OnRefreshListener.AILPRefreshResult.class,String.class);
            method.setAccessible(true);

            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {

                }

                @Override
                public void onSuccess(AILPRefreshResult result) {

                }
            };
            LanguagePackInterface.OnRefreshListener.AILPRefreshResult result = LanguagePackInterface.OnRefreshListener.AILPRefreshResult.REFRESHED_FROM_SERVER;
            method.invoke(mLanguagePackInterface, listener, result,"errorMessage");

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testActivate() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("activate", LanguagePackInterface.OnActivateListener.class);
            method.setAccessible(true);
            LanguagePackInterface.OnActivateListener listener = new LanguagePackInterface.OnActivateListener() {
                @Override
                public void onSuccess(String path) {

                }
                @Override
                public void onError(AILPActivateResult ailpActivateResult, String message) {
                }
            };
            method.invoke(mLanguagePackInterface, listener);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testProcessForLanguagePack(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("processForLanguagePack");
            method.setAccessible(true);
            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {
                }
                @Override
                public void onSuccess(AILPRefreshResult result) {
                }
            };
            method.invoke(mLanguagePackInterface,listener);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testDownloadLanguagePack(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("downloadLanguagePack");
            method.setAccessible(true);
            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {
                }
                @Override
                public void onSuccess(AILPRefreshResult result) {
                }
            };
            URL url = new URL("");
            method.invoke(mLanguagePackInterface,url,listener);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void testIsLanguagePackDownloadRequired(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("isLanguagePackDownloadRequired");
            method.setAccessible(true);
            LanguageModel defaultLocale = new LanguageModel();
            defaultLocale.setLocale("en_GB");
            defaultLocale.setUrl("https:\\/\\/hashim-rest.herokuapp.com\\/sd\\/dev\\/en_IN\\/appinfra\\/lp\\/en_GB.json");
            defaultLocale.setVersion("1");
            method.invoke(mLanguagePackInterface,defaultLocale);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testGetPreferredLocaleURL(){
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("getPreferredLocaleURL");
            method.setAccessible(true);
            method.invoke(mLanguagePackInterface);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testGetLanguagePackUtilFileOperations() {

        languagePackUtil.saveFile(getLanguageResponse(), LanguagePackConstants.LOCALE_FILE_DOWNLOADED);
        File file = languagePackUtil.getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED);
        assertNotNull(file);
        assertEquals(getLanguageResponse(), languagePackUtil.readFile(file));
        languagePackUtil.saveFile(getLanguageResponse(), LanguagePackConstants.LOCALE_FILE_ACTIVATED);
        assertEquals(getLanguageResponse(), languagePackUtil.readFile(file));

        assertTrue(languagePackUtil.deleteFile(LanguagePackConstants.LOCALE_FILE_DOWNLOADED));
    }

    public void testLanguagePackUtilSaveLocaleMetaData(){
        try {
            Method method = languagePackUtil.getClass().getDeclaredMethod("saveLocaleMetaData", LanguageModel.class);
            method.setAccessible(true);
            LanguageModel defaultLocale = new LanguageModel();
            defaultLocale.setLocale("en_GB");
            defaultLocale.setUrl("https:\\/\\/hashim-rest.herokuapp.com\\/sd\\/dev\\/en_IN\\/appinfra\\/lp\\/en_GB.json");
            defaultLocale.setVersion("1");
            ArrayList<LanguageModel> arrayListLanguage=  new             ArrayList<LanguageModel>();
            arrayListLanguage.add(defaultLocale);
            LanguageList list = new LanguageList();
            list.setLanguages(arrayListLanguage);
            method.invoke(languagePackUtil, list.getLanguages().get(0));
            languagePackUtil.saveLocaleMetaData(list.getLanguages().get(0));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch(Exception e){

        }
    }

    public void testLanguagePackUtilRenameOnActivate(){
        try {
            Method method = languagePackUtil.getClass().getDeclaredMethod("renameOnActivate");
            method.setAccessible(true);
            method.invoke(languagePackUtil);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private String getLanguageResponse() {

        return "{\"AI_testKey\":\"se-testValue-en_GB\",\"AI_sbText\":\"se-sbValue-en_GB\"}";
    }
}
