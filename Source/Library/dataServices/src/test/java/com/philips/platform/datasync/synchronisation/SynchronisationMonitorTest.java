package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 01/12/16.
 */
public class SynchronisationMonitorTest {

    private SynchronisationMonitor monitor;

    @Mock
    private DataPullSynchronise synchroniseMock;

    @Mock
    private DataPushSynchronise dataPushSynchroniseMock;

    @Mock
    private Eventing eventingMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        monitor = new SynchronisationMonitor(synchroniseMock, dataPushSynchroniseMock);
        monitor.start(eventingMock);
    }

    @Test
    public void ShouldCallSynchronise_WhenSyncAsked() throws Exception {
        DateTime dateTime = DateTime.now().minusDays(4);
        ReadDataFromBackendRequest event = new ReadDataFromBackendRequest(dateTime);
        monitor.onEventAsync(event);

        verify(synchroniseMock).startSynchronise(dateTime, event.getEventId());
    }

    @Test
    public void ShouldCallDataPushSynchronise_WhenSyncAsked() throws Exception {
        WriteDataToBackendRequest event = new WriteDataToBackendRequest();
        monitor.onEventAsync(event);

        verify(dataPushSynchroniseMock).startSynchronise(event.getEventId());
    }

}