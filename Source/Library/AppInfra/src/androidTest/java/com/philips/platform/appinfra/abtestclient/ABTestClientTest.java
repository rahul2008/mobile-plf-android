package com.philips.platform.appinfra.abtestclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ABTestClient Test class.
 */


public class ABTestClientTest extends AppInfraInstrumentation {

    private ABTestClientInterface mAbTestClientInterface;
    private AppInfra mAppInfra;
    private Context mContext;
    private Method method;
    private ABTestClientManager abTestClienTestManager;
    private CacheModel cacheModel;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        assertNotNull(mContext);

        mAppInfra = new AppInfra.Builder().build(mContext);
        assertNotNull(mAppInfra);
        testConfig();

        abTestClienTestManager = new ABTestClientManager(mAppInfra);
        mAbTestClientInterface = (ABTestClientInterface) abTestClienTestManager;
        assertNotNull(mAbTestClientInterface);
        cacheModel = new CacheModel();
        cacheModel = loadCacheModel();
        assertNotNull(cacheModel);
        assertNotNull(abTestClienTestManager);
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("isOnline");
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);

        } catch (NoSuchMethodException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }

    }

    public void testConfig() {

        final AppConfigurationManager  configInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                            "Exception in test config"+e.getMessage());
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(configInterface).build(mContext);
    }

    public void testgetTestValue() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestValue", Object[].class);
            final String s = (String) method.invoke(abTestClienTestManager, new Object[]{new Object[]
                    {"philipsmobileappabtest1content", "defaultValue", "ABTestClientInterface" +
                            ".UPDATETYPES.EVERY_APP_START", null}});
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in test value"+e.getMessage());
        }
        final String exp = mAbTestClientInterface.getTestValue("philipsmobileappabtest1content", "defaultValue",
                ABTestClientInterface.UPDATETYPES.EVERY_APP_START, null);
        assertNotNull(exp);
    }

    public void testMappedRequestName(){
        String requestName=abTestClienTestManager.mappedRequestName("philipsmobileappabtest1content");
        assertNotNull(requestName);
        final String exp = mAbTestClientInterface.getTestValue(requestName, "defaultValue",
                ABTestClientInterface.UPDATETYPES.EVERY_APP_START, null);
        assertNotNull(exp);

    }

    public void testUpdateCache() {
        assertNotNull(mAbTestClientInterface);

        mAbTestClientInterface.updateCache(new ABTestClientInterface.OnRefreshListener() {
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

    public void testloadCache() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("loadfromCache");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in load cache"+ e.getMessage());
        }
    }

    public void testprecacheExperience() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("precacheExperience");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in precache"+e.getMessage());

        }
    }

    public void testrefreshForVariableType() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("refreshForVariableType",
                    Integer.class);
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in refresh variable"+e.getMessage());
        }
    }

    public void testgetTestNameFromConfig() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestNameFromConfig");
            final ArrayList<String> s = (ArrayList<String>) method.invoke(abTestClienTestManager);
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in test name config"+e.getMessage());
        }
    }

    public void testgetTestValueFromServer() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestValueFromServer", Object[].class);
            final String s = (String) method.invoke(abTestClienTestManager, new Object[]{new Object[]
                    {"philipsmobileappabtest1content", "defaultValue", "ABTestClientInterface" +
                            ".UPDATETYPES.EVERY_APP_START", null}});
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in value from server"+ e.getMessage());
        }
    }

    public void testgetTestValueFromMemoryCache() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestValueFromMemoryCache",
                    String.class);
//            String s = (String) method.invoke(abTestClienTestManager, new Object[]{new Object[]
//                    {"philipsmobileappabtest1content"}});
            method.setAccessible(true);
            method.invoke(abTestClienTestManager, "philipsmobileappabtest1content");
            //assertNotNull(s);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in value from memor cache"+e.getMessage());
        }
    }

    public void testisAppUpdated() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("isAppUpdated");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in app update "+e.getMessage());
        }
    }

    private CacheModel loadCacheModel() {
        final CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue("default");
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
        valueModel.setAppVersion("1.1.0");
        final HashMap<String, CacheModel.ValueModel> cacheValue = new HashMap<>();
        cacheValue.put("philipsmobileappabtest1content", valueModel);
        cacheModel.setTestValues(cacheValue);
        return cacheModel;
    }

    public void testsaveCachetoPreference() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("saveCachetoPreferenc",
                    CacheModel.class);
            method.setAccessible(true);
            method.invoke(abTestClienTestManager, loadCacheModel());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Excepton in save cache"+ e.getMessage());
        }
    }

    public void testgetCachefromPreference() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getCachefromPreference");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in get cache"+ e.getMessage());
        }
    }

    public void testUpdateMemorycacheForTestName() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("updateMemorycacheForTestName", String.class, String.class, null);
            method.setAccessible(true);
            method.invoke(abTestClienTestManager, "Test name", "Content", null);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in update memory cache"+e.getMessage());
        }
    }

    public void testCacheStatusValue() {
        abTestClienTestManager.mCachestatusvalues = ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCES_NOT_UPDATED;
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getCacheStatus");
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_ABTEST_CLIENT,
                    "Exception in Cache Status Value"+e.getMessage());
        }

        assertNotNull(abTestClienTestManager.mCachestatusvalues);
    }

}
