package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 01/12/16.
 */
public class UpdatingMonitorTest {

    @Mock
    DBUpdatingInterface dbUpdatingInterface;

    @Mock
    DBDeletingInterface dbDeletingInterface;

    @Mock
    MomentUpdateRequest momentUpdateRequestmock;

    @Mock
    DatabaseConsentUpdateRequest consentUpdateRequestmock;

    @Mock
    ReadDataFromBackendResponse readDataFromBackendResponseMock;

    @Mock
    BackendMomentListSaveRequest backendMomentListSaveRequestMock;

    @Mock
    MomentDataSenderCreatedRequest momentDataSenderCreatedRequestMock;

    @Mock
    ConsentBackendSaveResponse consentBackendSaveResponseMock;
    @Mock
    Moment momentMock;

    @Mock
    Consent consentMock;
    @Mock
    DBFetchingInterface dbFetchingInterface;

    UpdatingMonitor updatingMonitor;
    @Mock
    BackendResponse backendResponseMock;
    @Mock
    BackendMomentRequestFailed backendMomentRequestFailedMock;
    @Mock
    private Eventing eventingMock;

    @Before
    public void setUp() {
        initMocks(this);

        updatingMonitor = new UpdatingMonitor(dbUpdatingInterface, dbDeletingInterface, dbFetchingInterface);
        updatingMonitor.start(eventingMock);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentUpdateRequestIsCalled() throws Exception {
        when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        when(momentUpdateRequestmock.getMoment()).thenReturn(momentMock);
        updatingMonitor.onEventAsync(momentUpdateRequestmock);
        verify(momentMock).setSynced(false);
        verify(dbUpdatingInterface).updateOrSaveMomentInDatabase(momentMock);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenDatabaseConsentUpdateRequestIsCalled() throws Exception {

        when(consentUpdateRequestmock.getConsent()).thenReturn(consentMock);
        updatingMonitor.onEventAsync(consentUpdateRequestmock);
//        verify(consentMock).setSynced(false);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenConsentBackendSaveResponseIsCalled() throws Exception {

        when(consentBackendSaveResponseMock.getConsent()).thenReturn(consentMock);
        updatingMonitor.onEventAsync(consentBackendSaveResponseMock);
//        verify(consentMock).setSynced(false);
    }

    /*@Test
    public void shouldDeleteUpdateAndPostMoment_whenonEventBackgroundThreadIsCalled() throws Exception {

        when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        updatingMonitor.onEventBackgroundThread(backendResponseMock);
        verify(dbUpdatingInterface).postRetrofitError(backendResponseMock.getCallException());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadIsCalled() throws Exception {

        when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        updatingMonitor.onEventBackgroundThread(backendMomentRequestFailedMock);
        verify(dbUpdatingInterface).updateFailed(backendMomentRequestFailedMock.getException());
    }*/

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenReadDataFromBackendResponsePassed() throws Exception {
        updatingMonitor.onEventBackgroundThread(readDataFromBackendResponseMock);
        verify(dbFetchingInterface).fetchMoments();
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestPassed() throws Exception {
        updatingMonitor.onEventBackgroundThread(backendMomentListSaveRequestMock);
        List<? extends Moment> moments = backendMomentListSaveRequestMock.getList();
       // verify(dbUpdatingInterface).processMomentsReceivedFromBackend(moments);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenMomentDataSenderCreatedRequestPassed() throws Exception {
        updatingMonitor.onEventBackgroundThread(momentDataSenderCreatedRequestMock);
        List<? extends Moment> moments = momentDataSenderCreatedRequestMock.getList();
        // verify(dbUpdatingInterface).processCreatedMoment(moments);
    }

}