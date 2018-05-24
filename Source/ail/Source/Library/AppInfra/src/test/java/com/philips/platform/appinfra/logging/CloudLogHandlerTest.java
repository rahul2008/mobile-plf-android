package com.philips.platform.appinfra.logging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.logging.database.AILCloudLogData;
import com.philips.platform.appinfra.logging.database.AILCloudLogDataBuilder;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.LogRecord;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogHandlerTest extends TestCase {

    private CloudLogHandler cloudLogHandler;
    @Mock
    private AppInfra appInfra;
    @Mock
    private CloudLogProcessor cloudLogProcessor;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testConstructorInit() {
        cloudLogHandler = new CloudLogHandler(appInfra) {
            @NonNull
            @Override
            CloudLogProcessor getCloudLogProcessor() {
                return cloudLogProcessor;
            }
        };
        verify(cloudLogProcessor).start();
        verify(cloudLogProcessor).prepareHandler();
    }
}