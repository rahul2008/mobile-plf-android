package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

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

    private AISDResponse aisdResponse;

    private String sdUrlJson = "{\n" +
            "\t\"success\": true,\n" +
            "\t\"payload\": {\n" +
            "\t\t\"country\": \"IN\",\n" +
            "\t\t\"matchByLanguage\": {\n" +
            "\t\t\t\"available\": true,\n" +
            "\t\t\t\"results\": [{\n" +
            "\t\t\t\t\"locale\": \"en_IN\",\n" +
            "\t\t\t\t\"configs\": [{\n" +
            "\t\t\t\t\t\"micrositeId\": \"77001\",\n" +
            "\t\t\t\t\t\"urls\": {\n" +
            "\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.identity.service\": \"https://dev.appinfra.testing.identity.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.configuration.service\": \"https://dev.appinfra.testing.configuration.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.discovery.service\": \"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_IN&country=IN&testappstate=apps%2b%2benv%2bdev\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"tags\": [{\n" +
            "\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
            "\t\t\t\t\t\t\"name\": \"dev\",\n" +
            "\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
            "\t\t\t\t\t}]\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}]\n" +
            "\t\t},\n" +
            "\t\t\"matchByCountry\": {\n" +
            "\t\t\t\"available\": true,\n" +
            "\t\t\t\"results\": [{\n" +
            "\t\t\t\t\"locale\": \"en_IN\",\n" +
            "\t\t\t\t\"configs\": [{\n" +
            "\t\t\t\t\t\"micrositeId\": \"77001\",\n" +
            "\t\t\t\t\t\"urls\": {\n" +
            "\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.identity.service\": \"https://dev.appinfra.testing.identity.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.configuration.service\": \"https://dev.appinfra.testing.configuration.service/en_IN/B2C/77001\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.discovery.service\": \"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_IN&country=IN&testappstate=apps%2b%2benv%2bdev\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"tags\": [{\n" +
            "\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
            "\t\t\t\t\t\t\"name\": \"dev\",\n" +
            "\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
            "\t\t\t\t\t}]\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}]\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"httpStatus\": \"OK\"\n" +
            "}";

    private String sdUrlPlatformjson = "{\n" +
            "\t\"success\": true,\n" +
            "\t\"payload\": {\n" +
            "\t\t\"country\": \"IN\",\n" +
            "\t\t\"matchByLanguage\": {\n" +
            "\t\t\t\"available\": true,\n" +
            "\t\t\t\"results\": [{\n" +
            "\t\t\t\t\"locale\": \"en_IN\",\n" +
            "\t\t\t\t\"configs\": [{\n" +
            "\t\t\t\t\t\"micrositeId\": \"77000\",\n" +
            "\t\t\t\t\t\"urls\": {\n" +
            "\t\t\t\t\t\t\"userreg.janrain.cdn\": \"https://d1lqe9temigv1p.cloudfront.net\",\n" +
            "\t\t\t\t\t\t\"userreg.janrain.api\": \"https://philips.dev.janraincapture.com\",\n" +
            "\t\t\t\t\t\t\"userreg.landing.emailverif\": \"https://10.128.41.111:4503/content/B2C/en_IN/verify-account.html\",\n" +
            "\t\t\t\t\t\t\"userreg.landing.resetpass\": \"https://10.128.41.111:4503/content/B2C/en_IN/myphilips/reset-password.html?cl=mob.html\",\n" +
            "\t\t\t\t\t\t\"userreg.hsdp.userserv\": \"https://user-registration-assembly-testing.us-east.philips-healthsuite.com\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77000\",\n" +
            "\t\t\t\t\t\t\"prxclient.assets\": \"https://tst.philips.com/prx/product/%sector%/en_IN/%catalog%/products/%ctn%.assets\"\n" +
            "\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"tags\": [{\n" +
            "\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
            "\t\t\t\t\t\t\"name\": \"dev\",\n" +
            "\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
            "\t\t\t\t\t}]\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}]\n" +
            "\t\t},\n" +
            "\t\t\"matchByCountry\": {\n" +
            "\t\t\t\"available\": true,\n" +
            "\t\t\t\"results\": [{\n" +
            "\t\t\t\t\"locale\": \"en_IN\",\n" +
            "\t\t\t\t\"configs\": [{\n" +
            "\t\t\t\t\t\"micrositeId\": \"77000\",\n" +
            "\t\t\t\t\t\"urls\": {\n" +
            "\t\t\t\t\t\t\"userreg.janrain.cdn\": \"https://d1lqe9temigv1p.cloudfront.net\",\n" +
            "\t\t\t\t\t\t\"userreg.janrain.api\": \"https://philips.dev.janraincapture.com\",\n" +
            "\t\t\t\t\t\t\"userreg.landing.emailverif\": \"https://10.128.41.111:4503/content/B2C/en_IN/verify-account.html\",\n" +
            "\t\t\t\t\t\t\"userreg.landing.resetpass\": \"https://10.128.41.111:4503/content/B2C/en_IN/myphilips/reset-password.html?cl=mob.html\",\n" +
            "\t\t\t\t\t\t\"userreg.hsdp.userserv\": \"https://user-registration-assembly-testing.us-east.philips-healthsuite.com\",\n" +
            "\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77000\",\n" +
            "\t\t\t\t\t\t\"prxclient.assets\": \"https://tst.philips.com/prx/product/%sector%/en_IN/%catalog%/products/%ctn%.assets\"\n" +
            "\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"tags\": [{\n" +
            "\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
            "\t\t\t\t\t\t\"name\": \"dev\",\n" +
            "\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
            "\t\t\t\t\t}]\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}]\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"httpStatus\": \"OK\"\n" +
            "}";

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
        mRequestItemManager = new RequestManager(context, mAppInfra);
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

        try {
            JSONObject propositionObject = new JSONObject(sdUrlJson);
            assertNotNull(propositionObject);
            ServiceDiscovery propostionService = parseResponse(propositionObject);
            assertNotNull(propostionService);
            JSONObject platformObject = new JSONObject(sdUrlPlatformjson);
            assertNotNull(platformObject);
            ServiceDiscovery platformService = parseResponse(platformObject);
            assertNotNull(platformService);
            aisdResponse = new AISDResponse();
            aisdResponse.setPropositionURLs(propostionService);
            aisdResponse.setPlatformURLs(platformService);
            assertNotNull(aisdResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
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

    public void testApplyURLParameters() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");
        try {
            URL url = new URL("https://d1lqe9temigv1p.cloudfront.net");
            assertNotNull(mServiceDiscoveryManager.applyURLParameters(url, parameters));
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

//    public void testemptyresultarray() {
//
//        String resJson = "{\n" +
//                "    \"success\": true,\n" +
//                "    \"payload\": {\n" +
//                "        \"country\": \"US\",\n" +
//                "        \"matchByLanguage\": {\n" +
//                "            \"available\": false,\n" +
//                "            \"results\": [\n" +
//                "                        \n" +
//                "                        ]\n" +
//                "        },\n" +
//                "        \"matchByCountry\": {\n" +
//                "            \"available\": false,\n" +
//                "            \"results\": [\n" +
//                "                        \n" +
//                "                        ]\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"httpStatus\": \"OK\"\n" +
//                "}\n";
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(resJson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        serviceDiscovery.parseResponse(context,mAppInfra ,jsonObject);
//        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId,
//                new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
//                    @Override
//                    public void onSuccess(String locale) {
//                        assertNotNull(locale);
//                    }
//
//                    @Override
//                    public void onError(ERRORVALUES error, String message) {
//                        assertNotNull(error);
//                        assertNotNull(message);
//                    }
//                }, false);
//    }

    public void testAisdResponse() {
        try {
            JSONObject propositionObject = new JSONObject(sdUrlJson);
            ServiceDiscovery propostionService = parseResponse(propositionObject);

            assertNotNull(propostionService);
            JSONObject platformObject = new JSONObject(sdUrlPlatformjson);
            ServiceDiscovery platformService = parseResponse(platformObject);
            assertNotNull(platformService);
            aisdResponse = new AISDResponse();
            aisdResponse.setPropositionURLs(propostionService);
            aisdResponse.setPlatformURLs(platformService);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private ServiceDiscovery parseResponse(JSONObject response) {
        ServiceDiscovery result = new ServiceDiscovery();
        result.setSuccess(response.optBoolean("success"));
        if (!result.isSuccess()) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
            result.setError(err);
        } else { // no sense in further processing if server reports error
            // START setting match by country
            result.parseResponse(context, mAppInfra, response);
        }

        return result;
    }


    public void testgetServiceURL() {
        URL url = aisdResponse.getServiceURL("userreg.janrain.api", AISDResponse.AISDPreference.AISDCountryPreference, null);
        assertEquals("https://philips.dev.janraincapture.com", url.toString());

        URL urllang = aisdResponse.getServiceURL("userreg.janrain.api", AISDResponse.AISDPreference.AISDLanguagePreference, null);
        assertEquals("https://philips.dev.janraincapture.com", urllang.toString());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");

        URL rurl = aisdResponse.getServiceURL("prxclient.assets", AISDResponse.AISDPreference.AISDCountryPreference, parameters);
        assertNotNull(url.toString());
        assertEquals("https://tst.philips.com/prx/product/B2C/en_IN/shavers/products/HD9740.assets",
                rurl.toString());


        URL urlLang = aisdResponse.getServiceURL("prxclient.assets", AISDResponse.AISDPreference.AISDLanguagePreference, parameters);
        assertNotNull(urlLang.toString());
        assertEquals("https://tst.philips.com/prx/product/B2C/en_IN/shavers/products/HD9740.assets",
                rurl.toString());
    }

    public void testgetServicesUrl() {
        HashMap<String, ServiceDiscoveryService> responseMap;
        String[] serviceIds = {"userreg.janrain.api", "prxclient.assets"};
        ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));

        responseMap = aisdResponse.getServicesUrl(serviceId,
                AISDResponse.AISDPreference.AISDCountryPreference, null);
        assertNotNull(responseMap);

        responseMap = aisdResponse.getServicesUrl(serviceId,
                AISDResponse.AISDPreference.AISDLanguagePreference, null);
        assertNotNull(responseMap);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("ctn", "HD9740");
        parameters.put("sector", "B2C");
        parameters.put("catalog", "shavers");

        responseMap = aisdResponse.getServicesUrl(serviceId,
                AISDResponse.AISDPreference.AISDCountryPreference, parameters);
        assertNotNull(responseMap);

        responseMap = aisdResponse.getServicesUrl(serviceId,
                AISDResponse.AISDPreference.AISDLanguagePreference, parameters);
        assertNotNull(responseMap);
    }

    public void testgetLocaleWithPreference() {
        String localbyCountry = aisdResponse.getLocaleWithPreference(AISDResponse.AISDPreference.AISDCountryPreference);
        assertEquals("en_IN", localbyCountry);
        String localbyLang = aisdResponse.getLocaleWithPreference(AISDResponse.AISDPreference.AISDLanguagePreference);
        assertEquals("en_IN", localbyLang);
    }

    public void testgetCountryCode() {
        String country = aisdResponse.getCountryCode();
        assertNotNull(country);
    }

    public void testgetError() {
        ServiceDiscovery.Error err = aisdResponse.getError();
        assertEquals(null, err);
    }

    public void testisSuccess() {
        assertTrue(aisdResponse.isSuccess());
    }
}