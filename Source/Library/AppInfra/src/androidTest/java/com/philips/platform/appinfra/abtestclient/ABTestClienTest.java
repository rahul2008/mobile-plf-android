package com.philips.platform.appinfra.abtestclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310243577 on 10/13/2016.
 */

public class ABTestClienTest extends MockitoTestCase {

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
        mAbTestClientInterface = mAppInfra.getAbTesting();
        assertNotNull(mAbTestClientInterface);
        abTestClienTestManager = new ABTestClientManager(mAppInfra);
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

    public void testCacheStatusValue() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getCacheStatus");
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
        assertNotNull(mAbTestClientInterface.getCacheStatus());
    }


    public void testgetTestValue() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestValue", Object[].class);
            String s = (String) method.invoke(abTestClienTestManager, new Object[]{new Object[]
                    {"philipsmobileappabtest1content", "defaultValue", "ABTestClientInterface" +
                            ".UPDATETYPES.EVERY_APP_START", null}});
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
        String exp = mAbTestClientInterface.getTestValue("philipsmobileappabtest1content", "defaultValue",
                ABTestClientInterface.UPDATETYPES.EVERY_APP_START, null);
        assertNotNull(exp);
    }


    public void testUpdateCache() {
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
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testprecacheExperience() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("precacheExperience");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testrefreshForVariableType() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("refreshForVariableType",
                    Integer.class);
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testgetTestNameFromConfig() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestNameFromConfig");
            ArrayList<String> s = (ArrayList) method.invoke(abTestClienTestManager);
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testgetTestValueFromServer() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getTestValueFromServer", Object[].class);
            String s = (String) method.invoke(abTestClienTestManager, new Object[]{new Object[]
                    {"philipsmobileappabtest1content", "defaultValue", "ABTestClientInterface" +
                            ".UPDATETYPES.EVERY_APP_START", null}});
            method.setAccessible(true);
            method.invoke(mAbTestClientInterface);
            assertNotNull(s);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
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
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testisAppUpdated() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("isAppUpdated");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    private CacheModel loadCacheModel() {
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setTestValue("default");
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
        valueModel.setAppVersion("1.1.0");
        HashMap<String, CacheModel.ValueModel> cacheValue = new HashMap<>();
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
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }

    public void testgetCachefromPreference() {
        try {
            method = abTestClienTestManager.getClass().getDeclaredMethod("getCachefromPreference");
            method.setAccessible(true);
            method.invoke(abTestClienTestManager);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ABTestClient",
                    e.getMessage());
        }
    }
}
