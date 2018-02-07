package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.json.JSONObject;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AppTagging Test class.
 */
public class AppTaggingHandlerTest extends AppInfraInstrumentation {

    private AppTagging appTagging;
    private AppInfra mAppInfra;
    private AppConfigurationInterface.AppConfigurationError configError;
    private AppTaggingHandler  mAppTaggingHandler;
    private AppTaggingHandler mAppTaggingHandlerMock;
    private AppInfra appInfraMock;
    private LoggingInterface loggingInterfaceMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        mAppInfra = new AppInfra.Builder().build(context);
        AppConfigurationManager mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                    e.getMessage();
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
        configError = new AppConfigurationInterface
                .AppConfigurationError();
        mAppTaggingHandler=new AppTaggingHandler(mAppInfra);
        mAppTaggingHandlerMock = mock(AppTaggingHandler.class);
        appInfraMock = mock(AppInfra.class);
        when(appInfraMock.getAppInfraContext()).thenReturn(context);
        appTagging=new AppTagging(appInfraMock) {
            @Override
            AppTaggingHandler getAppTaggingHandler() {
                return mAppTaggingHandlerMock;
            }
        };
        mAppTaggingHandlerMock.setAppInfra(appInfraMock);
        loggingInterfaceMock = mock(LoggingInterface.class);
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
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
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1", true);
    }

    public void testTrackPageWithInfoNullKey_WithoutDictionary() {
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null, true);
    }

    public void testTrackPageWithInfoNullValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", null);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null,true);
    }

    public void testTrackPageWithInfoNullKeyValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, null);
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
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair,true);
        }
    }

    public void testTrackPageWithInfo_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1",true);
    }

    public void testTrackPageWithInfoNullKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1",true);
    }

    public void testTrackPageWithInfoNullValue_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null,true);
    }

    public void testTrackPageWithInfoNullValueKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null);
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
            verify(mAppTaggingHandlerMock).track("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", keyValuePair,true);
        }
      }

    public void testMockTrackActionWithInfo() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1",false);
    }

    public void testMockTrackActionWithInfoNullKey() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, "value1",false);
    }

    public void testMockTrackActionWithInfoNullValue() {
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, null);
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
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair,false);
        }
    }

    public void testMockTrackActionWithInfo_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1",false);
    }

    public void testMockTrackActionWithInfoNullKey_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1");
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1",false);
    }

    public void testMockTrackActionWithInfoNullValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null,false);
    }

    public void testMockTrackActionWithInfoNullKeyValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null);
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
        final HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put("socialItem", "Bindas");
        trackMap.put("socialType", AppTaggingInterface.SocialMedium.Facebook.toString());
        verify(mAppTaggingHandlerMock).track("socialShare", trackMap,false);
    }

    public void testTrackLinkExternal() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackLinkExternal("http://www.philips.co.in/");
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "exitLinkName","\"http://www.philips.co.in/\"",false);

    }

    public void testTrackFileDownload() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackFileDownload("Bindas");
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "fileName","Bindas",false);


    }

    public void testTimedActionStart() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionStart("Tagging_trackTimedAction");
        verify(mAppTaggingHandlerMock).timeActionStart("Tagging_trackTimedAction");

    }

    public void testTrackTimedActionEnd() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionEnd("Tagging_trackTimedAction");
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
