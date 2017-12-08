package com.philips.platform.core.monitors;

import com.philips.platform.core.utils.EventingImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventMonitorTest {

    private class TestEventMonitor extends EventMonitor {
    }

    @Mock
    EventingImpl eventingMock;
    private EventMonitor eventMonitor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventMonitor = new TestEventMonitor();
    }

    @Test
    public void ShouldRegisterToBus_WhenStarted() throws Exception {
        eventMonitor.start(eventingMock);
        verify(eventingMock).register(eventMonitor);
    }

    @Test
    public void ShouldIgnoreStopCall_WhenNotStarted() throws Exception {
        eventMonitor.stop();
    }

    @Test
    public void ShouldUnregisterFromBus_WhenStopped() throws Exception {
        eventMonitor.start(eventingMock);
        eventMonitor.stop();

        verify(eventingMock).unregister(eventMonitor);
    }

    @Test
    public void ShouldNotRegisterTwice_WhenStartedTwiceInARow() throws Exception {
        eventMonitor.start(eventingMock);
        when(eventingMock.isRegistered(eventMonitor)).thenReturn(true);

        eventMonitor.start(eventingMock);

        verify(eventingMock).register(eventMonitor);
    }

    @Test
    public void ShouldReRegister_WhenStartedStoppedAndStarted() throws Exception {
        eventMonitor.start(eventingMock);
        eventMonitor.stop();
        eventMonitor.start(eventingMock);

        verify(eventingMock, times(2)).register(eventMonitor);
    }
}
