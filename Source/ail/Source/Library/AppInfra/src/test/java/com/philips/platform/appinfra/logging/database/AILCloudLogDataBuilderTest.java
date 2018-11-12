/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.logging.database;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AILCloudLogDataBuilderTest {

    private AILCloudLogDataBuilder ailCloudLogDataBuilder;

    @Mock
    private AppInfra appInfra;

    @Mock
    private AppInfraLogging loggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfra.getLogging()).thenReturn(loggingInterface);
        when(loggingInterface.getAilCloudLogMetaData()).thenReturn(new AILCloudLogMetaData());
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra);
    }

    @Test
    public void testSettingBuildCloudLogModel() throws AILCloudLogDataBuilder.MessageSizeExceedsException {
        LogRecord logRecord = mock(LogRecord.class);
        when(logRecord.getLevel()).thenReturn(Level.ALL);
        AILCloudLogMetaData ailCloudLogMetaData = getAILCloudLogMetaData();
        when(loggingInterface.getAilCloudLogMetaData()).thenReturn(ailCloudLogMetaData);
        Object[] objects = new Object[4];
        objects[0] = "data";
        objects[1] = "ail";
        objects[2] = "18.2.0";
        objects[3] = getMap();
        when(logRecord.getParameters()).thenReturn(objects);
        AILCloudLogData ailCloudLogData = ailCloudLogDataBuilder.buildCloudLogModel(logRecord);
        assertEquals(ailCloudLogData.logDescription, "data");
        assertEquals(ailCloudLogData.appsId, "app_id");
        assertEquals(ailCloudLogData.appState, "app_state");
        assertEquals(ailCloudLogData.locale, "locale");
        assertEquals(ailCloudLogData.homecountry, "en");
        assertEquals(ailCloudLogData.userUUID, "hsdp_uuid");
    }

    private Object getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", "value");
        return hashMap;
    }

    private AILCloudLogMetaData getAILCloudLogMetaData() {
        AILCloudLogMetaData ailCloudLogMetaData = new AILCloudLogMetaData();
        ailCloudLogMetaData.setAppId("app_id");
        ailCloudLogMetaData.setAppState("app_state");
        ailCloudLogMetaData.setLocale("locale");
        ailCloudLogMetaData.setHomeCountry("en");
        ailCloudLogMetaData.setUserUUID("uuid");
        ailCloudLogMetaData.setAppVersion("appVersion");
        return ailCloudLogMetaData;
    }
}