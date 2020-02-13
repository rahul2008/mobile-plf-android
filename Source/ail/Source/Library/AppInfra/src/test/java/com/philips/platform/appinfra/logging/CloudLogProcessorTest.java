package com.philips.platform.appinfra.logging;

import android.os.Handler;
import androidx.annotation.NonNull;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Yogesh on 5/9/18.
 */
public class CloudLogProcessorTest extends TestCase {


    private CloudLogProcessor cloudLogProcessor;
    @Mock
    private Handler handler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        cloudLogProcessor = new CloudLogProcessor("name") {
            @NonNull
            @Override
            android.os.Handler getHandler() {
                return handler;
            }
        };
        cloudLogProcessor.prepareHandler();
    }

    public void testPostTask() {
        Runnable runnable = mock(Runnable.class);
        cloudLogProcessor.postTask(runnable);
        verify(handler).post(runnable);

    }
}