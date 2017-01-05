package com.philips.platform.datasync.consent;

import android.app.Application;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 28/11/16.
 */
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
    private ConsentsConverter consentsConverterMock;

    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private ConsentsClient consentsClientMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private ConsentBackendSaveRequest consentSaveRequestMock;

    @Mock
    private ConsentBackendGetRequest consentBackendGetRequestMock;

    @Mock
    private Consent consentMock;

    @Mock
    private List<UCoreConsentDetail> uCoreConsentDetailMock;

    @Mock
    private Collection<? extends ConsentDetail> consentDetailListMock;

    @Mock
    private ConsentBackendListSaveRequest consentSaveListRequestMock;

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

        DataServicesManager.getInstance().mAppComponent = appComponantMock;
        consentsMonitor = new ConsentsMonitor(uCoreAdapterMock, consentsConverterMock, gsonConverterMock);
        consentsMonitor.uCoreAccessProvider = accessProviderMock;
        consentsMonitor.start(eventingMock);
    }


    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedInDuringGetConsent() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsNullDuringGetConsent() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmptyDuringGet() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldNotSaveConsent_WhenRequestTypeIsNotSave() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.UPDATE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);

        consentsMonitor.onEventAsync(consentSaveRequestMock);
    }
//
//    @Test
//    public void ShouldSaveConsent_WhenRequestTypeIsSave() throws Exception {
//        doReturn(Collections.singletonList(uCoreConsentDetailMock)).when(consentsConverterMock).convertToUCoreConsentDetails(anyListOf(ConsentDetail.class));
//        when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
//        when(consentSaveRequestMock.getConsent()).thenReturn(consentMock);
//        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);
//
//        consentsMonitor.onEventAsync(consentSaveRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//        assertThat(responseCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
//
//    }

    @Test
    public void ShouldNotSaveConsent_WhenRequestTypeIsSaveAndConsentDetailsIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock, never()).post(isA(ConsentBackendSaveResponse.class));
    }

    @Test
    public void ShouldPostError_WhenBackendSaveFails() throws Exception {
        Response response = new Response("", 401, "Error", new ArrayList<Header>(), null);
        final RetrofitError retrofitError = RetrofitError.httpError("url", response, null, null);

        doReturn(Collections.singletonList(uCoreConsentDetailMock)).when(consentsConverterMock).convertToUCoreConsentDetails(anyListOf(ConsentDetail.class));
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenThrow(retrofitError);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        final BackendResponse backendResponse = errorCaptor.getValue();
        assertThat(backendResponse.getReferenceId()).isEqualTo(REFERENCE_ID);
        assertThat(backendResponse.succeed()).isFalse();
        // assertThat(backendResponse.getCallException()).isEqualTo(retrofitError.getSuccessType().);
    }

    @Test
    public void ShouldPostError_WhenGetRequestIsFetchedWithOutUserLogin() throws Exception {
       // dataServicesManager.initialize(context, verticalDataCreater, null,null);
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

//    @Test
//    public void ShouldRetrieveConsentDetailsAndPostTheSuccessEvent_WhenUserIsLoggedIn() throws Exception {
////        when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//        when(uCoreConsentDetailMock.isEmpty()).thenReturn(false);
////        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
//        when(consentsConverterMock.convertToAppConsentDetails(uCoreConsentDetailMock, USER_ID)).thenReturn(consentMock);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//        assertThat(responseCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
//    }

    @Test
    public void ShouldSaveConsent_WhenListOfConsentIsPassed() throws Exception {
        doReturn(Collections.singletonList(consentMock)).when(consentSaveListRequestMock).getConsentList();
        doReturn(Collections.singletonList(uCoreConsentDetailMock)).when(consentsConverterMock).convertToUCoreConsentDetails(anyListOf(ConsentDetail.class));
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
        when(uCoreConsentDetailMock.isEmpty()).thenReturn(false);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(consentsConverterMock.convertToAppConsentDetails(uCoreConsentDetailMock, USER_ID)).thenReturn(consentMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        consentsMonitor.onEventAsync(consentSaveListRequestMock);

        verify(eventingMock, times(2)).post(eventArgumentCaptor.capture());

        List<Event> events = eventArgumentCaptor.getAllValues();

        assertThat(events).hasSize(2);
        assertThat(events.get(0)).isInstanceOf(BackendResponse.class);
        assertThat(events.get(1)).isInstanceOf(ConsentBackendListSaveResponse.class);
    }

//    @Test
//    public void ShouldPostExceptionEvent_WhenGetRequestIsFetchedWithNullConsent() throws Exception {
//        //when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//
//        assertThat(responseCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
//        assertThat(responseCaptor.getValue().getConsent()).isNull();
//    }
//
//   @Test
//    public void ShouldReportEmptyConsent_When200AndListIsEmpty() throws Exception {
//        //when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//        when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//        final ConsentBackendSaveResponse consentEvent = responseCaptor.getValue();
//        assertThat(consentEvent.getReferenceId()).isEqualTo(REFERENCE_ID);
//        assertThat(consentEvent.getConsent()).isNull();
//    }
}