package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.GetNonSynchronizedConsentssResponse;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 07/12/16.
 */
public class ConsentsDataFetcherTest {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final int REFERENCE_ID = 0;

    private ConsentsMonitor consentsMonitor;

    private Void response = null;

    @Mock
    private UCoreAccessProvider uCoreAccessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private ConsentDataSender consentDataSenderMock;


    private ConsentsDataFetcher consentDataFetcher;

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

    @Mock
    private GetNonSynchronizedConsentssResponse getNonSynchronizedConsentssResponseMock;

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
        consentDataFetcher=new ConsentsDataFetcher(uCoreAdapterMock,gsonConverterMock,consentsConverterMock);
        consentDataFetcher.uCoreAccessProvider = uCoreAccessProviderMock;
        consentDataFetcher.eventing=eventingMock;

    }

   /* @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock, never()).post(ConsentBackendGetRequestEventCaptor.capture());
    }*/

   /* @Test
    public void shouldFetchDataSince_WhenDataSenderIsNotBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock).post(ConsentBackendGetRequestEventCaptor.capture());
    }*/

    @Test
    public void shouldReturnConsentDetails_WhenGetConsentDetailsIsCalled() throws Exception {
        consentDataFetcher.getConsentDetails();

    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderToSetConsentDetail() throws Exception {
        consentDataFetcher.setConsentDetails(Collections.singletonList(consentDetailMock));
    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderTofetchAllData() throws Exception {
        consentDataFetcher.fetchAllData();
    }

       /* @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmptyDuringGet() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }
*/
  /*  @Test
    public void ShouldNotSaveConsent_WhenRequestTypeIsNotSave() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentDetailMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);

        consentsMonitor.onEventAsync(consentSaveRequestMock);
        assertThat(consentSaveRequestMock.getRequestType()).isSameAs(ConsentBackendSaveRequest.RequestType.SAVE);
    }*/

   /* @Test
    public void ShouldNotSaveConsent_WhenRequestTypeIsSaveAndConsentDetailsIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentDetailMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock, never()).post(isA(ConsentBackendSaveResponse.class));
    }*/

   /* @Test
    public void ShouldPostError_WhenBackendSaveFails() throws Exception {
        Response response = new Response("", 401, "Error", new ArrayList<Header>(), null);
        final RetrofitError retrofitError = RetrofitError.httpError("url", response, null, null);

        doReturn(Collections.singletonList(uCoreConsentDetailMock)).when(consentsConverterMock).convertToUCoreConsentDetails(anyListOf(ConsentDetail.class));
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(consentSaveRequestMock.getRequestType()).thenReturn(ConsentBackendSaveRequest.RequestType.SAVE);
        when(consentSaveRequestMock.getConsent()).thenReturn(consentDetailMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenThrow(retrofitError);

        consentsMonitor.onEventAsync(consentSaveRequestMock);

        verify(eventingMock).post(errorCaptor.capture());
        final BackendResponse backendResponse = errorCaptor.getValue();
        assertThat(backendResponse.getReferenceId()).isEqualTo(REFERENCE_ID);
        assertThat(backendResponse.succeed()).isFalse();
//        assertThat(backendResponse.getCallException()).isEqualTo(retrofitError.getSuccessType());
    }
*/
 /*   @Test
    public void ShouldPostError_WhenGetRequestIsFetchedWithOutUserLogin() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }*/

//    @Test
//    public void ShouldRetrieveConsentDetailsAndPostTheSuccessEvent_WhenUserIsLoggedIn() throws Exception {
//        when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(accessProviderMock.getHSDPAccessToken()).thenReturn(ACCESS_TOKEN);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//        when(uCoreConsentDetailMock.isEmpty()).thenReturn(false);
//        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
//        when(consentsConverterMock.convertToAppConsentDetails(uCoreConsentDetailMock, USER_ID)).thenReturn(consentDetailMock);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//        assertThat(responseCaptor.getValue().getReferenceId()).isNotEqualTo(REFERENCE_ID);
//    }

  /*  @Test
    public void ShouldSaveConsent_WhenListOfConsentIsPassed() throws Exception {
        doReturn(Collections.singletonList(consentDetailMock)).when(consentSaveListRequestMock).getConsentList();
        doReturn(Collections.singletonList(uCoreConsentDetailMock)).when(consentsConverterMock).convertToUCoreConsentDetails(anyListOf(ConsentDetail.class));
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
        when(uCoreConsentDetailMock.isEmpty()).thenReturn(false);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(consentsConverterMock.convertToAppConsentDetails(uCoreConsentDetailMock, USER_ID)).thenReturn(consentDetailMock);
        when(consentsClientMock.saveConsent(anyString(), anyListOf(UCoreConsentDetail.class))).thenReturn(response);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        consentsMonitor.onEventAsync(consentSaveListRequestMock);

        verify(eventingMock, times(2)).post(eventArgumentCaptor.capture());

        List<Event> events = eventArgumentCaptor.getAllValues();

        assertThat(events).hasSize(2);
        assertThat(events.get(0)).isInstanceOf(BackendResponse.class);
        assertThat(events.get(1)).isInstanceOf(ConsentBackendListSaveResponse.class);
    }*/

//    @Test
//    public void ShouldPostExceptionEvent_WhenGetRequestIsFetchedWithNullConsent() throws Exception {
//        when(accessProviderMock.isLoggedIn()).thenReturn(true);
//        when(accessProviderMock.getHSDPAccessToken()).thenReturn(ACCESS_TOKEN);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//
//        assertThat(responseCaptor.getValue().getReferenceId()).isNotEqualTo(REFERENCE_ID);
//        assertThat(responseCaptor.getValue().getConsent()).isNull();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldReportEmptyConsent_When200AndListIsEmptyAndNotEqualToReferenceId() throws Exception {
//        when(consentsMonitor.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
//        when(consentsMonitor.uCoreAccessProvider.getHSDPAccessToken()).thenReturn(ACCESS_TOKEN);
//        when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//
//        verify(eventingMock).post(responseCaptor.capture());
//        final ConsentBackendSaveResponse consentEvent = responseCaptor.getValue();
//        assertThat(consentEvent.getReferenceId()).isNotEqualTo(REFERENCE_ID);
//        assertThat(consentEvent.getConsent()).isNull();
//    }

   /* @Test
    public void ShouldReturn_WhenUCoreAccessProviderIsNull() throws Exception {
        consentsMonitor.uCoreAccessProvider = null;
        when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);

        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
        verifyNoMoreInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldReturn_WhenConsentDetailsIsNull() throws Exception {
        when(consentsMonitor.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(consentsMonitor.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);

        when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
        when(consentBackendGetRequestMock.getConsentDetails()).thenReturn(null);
        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
        verifyNoMoreInteractions(uCoreAdapterMock);
    }*/

//    @Test
//    public void ShouldReturnEvent_WhenConcentDetailsIsNotNullAndUCoreConsentDetailIsEmpity() throws Exception {
//        when(consentsMonitor.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
//        when(consentsMonitor.uCoreAccessProvider.getHSDPAccessToken()).thenReturn(ACCESS_TOKEN);
//        // when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
//        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
//
//        List<ConsentDetail> consentDetailList = new ArrayList<ConsentDetail>();
//        consentDetailList.add(0, mock(ConsentDetail.class));
//        when(consentBackendGetRequestMock.getConsentDetails()).thenReturn(consentDetailList);
//
//        consentsMonitor.onEventAsync(consentBackendGetRequestMock);
//        verifyNoMoreInteractions(uCoreConsentDetailMock);
//    }
//
    @Test
    public void ShouldReturnEvent_WhenConcentDetailsIsNotNullAndUCoreConsentDetailISNotEmpity() throws Exception {
        when(consentDataFetcher.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(consentDataFetcher.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(uCoreAdapterMock.getAppFrameworkClient(ConsentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(consentsClientMock);
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
        when(uCoreConsentDetailMock.get(0)).thenReturn(new UCoreConsentDetail("dfs", "dfs", "dsfs", "dfs"));
        List<ConsentDetail> consentDetails=new ArrayList<>();
        consentDetails.add(mock((ConsentDetail.class)));
        getNonSynchronizedConsentssResponseMock =new GetNonSynchronizedConsentssResponse(consentDetails);

       // when(getNonSynchronizedConsentssResponseMock.getConsentDetails()).thenReturn(consentDetails);

        consentDataFetcher.onEventAsync(getNonSynchronizedConsentssResponseMock);

        verify(eventingMock).post(isA(ConsentBackendSaveResponse.class));
    }

    @Test
    public void ShouldReturn_WhenConsentDetailsIsNull() throws Exception {
        when(consentDataFetcher.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(consentDataFetcher.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);

        when(uCoreConsentDetailMock.isEmpty()).thenReturn(true);
        when(consentsClientMock.getConsent(anyString(), anyListOf(String.class), anyListOf(String.class), anyListOf(String.class))).thenReturn(uCoreConsentDetailMock);
        List<ConsentDetail> consentDetails=new ArrayList<>();
        //consentDetails.add(mock((ConsentDetail.class)));
        getNonSynchronizedConsentssResponseMock =new GetNonSynchronizedConsentssResponse(consentDetails);
        //when(getNonSynchronizedConsentssResponseMock.getConsentDetails()).thenReturn(null);
        consentDataFetcher.onEventAsync(getNonSynchronizedConsentssResponseMock);
        verifyNoMoreInteractions(uCoreAdapterMock);
    }


    @Test
    public void shouldPostError_WhenUserIsInvalid() throws Exception {

        consentDataFetcher.uCoreAccessProvider=null;
        consentDataFetcher.isUserInvalid();
        consentDataFetcher.postError(1,consentDataFetcher.getNonLoggedInError());
        verify(eventingMock).post(isA(BackendResponse.class));
    }
}