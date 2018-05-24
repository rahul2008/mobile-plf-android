package com.philips.platform.appinfra.logging.database;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.MessageSizeExceedsException;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AILCloudLogDataBuilderTest extends TestCase {

    private AILCloudLogDataBuilder ailCloudLogDataBuilder;

    @Mock
    private AppInfra appInfra;

    @Mock
    private AppInfraLogging loggingInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(appInfra.getLogging()).thenReturn(loggingInterface);
        when(loggingInterface.getAilCloudLogMetaData()).thenReturn(new AILCloudLogMetaData());
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra);
    }

    public void testSettingBuildCloudLogModel() throws MessageSizeExceedsException {
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
        assertEquals(ailCloudLogData.userUUID, "uuid");
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