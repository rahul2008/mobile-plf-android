package com.philips.platform.datasync;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.monitors.EventMonitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 06/12/16.
 */
public class BackendTest {


    @Mock
    Eventing eventingMock;

    @Mock
    EventMonitor eventMonitorMock;

    private Backend backend;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        backend = new Backend(eventMonitorMock, eventMonitorMock);
    }

    @Test
    public void ShouldStartAllMonitors_WhenStarted() {
        backend.start(eventingMock);

        verify(eventMonitorMock, times(2)).start(eventingMock);
    }

    @Test
    public void ShouldStartAllMonitors_WhenStop() {
        backend.stop();

        verify(eventMonitorMock, times(2)).stop();
    }

}