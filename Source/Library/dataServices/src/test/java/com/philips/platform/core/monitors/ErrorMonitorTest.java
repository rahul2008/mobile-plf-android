package com.philips.platform.core.monitors;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit.RetrofitError;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ErrorMonitorTest {

    ErrorMonitor errorMonitor;

    @Mock
    ErrorHandlingInterface errorHandlingInterface;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    RetrofitError retrofitError;

    @Mock
    private UserRegistrationInterface userRegistrationInterfaceMock;

    @Mock
    BackendDataRequestFailed backendDataRequestFailed;

    @Mock
    SynchronisationManager synchronisationManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        errorMonitor = new ErrorMonitor(errorHandlingInterface);
        errorMonitor.userRegistrationInterface = userRegistrationInterfaceMock;
        errorMonitor.synchronisationManager = synchronisationManager;
    }

    @Test
    public void ShouldStart_WhenonEventBackgroundThread() throws Exception {
        errorMonitor.onEventAsync(backendDataRequestFailed);
        verify(errorHandlingInterface).syncError(-1);
    }

}
