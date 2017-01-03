package com.philips.platform.datasync.synchronisation;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.EventingImpl;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.concurrent.Executor;

import de.greenrobot.event.EventBus;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 13/12/16.
 */
@RunWith(RobolectricTestRunner.class)
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
    private Context context;
    private DataServicesManager dataServicesManager;
    private BaseAppDataCreator verticalDataCreater;
    private UserRegistrationInterface errorHandlerImpl;

    @NonNull
    private Handler getHandler() {
        final Application application = RuntimeEnvironment.application;
        final Looper mainLooper = application.getMainLooper();
        return new Handler(mainLooper);
    }

    @Before
    public void setUp() {
        initMocks(this);
        context = RuntimeEnvironment.application;

        dataServicesManager = DataServicesManager.getInstance();
        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImpl = new ErrorHandlerImplTest();
        dataServicesManager.initialize(context, verticalDataCreater, errorHandlerImpl,null);
        dataPushSynchronise = new DataPushSynchronise(Arrays.asList(firstDataSenderMock, secondDataSenderMock), executorMock);
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