package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310238655 on 6/28/2016.
 */
public class ServiceDiscoveryTestcase extends MockitoTestCase {

    private ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    private ServiceDiscoveryManager mServiceDiscoveryManager = null;
    private AppInfra mAppInfra;
    private ServiceDiscovery mserviceDiscovery = null;
    private MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
    private String mServiceId = "userreg.janrain.cdn";
    // Context context = Mockito.mock(Context.class);
    private ArrayList<String> mServicesId = new ArrayList<String>(
            Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));

    private AppConfigurationManager mConfigInterface;
    private Context context;

    private RequestManager mRequestItemManager = null;
    private ServiceDiscovery serviceDiscovery = null;
    private Method method;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        serviceDiscovery = new ServiceDiscovery();

        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        testConfig();
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mRequestItemManager = new RequestManager(context ,mAppInfra);
        assertNotNull(mRequestItemManager);
        mserviceDiscovery = new ServiceDiscovery();
        mserviceDiscovery = loadServiceDiscoveryModel();
        mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        assertNotNull(mserviceDiscovery);
        assertNotNull(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("isOnline");
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testConfig() {

        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = "{\n" +
                            "  \"UR\": {\n" +
                            "\n" +
                            "    \"Development\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
                            "    \"Testing\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
                            "    \"Evaluation\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
                            "    \"Staging\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
                            "    \"Production\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
                            "\n" +
                            "  },\n" +
                            "  \"AI\": {\n" +
                            "    \"MicrositeID\": 77001,\n" +
                            "    \"RegistrationEnvironment\": \"Staging\",\n" +
                            "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
                            "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
                            "    \"Map\": {\"one\": \"123\", \"two\": \"123.45\"},\n" +
                            "    \"EE\": [123,234 ]\n" +
                            "  }, \n" +
                            " \"appinfra\": { \n" +
                            "   \"appidentity.micrositeId\" : \"77000\",\n" +
                            "  \"appidentity.sector\"  : \"B2C\",\n" +
                            " \"appidentity.appState\"  : \"Staging\",\n" +
                            "\"appidentity.serviceDiscoveryEnvironment\"  : \"Staging\",\n" +
                            "\"restclient.cacheSizeInKB\"  : 1024 \n" +
                            "} \n" + "}";
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
    }

    public void testBuildUrl() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("buildUrl");
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testApplyURLParameters(){
       
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        try {
            URL url = new URL("https://d1lqe9temigv1p.cloudfront.net");
            assertNotNull(mServiceDiscoveryManager.applyURLParameters(url,parameters));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void testdownloadServices() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("downloadServices");
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testgetCountry() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("getCountry", ServiceDiscovery.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, loadServiceDiscoveryModel());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testsetHomeCountry() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("setHomeCountry", String.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, "IN");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testsaveToSecureStore() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("saveToSecureStore", String.class, boolean.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, "test", true);

            method = ServiceDiscoveryManager.class.getDeclaredMethod("fetchFromSecureStorage", boolean.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, true);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testRefresh() {
        mserviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {
                assertNotNull("Test");
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testgetServiceUrlWithLanguagePreference() throws Exception {
        //mServiceDiscoveryManager.setServiceDiscovery(loadServiceDiscoveryModel());
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("getServiceUrlWithLanguagePreference",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, "test", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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

    public void testgetServicesWithLanguageMapUrl() throws Exception {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.getServicesWithLanguagePreference(mServicesId,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map urlMap) {
                        assertNotNull(urlMap);
                        assertFalse(urlMap.size() < 0);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(message);
                    }
                });
    }


    public void testgetServiceUrlWithCountryPreference() throws Exception {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {

                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(message);
                    }
                });
    }


    public void testgetServicesWithCountryMapUrl() throws Exception {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.getServicesWithCountryPreference(mServicesId,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map urlMap) {
                        assertNotNull(urlMap);
                        assertFalse(urlMap.size() < 0);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(message);
                    }
                });
    }

    public void testgetServiceLocaleWithCountryPreference() throws Exception {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

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
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {


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

    public void testgetHomeCountry() {
        mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
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

    public void testBuildURL() {
        Method method;
        try {
            method = mServiceDiscoveryManager.getClass().getDeclaredMethod("buildUrl");
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery",
                    e.getMessage());
        }
    }


    private MatchByCountryOrLanguage commonMatchByCountryOrLanguage(boolean isPossitivecase) {

        MatchByCountryOrLanguage.Config.Tag mTag = new MatchByCountryOrLanguage.Config.Tag();
        mTag.setId("TestTagId");
        mTag.setName("TestTagName");
        mTag.setKey("TestTagKey");

        assertNotNull(mTag.getId());
        assertNotNull(mTag.getKey());
        assertNotNull(mTag.getName());

        ArrayList<MatchByCountryOrLanguage.Config.Tag> mTagArray = new ArrayList<MatchByCountryOrLanguage.Config.Tag>();
        mTagArray.add(mTag);
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (isPossitivecase) {
            mMap.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
            mMap.put("userreg.janrain.api", "https://philips.eval.janraincapture.com");
        } else {
            mMap.put("userreg.janrain.cdn", "TestCase");
            mMap.put("TestCase", "TestCase");
        }


        MatchByCountryOrLanguage.Config mconfig = new MatchByCountryOrLanguage.Config();
        mconfig.setMicrositeId("TestMicrositeId");
        mconfig.setTags(mTagArray);
        mconfig.setUrls(mMap);

        assertNotNull(mconfig.getMicrositeId());
        assertNotNull(mconfig.getTags());
        assertNotNull(mconfig.getUrls());

        ArrayList<MatchByCountryOrLanguage.Config> mConfigArray = new ArrayList<MatchByCountryOrLanguage.Config>();
        mConfigArray.add(mconfig);

        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        mMatchByCountryOrLanguage.setAvailable(false);
        mMatchByCountryOrLanguage.setConfigs(mConfigArray);

        assertNotNull(mMatchByCountryOrLanguage.getLocale());
        assertNotNull(mMatchByCountryOrLanguage.getConfigs());
        assertFalse(mMatchByCountryOrLanguage.isAvailable());
        return mMatchByCountryOrLanguage;
    }

    private ServiceDiscovery loadServiceDiscoveryModel() {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        serviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        serviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        serviceDiscovery.setSuccess(true);
        serviceDiscovery.setHttpStatus("Success");
        serviceDiscovery.setCountry("TestCountry");

        ServiceDiscovery.Error error = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
        error.setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT);
        assertNotNull(error.getErrorvalue());
        error.setMessage("Test");
        assertNotNull(error.getMessage());
        serviceDiscovery.setError(error);


        return serviceDiscovery;
    }


    public void testserviceURLwithCountryorLanguagePreferencesForNullServiceId() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(null,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceId() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, true, null);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProg() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, null, false, null);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner
            () {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceswithCountryorLanguagePreferencesForNullServiceId() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(null,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceId() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, true, null);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProg() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, null, false, null);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId,
                new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false, null);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceId() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                    @Override
                    public void onSuccess(String locale) {
                        assertNotNull(locale);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, true);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProg() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, null, false);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner
            () {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                    @Override
                    public void onSuccess(String locale) {
                        assertNotNull(locale);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
        serviceDiscovery = loadServiceDiscoveryModel();
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                    @Override
                    public void onSuccess(String locale) {
                        assertNotNull(locale);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false);
    }

    public void testgetServiceUrlWithCountryPreferenceReplacedUrl() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("prxclient.assets", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                }, parameters);
    }


    public void testgetServiceUrlWithLanguagePreferenceReplacedUrl() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference("prxclient.assets", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        assertNotNull(url);
                    }

                }, parameters);
    }

    public void testgetServicesWithCountryPreferencewithServiceIds() {

        String[] serviceIds = {"prxclient.assets", "prxclient.summary"};
        ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, new
                ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                    }

                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }
                }, parameters);
    }

    public void testgetServicesWithLanguagePreferencewithServiceIds() {
        String[] serviceIds = {"prxclient.assets", "prxclient.summary"};
        ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, new
                ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                    }

                    @Override
                    public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                        assertNotNull(urlMap);
                    }
                }, parameters);
    }


    public void testfilterDataForUrlbyLang() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("IN");
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testfilterDataForUrlbyLangNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale("IN");
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyLangNegetivePathForServiceId() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));

        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testfilterDataForUrlbyLangArrayNegetivePathForserviceDiscovery() {
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    assertNotNull(urlMap);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyLangArrayNegetivePathForServiceID() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    assertNotNull(urlMap);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyCountry() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));

        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testfilterDataForUrlbyCountryNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyCountryNegetivePathForServiceIDWithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    assertNotNull(url);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyCountryArray() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("IN");

        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    assertNotNull(urlMap);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    assertNotNull(urlMap);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForUrlbyCountryArrayNegetivePathFormServicesIdWithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                @Override
                public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                    assertNotNull(urlMap);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForLocalByLangNegetivePathLocale() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);
        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForLocalByLang",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                @Override
                public void onSuccess(String locale) {
                    assertNotNull(locale);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testfilterDataForLocalByCountryNegetivePathLocale() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);

        try {
            method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForLocalByCountry",
                    String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
            method.setAccessible(true);
            method.invoke(mServiceDiscoveryManager, mServicesId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                @Override
                public void onSuccess(String locale) {
                    assertNotNull(locale);
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    assertNotNull(error);
                    assertNotNull(message);
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testemptyresultarray() {

        String resJson = "{\n" +
                "    \"success\": true,\n" +
                "    \"payload\": {\n" +
                "        \"country\": \"US\",\n" +
                "        \"matchByLanguage\": {\n" +
                "            \"available\": false,\n" +
                "            \"results\": [\n" +
                "                        \n" +
                "                        ]\n" +
                "        },\n" +
                "        \"matchByCountry\": {\n" +
                "            \"available\": false,\n" +
                "            \"results\": [\n" +
                "                        \n" +
                "                        ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"httpStatus\": \"OK\"\n" +
                "}\n";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(resJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        serviceDiscovery.parseResponse(mAppInfra ,jsonObject);
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                    @Override
                    public void onSuccess(String locale) {
                        assertNotNull(locale);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        assertNotNull(error);
                        assertNotNull(message);
                    }
                }, false);
    }
}