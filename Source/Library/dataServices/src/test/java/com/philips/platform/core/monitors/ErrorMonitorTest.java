package com.philips.platform.core.monitors;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit.RetrofitError;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by 310218660 on 1/8/2017.
 */

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
    BackendMomentRequestFailed backendMomentRequestFailed;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        errorMonitor = new ErrorMonitor(errorHandlingInterface);
        errorMonitor.userRegistrationInterface = userRegistrationInterfaceMock;
    }

    @Test
    public void ShouldStart_WhenonEventBackgroundThread() throws Exception {
        errorMonitor.onEventBackgroundThread(backendMomentRequestFailed);
        verify(errorHandlingInterface).syncError(-1);
    }

}
