package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * AppTagging Test class.
 */
public class AppTaggingTest extends AppInfraInstrumentation {

    AppTaggingInterface mAIAppTaggingInterface;
    AppTaggingInterface mockAppTaggingInterface;
    AppConfigurationManager mConfigInterface;
    AppTagging mAppTagging;
    private Context context;
    private AppInfra mAppInfra;
    private AppConfigurationInterface.AppConfigurationError configError;
    private BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(AppTagging.ACTION_TAGGING_DATA)) {
                    Map textExtra = (Map) intent.getSerializableExtra(AppTagging.EXTRA_TAGGING_DATA);
                    assertNotNull(textExtra);
                }
            }

        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);

        testConfig("Staging");
        testAdobeJsonConfig(true);

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
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
        configError = new AppConfigurationInterface
                .AppConfigurationError();

        assertNotNull(configError);

        Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
        assertNotNull(dynAppState.toString());

        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent
                ("Component name", "Component ID");
        assertNotNull(mAIAppTaggingInterface);

        mockAppTaggingInterface = mock(AppTaggingInterface.class);
    }

    public void testCheckForSslConnection() {
        JSONObject jSONObject = testGetMasterADBMobileConfig();
        if (jSONObject != null) {
            assertNotNull(jSONObject);
            try {
                final boolean sslValue = jSONObject.getJSONObject("analytics").optBoolean("ssl");
                if (sslValue) {
                    assertTrue(sslValue);
                } else {
                    assertFalse(sslValue);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            assertNull(jSONObject);
        }

    }

    public JSONObject testGetMasterADBMobileConfig() {
        JSONObject result = null;
        try {
            final InputStream mInputStream = context.getAssets().open("ADBMobileConfig.json");
            final BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
            final StringBuilder mStringBuilder = new StringBuilder();
            String line;
            while ((line = mBufferedReader.readLine()) != null) {
                mStringBuilder.append(line).append('\n');
            }
            result = new JSONObject(mStringBuilder.toString());
            if (result != null) {
                assertNotNull(result);
            } else {
                assertNull(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public void testSetPreviousPage() {
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");

    }

    public void testConfig(final String value) {

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
                            "    \"EE\": [123,234 ]\n" +
                            "  }, \n" +
                            " \"appinfra\": { \n" +
                            "   \"appidentity.micrositeId\" : \"77000\",\n" +
                            "    \"tagging.sensitiveData\": [\"bundleId\", \"language\"  ],\n" +
                            "  \"appidentity.sector\"  : \"B2C\",\n" +
                            " \"appidentity.appState\"  : \"" + value + "\",\n" +
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

    public void testAdobeJsonConfig(final boolean value) {

        mAppTagging = new AppTagging(mAppInfra) {
            @Override
            protected JSONObject getMasterADBMobileConfig() {
                JSONObject result = null;

                JSONObject obj = new JSONObject();

                try {
                    obj.put("ssl", Boolean.valueOf(true));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    result = new JSONObject();
                    result.put("analytics", obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        mAppTagging.mComponentID = "mComponentID";
        mAppTagging.mComponentVersion = "mComponentVersion";
        mAppInfra = new AppInfra.Builder().setTagging(mAppTagging).build(context);
        mAppInfra.setConfigInterface(mConfigInterface);
    }

    public void testPrivacyConsent() {
        mAppTagging.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, mAppTagging.getPrivacyConsent());
        mAppTagging.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, mAppTagging.getPrivacyConsent());
        mAppTagging.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.UNKNOWN);
//        assertEquals(AppTaggingInterface.PrivacyStatus.UNKNOWN, mAppTagging.getPrivacyConsent());
    }

    public void testTrackPageWithInfo_WithoutDictionary() {
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);

        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");

        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", null, "value1");

        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", null);

        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", null, null);
    }

    public void testTrackPageWithInfo_WithDictionary() {
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
        }
        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", null);
    }

    public void testTrackPageWithInfo_pagename_exceeds_100() {
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);

        mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1");

        mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1");

        mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null);

        mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null);

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", keyValuePair);
        }
        mAppTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null);
    }

    public void testMockTrackActionWithInfo() {

        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");

        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", null, "value1");

        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", null);

        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", null, null);

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
        }
        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", null);
    }

    public void testMockTrackActionWithInfo_eventname_exceeds_255() {

        mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1");

        mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1");

        mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null);

        mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null);

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", keyValuePair);
        }
        mAppTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null);
    }

    public void testLifecycle() {
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppInfra);
        Application mockApplication = mock(Application.class);
        mockApplication.registerActivityLifecycleCallbacks(handler);
        mockApplication.registerComponentCallbacks(handler);

    }

    public void testEmumValues() {
        assertEquals("facebook", AppTaggingInterface.SocialMedium.Facebook.toString());
        assertEquals("twitter", AppTaggingInterface.SocialMedium.Twitter.toString());
        assertEquals("mail", AppTaggingInterface.SocialMedium.Mail.toString());
        assertEquals("airdrop", AppTaggingInterface.SocialMedium.AirDrop.toString());
    }

    public void testTrackVideoStart() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertEquals(((String) args[0]), "Bindas");
                return null;
            }
        }).when(mockAppTaggingInterface).trackVideoStart("Bindas");

    }

    public void testTrackVideoEnd() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertEquals(((String) args[0]), "Bindas");
                return null;
            }
        }).when(mockAppTaggingInterface).trackVideoEnd("Bindas");

    }

    public void testTrackSocialSharing() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertEquals(((String) args[1]), "Bindas");
                return null;
            }
        }).when(mockAppTaggingInterface).trackSocialSharing(AppTaggingInterface.SocialMedium.Facebook, "Bindas");
    }

    public void testTrackLinkExternal() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertEquals(((String) args[0]), "http://www.philips.co.in/");
                return null;
            }
        }).when(mockAppTaggingInterface).trackLinkExternal("http://www.philips.co.in/");
    }

    public void testTrackFileDownload() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertEquals(((String) args[0]), "Bindas");
                return null;
            }
        }).when(mockAppTaggingInterface).trackFileDownload("Bindas");
    }

    public void testTimedActionStart() {
        mockAppTaggingInterface.trackTimedActionStart("Tagging_trackTimedAction");
    }

    public void testTrackTimedActionEnd() {
        mockAppTaggingInterface.trackTimedActionEnd("Tagging_trackTimedAction");
    }

    public void testVideostartactions() {
        try {
            Method method = AppTagging.class.getDeclaredMethod("trackVideoStart", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "Start");


            method = AppTagging.class.getDeclaredMethod("trackVideoEnd", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "Start");


            method = AppTagging.class.getDeclaredMethod("trackLinkExternal", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "Start");


            method = AppTagging.class.getDeclaredMethod("trackFileDownload", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "Start");

            method = AppTagging.class.getDeclaredMethod("setPrivacyConsentForSensitiveData", boolean.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, true);

            method = AppTagging.class.getDeclaredMethod("getPrivacyConsentForSensitiveData");
            method.setAccessible(true);
            method.invoke(mAppTagging);
            mAppTagging.setPrivacyConsentForSensitiveData(true);
            assertTrue(mAppTagging.getPrivacyConsentForSensitiveData());
            assertNotNull(mAppTagging.getPrivacyConsentForSensitiveData());

            method = AppTagging.class.getDeclaredMethod("trackLinkExternal", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "Start");

            method = AppTagging.class.getDeclaredMethod("setPreviousPage", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "setPreviousPage");

            method = AppTagging.class.getDeclaredMethod("pauseLifecycleInfo");
            method.setAccessible(true);
            method.invoke(mAppTagging);

            method = AppTagging.class.getDeclaredMethod("collectLifecycleInfo", Activity.class);
            method.setAccessible(true);
            Testclass tTestclass = new Testclass();
            method.invoke(mAppTagging, (Activity) tTestclass);

            method = AppTagging.class.getDeclaredMethod("collectLifecycleInfo", Activity.class, Map.class);
            method.setAccessible(true);
            Map<String, String> map = new HashMap();
            map.put("Test1", "Test2");
            method.invoke(mAppTagging, tTestclass, map);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void testPrivacyConsentForSensitiveData() {
        mAppTagging.setPrivacyConsentForSensitiveData(true);
        assertTrue(mAppTagging.getPrivacyConsentForSensitiveData());
        mAppTagging.setPrivacyConsentForSensitiveData(false);
        assertFalse(mAppTagging.getPrivacyConsentForSensitiveData());

    }

    public void testgetAppState() {
        try {
            testConfig(null);
            Method method = AppTagging.class.getDeclaredMethod("getAppStateFromConfig");
            method.setAccessible(true);
            method.invoke(mAppTagging);

        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testApplicationLifeCycle() {
        ApplicationLifeCycleHandler mApplicationLifeCycleHandler = new ApplicationLifeCycleHandler(mAppInfra);
        assertNotNull(mApplicationLifeCycleHandler);
        ApplicationLifeCycleHandler.isInBackground = true;
        Testclass tTestclass = new Testclass();
        Bundle bundle = new Bundle();
        Configuration mConfiguration = new Configuration();

        try {

            Method method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityResumed", Activity.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityPaused", Activity.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityCreated", Activity.class, Bundle.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass, bundle);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityDestroyed", Activity.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivitySaveInstanceState", Activity.class, Bundle.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass, bundle);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityStarted", Activity.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityStopped", Activity.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, tTestclass);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onConfigurationChanged", Configuration.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, mConfiguration);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onLowMemory");
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onTrimMemory", int.class);
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, 20);


        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testTrackActionMethods() {
        try {

            testConfig("Staging");
            testAdobeJsonConfig(false);

            Method method = AppTagging.class.getDeclaredMethod("trackTimedActionStart", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestTrackAction");

            testAdobeJsonConfig(true);
            method = AppTagging.class.getDeclaredMethod("trackTimedActionStart", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestTrackAction");

            testConfig("Staging");
            testAdobeJsonConfig(false);

            method = AppTagging.class.getDeclaredMethod("trackTimedActionEnd", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestTrackAction");

            testAdobeJsonConfig(true);
            method = AppTagging.class.getDeclaredMethod("trackTimedActionEnd", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestTrackAction");
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void testCheckStates() {
        try {

            testConfig("Staging");

            Method method = AppTagging.class.getDeclaredMethod("checkForProductionState");
            method.setAccessible(true);
            method.invoke(mAppTagging);

            testConfig(null);

            method = AppTagging.class.getDeclaredMethod("checkForProductionState");
            method.setAccessible(true);
            method.invoke(mAppTagging);

            method = AppTagging.class.getDeclaredMethod("getMasterADBMobileConfig");
            method.setAccessible(true);
            method.invoke(mAppTagging);

            testAdobeJsonConfig(false);
            method = AppTagging.class.getDeclaredMethod("checkForSslConnection");
            method.setAccessible(true);
            method.invoke(mAppTagging);


        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testgetTrackingIdentifier() {
        assertNotNull(mAppTagging.getTrackingIdentifier());
    }

    public void testTaggingData() {
        mAIAppTaggingInterface.createInstanceForComponent("Testin Tagging", "Testing Ver");
        mAIAppTaggingInterface.registerTaggingData(rec);
        mAIAppTaggingInterface.trackPageWithInfo(AppTagging.PAGE_NAME, "Test Page", "Test Name");
        mAIAppTaggingInterface.trackActionWithInfo(AppTagging.ACTION_NAME, "Test Action", "Test Action");
        mAIAppTaggingInterface.unregisterTaggingData(rec);
    }

    public void testTaggingDataNeagtiveCase() {
        mAIAppTaggingInterface.createInstanceForComponent("Testin Tagging", "Testing Ver");
        mAIAppTaggingInterface.registerTaggingData(null);
        mAIAppTaggingInterface.trackPageWithInfo("ailPageName_second", "Test Page ", "Test Name");
        mAIAppTaggingInterface.trackActionWithInfo(AppTagging.ACTION_NAME, "Test Action", "Test Action");
        mAIAppTaggingInterface.unregisterTaggingData(null);
    }

    public class Testclass extends Activity {
        Testclass() {
            Log.i("Example", "Example");
        }
    }
}
