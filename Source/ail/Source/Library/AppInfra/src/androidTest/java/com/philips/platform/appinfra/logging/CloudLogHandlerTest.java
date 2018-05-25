package com.philips.platform.appinfra.logging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.LogRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogHandlerTest extends AppInfraInstrumentation {

    @Mock
    private AppInfra appInfra;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testPublish() throws MessageSizeExceedsException {
        final AILCloudLogDataBuilder ailCloudLogDataBuilder =mock(AILCloudLogDataBuilder.class);
        final AILCloudLogDBManager cloudLogDBManager =mock(AILCloudLogDBManager.class);
        CloudLogHandler cloudLogHandler = new CloudLogHandler(appInfra) {
            @NonNull
            @Override
            AILCloudLogDataBuilder getAilCloudLogDataBuilder(AppInfra appInfra) {
                return ailCloudLogDataBuilder;
            }

            @Override
            AILCloudLogDBManager getLogDbManager() {
                return cloudLogDBManager;
            }
        };
        AILCloudLogData ailCloudLogData = mock(AILCloudLogData.class);
        LogRecord logRecord = mock(LogRecord.class);
        when(ailCloudLogDataBuilder.buildCloudLogModel(logRecord)).thenReturn(ailCloudLogData);
        cloudLogHandler.publish(logRecord);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(cloudLogDBManager).insertLog(ailCloudLogData);
    }
}