package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.PartialPullSuccess;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronisationMonitorTest {

    private static final String START_DATE = new DateTime().toString();
    private static final String END_DATE = new DateTime().toString();

    private SynchronisationMonitor monitor;

    private Event event;

    @Mock
    private DataPullSynchronise dataPullSynchroniseMock;

    @Mock
    private DataPushSynchronise dataPushSynchroniseMock;

    @Mock
    private AppComponent appComponent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponent);

        monitor = new SynchronisationMonitor();
        monitor.pullSynchronise = dataPullSynchroniseMock;
        monitor.pushSynchronise = dataPushSynchroniseMock;
    }

    @Test
    public void startFetch() {
        thenVerifyPullIsInvoked(whenPullEventIsPosted().getEventId());
    }

    @Test
    public void startFetchByDateRange() {
        thenVerifyPullByDateRangeIsInvoked(whenPullByDateRangeEventIsPosted().getEventId());
    }

    @Test
    public void startPush() {
        thenVerifyPushIsInvoked(whenPushEventIsPosted().getEventId());
    }

    @Test
    public void onEvent_PostPartialSuccess() {
        whenPartialPullSuccessEventIsPosted();
        thenVerifyPartialSuccessIsInvoked();
    }

    private void thenVerifyPartialSuccessIsInvoked() {
        verify(dataPullSynchroniseMock).postPartialSyncError(START_DATE);
    }

    private void whenPartialPullSuccessEventIsPosted() {
        monitor.onEventAsync(new PartialPullSuccess(START_DATE));
    }

    private void thenVerifyPullIsInvoked(int eventID) {
        verify(dataPullSynchroniseMock).startSynchronise(eventID);
    }

    private void thenVerifyPullByDateRangeIsInvoked(int eventId) {
        verify(dataPullSynchroniseMock).startSynchronise(START_DATE, END_DATE, eventId);
    }

    private void thenVerifyPushIsInvoked(int eventID) {
        verify(dataPushSynchroniseMock).startSynchronise(eventID);
    }

    private ReadDataFromBackendRequest whenPullEventIsPosted() {
        ReadDataFromBackendRequest event = new ReadDataFromBackendRequest();
        monitor.onEventAsync(event);
        return event;
    }

    private FetchByDateRange whenPullByDateRangeEventIsPosted() {
        FetchByDateRange event = new FetchByDateRange(START_DATE, END_DATE);
        monitor.onEventAsync(event);
        return event;
    }

    private WriteDataToBackendRequest whenPushEventIsPosted() {
        WriteDataToBackendRequest event = new WriteDataToBackendRequest();
        monitor.onEventAsync(event);
        return event;
    }
}