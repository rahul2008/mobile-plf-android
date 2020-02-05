/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.LogRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogHandlerTest {

    @Mock
    private AppInfra appInfra;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPublish() throws AILCloudLogDataBuilder.MessageSizeExceedsException {
        final AILCloudLogDataBuilder ailCloudLogDataBuilder = mock(AILCloudLogDataBuilder.class);
        final AILCloudLogDBManager cloudLogDBManager = mock(AILCloudLogDBManager.class);
        CloudLogHandler cloudLogHandler = new CloudLogHandler(appInfra) {
            @NonNull
            @Override
            AILCloudLogDataBuilder getAilCloudLogDataBuilder(AppInfraInterface appInfra) {
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(cloudLogDBManager).insertLog(ailCloudLogData);
    }
}