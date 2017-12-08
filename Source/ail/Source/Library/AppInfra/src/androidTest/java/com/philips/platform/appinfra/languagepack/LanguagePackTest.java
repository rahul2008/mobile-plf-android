package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.languagepack.model.LanguageList;
import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LOCALE_FILE_ACTIVATED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * LanguagePack Test class.
 */

public class LanguagePackTest extends AppInfraInstrumentation {

    private final String LANGUAGE_PACK_CONFIG_SERVICE_ID_KEY = "LANGUAGEPACK.SERVICEID";
    private final String LANGUAGE_PACK_CONFIG_SERVICE_ID = "appinfra.languagepack";
    private final String LANGUAGE_PACK_OVERVIEW_URL = "https://hashim-rest.herokuapp.com/sd/tst/en_IN/appinfra/lp.json";
    Method method;
    AppConfigurationInterface mConfigInterface = null;
    LanguagePackInterface mLanguagePackInterface = null;
    LanguagePackManager mLanguagePackManager = null;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    private FileUtils languagePackUtil;
    private AppInfra mAppInfra;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);


        // overriding ConfigManager to get Test JSON data, as AppInfra library does not have uApp configuration file
        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    Log.e(getClass()+"","Error in Language pack test setup");
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
                        Log.e(getClass()+"","Error in Language pack test setup service discovery interface callback");
                    }
                } else {
                    listener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "Invalid ServiceID");
                }
            }
        };
        assertNotNull(mServiceDiscoveryInterface);

        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).setServiceDiscovery(mServiceDiscoveryInterface).build(context);
        mLanguagePackManager = new LanguagePackManager(mAppInfra);
        mLanguagePackInterface = (LanguagePackInterface) mLanguagePackManager;

        assertNotNull(mLanguagePackInterface);

        languagePackUtil = new FileUtils(mAppInfra.getAppInfraContext());
        assertNotNull(languagePackUtil);

    }

    public void testLanguagePackConfig() {
        assertNotNull(LanguagePackManager.getLanguagePackConfig(mAppInfra.getConfigInterface(), mAppInfra));
    }

    public void testPostActivateSuccess() throws InterruptedException {
        LanguagePackInterface.OnActivateListener onActivateListenerMock = mock(LanguagePackInterface.OnActivateListener.class);
        Runnable runnable = mLanguagePackManager.postActivateSuccess(onActivateListenerMock);
        new Thread(runnable).start();
        Thread.sleep(500);
        verify(onActivateListenerMock).onSuccess(languagePackUtil.getFilePath(LOCALE_FILE_ACTIVATED, LanguagePackConstants.LANGUAGE_PACK_PATH).getAbsolutePath());
    }

    public void testActivateSuccess() throws InterruptedException {
        final Handler handlerMock = mock(Handler.class);
        mLanguagePackManager = new LanguagePackManager(mAppInfra) {
            @Override
            Handler getHandler(Context context) {
                return handlerMock;
            }
        };
        LanguagePackInterface.OnActivateListener onActivateListenerMock = mock(LanguagePackInterface.OnActivateListener.class);
        languagePackUtil.saveFile(getMetadata(), LanguagePackConstants.LOCALE_FILE_INFO, LanguagePackConstants.LANGUAGE_PACK_PATH);
        mLanguagePackManager.activate(onActivateListenerMock);
        Thread.sleep(500);
        Runnable runnable = mLanguagePackManager.postActivateSuccess(onActivateListenerMock);
        new Thread(runnable).start();
        Thread.sleep(500);
        verify(onActivateListenerMock).onSuccess(languagePackUtil.getFilePath(LOCALE_FILE_ACTIVATED, LanguagePackConstants.LANGUAGE_PACK_PATH).getAbsolutePath());
    }

    private String getMetadata() {
        try {
            final JSONObject metadataJsonObject = new JSONObject();
            metadataJsonObject.put(LanguagePackConstants.LOCALE, "some_locale");
            metadataJsonObject.put(LanguagePackConstants.VERSION, "20");
            metadataJsonObject.put(LanguagePackConstants.URL, "some_url");
            return metadataJsonObject.toString();
        } catch (JSONException e) {
            e.getMessage();
        }
        return null;
    }

    public void testIsLanguagePackDownloadRequired() {
        LanguagePackModel languagePackModel = new LanguagePackModel();
        languagePackModel.setLocale("some_locale");
        languagePackModel.setUrl("some_url");
        languagePackModel.setVersion("25");
        languagePackUtil.saveFile(getMetadata(), LanguagePackConstants.LOCALE_FILE_INFO, LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(mLanguagePackManager.isLanguagePackDownloadRequired(languagePackModel));
        languagePackModel.setVersion("15");
        assertFalse(mLanguagePackManager.isLanguagePackDownloadRequired(languagePackModel));
    }

    public void testFetchServiceIDFromConfigAndURLfromSD() {
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
                    Log.v("REFRESH ERROR", error.toString() + "  " + message);
                }

                @Override
                public void onSuccess(AILPRefreshResult result) {
                    Log.v("REFRESH SUCCESS", result.toString());
                }
            };
            method.invoke(mLanguagePackInterface, listener);
            mLanguagePackInterface.refresh(listener);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Error in refresh");
        }
    }

    public void testRefreshNew() {
        AppInfra appInfra = mock(AppInfra.class);
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        ServiceDiscoveryInterface serviceDiscoveryInterface = mock(ServiceDiscoveryInterface.class);

        Mockito.when(appInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
        Mockito.when(appInfra.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
        LanguagePackInterface.OnRefreshListener onRefreshListener = mock(LanguagePackInterface.OnRefreshListener.class);
        final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener = mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);

        final AppConfigurationInterface.AppConfigurationError appConfigurationError = mock(AppConfigurationInterface.AppConfigurationError.class);

        mLanguagePackManager = new LanguagePackManager(appInfra) {
            @NonNull
            @Override
            protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener(OnRefreshListener aILPRefreshResult) {
                return onGetServiceUrlListener;
            }


        };
        mLanguagePackManager.refresh(onRefreshListener);
        Mockito.verify(onRefreshListener).onError(LanguagePackInterface.OnRefreshListener.AILPRefreshResult.REFRESH_FAILED, "Invalid ServiceID");
    }


    public void testPostRefreshSuccess() {
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
            method.invoke(mLanguagePackManager, listener, result);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Error in Post refresh success");
        }
    }

    public void testPostRefreshError() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("postRefreshError", LanguagePackInterface.OnRefreshListener.class, LanguagePackInterface.OnRefreshListener.AILPRefreshResult.class, String.class);
            method.setAccessible(true);

            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {

                }

                @Override
                public void onSuccess(AILPRefreshResult result) {

                }
            };
            LanguagePackInterface.OnRefreshListener.AILPRefreshResult result = LanguagePackInterface.OnRefreshListener.AILPRefreshResult.REFRESH_FAILED;
            method.invoke(mLanguagePackManager, listener, result, "errorMessage");

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Error in Post refresh error");
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
            Log.e(getClass()+"","Error in test active");
        }
    }

    public void testProcessForLanguagePack() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("processForLanguagePack", JSONObject.class, LanguagePackInterface.OnRefreshListener.class);
            method.setAccessible(true);
            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
                @Override
                public void onError(AILPRefreshResult error, String message) {
                }

                @Override
                public void onSuccess(AILPRefreshResult result) {
                }
            };
            JSONObject jobj = new JSONObject(getOverviewJSON());
            method.invoke(mLanguagePackManager, jobj, listener);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | JSONException e) {
            Log.e(getClass()+"","Error in Process forLanguage pack");
        }
    }


