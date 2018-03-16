package com.philips.platform.datasync.synchronisation;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.DeleteExpiredInsightRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.spy.SynchronisationCompleteListenerSpy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronisationManagerTest {

    private static final DateTime START_DATE = new DateTime();
    private static final DateTime END_DATE = new DateTime();

    private SynchronisationCompleteListenerSpy synchronisationCompleteListenerSpy;

    private SynchronisationManager synchronisationManager;

    @Mock
    private Eventing eventingMock;

    @Mock
    private AppComponent appComponentMock;

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
        synchronisationManager.mEventing = eventingMock;
        synchronisationCompleteListenerSpy = new SynchronisationCompleteListenerSpy();
        synchronisationManager.mSynchronisationCompleteListener = synchronisationCompleteListenerSpy;
        synchronisationManager.expiredDeletionTimeStorage = prefsMock;
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putString(anyString(), anyString())).thenReturn(prefsEditorMock);
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
    public void postEventWriteDataToBackendRequest_whenDataPullSuccessIsCalled() throws Exception {
        givenLastDeletionTimeIsHoursAgo(27);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();
        forceSuccessCallbackWhenDeleteExpiredInsightRequest();

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsPosted(WriteDataToBackendRequest.class);
    }

    @Test
    public void shouldDeleteAllExpiredMoments_whenDataPushSuccessIsCalled()  {
        givenLastDeletionTimeIsHoursAgo(27);

        synchronisationManager.dataPullSuccess();

        verify(eventingMock).post(isA(DeleteExpiredMomentRequest.class));
    }

    @Test
    public void shouldDeleteAllExpiredInsights_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(27);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();

        synchronisationManager.dataPullSuccess();


        verify(eventingMock).post(isA(DeleteExpiredInsightRequest.class));
    }

    @Test
    public void shouldSaveLastDeletionDateTime_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(27);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();
        forceSuccessCallbackWhenDeleteExpiredInsightRequest();

        synchronisationManager.dataPullSuccess();

        verifyDeletionTimeUpdated();
    }

    @Test
    public void shouldNotDeleteAllExpiredMoments_whenDataPushSuccessIsCalled()  {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        verify(eventingMock, never()).post(isA(DeleteExpiredMomentRequest.class));
    }

    @Test
    public void shouldNotDeleteAllExpiredInsights_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(3);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();

        synchronisationManager.dataPullSuccess();


        verify(eventingMock, never()).post(isA(DeleteExpiredInsightRequest.class));
    }

    @Test
    public void shouldNotSaveLastDeletionDateTime_whenDataPushSuccessIsCalled() {
        givenLastDeletionTimeIsHoursAgo(3);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();
        forceSuccessCallbackWhenDeleteExpiredInsightRequest();

        synchronisationManager.dataPullSuccess();

        verifyDeletionTimeNotUpdated();
    }

    @Test
    public void shouldNotUpdateDeletionTime_whenDataPushSuccessIsCalled_butDeletingMomentsFailed()  {
        givenLastDeletionTimeIsHoursAgo(25);
        forceFailureCallbackWhenDeleteExpiredMomentRequest();

        synchronisationManager.dataPullSuccess();

        verify(eventingMock, never()).post(isA(DeleteExpiredInsightRequest.class));
    }

    @Test
    public void shouldNotUpdateDeletionTime_whenDataPushSuccessIsCalled_butDeletingInsightsFailed()  {
        givenLastDeletionTimeIsHoursAgo(25);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();
        forceFailureCallbackWhenDeleteExpiredInsightRequest();

        synchronisationManager.dataPullSuccess();

        verifyDeletionTimeNotUpdated();
    }

    @Test
    public void startSync_WithNoDateRange() {
        whenStartSyncIsInvoked(synchronisationCompleteListenerSpy);
        thenVerifyEventIsPosted(ReadDataFromBackendRequest.class);
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
        thenVerifyEventIsPosted(FetchByDateRange.class);
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

        thenVerifyEventIsNotInEventBus(DeleteExpiredMomentRequest.class);
    }

    @Test
    public void shouldNotSendDeleteExpiredInsightsEvent_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        thenVerifyEventIsNotInEventBus(DeleteExpiredInsightRequest.class);
    }

    @Test
    public void shouldSendWriteDataToBackendRequest_whenDataPullSuccess_LessThan24hoursAgo() {
        givenLastDeletionTimeIsHoursAgo(3);

        synchronisationManager.dataPullSuccess();

        verify(eventingMock).post(isA(WriteDataToBackendRequest.class));
    }

    @Test
    public void shouldSetDeletionFlag_whenSuccessfullyClearedExpiredData_andDeletionTimeLongAgo() {
        givenLastDeletionTimeIsHoursAgo(27);
        forceSuccessCallbackWhenDeleteExpiredMomentRequest();
        forceSuccessCallbackWhenDeleteExpiredInsightRequest();

        synchronisationManager.dataPullSuccess();

        verifyDeletionTimeUpdated();
    }

    private void givenSomeSyncInProgress() {
        synchronisationManager.startSync(synchronisationCompleteListenerSpy);
        reset(eventingMock);
    }

    private void whenStartSyncIsInvoked(SynchronisationCompleteListener synchronisationCompleteListenerMock) {
        synchronisationManager.startSync(synchronisationCompleteListenerMock);
    }

    private void whenStartSyncByDateRangeIsInvoked(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListenerMock) {
        synchronisationManager.startSync(startDate, endDate, synchronisationCompleteListenerMock);
    }

    private void thenVerifyEventIsPosted(Class eventClass) {
        verify(eventingMock).post((Event) isA(eventClass));
//        assertEquals(event, eventingSpy.postedEvent.getClass().getSimpleName());
    }

    private void thenVerifyEventIsNotInEventBus(final Class event) {
        verify(eventingMock, never()).post((Event) isA(event));
//        boolean isThere = false;
//        for (Event e : eventingSpy.eventBus) {
//            if (e.getClass().getSimpleName().equals(event))
//                isThere = true;
//        }
//        assertFalse(isThere);
    }

    private void thenVerifyNoEventIsPosted() {
        verify(eventingMock, never()).post((Event) any());
    }

    private void thenVerifyOnSyncFailedIsCalled(String message) {
        assertEquals(message, synchronisationCompleteListenerSpy.exception.getMessage());
    }

    private void givenLastDeletionTimeIsHoursAgo(int hours) {
        DateTime oldTime = DateTime.now(DateTimeZone.UTC).minusHours(hours);
        when(prefsMock.getString(eq("LAST_EXPIRED_DELETION_DATE_TIME"), anyString())).thenReturn(oldTime.toString());
    }

    private void forceSuccessCallbackWhenDeleteExpiredMomentRequest() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DeleteExpiredMomentRequest deleteExpiredMomentRequest = (DeleteExpiredMomentRequest) invocation.getArguments()[0];
                DBRequestListener<Integer> listener = deleteExpiredMomentRequest.getDbRequestListener();

                listener.onSuccess(null);
                return null;
            }
        }).when(eventingMock).post(isA(DeleteExpiredMomentRequest.class));
    }

    private void forceFailureCallbackWhenDeleteExpiredMomentRequest() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DeleteExpiredMomentRequest deleteExpiredMomentRequest = (DeleteExpiredMomentRequest) invocation.getArguments()[0];
                DBRequestListener<Integer> listener = deleteExpiredMomentRequest.getDbRequestListener();

                listener.onFailure(new Exception());
                return null;
            }
        }).when(eventingMock).post(isA(DeleteExpiredMomentRequest.class));
    }

    private void forceSuccessCallbackWhenDeleteExpiredInsightRequest() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DeleteExpiredInsightRequest deleteExpiredInsightRequest = (DeleteExpiredInsightRequest) invocation.getArguments()[0];
                DBRequestListener<Insight> listener = deleteExpiredInsightRequest.getDbRequestListener();

                listener.onSuccess(null);
                return null;
            }
        }).when(eventingMock).post(isA(DeleteExpiredInsightRequest.class));
    }

    private void forceFailureCallbackWhenDeleteExpiredInsightRequest() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DeleteExpiredInsightRequest deleteExpiredInsightRequest = (DeleteExpiredInsightRequest) invocation.getArguments()[0];
                DBRequestListener<Insight> listener = deleteExpiredInsightRequest.getDbRequestListener();

                listener.onFailure(new Exception());
                return null;
            }
        }).when(eventingMock).post(isA(DeleteExpiredInsightRequest.class));
    }

    private void verifyDeletionTimeUpdated() {
        verify(prefsEditorMock).putString(eq("LAST_EXPIRED_DELETION_DATE_TIME"), anyString());
    }

    private void verifyDeletionTimeNotUpdated() {
        verify(prefsEditorMock, never()).putString(eq("LAST_EXPIRED_DELETION_DATE_TIME"), anyString());
    }
}