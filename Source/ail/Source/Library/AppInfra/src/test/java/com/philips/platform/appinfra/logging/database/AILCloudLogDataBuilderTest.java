package com.philips.platform.appinfra.logging.database;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
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
    private LoggingConfiguration loggingConfiguration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(appInfra.getAilCloudLogMetaData()).thenReturn(new AILCloudLogMetaData());
        ailCloudLogDataBuilder = new AILCloudLogDataBuilder(appInfra, loggingConfiguration);
    }

    public void testBuildCloudLogModel() {
        LogRecord logRecord = mock(LogRecord.class);
        when(logRecord.getLevel()).thenReturn(Level.ALL);
        Object[] objects = new Object[2];
        objects[0] = "data";
        objects[1] = getMap();
        when(logRecord.getParameters()).thenReturn(objects);
        AILCloudLogData ailCloudLogData = ailCloudLogDataBuilder.buildCloudLogModel(logRecord);
        assertEquals(ailCloudLogData.logDescription, "data");
    }

    public Object getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", "value");
        return hashMap;
    }
}