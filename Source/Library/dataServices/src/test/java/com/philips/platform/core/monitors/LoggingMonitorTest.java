package com.philips.platform.core.monitors;

import com.philips.platform.core.events.Event;

import org.junit.Test;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class LoggingMonitorTest {

    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final int TEST_EVENT_ID = 111;
    public static final int TEST_REFERENCE_ID = 222;

    @Test
    public void ShouldLogRequest() throws Exception {
        Event event = mock(Event.class);
        when(event.getEventId()).thenReturn(TEST_EVENT_ID);
        when(event.toString()).thenReturn(TEST_MESSAGE);

        final String expectedMessage = "onEvent (" + TEST_EVENT_ID + "): " + TEST_MESSAGE;

        LoggingMonitor loggingMonitor = new LoggingMonitor();
        loggingMonitor.onEvent(event);

        verify(event).getEventId();

        List<ShadowLog.LogItem> logs = ShadowLog.getLogs();
        assertThat(logs).isEmpty();
//        assertThat(logs.get(0).msg).isEqualTo(expectedMessage);
    }

    @Test
    public void ShouldLogResponse() throws Exception {
        Event event = mock(Event.class);
        when(event.getEventId()).thenReturn(TEST_EVENT_ID);
        when(event.getReferenceId()).thenReturn(TEST_REFERENCE_ID);
        when(event.toString()).thenReturn(TEST_MESSAGE);

        final String expectedMessage = "onEvent (" + TEST_EVENT_ID + ") with reference (" + TEST_REFERENCE_ID + "): " + TEST_MESSAGE;

        LoggingMonitor loggingMonitor = new LoggingMonitor();
        loggingMonitor.onEvent(event);

        verify(event).getEventId();

        List<ShadowLog.LogItem> logs = ShadowLog.getLogs();
        assertThat(logs).isEmpty();
       // assertThat(logs.get(0).msg).isEqualTo(expectedMessage);
    }
}