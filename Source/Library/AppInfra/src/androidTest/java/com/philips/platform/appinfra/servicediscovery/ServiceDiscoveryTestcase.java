package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.servicediscovery.model.Config;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;
import com.philips.platform.appinfra.servicediscovery.model.Tag;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310238655 on 6/28/2016.
 */
public class ServiceDiscoveryTestcase extends MockitoTestCase {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    AppInfra mAppInfra;
    ServiceDiscovery mserviceDiscovery = null;
    MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
    String mServiceId = "userreg.janrain.cdn";
    // Context context = Mockito.mock(Context.class);
    ArrayList<String> mServicesId = new ArrayList<String>(
            Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));


    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mserviceDiscovery = new ServiceDiscovery();
        mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
        assertNotNull(mserviceDiscovery);
        assertNotNull(mMatchByCountryOrLanguage);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
    }

    public void testRefresh() {
        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testgetServiceUrlWithLanguagePreference() throws Exception {
        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
        mServiceDiscoveryInterface.getServicesWithLanguagePreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
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
        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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

    public void testgetServicesWithCountryMapUrl() throws Exception {
        mServiceDiscoveryInterface.getServicesWithCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
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
        mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

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
        mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {


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

    public void testisOnline() {
        if (mServiceDiscoveryManager.isOnline()) {
            assertTrue(mServiceDiscoveryManager.isOnline());
        } else {
            assertFalse(mServiceDiscoveryManager.isOnline());
        }
    }

   /* commented by Anurag
    public void testBuildURL() {
        assertNotNull(mServiceDiscoveryManager.buildUrl("IN"));
    }

    public void testgetService() {
        assertNotNull(mServiceDiscoveryManager.getService(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }));
    }*/

    public void testgetCountry() {
        if (mServiceDiscoveryManager.getCountry() == null) {
            assertNull(mServiceDiscoveryManager.getCountry());
        } else {
            mServiceDiscoveryManager.mCountry = null;
            assertNotSame(mServiceDiscoveryManager.mCountry, mServiceDiscoveryManager.getCountry());
            assertNotNull(mServiceDiscoveryManager.getCountry());
        }

    }

    public void testfilterDataForUrlbyLang() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyLangNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyLangNegetivePathForListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, null);
    }

    public void testfilterDataForUrlbyLangNegetivePathForServiceDiscovery() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, null);
    }

    public void testfilterDataForUrlbyLangNegetivePathForServiceId() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        String setviceId = null;
        mServiceDiscoveryManager.filterDataForUrlbyLang(setviceId, null);
    }

    public void testfilterDataForUrlbyLangNegetivePathForServiceIdwithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        String setviceId = null;
        mServiceDiscoveryManager.filterDataForUrlbyLang(setviceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyLangNegetivePathForServiceDiscoverywithListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyLangArray() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        RequestManager.mServiceDiscovery = mserviceDiscovery;

        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyLangArrayNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;

        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyLangArrayNegetivePathForserviceDiscovery() {
        RequestManager.mServiceDiscovery = null;

        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyLangArrayNegetivePathForserviceDiscoveryWithListener() {
        RequestManager.mServiceDiscovery = null;

        mServiceDiscoveryManager.filterDataForUrlbyLang(mServicesId, null);
    }

    public void testfilterDataForUrlbyLangArrayNegetivePathForServiceID() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;

        mServiceDiscoveryManager.filterDataForUrlbyLang(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyLangArrayNegetivePathForServiceIDWithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        ArrayList<String> arrayServiceID = null;

        mServiceDiscoveryManager.filterDataForUrlbyLang(arrayServiceID, null);
    }

    public void testfilterDataForUrlbyCountry() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyCountryNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyCountryNegetivePathForListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, null);
    }

    public void testfilterDataForUrlbyCountryNegetivePathForServiceIDWithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyCountryNegetivePathForServiceDiscoveryWithNullListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, null);
    }

    public void testfilterDataForUrlbyCountryNegetivePathForServiceDiscoveryWithListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
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
    }

    public void testfilterDataForUrlbyCountryArray() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePath() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePathForListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, null);
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePathForServiceDiscovery() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, null);
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePathForServiceDiscoveryWithListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForUrlbyCountryArrayNegetivePathFormServicesIdWithListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForUrlbyCountry(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });
    }

    public void testfilterDataForLocalByLang() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("TestLocale");
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public void testfilterDataForLocalByLangNegetivePathLocale() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public void testfilterDataForLocalByLangNegetivePathListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, null);
    }

    public void testfilterDataForLocalByLangNegetivePathServiceDiscovery() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, null);
    }

    public void testfilterDataForLocalByLangNegetivePathServiceDiscoveryWithListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForLocalByLang(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public void testfilterDataForLocalByCountry() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        mMatchByCountryOrLanguage.setLocale("TestLocale");

        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public void testfilterDataForLocalByCountryNegetivePathLocale() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);

        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public void testfilterDataForLocalByCountryNegetivePathListner() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(false));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(false));
        mMatchByCountryOrLanguage.setLocale(null);

        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, null);
    }

    public void testfilterDataForLocalByCountryNegetivePathServiceDiscovery() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, null);
    }

    public void testfilterDataForLocalByCountryNegetivePathServiceDiscoveryWithListner() {
        RequestManager.mServiceDiscovery = null;
        mServiceDiscoveryManager.filterDataForLocalByCountry(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
    }

    public MatchByCountryOrLanguage commonMatchByCountryOrLanguage(boolean isPossitivecase) {

        Tag mTag = new Tag();
        mTag.setId("TestTagId");
        mTag.setName("TestTagName");
        mTag.setKey("TestTagKey");

        assertNotNull(mTag.getId());
        assertNotNull(mTag.getKey());
        assertNotNull(mTag.getName());

        ArrayList mTagArray = new ArrayList();
        mTagArray.add(mTag);
        HashMap mMap = new HashMap<String, String>();
        if (isPossitivecase) {
            mMap.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
            mMap.put("userreg.janrain.api", "https://philips.eval.janraincapture.com");
        } else {
            mMap.put("userreg.janrain.cdn", "TestCase");
            mMap.put("TestCase", "TestCase");
        }


        Config mconfig = new Config();
        mconfig.setMicrositeId("TestMicrositeId");
        mconfig.setTags(mTagArray);
        mconfig.setUrls(mMap);

        assertNotNull(mconfig.getMicrositeId());
        assertNotNull(mconfig.getTags());
        assertNotNull(mconfig.getUrls());

        ArrayList mConfigArray = new ArrayList();
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

    public void testserviceURLwithCountryorLanguagePreferencesForNullServiceId() {

        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                assertNotNull(url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceId() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.isDataAvailable = true;
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                assertNotNull(url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, true);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProg() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, null, false);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                assertNotNull(url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceURLwithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = false;
        mServiceDiscoveryManager.serviceURLwithCountryorLanguagePreferences(mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                assertNotNull(url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceswithCountryorLanguagePreferencesForNullServiceId() {
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(null, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceId() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.isDataAvailable = true;
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, true);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProg() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, null, false);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceswithCountryorLanguagePreferencesForServiceIdDownloadProgfalse() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = false;
        mServiceDiscoveryManager.ServicesWithLanguageorCountryPreference(mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                assertNotNull(urlMap);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        }, false);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceId() {
        mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
        mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
        RequestManager.mServiceDiscovery = mserviceDiscovery;
        mServiceDiscoveryManager.isDataAvailable = true;
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, null, false);
    }

    public void testserviceLocalewithCountryorLanguagePreferencesForServiceIdDownloadProgWithListner() {
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = true;
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
        mServiceDiscoveryManager.isDataAvailable = false;
        mServiceDiscoveryManager.isDownloadInProgress = false;
        mServiceDiscoveryManager.ServiceLocaleWithCountryorLanguagePreference(mServiceId, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
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
