package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronisationManagerTest {

    @Mock
    private AppComponent appComponantMock;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImpl;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListner;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListenerMock;

    SynchronisationManager synchronisationManager;

    @Mock
    Eventing eventingMock;

    @Mock
    ExecutorService executorServiceMock;


    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImpl = new ErrorHandlerImplTest();
        DataServicesManager.getInstance().setAppComponant(appComponantMock);

        synchronisationManager=new SynchronisationManager();
        synchronisationManager.mEventing=eventingMock;
        synchronisationManager.mSynchronisationCompleteListner = synchronisationCompleteListenerMock;

    }

    @Test
    public void shouldStartSync_WhenstartSyncIsCalled() throws Exception {
        synchronisationManager.startSync(synchronisationCompleteListenerMock);
    }

    @Test
    public void postEventWriteDataToBackendRequest_whenDataPullSuccessIsCalled() throws Exception {
        synchronisationManager.dataPullSuccess();
        verify(eventingMock).post(isA(WriteDataToBackendRequest.class));
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
}