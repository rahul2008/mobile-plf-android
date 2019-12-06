package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogHandlerTest extends TestCase {

    @Mock
    private AppInfraInterface appInfra;
    @Mock
    private CloudLogProcessor cloudLogProcessor;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testConstructorInit() {
      new CloudLogHandler(appInfra) {
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