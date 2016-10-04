package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.google.gson.JsonObject;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310238655 on 8/11/2016.
 */
public class RequestManagerTestCase extends MockitoTestCase {

    private Context context;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    //    RequestManager mRequestManager = null;
    RequestItemManager mRequestItemManager = null;
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
        mRequestItemManager = new RequestItemManager(context, mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mRequestItemManager);
    }

    public void testRequestManager() {

        RequestItemManager mRequestManagerTest = new RequestItemManager(context, mAppInfra);
        assertNotSame(mRequestItemManager, mRequestManagerTest);
    }

    public void testexecute() {
        mRequestItemManager.execute("https://acc.philips.com/api/v1/discovery/b2c/77000?locale=en_US&tags=apps%2b%2benv%2bstage&country=IN");


    }

    public void testexecuteNegetivePath() {
        mRequestItemManager.execute("https://acc");

    }

    public JSONObject makJsonObject(boolean isSuccess, int resultJsonArraySize) {


        JSONObject urls = new JSONObject();
        try {
            urls.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
            urls.put("userreg.janrain.api", "https://philips.janraincapture.com");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject tagsObject = new JSONObject();

        try {
            tagsObject.put("id", "apps:env/prod");
            tagsObject.put("name", "prod");
            tagsObject.put("key", "apps++env+prod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray tags = new JSONArray();

        tags.put(tagsObject);

        JSONObject Config = new JSONObject();
        try {
            Config.put("micrositeId", "77000");
            Config.put("urls", urls);
            Config.put("tags", tags);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JSONArray ConfigArray = new JSONArray();
        ConfigArray.put(Config);


        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put("locale", "nl_NL");
            resultObject.put("configs", ConfigArray);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject playLoad = new JSONObject();
        try {
            playLoad.put("country", "IN");
            playLoad.put("matchByLanguage", MatchbyLangauge);
            playLoad.put("matchByCountry", MatchbyLangauge);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject MainObj = new JSONObject();
        try {
            if(isSuccess){
                MainObj.put("success", true);
            }else {
                MainObj.put("success", false);
            }

            MainObj.put("payload", playLoad);
            MainObj.put("httpStatus", "OK");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String jsonStr = MainObj.toString();

        System.out.println("jsonString: " + jsonStr);

        return MainObj;
    }

    public void testparseResponse() {
        Method method = null;
        try {

            method = RequestItemManager.class.getDeclaredMethod("parseResponse", JSONObject.class);
            method.setAccessible(true);
            method.invoke(mRequestItemManager, makJsonObject(true,1));
            method.invoke(mRequestItemManager, makJsonObject(false,2));
            method.invoke(mRequestItemManager, makJsonObject(true,0));

            method = RequestItemManager.class.getDeclaredMethod("execute", String.class);
            method.setAccessible(true);
            method.invoke(mRequestItemManager, "https://www.philips.com/api/v1/discovery/b2c/77000?locale=nl_NL&tags=apps%2b%2benv%2bprod&country=CN");

            method.invoke(mRequestItemManager, "http://www.philips.com/api/v1/discovery/b2c/77000?locale=nl_NL&tags=apps%2b%2benv%2bprod&country=CN");

            method.invoke(mRequestItemManager, "Test URL");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
