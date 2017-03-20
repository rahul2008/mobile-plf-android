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

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
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

    @Mock
    TypedByteArray typedByteArrayMock;

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

    @Test
    public void ShouldCall_posterror() throws Exception {
        errorMonitor.postError(retrofitError);
        verify(errorHandlingInterface).syncError(-1);
    }

    @Test
    public void ShouldCall_reportError() throws Exception {
        errorMonitor.reportError(500);
        verify(errorHandlingInterface).syncError(500);
    }

    @Test
    public void ShouldCall_reportErrorUnAuthorized() throws Exception {
        errorMonitor.reportError(401);
        verifyNoMoreInteractions(errorHandlingInterface);
       // verify(errorHandlingInterface).syncError(-1);
    }

   /* @Test
    public void ShouldStart_WhenonEventBackgroundThreadBackendResponse_called() throws Exception {
        errorMonitor.onEventAsync(new BackendResponse(501));
        verify(errorHandlingInterface).syncError(-1);
    }*/

    @Test
    public void ShouldPostError_On_Error() throws Exception {
        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 403, string, headers, typedByteArrayMock);
        when(retrofitError.getResponse()).thenReturn(response);
        errorMonitor.postError(retrofitError);
    }
}
