package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 07/12/16.
 */
public class DBMonitorsTest {

    @Mock
    DBMonitors dbMonitors;
    @Mock
    List<EventMonitor> eventMonitors;
    @Mock
    Eventing eventing;
    @Mock
    EventMonitor eventMonitor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        dbMonitors = new DBMonitors(eventMonitors);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldStart_WhenMonitorStarts() throws Exception {
        dbMonitors.start(eventing);
        Mockito.verify(eventMonitor).start(eventing);
    }

    @Test
    public void ShouldStartListOfMonitors_WhenMonitorStarts() throws Exception {
        eventMonitor.start(eventing);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldStop_WhenMonitorStop() throws Exception {
        dbMonitors.stop();
        Mockito.verify(eventMonitor).stop();
    }

}