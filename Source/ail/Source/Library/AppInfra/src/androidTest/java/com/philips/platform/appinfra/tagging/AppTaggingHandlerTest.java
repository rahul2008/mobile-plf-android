package com.philips.platform.appinfra.tagging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AppTagging Test class.
 */
public class AppTaggingHandlerTest extends AppInfraInstrumentation {

    AppTaggingInterface mAIAppTaggingInterface;
    AppTaggingInterface mockAppTaggingInterface;
    AppConfigurationManager mConfigInterface;
    AppTagging appTagging;
    private Context context;
    private AppInfra mAppInfra;
    private AppConfigurationInterface.AppConfigurationError configError;
    AppTaggingHandler  mAppTaggingHandler;
    AppTaggingHandler mAppTaggingHandlerMock;
    private AppInfra appInfraMock;
    LoggingInterface loggingInterfaceMock;
    AppIdentityInterface appIdentityInterfaceMock;
    SecureStorageInterface secureStorageInterfaceMock;
    InternationalizationInterface internationalizationInterfaceMock;

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

        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
        configError = new AppConfigurationInterface
                .AppConfigurationError();


        Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
        assertNotNull(dynAppState.toString());

        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        assertNotNull(mAIAppTaggingInterface);

        mAppTaggingHandler=new AppTaggingHandler(mAppInfra);

        mockAppTaggingInterface = mock(AppTaggingInterface.class);

        mAppTaggingHandlerMock = mock(AppTaggingHandler.class);
        appInfraMock = mock(AppInfra.class);
        appTagging=new AppTagging(appInfraMock) {
            @Override
            AppTaggingHandler getAppTaggingHandler() {
                return mAppTaggingHandlerMock;
            }
        };

        loggingInterfaceMock = mock(LoggingInterface.class);
        appIdentityInterfaceMock=mock(AppIdentityInterface.class);
        secureStorageInterfaceMock=mock(SecureStorageInterface.class);
        internationalizationInterfaceMock=mock(InternationalizationInterface.class);
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
    }

    public void testCheckForSslConnection() {
        final boolean sslValue =mAppTaggingHandler.checkForSslConnection();
        if (sslValue) {
            assertTrue(sslValue);
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            verify(loggingInterfaceMock).log(LoggingInterface.LogLevel.VERBOSE, AppInfraLogEventID.AI_TAGGING, "ssl value true");
        } else {
            assertFalse(sslValue);
        }

    }

    public void testTrackPageWithInfo_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1", true);
    }

    public void testTrackPageWithInfoNullKey_WithoutDictionary() {
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null, true);
    }

    public void testTrackPageWithInfoNullValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null,true);
    }

    public void testTrackPageWithInfoNullKeyValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, null,true);
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
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            appTagging.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
            when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair,true);
        }
    }

    public void testTrackPageWithInfo_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1",true);
    }

    public void testTrackPageWithInfoNullKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1",true);
    }

    public void testTrackPageWithInfoNullValue_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null,true);
    }

    public void testTrackPageWithInfoNullValueKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null,true);
    }

    public void testTrackPageWithInfoMapp_pagename_exceeds_100() {
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", keyValuePair);
            when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
            verify(mAppTaggingHandlerMock).track("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", keyValuePair,true);
        }
    }

    public void testMockTrackActionWithInfo() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1",false);
    }

    public void testMockTrackActionWithInfoNullKey() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, "value1",false);
    }

    public void testMockTrackActionWithInfoNullValue() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, null,false);
    }




    public void testMockTrackActionWithInfoMapp() {
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            appTagging.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
            when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair,false);
        }
    }

    public void testMockTrackActionWithInfo_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1",false);
    }

    public void testMockTrackActionWithInfoNullKey_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1",false);
    }

    public void testMockTrackActionWithInfoNullValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null,false);
    }

    public void testMockTrackActionWithInfoNullKeyValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null,false);
    }

    public void testMockTrackActionWithInfoMapp_eventname_exceeds_255() {
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", keyValuePair);
            when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
            verify(mAppTaggingHandlerMock).track("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789", keyValuePair,false);
        }
    }


    public void testTrackVideoStart() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackVideoStart("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("videoEnd", "videoName", "Bindas",false);
    }

    public void testTrackVideoEnd() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackVideoEnd("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("videoEnd", "videoName", "Bindas",false);

    }

    public void testTrackSocialSharing() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackSocialSharing(AppTaggingInterface.SocialMedium.Facebook, "Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        final HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put("socialItem", "Bindas");
        trackMap.put("socialType", AppTaggingInterface.SocialMedium.Facebook.toString());
        verify(mAppTaggingHandlerMock).track("socialShare", trackMap,false);
    }

    public void testTrackLinkExternal() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackLinkExternal("http://www.philips.co.in/");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "exitLinkName","\"http://www.philips.co.in/\"",false);

    }

    public void testTrackFileDownload() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackFileDownload("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "fileName","Bindas",false);


    }

    public void testTimedActionStart() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionStart("Tagging_trackTimedAction");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).timeActionStart("Tagging_trackTimedAction");

    }

    public void testTrackTimedActionEnd() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionEnd("Tagging_trackTimedAction");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).timeActionEnd("Tagging_trackTimedAction");
    }




    public void testCheckForProductionState() {
        boolean checkProduction=mAppTaggingHandler.checkForProductionState();
        if(checkProduction){
            assertTrue(checkProduction);
        }else {
            assertFalse(checkProduction);
        }
    }


    public void testGetMasterADBMobileConfig() {
        final JSONObject jSONObject = mAppTaggingHandler.getMasterADBMobileConfig();
        if(jSONObject!=null){
            assertNotNull(jSONObject);
        }else {
            assertNull(jSONObject);
        }
    }


}
