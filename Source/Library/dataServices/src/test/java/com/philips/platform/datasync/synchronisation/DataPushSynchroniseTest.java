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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 13/12/16.
 */
public class DataPushSynchroniseTest {

    private final int TEST_REFERENCE_ID = 1;

    private DataPushSynchronise dataPushSynchronise;

    @Mock
    private Eventing eventingMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private Executor executorMock;

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

    @Captor
    private ArgumentCaptor<GetNonSynchronizedDataRequest> getNonSynchronizedDataRequestArgumentCaptor;

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
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);

        //verify(eventingMock).post(errorEventArgumentCaptor.capture());

       // assertThat(errorEventArgumentCaptor.getValue().getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostGetNonSynchronizedEvent_WhenUserIsLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);

        // verify(eventingMock).post(getNonSynchronizedDataRequestArgumentCaptor.capture());

       // assertThat(getNonSynchronizedDataRequestArgumentCaptor.getValue().getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostGetNonSynchronizedEvent_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        dataPushSynchronise.startSynchronise(TEST_REFERENCE_ID);

        // verify(eventingMock).post(getNonSynchronizedDataRequestArgumentCaptor.capture());

        // assertThat(getNonSynchronizedDataRequestArgumentCaptor.getValue().getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldCallInExecutor_WhenSyncSucceed() throws Exception {

        when(responseEventMock.getReferenceId()).thenReturn(TEST_REFERENCE_ID);

        dataPushSynchronise.onEventAsync(responseEventMock);
        //runExecutor();
    }

    @Test
    public void ShouldUnRegister_WhenUnRegisterEventWhenTrue() throws Exception {

        when(eventingMock.isRegistered(responseEventMock)).thenReturn(true);
        dataPushSynchronise.unRegisterEvent();
        //verify(eventingMock).unregister(responseEventMock);
        //runExecutor();
    }

    private void runExecutor() {
        verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());

        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }

}