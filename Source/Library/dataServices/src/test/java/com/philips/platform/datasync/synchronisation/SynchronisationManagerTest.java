package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.spy.EventingSpy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronisationManagerTest {

    @Mock
    private AppComponent appComponantMock;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListner;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListenerMock;

    @Mock
    ExecutorService executorServiceMock;

    SynchronisationManager synchronisationManager;

    private EventingSpy eventingSpy;
    private static final String START_DATE = new DateTime().toString();
    private static final String END_DATE = new DateTime().toString();

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        synchronisationManager = new SynchronisationManager();
        eventingSpy = new EventingSpy();
        synchronisationManager.mEventing = eventingSpy;
        synchronisationManager.mSynchronisationCompleteListener = synchronisationCompleteListenerMock;
    }

    @Test
    public void postEventWriteDataToBackendRequest_whenDataPullSuccessIsCalled() throws Exception {
        synchronisationManager.dataPullSuccess();
        thenVerifyEventIsPosted("WriteDataToBackendRequest");
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
    public void shouldTerminatePull_WhenShutdownAndAwaitTerminationIsCalled() throws Exception {
        synchronisationManager.shutdownAndAwaitTermination(executorServiceMock);
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
    public void startSync_WithNoDateRange() {
        whenStartFetchIsInvoked(null, null, synchronisationCompleteListenerMock);
        thenVerifyEventIsPosted("ReadDataFromBackendRequest");
    }

    @Test
    public void startFetch_WithDateRange() {
        whenStartFetchIsInvoked(START_DATE, END_DATE, synchronisationCompleteListenerMock);
        thenVerifyEventIsPosted("FetchByDateRange");
    }

    private void whenStartFetchIsInvoked(String startDate, String endDate, SynchronisationCompleteListener synchronisationCompleteListenerMock) {
        synchronisationManager.startSync(startDate, endDate, synchronisationCompleteListenerMock);
    }

    private void thenVerifyEventIsPosted(String event) {
        assertEquals(event, eventingSpy.postedEvent.getClass().getSimpleName());
    }
}