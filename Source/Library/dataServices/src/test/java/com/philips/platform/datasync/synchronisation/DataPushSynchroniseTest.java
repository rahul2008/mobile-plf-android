package com.philips.platform.datasync.synchronisation;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataPushSynchroniseTest {

    private final int TEST_REFERENCE_ID = 1;

    private DataPushSynchronise dataPushSynchronise;

    @Mock
    private Eventing eventingMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private ExecutorService executorMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedEventMock;

    @Mock
    private DataSender firstDataSenderMock;

    @Mock
    private DataSender secondDataSenderMock;

    @Mock
    private GetNonSynchronizedDataResponse responseEventMock;

//    @Captor
//    private ArgumentCaptor<WriteDataToBackendResponse> successCaptor;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventArgumentCaptor;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Captor
    private ArgumentCaptor<GetNonSynchronizedDataRequest> getNonSynchronizedDataRequestArgumentCaptor;

    @Captor
    private ArgumentCaptor<BackendResponse> backendResponseArgumentCaptor;

    private BaseAppDataCreator verticalDataCreater;
    private UserRegistrationInterface errorHandlerImpl;

    @Mock
    private AppComponent appComponantMock;

    @NonNull
    private Handler getHandler() {
        final Application application = RuntimeEnvironment.application;
        final Looper mainLooper = application.getMainLooper();
        return new Handler(mainLooper);
    }

    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImpl = new ErrorHandlerImplTest();
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        Set set=new HashSet();
        set.add("moment");
        set.add("Settings");
        set.add("characteristics");
        set.add("consent");

        DataServicesManager.getInstance().configureSyncDataType(set);

        dataPushSynchronise = new DataPushSynchronise(Arrays.asList(firstDataSenderMock, secondDataSenderMock));
        dataPushSynchronise.accessProvider = accessProviderMock;
        dataPushSynchronise.eventing = eventingMock;

        dataPushSynchronise.synchronisationManager=synchronisationManagerMock;
        dataPushSynchronise.executor = executorMock;
        ArrayList list = new ArrayList();
        list.add(firstDataSenderMock);
        list.add(secondDataSenderMock);
        dataPushSynchronise.senders = list;
        dataPushSynchronise.configurableSenders = list;
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);
        verify(eventingMock).post(errorEventArgumentCaptor.capture());
        assertThat(errorEventArgumentCaptor.getValue().getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostGetNonSynchronizedEvent_WhenUserIsLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);
        verify(eventingMock).post(getNonSynchronizedDataRequestArgumentCaptor.capture());
        assertThat(getNonSynchronizedDataRequestArgumentCaptor.getValue().getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostGetNonSynchronizedEvent_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);
        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void ShouldCallInExecutor_WhenSyncSucceed() throws Exception {
        when(responseEventMock.getReferenceId()).thenReturn(TEST_REFERENCE_ID);
        dataPushSynchronise.onEventAsync(responseEventMock);
        runExecutor();
        //TODO: Spoorti - Fix it and see what has to be verified
       // verify(synchronisationManagerMock).shutdownAndAwaitTermination(executorMock);
    }

    @Test
    public void ShouldUnRegister_WhenUnRegisterEventWhenTrue() throws Exception {
        when(eventingMock.isRegistered(dataPushSynchronise)).thenReturn(true);
        dataPushSynchronise.unRegisterEvent();
        verify(eventingMock).unregister(dataPushSynchronise);
        //runExecutor();
    }

    private void runExecutor() {
       // verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());

        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }

}