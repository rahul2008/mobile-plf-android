package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by 310238655 on 4/29/2016.
 */
public class AppTaggingTest extends MockitoTestCase {

    private Context context;
    private AppInfra mAppInfra;

    AppTaggingInterface mAIAppTaggingInterface;
    AppTaggingInterface mockAppTaggingInterface;

    AppConfigurationManager mConfigInterface;

    AppTagging mAppTagging;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        testConfig("Staging");
        testAdobeJsonConfig(true);

        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent
                ("Component name", "Component ID");

        mockAppTaggingInterface = mock(AppTaggingInterface.class);
    }


    public void testSetPreviousPage() {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).setPreviousPage("SomePreviousPage");

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
                    obj.put("ssl", new Boolean(true));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String testJson = "{\n" +
                            "  \"analytics\": {\n" +
                            "\n" +
                            " \"ssl\"  : \"" + value + "\",\n" +
                            "\n";

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

    public void testTrackPageWithInfo() {
        mAppTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockAppTaggingInterface).trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

    public void testMockTrackActionWithInfo() {

        mAppTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAppTagging.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockAppTaggingInterface).trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

    public void testLifecycle() {
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAppTagging);
        Application mockApplication = mock(Application.class);

       /* mockApplication.registerActivityLifecycleCallbacks(handler);
        mockApplication.registerComponentCallbacks(handler);*/
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockApplication).registerActivityLifecycleCallbacks(handler);

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockApplication).registerComponentCallbacks(handler);
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
        Method method = null;

        try {
            testConfig("Production");
            method = AppTagging.class.getDeclaredMethod("trackTimedActionStart", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestData");

            method = AppTagging.class.getDeclaredMethod("trackTimedActionEnd", String.class);
            method.setAccessible(true);
            method.invoke(mAppTagging, "TestData");


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testVideostartactions() {
        Method method = null;
        try {
            method = AppTagging.class.getDeclaredMethod("trackVideoStart", String.class);
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

            method = AppTagging.class.getDeclaredMethod("collectLifecycleInfo", new Class[]{Activity.class, Map.class});
            method.setAccessible(true);
            Map map = new HashMap();
            map.put("Test1", "Test2");
            method.invoke(mAppTagging, new Object[]{(Activity) tTestclass, map});


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public class Testclass extends Activity {
        Testclass() {
            Log.i("Example", "Example");
        }
    }

    public void testSocialSharing() {
        Method method = null;
        try {
            method = AppTagging.class.getDeclaredMethod("trackSocialSharing", new Class[]{AppTaggingInterface.SocialMedium.class, String.class});
            method.setAccessible(true);
            method.invoke(mAppTagging, new Object[]{AppTaggingInterface.SocialMedium.Facebook, "TestSocial"});

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testgetAppState() {
        Method method = null;
        try {
            testConfig(null);
            method = AppTagging.class.getDeclaredMethod("getAppStateFromConfig");
            method.setAccessible(true);
            method.invoke(mAppTagging);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testApplicationLifeCycle() {
        ApplicationLifeCycleHandler mApplicationLifeCycleHandler = new ApplicationLifeCycleHandler(mAppTagging);
        assertNotNull(mApplicationLifeCycleHandler);
        mApplicationLifeCycleHandler.isInBackground = true;
        Testclass tTestclass = new Testclass();
        Bundle bundle = new Bundle();
        Configuration mConfiguration = new Configuration();

        Method method = null;
        try {
            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityResumed", new Class[]{Activity.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityPaused", new Class[]{Activity.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityCreated", new Class[]{Activity.class, Bundle.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass, bundle});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityDestroyed", new Class[]{Activity.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivitySaveInstanceState", new Class[]{Activity.class, Bundle.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass, bundle});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityStarted", new Class[]{Activity.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onActivityStopped", new Class[]{Activity.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{(Activity) tTestclass});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onConfigurationChanged", new Class[]{Configuration.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{mConfiguration});

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onLowMemory");
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler);

            method = ApplicationLifeCycleHandler.class.getDeclaredMethod("onTrimMemory", new Class[]{int.class});
            method.setAccessible(true);
            method.invoke(mApplicationLifeCycleHandler, new Object[]{20});


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void testTrackActionMethods() {
        Method method = null;
        try {

            testConfig("Staging");
            testAdobeJsonConfig(false);

            method = AppTagging.class.getDeclaredMethod("trackTimedActionStart", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(mAppTagging, new Object[]{"TestTrackAction"});

            testAdobeJsonConfig(true);
            method = AppTagging.class.getDeclaredMethod("trackTimedActionStart", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(mAppTagging, new Object[]{"TestTrackAction"});

            testConfig("Staging");
            testAdobeJsonConfig(false);

            method = AppTagging.class.getDeclaredMethod("trackTimedActionEnd", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(mAppTagging, new Object[]{"TestTrackAction"});

            testAdobeJsonConfig(true);
            method = AppTagging.class.getDeclaredMethod("trackTimedActionEnd", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(mAppTagging, new Object[]{"TestTrackAction"});


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void testCheckStates() {
        Method method = null;
        try {

            testConfig("Staging");

            method = AppTagging.class.getDeclaredMethod("checkForProductionState");
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

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
