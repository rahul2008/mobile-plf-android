package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConsentsMonitorTest {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final int REFERENCE_ID = 0;

    private ConsentsMonitor consentsMonitor;

    private Void response = null;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private ConsentDataSender consentDataSenderMock;

    @Mock
    private ConsentsDataFetcher consentsDataFetcherMock;

    @Mock
    private ConsentsConverter consentsConverterMock;

    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private ConsentsClient consentsClientMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private ConsentBackendSaveRequest consentSaveRequestMock;

   /* @Mock
    private ConsentBackendGetRequest consentBackendGetRequestMock;*/

    @Mock
    private ConsentDetail consentDetailMock;

    @Mock
    private List<UCoreConsentDetail> uCoreConsentDetailMock;

    @Mock
    private Collection<? extends ConsentDetail> consentDetailListMock;

    @Mock
    private ConsentBackendSaveRequest consentSaveListRequestMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveResponse> responseCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Event> eventArgumentCaptor;

    private DataServicesManager dataServicesManager;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImplTest;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImplTest = new ErrorHandlerImplTest();

        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        consentsMonitor = new ConsentsMonitor(consentDataSenderMock, consentsDataFetcherMock);
        consentsMonitor.uCoreAccessProvider = accessProviderMock;
        consentsMonitor.start(eventingMock);
    }


    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }
/*
    @Test
    public void ShouldPostError_WhenUserIsNotLoggedInDuringGetConsent() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }*/

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

  /*  @Test
    public void ShouldPostError_WhenUserAccessTokenIsNullDuringGetConsent() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }*/

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }


}