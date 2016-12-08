package com.philips.platform.core.monitors;

import android.os.Handler;
import android.util.Log;

import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.utils.DSLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import de.greenrobot.event.SubscriberExceptionEvent;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ExceptionMonitorTest {

    private ExceptionMonitor exceptionMonitor;

    @Mock
    private Handler handlerMock;

    @Mock
    Log log;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Mock
    ExceptionEvent eventMock;

    @Mock
    DSLog dsLogMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        exceptionMonitor = new ExceptionMonitor(handlerMock);
    }

    @Test(expected = IllegalStateException.class)
    public void ShouldThrowException_WhenExceptionEventIsReceived() throws Exception {
        exceptionMonitor.onEventMainThread(eventMock);

        verify(eventMock).getEventId();
        verify(eventMock).getCause();
        verify(eventMock).getMessage();

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
    }

    @Test(expected = IllegalStateException.class)
    public void ShouldThrowException_WhenSubscriberExceptionEventIsReceived() throws Exception {
        SubscriberExceptionEvent event = new SubscriberExceptionEvent(null, new RuntimeException("test"), null, null);

        exceptionMonitor.onEventMainThread(event);

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
    }
}
