/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.tagging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adobe.mobile.Analytics;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AppTagging Test class.
 */
@Ignore
public class AppTaggingHandlerTest {

    private AppTaggingInterface mAIAppTaggingInterface;
    private AppTaggingInterface mockAppTaggingInterface;
    private AppConfigurationManager mConfigInterface;
    private AppTagging appTagging;
    private Context context;
    private AppInfra mAppInfra;
    private AppConfigurationInterface.AppConfigurationError configError;
    private AppTaggingHandler mAppTaggingHandler;
    private AppTaggingHandler mAppTaggingHandlerMock;
    private AppInfra appInfraMock;
    private LoggingInterface loggingInterfaceMock;
    private AppIdentityInterface appIdentityInterfaceMock;
    private SecureStorageInterface secureStorageInterfaceMock;
    private InternationalizationInterface internationalizationInterfaceMock;

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

    @Before
    public void setUp() throws Exception {
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
                } catch (Exception ignored) {
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

        mAppTaggingHandler = new AppTaggingHandler(mAppInfra);

        mockAppTaggingInterface = mock(AppTaggingInterface.class);

        mAppTaggingHandlerMock = mock(AppTaggingHandler.class);
        appInfraMock = mock(AppInfra.class);
        appTagging = new AppTagging(appInfraMock) {
            @Override
            AppTaggingHandler getAppTaggingHandler() {
                return mAppTaggingHandlerMock;
            }
        };

        loggingInterfaceMock = mock(LoggingInterface.class);
        appIdentityInterfaceMock = mock(AppIdentityInterface.class);
        secureStorageInterfaceMock = mock(SecureStorageInterface.class);
        internationalizationInterfaceMock = mock(InternationalizationInterface.class);
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
    }

    @Test
    public void testCheckForSslConnection() {
        final boolean sslValue = mAppTaggingHandler.checkForSslConnection();
        if (sslValue) {
            assertTrue(sslValue);
            when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
            verify(loggingInterfaceMock).log(LoggingInterface.LogLevel.VERBOSE, AppInfraLogEventID.AI_TAGGING, "ssl value true");
        } else {
            assertFalse(sslValue);
        }
    }

    @Test
    public void testTrackPageWithInfo_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1", true);
    }

    @Test
    public void testTrackPageWithInfoNullKey_WithoutDictionary() {
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null, true);
    }

    @Test
    public void testTrackPageWithInfoNullValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", null, true);
    }

    @Test
    public void testTrackPageWithInfoNullKeyValue_WithoutDictionary() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("AppTaggingDemoPage", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, null, true);
    }

    @Test
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
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair, true);
        }
    }

    @Test
    public void testTrackPageWithInfo_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", "value1", true);
    }

    @Test
    public void testTrackPageWithInfoNullKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, "value1", true);
    }

    @Test
    public void testTrackPageWithInfoNullValue_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", "key1", null, true);
    }

    @Test
    public void testTrackPageWithInfoNullValueKey_pagename_exceeds_100() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackPageWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", null, null, true);
    }

    @Test
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
            verify(mAppTaggingHandlerMock).track("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/", keyValuePair, true);
        }
    }

    @Test
    public void testMockTrackActionWithInfo() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", "key1", "value1", false);
    }

    @Test
    public void testMockTrackActionWithInfoNullKey() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, "value1", false);
    }

    @Test
    public void testMockTrackActionWithInfoNullValue() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("AppTaggingDemoPage", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("AppTaggingDemoPage", null, null, false);
    }

    @Test
    public void testMockTrackActionWithInfoMap() {
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
            verify(mAppTaggingHandlerMock).track("AppTaggingDemoPage", keyValuePair, false);
        }
    }

    @Test
    public void testMockTrackActionWithInfo_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", "value1", false);
    }

    @Test
    public void testMockTrackActionWithInfoNullKey_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, "value1", false);
    }

    @Test
    public void testMockTrackActionWithInfoNullValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", "key1", null, false);
    }

    @Test
    public void testMockTrackActionWithInfoNullKeyValue_eventname_exceeds_255() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackActionWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789/abcdefghijklmnopqrstuvwxyz0123456789+abcdefghijklmnopqrstuvwxyz0123456789", null, null, false);
    }

    @Test
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
            verify(mAppTaggingHandlerMock).track("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789", keyValuePair, false);
        }
    }

    @Test
    public void testTrackVideoStart() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackVideoStart("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("videoEnd", "videoName", "Bindas", false);
    }

    @Test
    public void testTrackVideoEnd() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackVideoEnd("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("videoEnd", "videoName", "Bindas", false);

    }

    @Test
    public void testTrackSocialSharing() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackSocialSharing(AppTaggingInterface.SocialMedium.Facebook, "Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        final HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put("socialItem", "Bindas");
        trackMap.put("socialType", AppTaggingInterface.SocialMedium.Facebook.toString());
        verify(mAppTaggingHandlerMock).track("socialShare", trackMap, false);
    }

    @Test
    public void testTrackLinkExternal() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackLinkExternal("http://www.philips.co.in/");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "exitLinkName", "\"http://www.philips.co.in/\"", false);
    }

    @Test
    public void testTrackFileDownload() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackFileDownload("Bindas");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).trackWithInfo("sendData", "fileName", "Bindas", false);
    }

    @Test
    public void testTimedActionStart() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionStart("Tagging_trackTimedAction");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).timeActionStart("Tagging_trackTimedAction", null);
    }

    @Test
    public void testTrackTimedActionEnd() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        appTagging.trackTimedActionEnd("Tagging_trackTimedAction");
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);
        verify(mAppTaggingHandlerMock).timeActionEnd("Tagging_trackTimedAction", null);
    }

    @Test
    public void testTrackTimedActionEndWithParameters() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        when(mAppTaggingHandlerMock.checkForSslConnection()).thenReturn(true);

        Analytics.TimedActionBlock<Boolean> logic = new Analytics.TimedActionBlock<Boolean>() {
            @Override
            public Boolean call(long l, long l1, Map<String, Object> map) {
                return null;
            }
        };
        appTagging.trackTimedActionEnd("Tagging_trackTimedAction", logic);
        verify(mAppTaggingHandlerMock).timeActionEnd("Tagging_trackTimedAction", logic);
    }

    @Test
    public void testCheckForProductionState() {
        boolean checkProduction = mAppTaggingHandler.checkForProductionState();
        if (checkProduction) {
            assertTrue(checkProduction);
        } else {
            assertFalse(checkProduction);
        }
    }

    @Test
    public void testGetMasterADBMobileConfig() {
        final JSONObject jSONObject = mAppTaggingHandler.getMasterADBMobileConfig();
        if (jSONObject != null) {
            assertNotNull(jSONObject);
        } else {
            assertNull(jSONObject);
        }
    }

}