//    public void testDownloadLanguagePack() {
//        try {
//            Method method = mLanguagePackManager.getClass().getDeclaredMethod("downloadLanguagePack"
//                    , String.class, LanguagePackInterface.OnRefreshListener.class);
//            method.setAccessible(true);
//            LanguagePackInterface.OnRefreshListener listener = new LanguagePackInterface.OnRefreshListener() {
//                @Override
//                public void onError(AILPRefreshResult error, String message) {
//                }
//
//                @Override
//                public void onSuccess(AILPRefreshResult result) {
//                }
//            };
//            //URL url = new URL("https:\\/\\/hashim-rest.herokuapp.com\\/sd\\/dev\\/en_IN\\/appinfra\\/lp\\/en_GB.json");
//            method.invoke(mLanguagePackManager, "https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/en_GB.json", listener);
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }


    public void testGetPreferredLocaleURL() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("getPreferredLocaleURL");
            method.setAccessible(true);
            method.invoke(mLanguagePackManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Error in preferred locale");
        }
    }


    ////////////////// Language Pack util test
    public void testGetLanguagePackUtilFileOperations() {

        languagePackUtil.saveFile(getLanguageResponse(), LanguagePackConstants.LOCALE_FILE_DOWNLOADED ,
                LanguagePackConstants.LANGUAGE_PACK_PATH);
        File file = languagePackUtil.getFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED,
                LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertNotNull(file);
        assertEquals(getLanguageResponse(), languagePackUtil.readFile(file));


        languagePackUtil.saveFile(getLanguageResponse(), LOCALE_FILE_ACTIVATED ,
                LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertEquals(getLanguageResponse(), languagePackUtil.readFile(file));

       /* assertTrue(languagePackUtil.deleteFile(LanguagePackConstants.LOCALE_FILE_DOWNLOADED,
                LanguagePackConstants.LOCALE_FILE_DOWNLOADED));*/
    }

    public void testLanguagePackUtilSaveLocaleMetaData() {
        try {
            Method method = languagePackUtil.getClass().getDeclaredMethod("saveLocaleMetaData", LanguagePackModel.class);
            method.setAccessible(true);
            LanguagePackModel defaultLocale = new LanguagePackModel();
            defaultLocale.setLocale("en_GB");
            defaultLocale.setUrl("https:\\/\\/hashim-rest.herokuapp.com\\/sd\\/dev\\/en_IN\\/appinfra\\/lp\\/en_GB.json");
            defaultLocale.setVersion("1");
            ArrayList<LanguagePackModel> arrayListLanguage = new ArrayList<LanguagePackModel>();
            arrayListLanguage.add(defaultLocale);
            LanguageList list = new LanguageList();
            list.setLanguages(arrayListLanguage);
            method.invoke(languagePackUtil, list.getLanguages().get(0));
            languagePackUtil.saveLocaleMetaData(list.getLanguages().get(0));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Illegalaccess or Nosuch method orInvocationTarget Error in Language pack test save locale meta data");
        } catch (Exception exception) {
            Log.e(getClass()+"","Error in Language pack test save locale meta data");
        }
    }

    public void testLanguagePackUtilRenameOnActivate() {
        try {
            Method method = languagePackUtil.getClass().getDeclaredMethod("renameOnActivate");
            method.setAccessible(true);
            method.invoke(languagePackUtil);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(getClass()+"","Error in Rename on acivate");
        }
    }


    public void testGetDefaultLocale() {
        try {
            Method method = mLanguagePackManager.getClass().getDeclaredMethod("getDefaultLocale");
            method.setAccessible(true);
            method.invoke(mLanguagePackManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        }
    }


    private String getLanguageResponse() {

        return "{\"AI_testKey\":\"se-testValue-en_GB\",\"AI_sbText\":\"se-sbValue-en_GB\"}";
    }

    private String getOverviewJSON() {


        return "{\n" +
                "  \"languages\": [\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/nl_NL.json\",\n" +
                "      \"locale\": \"nl_NL\",\n" +
                "      \"remoteVersion\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/fr_FR.json\",\n" +
                "      \"locale\": \"fr_FR\",\n" +
                "      \"remoteVersion\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/en_GB.json\",\n" +
                "      \"locale\": \"en_GB\",\n" +
                "      \"remoteVersion\": \"3\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/en_US.json\",\n" +
                "      \"locale\": \"en_US\",\n" +
                "      \"remoteVersion\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/zh_CN.json\",\n" +
                "      \"locale\": \"zh_CN\",\n" +
                "      \"remoteVersion\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/zh_TW.json\",\n" +
                "      \"locale\": \"zh_TW\",\n" +
                "      \"remoteVersion\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"url\": \"https://hashim-rest.herokuapp.com/sd/dev/en_IN/appinfra/lp/fr_CA.json\",\n" +
                "      \"locale\": \"fr_CA\",\n" +
                "      \"remoteVersion\": \"2\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";


    }
}
