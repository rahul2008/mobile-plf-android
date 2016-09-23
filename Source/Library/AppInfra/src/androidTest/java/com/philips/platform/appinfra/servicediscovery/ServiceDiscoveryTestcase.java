//<<<<<<< HEAD
//////gpackage com.philips.platform.appinfra.servicediscovery;
////
////import android.content.Context;
////
////import com.philips.platform.appinfra.AppInfra;
////import com.philips.platform.appinfra.MockitoTestCase;
////import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
////import com.philips.platform.appinfra.servicediscovery.model.Config;
////import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
////import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
////import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;
////
////import org.json.JSONObject;
////
////import java.net.URL;
////import java.util.ArrayList;
////import java.util.Arrays;
////import java.util.HashMap;
////import java.util.Map;
////
/////**
//// * Created by 310238655 on 6/28/2016.
//// */
////public class ServiceDiscoveryTestcase extends MockitoTestCase {
////
////    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
////    ServiceDiscoveryManager mServiceDiscoveryManager = null;
////    AppInfra mAppInfra;
////    ServiceDiscovery mserviceDiscovery = null;
////    MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
////    String mServiceId = "userreg.janrain.cdn";
////    // Context context = Mockito.mock(Context.class);
////    ArrayList<String> mServicesId = new ArrayList<String>(
////            Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));
////
////    AppConfigurationManager mConfigInterface;
////    private Context context;
////
////    RequestItemManager mRequestItemManager = null;
////
////    @Override
////    protected void setUp() throws Exception {
////        super.setUp();
////        context = getInstrumentation().getContext();
////        assertNotNull(context);
////        mAppInfra = new AppInfra.Builder().build(context);
////        assertNotNull(mAppInfra);
////
////        mConfigInterface = new AppConfigurationManager(mAppInfra) {
////            @Override
////            protected JSONObject getMasterConfigFromApp() {
////                JSONObject result = null;
////                try {
////                    String testJson = "{\n" +
////                            "  \"UR\": {\n" +
////                            "\n" +
////                            "    \"Development\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
////                            "    \"Testing\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
////                            "    \"Evaluation\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
////                            "    \"Staging\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
////                            "    \"Production\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
////                            "\n" +
////                            "  },\n" +
////                            "  \"AI\": {\n" +
////                            "    \"MicrositeID\": 77001,\n" +
////                            "    \"RegistrationEnvironment\": \"Staging\",\n" +
////                            "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
////                            "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
////                            "    \"EE\": [123,234 ]\n" +
////                            "  }, \n" +
////                            " \"appinfra\": { \n" +
////                            "   \"appidentity.micrositeId\" : \"77000\",\n" +
////                            "  \"appidentity.sector\"  : \"B2C\",\n" +
////                            " \"appidentity.appState\"  : \"Staging\",\n" +
////                            "\"appidentity.serviceDiscoveryEnvironment\"  : \"Staging\",\n" +
////                            "\"restclient.cacheSizeInKB\"  : 1024 \n" +
////                            "} \n" + "}";
////                    result = new JSONObject(testJson);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                return result;
////            }
////
////        };
////        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
////
////
////        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
////        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
////        mRequestItemManager = new RequestItemManager(context, mAppInfra);
////        assertNotNull(mRequestItemManager);
////        mserviceDiscovery = new ServiceDiscovery();
////        mserviceDiscovery = loadServiceDiscoveryModel();
////        mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
////        assertNotNull(mserviceDiscovery);
////        assertNotNull(mMatchByCountryOrLanguage);
////        assertNotNull(mServiceDiscoveryInterface);
////        assertNotNull(mServiceDiscoveryManager);
//=======
//package com.philips.platform.appinfra.servicediscovery;
//
//import android.content.Context;
//
//import com.philips.platform.appinfra.AppInfra;
//import com.philips.platform.appinfra.MockitoTestCase;
//import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
//import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
//import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;
//
//import org.json.JSONArray;
//import org.mockito.Mockito;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by 310238655 on 6/28/2016.
// */
//public class ServiceDiscoveryTestcase extends MockitoTestCase {
//
//    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
//    ServiceDiscoveryManager mServiceDiscoveryManager = null;
//    AppInfra mAppInfra;
//    ServiceDiscovery mserviceDiscovery = null;
//    MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
//    String mServiceId = "userreg.janrain.cdn";
//    // Context context = Mockito.mock(Context.class);
//    ArrayList<String> mServicesId = new ArrayList<String>(
//            Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));
//
//
//    private Context context;
//
//    RequestItemManager mRequestItemManager = null;
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        context = getInstrumentation().getContext();
//        assertNotNull(context);
//        mAppInfra = new AppInfra.Builder().build(context);
//        assertNotNull(mAppInfra);
//        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
//        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
//        mRequestItemManager = new RequestItemManager(context, mAppInfra);
//        assertNotNull(mRequestItemManager);
//        mserviceDiscovery = new ServiceDiscovery();
//        mserviceDiscovery = loadServiceDiscoveryModel();
//        mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
//        assertNotNull(mserviceDiscovery);
//        assertNotNull(mMatchByCountryOrLanguage);
//        assertNotNull(mServiceDiscoveryInterface);
//        assertNotNull(mServiceDiscoveryManager);
//
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("isOnline");
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager);
//
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public void testBuildUrl() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("buildUrl");
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testdownloadServices() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("downloadServices");
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testgetCountry() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("getCountry", ServiceDiscovery.class);
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, loadServiceDiscoveryModel());
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testsetHomeCountry() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("setHomeCountry", String.class);
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, "IN");
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testsaveToSecureStore() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("saveToSecureStore", new Class[]{String.class, boolean.class});
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, new Object[]{"test", true});
//
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("fetchFromSecureStorage", boolean.class);
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, true);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testgetServiceUrlWithLanguagePreference() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("getServiceUrlWithLanguagePreference", new Class[]{String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class});
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, new Object[]{"test", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//                @Override
//                public void onSuccess(URL url) {
//                }
//
//                @Override
//                public void onError(ERRORVALUES error, String message) {
//                }
//            }});
//
////    public void testsaveToSecureStore() {
////        Method method = null;
////        try {
////            method = ServiceDiscoveryManager.class.getDeclaredMethod("saveToSecureStore", new Class[]{String.class, boolean.class});
////            method.setAccessible(true);
////            method.invoke(mServiceDiscoveryManager, new Object[]{"test", true});
////
////            method = ServiceDiscoveryManager.class.getDeclaredMethod("fetchFromSecureStorage", boolean.class);
////            method.setAccessible(true);
////            method.invoke(mServiceDiscoveryManager, true);
////        } catch (NoSuchMethodException e) {
////            e.printStackTrace();
////        } catch (InvocationTargetException e) {
////            e.printStackTrace();
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }
////    }
//
//    public void testgetServiceUrlWithLanguagePreference() {
//        Method method = null;
//        try {
//            method = ServiceDiscoveryManager.class.getDeclaredMethod("getServiceUrlWithLanguagePreference", new Class[]{String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class});
//            method.setAccessible(true);
//            method.invoke(mServiceDiscoveryManager, new Object[]{"test", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//                @Override
//                public void onSuccess(URL url) {
//                }
//
//                @Override
//                public void onError(ERRORVALUES error, String message) {
//                }
//            }});
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
////    public void testRefresh() {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
////            @Override
////            public void onSuccess() {
////                assertNotNull("Test");
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testgetServiceUrlWithLanguagePreference() throws Exception {
////
////        mServiceDiscoveryManager.setServiceDiscovery(loadServiceDiscoveryModel());
////
////
////        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
////
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url.toString());
////            }
////        });
////    }
//
////    public void testgetServicesWithLanguageMapUrl() throws Exception {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.getServicesWithLanguagePreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map urlMap) {
////                assertNotNull(urlMap);
////                assertFalse(urlMap.size() < 0);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
////        });
////    }
//
//
////    public void testgetServiceUrlWithCountryPreference() throws Exception {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
//>>>>>>> Test cases for Service Discovery
////
////
////    }
////
////    public void testRefresh() {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//<<<<<<< HEAD
////        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
//=======
////
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url.toString());
////            }
////        });
////    }
//
////    public void testgetServicesWithCountryMapUrl() throws Exception {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.getServicesWithCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map urlMap) {
////                assertNotNull(urlMap);
////                assertFalse(urlMap.size() < 0);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testgetServiceLocaleWithCountryPreference() throws Exception {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
////
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////        });
////    }
//
////    public void testgetServiceLocaleWithLanguagePreference() throws Exception {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(message);
////            }
////
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////        });
////    }
//
//    public void testgetHomeCountry() {
//        mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
//            @Override
//            public void onSuccess(String countryCode, SOURCE source) {
//                assertNotNull(countryCode);
//                assertNotNull(source);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        });
//    }
//
////    public void testisOnline() {
////        if (mServiceDiscoveryManager.isOnline()) {
////            assertTrue(mServiceDiscoveryManager.isOnline());
////        } else {
////            assertFalse(mServiceDiscoveryManager.isOnline());
////        }
////    }
//
//   /* commented by Anurag
//    public void testBuildURL() {
//        assertNotNull(mServiceDiscoveryManager.buildUrl("IN"));
//    }
//
//    public void testgetService() {
//        assertNotNull(mServiceDiscoveryManager.getService(new ServiceDiscoveryInterface.OnRefreshListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }));
//    }*/
//
////    public void testBuildUrl(){
////        assertNotNull(mServiceDiscoveryManager.buildUrl());
////   }
////    public void testgetciuntry(){
////        assertNotNull(mServiceDiscoveryManager.getCountry(loadServiceDiscoveryModel()));
////    }
//
////    public void testgetCountry() {
////        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
////        mserviceDiscovery = mServiceDiscoveryManager.serviceDiscovery;
////        if (mServiceDiscoveryManager.getCountry(mserviceDiscovery) == null) {
////            assertNull(mServiceDiscoveryManager.getCountry(mserviceDiscovery));
////        } else {
////            mServiceDiscoveryManager.mCountry = null;
////            assertNotSame(mServiceDiscoveryManager.mCountry, mServiceDiscoveryManager.getCountry(mserviceDiscovery));
////            assertNotNull(mServiceDiscoveryManager.getCountry(mserviceDiscovery));
////        }
////
////    }
//
//    public ServiceDiscovery loadServiceDiscoveryModel() {
//        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
//        serviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
//        serviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
//        serviceDiscovery.setSuccess(true);
//        serviceDiscovery.setHttpStatus("Success");
//        serviceDiscovery.setCountry("TestCountry");
//
//        ServiceDiscovery.Error error = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
//        error.setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT);
//        assertNotNull(error.getErrorvalue());
//        error.setMessage("Test");
//        assertNotNull(error.getMessage());
//        serviceDiscovery.setError(error);
//
//
//        return serviceDiscovery;
//    }
//
//    public MatchByCountryOrLanguage commonMatchByCountryOrLanguage(boolean isPossitivecase) {
//
//        MatchByCountryOrLanguage.Config.Tag mTag = new MatchByCountryOrLanguage.Config.Tag();
//        mTag.setId("TestTagId");
//        mTag.setName("TestTagName");
//        mTag.setKey("TestTagKey");
//
//        assertNotNull(mTag.getId());
//        assertNotNull(mTag.getKey());
//        assertNotNull(mTag.getName());
//
//        ArrayList mTagArray = new ArrayList();
//        mTagArray.add(mTag);
//        HashMap mMap = new HashMap<String, String>();
//        if (isPossitivecase) {
//            mMap.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
//            mMap.put("userreg.janrain.api", "https://philips.eval.janraincapture.com");
//        } else {
//            mMap.put("userreg.janrain.cdn", "TestCase");
//            mMap.put("TestCase", "TestCase");
//        }
//
//
//        MatchByCountryOrLanguage.Config mconfig = new MatchByCountryOrLanguage.Config();
//        mconfig.setMicrositeId("TestMicrositeId");
//        mconfig.setTags(mTagArray);
//        mconfig.setUrls(mMap);
//
//        assertNotNull(mconfig.getMicrositeId());
//        assertNotNull(mconfig.getTags());
//        assertNotNull(mconfig.getUrls());
//
//        ArrayList mConfigArray = new ArrayList();
//        mConfigArray.add(mconfig);
//
//        MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
//        mMatchByCountryOrLanguage.setLocale("TestLocale");
//        mMatchByCountryOrLanguage.setAvailable(false);
//        mMatchByCountryOrLanguage.setConfigs(mConfigArray);
//
//        assertNotNull(mMatchByCountryOrLanguage.getLocale());
//        assertNotNull(mMatchByCountryOrLanguage.getConfigs());
//        assertFalse(mMatchByCountryOrLanguage.isAvailable());
//        return mMatchByCountryOrLanguage;
//    }
//
//    public void testserviceURLwithCountryorLanguagePreferencesForNullServiceId() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//            @Override
//            public void onSuccess(URL url) {
//                assertNotNull(url);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceURLwithCountryorLanguagePreferencesForServiceId() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//            @Override
//            public void onSuccess(URL url) {
//                assertNotNull(url);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, true);
//    }
//
//    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProg() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, null, false);
//    }
//
//    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//            @Override
//            public void onSuccess(URL url) {
//                assertNotNull(url);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
//            @Override
//            public void onSuccess(URL url) {
//                assertNotNull(url);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceswithCountryorLanguagePreferencesForNullServiceId() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
//            @Override
//            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
//                assertNotNull(urlMap);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceswithCountryorLanguagePreferencesForServiceId() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
//            @Override
//            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
//                assertNotNull(urlMap);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, true);
//    }
//
//    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProg() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, null, false);
//    }
//
//    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
//            @Override
//            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
//                assertNotNull(urlMap);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
//            @Override
//            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
//                assertNotNull(urlMap);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceLocalewithCountryorLanguagePreferencesForServiceId() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//            @Override
//            public void onSuccess(String locale) {
//                assertNotNull(locale);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, true);
//    }
//
//    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProg() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, null, false);
//    }
//
//    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//            @Override
//            public void onSuccess(String locale) {
//                assertNotNull(locale);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
//    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//            @Override
//            public void onSuccess(String locale) {
//                assertNotNull(locale);
//            }
//
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                assertNotNull(error);
//                assertNotNull(message);
//            }
//        }, false);
//    }
//
////    public void testfilterDataForUrlbyLang() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        mMatchByCountryOrLanguage.setLocale("TestLocale");
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePath() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePathForListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, null);
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePathForServiceDiscovery() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, null);
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePathForServiceId() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        String setviceId = null;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(setviceId, null);
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePathForServiceIdwithListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        String setviceId = null;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(setviceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangNegetivePathForServiceDiscoverywithListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangArray() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        mMatchByCountryOrLanguage.setLocale("TestLocale");
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangArrayNegetivePath() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangArrayNegetivePathForserviceDiscovery() {
////        RequestManager.mServiceDiscovery = null;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangArrayNegetivePathForserviceDiscoveryWithListener() {
////        RequestManager.mServiceDiscovery = null;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, null);
////    }
//
////    public void testfilterDataForUrlbyLangArrayNegetivePathForServiceID() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyLangArrayNegetivePathForServiceIDWithListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        ArrayList<String> arrayServiceID = null;
////
////        mServiceDiscoveryManager.filterDataForUrlbyLang(arrayServiceID, null);
////    }
//
////    public void testfilterDataForUrlbyCountry() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryNegetivePath() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryNegetivePathForListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, null);
////    }
//
////    public void testfilterDataForUrlbyCountryNegetivePathForServiceIDWithListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryNegetivePathForServiceDiscoveryWithNullListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, null);
////    }
//
////    public void testfilterDataForUrlbyCountryNegetivePathForServiceDiscoveryWithListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
////            @Override
////            public void onSuccess(URL url) {
////                assertNotNull(url);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryArray() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        mMatchByCountryOrLanguage.setLocale("TestLocale");
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryArrayNegetivePath() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryArrayNegetivePathForListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, null);
////    }
//
////    public void testfilterDataForUrlbyCountryArrayNegetivePathForServiceDiscovery() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, null);
////    }
//
////    public void testfilterDataForUrlbyCountryArrayNegetivePathForServiceDiscoveryWithListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForUrlbyCountryArrayNegetivePathFormServicesIdWithListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForUrlbyCountry(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
////            @Override
////            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
////                assertNotNull(urlMap);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByLang() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        mMatchByCountryOrLanguage.setLocale("TestLocale");
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByLangNegetivePathLocale() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        mMatchByCountryOrLanguage.setLocale(null);
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByLangNegetivePathListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        mMatchByCountryOrLanguage.setLocale(null);
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, null);
////    }
//
////    public void testfilterDataForLocalByLangNegetivePathServiceDiscovery() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, null);
////    }
//
////    public void testfilterDataForLocalByLangNegetivePathServiceDiscoveryWithListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////
////                assertNotNull(locale);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByCountry() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
////        mMatchByCountryOrLanguage.setLocale("TestLocale");
////
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByCountryNegetivePathLocale() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        mMatchByCountryOrLanguage.setLocale(null);
////
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////                assertNotNull(locale);
////            }
////
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
////    public void testfilterDataForLocalByCountryNegetivePathListner() {
////        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
////        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
////        mMatchByCountryOrLanguage.setLocale(null);
////
////        RequestManager.mServiceDiscovery = mserviceDiscovery;
////        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, null);
////    }
//
////    public void testfilterDataForLocalByCountryNegetivePathServiceDiscovery() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, null);
////    }
//
////    public void testfilterDataForLocalByCountryNegetivePathServiceDiscoveryWithListner() {
////        RequestManager.mServiceDiscovery = null;
////        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
////            @Override
////            public void onSuccess(String locale) {
////
////                assertNotNull(locale);
////            }
////
//>>>>>>> Test cases for Service Discovery
////            @Override
////            public void onSuccess() {
////                assertNotNull("Test");
////            @Override
////            public void onError(ERRORVALUES error, String message) {
////                assertNotNull(error);
////                assertNotNull(message);
////            }
////        });
////    }
//
//
//}
