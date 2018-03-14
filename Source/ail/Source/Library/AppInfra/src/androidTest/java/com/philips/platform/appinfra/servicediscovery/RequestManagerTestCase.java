package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.tagging.AppInfraTaggingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_CLEAR_DATA;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_FETCH_FAILED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_STORE_FAILED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SERVICE_DISCOVERY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * RequestManager Test class.
 */
public class RequestManagerTestCase extends AppInfraInstrumentation {

    private Context context;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    //    RequestManager mRequestManager = null;
    RequestManager mRequestItemManager = null;
    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mRequestItemManager = new RequestManager(context, mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mRequestItemManager);
    }

    public void testRequestManager() {

        RequestManager mRequestManagerTest = new RequestManager(context, mAppInfra);
        assertNotSame(mRequestItemManager, mRequestManagerTest);
    }

    public void testTaggingCacheData() {
        AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        when(sharedPreferencesMock.edit()).thenThrow(new NullPointerException());
        mRequestItemManager = new RequestManager(context, mAppInfra, appInfraTaggingUtil) {
            @Override
            SharedPreferences getServiceDiscoverySharedPreferences() {
                return sharedPreferencesMock;
            }

        };
        context = getInstrumentation().getContext();
        mAppInfra = new AppInfra.Builder().build(context);
        mRequestItemManager.cacheServiceDiscovery(null, "https://www.philips.com/api/v1/discovery/B2C/70000?locale=en_US", ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);
        verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY, SD_STORE_FAILED);

    }

    public void testexecuteNegetivePath() {
        mRequestItemManager.execute("https://acc", ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);
    }

    private JSONObject makJsonObject(boolean isSuccess, int resultJsonArraySize) {
        JSONObject urls = new JSONObject();
        try {
            urls.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
            urls.put("userreg.janrain.api", "https://philips.janraincapture.com");

        } catch (JSONException e) {
        }

        JSONObject tagsObject = new JSONObject();

        try {
            tagsObject.put("id", "apps:env/prod");
            tagsObject.put("name", "prod");
            tagsObject.put("key", "apps++env+prod");
        } catch (JSONException e) {
        }
        JSONArray tags = new JSONArray();

        tags.put(tagsObject);

        JSONObject Config = new JSONObject();
        try {
            Config.put("micrositeId", "77000");
            Config.put("urls", urls);
            Config.put("tags", tags);

        } catch (JSONException e) {
        }


        JSONArray ConfigArray = new JSONArray();
        ConfigArray.put(Config);


        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put("locale", "nl_NL");
            resultObject.put("configs", ConfigArray);

        } catch (JSONException e) {
        }


        JSONArray results = new JSONArray();
        if (resultJsonArraySize == 0) {
            results.put(null);
        } else if (resultJsonArraySize == 1) {
            results.put(resultObject);
        } else if (resultJsonArraySize == 2) {
            results.put(resultObject);
            results.put(resultObject);
        }


        JSONObject MatchbyLangauge = new JSONObject();
        try {
            MatchbyLangauge.put("available", true);
            MatchbyLangauge.put("results", results);

        } catch (JSONException e) {
        }

        JSONObject playLoad = new JSONObject();
        try {
            playLoad.put("country", "IN");
            playLoad.put("matchByLanguage", MatchbyLangauge);
            playLoad.put("matchByCountry", MatchbyLangauge);

        } catch (JSONException e) {
        }

        JSONObject MainObj = new JSONObject();
        try {
            if (isSuccess) {
                MainObj.put("success", true);
            } else {
                MainObj.put("success", false);
            }

            MainObj.put("payload", playLoad);
            MainObj.put("httpStatus", "OK");

        } catch (JSONException e) {
        }


        String jsonStr = MainObj.toString();


        return MainObj;
    }

    public void testparseResponse() {
        Method method;
        try {

            method = RequestManager.class.getDeclaredMethod("parseResponse", JSONObject.class);
            method.setAccessible(true);
            method.invoke(mRequestItemManager, makJsonObject(true, 1));
            method.invoke(mRequestItemManager, makJsonObject(false, 2));
            method.invoke(mRequestItemManager, makJsonObject(true, 0));

            method = RequestManager.class.getDeclaredMethod("execute", String.class, ServiceDiscoveryManager.AISDURLType.class);
            method.setAccessible(true);
            method.invoke(mRequestItemManager, "https://www.philips.com/api/v1/discovery/b2c/77000?locale=nl_NL&tags=apps%2b%2benv%2bprod&country=CN", ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);

            method.invoke(mRequestItemManager, "http://www.philips.com/api/v1/discovery/b2c/77000?locale=nl_NL&tags=apps%2b%2benv%2bprod&country=CN",ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        }
    }

    public void testgetLocaleList() {
        String list = mRequestItemManager.getLocaleList();
        assertNotNull(list);
    }

    public void testGetCachedData() {
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        mRequestItemManager = new RequestManager(context, mAppInfra) {
            @Override
            SharedPreferences getServiceDiscoverySharedPreferences() {
                return sharedPreferencesMock;
            }

            @Override
            boolean getPropositionEnabled(AppInfra appInfra) {
                return true;
            }
        };
        when(sharedPreferencesMock.getString("SDPLATFORM",null)).thenThrow(new NullPointerException());
        AppInfraTaggingUtil appInfraTaggingAction = mock(AppInfraTaggingUtil.class);
        mRequestItemManager.getCachedData(appInfraTaggingAction);
        verify(appInfraTaggingAction).trackErrorAction(SERVICE_DISCOVERY, SD_FETCH_FAILED);
    }

    public void testgetUrlProposition() {
        String urlProposition = mRequestItemManager.getUrlProposition();
//        assertNotNull(urlProposition);
    }

    public void testgetUrlPlatform() {
        String urlPlatform = mRequestItemManager.getUrlPlatform();
//       assertNotNull(urlPlatform);
    }

    public void testisServiceDiscoveryDataExpired() {
        boolean dateexpired = mRequestItemManager.isServiceDiscoveryDataExpired();
    }

    public void testclearCacheServiceDiscovery() {
        AppInfraTaggingUtil appInfraTaggingAction = mock(AppInfraTaggingUtil.class);
        mRequestItemManager.clearCacheServiceDiscovery(appInfraTaggingAction);
        verify(appInfraTaggingAction).trackSuccessAction(SERVICE_DISCOVERY, SD_CLEAR_DATA);
    }
}
