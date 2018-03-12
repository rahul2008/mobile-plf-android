package com.philips.platform.datasync.synchronisation;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.core.events.DeleteExpiredInsightRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.spy.SynchronisationCompleteListenerSpy;
import com.philips.spy.EventingSpy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.ExecutorService;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronisationManagerTest {

    private static final DateTime START_DATE = new DateTime();
    private static final DateTime END_DATE = new DateTime();

    private EventingSpy eventingSpy;
    private SynchronisationCompleteListenerSpy synchronisationCompleteListenerSpy;

    @Mock
    private AppComponent appComponentMock;

    @Mock
    private ExecutorService executorServiceMock;

    private SynchronisationManager synchronisationManager;

    @Mock
    private Context context;

    @Mock
    private SharedPreferences prefsMock;

    @Mock
    private SharedPreferences.Editor prefsEditorMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponentMock);
        DataServicesManager.getInstance().setDataServiceContext(context);
        synchronisationManager = new SynchronisationManager();
        eventingSpy = new EventingSpy();
        eventingSpy.postedEvent = null;
        synchronisationManager.mEventing = eventingSpy;
        synchronisationCompleteListenerSpy = new SynchronisationCompleteListenerSpy();
        synchronisationManager.mSynchronisationCompleteListener = synchronisationCompleteListenerSpy;
        synchronisationManager.expiredDeletionTimeStorage = prefsMock;
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putString(anyString(), anyString())).thenReturn(prefsEditorMock);
    }

    @Test
    public void postEventWriteDataToBackendRequest_whenDataPullSuccessIsCalled() throws Exception {
        givenLastDeletionTimeIsHoursAgo(27);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsPosted(WriteDataToBackendRequest.class.getSimpleName());
    }

    @Test
    public void shouldMakeSynchronizationListenerNull_WhenDataPullFailIsCalled() throws Exception {
        synchronisationManager.dataPullFail(new Exception());
    }

    @Test
    public void shouldMakeSynchronizationListenerNull_WhenDataPushFailIsCalled() throws Exception {
        synchronisationManager.dataPushFail(new Exception());
    }

    @Test
    public void postEventWriteDataToBackendRequest_whenDataPushSuccessIsCalled() throws Exception {
        synchronisationManager.dataPushSuccess();
    }

    @Test
    public void shouldTerminatePull_WhenDataSyncCompleteIsCalled() throws Exception {
        synchronisationManager.dataSyncComplete();
    }

    @Test
    public void shouldTerminatePull_WhenStopSyncIsCalled() throws Exception {
        synchronisationManager.stopSync();
    }

    @Test
    public void shouldDeleteAllExpiredMoments_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(27);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsInEventBus("DeleteExpiredMomentRequest");
    }

    @Test
    public void shouldDeleteAllExpiredInsights_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(27);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsInEventBus("DeleteExpiredInsightRequest");
    }

    @Test
    public void shouldSaveLastDeletionDateTime_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(27);

        synchronisationManager.dataPullSuccess();

        thenLastDeletionDateTimeIsStored();
    }

    @Test
    public void startSync_WithNoDateRange() {
        whenStartSyncIsInvoked(synchronisationCompleteListenerSpy);
        thenVerifyEventIsPosted("ReadDataFromBackendRequest");
    }

    @Test
    public void startSyncWithNoDateRange_GivenSomeSyncInProgress() {
        givenSomeSyncInProgress();
        whenStartSyncIsInvoked(synchronisationCompleteListenerSpy);
        thenVerifyNoEventIsPosted();
    }

    @Test
    public void startFetch_WithDateRange() {
        whenStartSyncByDateRangeIsInvoked(START_DATE, END_DATE, synchronisationCompleteListenerSpy);
        thenVerifyEventIsPosted("FetchByDateRange");
    }

    @Test
    public void startFetchWithDateRange_GivenSomeSyncInProgress() {
        givenSomeSyncInProgress();
        whenStartSyncByDateRangeIsInvoked(START_DATE, END_DATE, synchronisationCompleteListenerSpy);
        thenVerifyNoEventIsPosted();
        thenVerifyOnSyncFailedIsCalled("Sync is already in progress");
    }

    @Test
    public void startFetchWithDateRange_GivenNullStartDate() {
        whenStartSyncByDateRangeIsInvoked(null, END_DATE, synchronisationCompleteListenerSpy);
        thenVerifyNoEventIsPosted();
        thenVerifyOnSyncFailedIsCalled("Invalid Date Range");
    }

    @Test
    public void startFetchWithDateRange_GivenNullEndDate() {
        givenSomeSyncInProgress();
        whenStartSyncByDateRangeIsInvoked(START_DATE, null, synchronisationCompleteListenerSpy);
        thenVerifyNoEventIsPosted();
        thenVerifyOnSyncFailedIsCalled("Invalid Date Range");
    }

    @Test
    public void shouldNotUpdateDeletionTime_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        verify(prefsEditorMock, never()).putString(anyString(), anyString());
    }

    @Test
    public void shouldNotSendDeleteExpiredMomentsEvent_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsNotInEventBus(DeleteExpiredMomentRequest.class.getSimpleName());
    }

    @Test
    public void shouldNotSendDeleteExpiredInsightsEvent_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsNotInEventBus(DeleteExpiredInsightRequest.class.getSimpleName());
    }

    @Test
    public void shouldSendWriteDataToBackendRequest_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsInEventBus("WriteDataToBackendRequest");
    }

    private void givenSomeSyncInProgress() {
        synchronisationManager.startSync(synchronisationCompleteListenerSpy);
        eventingSpy.postedEvent = null;
    }

    private void whenStartSyncIsInvoked(SynchronisationCompleteListener synchronisationCompleteListenerMock) {
        synchronisationManager.startSync(synchronisationCompleteListenerMock);
    }

    private void whenStartSyncByDateRangeIsInvoked(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListenerMock) {
        synchronisationManager.startSync(startDate, endDate, synchronisationCompleteListenerMock);
    }

    private void thenVerifyEventIsPosted(String event) {
        assertEquals(event, eventingSpy.postedEvent.getClass().getSimpleName());
    }

    private void thenVerifyEventIsInEventBus(final String event) {
        boolean isThere = false;
        for (Event e : eventingSpy.eventBus) {
            if (e.getClass().getSimpleName().equals(event))
                isThere = true;
        }
        assertTrue(isThere);
    }

    private void thenVerifyEventIsNotInEventBus(final String event) {
        boolean isThere = false;
        for (Event e : eventingSpy.eventBus) {
            if (e.getClass().getSimpleName().equals(event))
                isThere = true;
        }
        assertFalse(isThere);
    }

    private void thenLastDeletionDateTimeIsStored() {
        assertNotNull(synchronisationManager.expiredDeletionTimeStorage);
    }

    private void thenVerifyNoEventIsPosted() {
        assertNull(eventingSpy.postedEvent);
    }

    private void thenVerifyOnSyncFailedIsCalled(String message) {
        assertEquals(message, synchronisationCompleteListenerSpy.exception.getMessage());
    }

    private void givenLastDeletionTimeIsHoursAgo(int hours) {
        DateTime oldTime = DateTime.now(DateTimeZone.UTC).minusHours(hours);
        when(prefsMock.getString(eq("LAST_EXPIRED_DELETION_DATE_TIME"), anyString())).thenReturn(oldTime.toString());
    }
}