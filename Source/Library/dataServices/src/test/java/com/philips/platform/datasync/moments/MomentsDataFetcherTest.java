package com.philips.platform.datasync.moments;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsDataFetcherTest {
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String USER_ID = "TEST_GUID";
    public static final String SUBJECT_ID = "SUBJECT_ID";
    public static final String DATE_TIME = "TEST_DATE_TIME";
    private static final String TEST_MOMENT_SYNC_URL = "TEST_MOMENT_SYNC_URL";


    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";

    private String TEST_USER_ID = "TEST_USER_ID";

    private MomentsDataFetcher fetcher;

    private UCoreMomentsHistory momentsHistory = new UCoreMomentsHistory();
    private UCoreMomentsHistory userMomentsHistory = new UCoreMomentsHistory();
    private List<UCoreMoment> uCoreMomentList = new ArrayList<>();
    private List<Moment> momentList = new ArrayList<>();

    @Mock
    private Eventing eventingMock;

    @Mock
    TypedByteArray typedByteArrayMock;


    @Mock
    private RetrofitError retrofitErrorMock;

    @Mock
    private MomentsConverter converterMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter coreAdapterMock;

    @Mock
    private MomentsClient momentsClientMock;

    @Mock
    private GsonConverter gsonConverterMock;

    @Captor
    private ArgumentCaptor<ListEvent<? extends BaseAppData>> saveRequestCaptor;

    @Mock
    UCoreMomentsHistory momentsHistoryMock;

    @Mock
    Moment momentMock;

    @Mock
    AppComponent appComponantMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    UserRegistrationInterface userRegistrationInterfaceMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(accessProviderMock.getSubjectId()).thenReturn(SUBJECT_ID);
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);
        momentsHistory.setUCoreMoments(uCoreMomentList);
        momentsHistory.setSyncurl(TEST_MOMENT_SYNC_URL);

        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        fetcher = new MomentsDataFetcher(coreAdapterMock, converterMock, gsonConverterMock);
        fetcher.eventing = eventingMock;
        fetcher.accessProvider = accessProviderMock;
        fetcher.synchronisationManager = synchronisationManagerMock;
        fetcher.userRegistrationInterface = userRegistrationInterfaceMock;
    }

    @Test
    public void ShouldNotFetchData_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test
    public void ShouldNotFetchData_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test
    public void ShouldNotFetchData_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test
    public void ShouldPostSaveMoments_WhenConversionReturnsMoments() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn(DATE_TIME);
        momentsHistory.setUCoreMoments(uCoreMomentList);
        final UCoreMoment uCoreMomentMock = mock(UCoreMoment.class);
        uCoreMomentList.add(uCoreMomentMock);
        when(momentsClientMock.getMomentsHistory(USER_ID, SUBJECT_ID, DATE_TIME)).thenReturn(momentsHistory);
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, DATE_TIME)).thenReturn(userMomentsHistory);

        momentList.add(momentMock);
        when(converterMock.convert(uCoreMomentList)).thenReturn(momentList);

        //noinspection ThrowableResultOfMethodCallIgnored
        RetrofitError retrofitError = fetcher.fetchDataSince(null);

        verify(eventingMock).post(saveRequestCaptor.capture());
//        BackendMomentListSaveRequest request = (BackendMomentListSaveRequest) saveRequestCaptor.getAllValues().get(0);
//        assertThat(request.getList()).isSameAs(momentList);
    }

    @Test
    public void ShouldNotPostEvent_WhenMomentReturnedIsLessThanZero() {
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn(DATE_TIME);
        when(momentsClientMock.getMomentsHistory(USER_ID, SUBJECT_ID, DATE_TIME)).thenReturn(momentsHistory);
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, DATE_TIME)).thenReturn(momentsHistory);

        when(converterMock.convert(uCoreMomentList)).thenReturn(momentList);

        RetrofitError result = fetcher.fetchDataSince(null);

        assertThat(result).isNull();
        verify(accessProviderMock).saveLastSyncTimeStamp(TEST_MOMENT_SYNC_URL, UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY);

        verify(eventingMock).post(isA(BackendMomentListSaveRequest.class));
    }

    @Test
    public void ShouldNotPostEvent_WhenFetchBabyIdIsEmpty() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getSubjectId()).thenReturn("");
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(null);

        RetrofitError retrofitError = fetcher.fetchDataSince(null);

        verify(momentsClientMock, never()).getMomentsHistory(anyString(), anyString(), eq(new DateTime().toString()));
        verify(converterMock, never()).convert(uCoreMomentList);
        verify(eventingMock, never()).post(isA(BackendMomentListSaveRequest.class));
    }

    @Test
    public void ShouldPostEvent_WhenFetchBabyIdIsNotEmpty_And_ClientIsNotNull() {
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn(DATE_TIME);

        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        when(accessProviderMock.getSubjectId()).thenReturn(SUBJECT_ID);

        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);

        when(momentsClientMock.getMomentsHistory(USER_ID, SUBJECT_ID, DATE_TIME)).thenReturn(momentsHistoryMock);
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, DATE_TIME)).thenReturn(momentsHistoryMock);

        when(momentsHistoryMock.getUCoreMoments()).thenReturn(uCoreMomentList);
        final UCoreMoment uCoreMomentMock = mock(UCoreMoment.class);
        uCoreMomentList.add(uCoreMomentMock);

        Mockito.when(converterMock.convert(momentsHistoryMock.getUCoreMoments())).thenReturn(momentList);
        momentList.add(momentMock);
        RetrofitError retrofitError = fetcher.fetchDataSince(null);

        verify(converterMock, times(1)).convert(uCoreMomentList);
        verify(eventingMock).post(isA(BackendMomentListSaveRequest.class));
    }

    @Test
    public void ShouldThrowRetrofitError_WhenFetchDataFails() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
      //  final UserCharacteristicsClient uCoreClientMock = mock(Mo.class);
        /*when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class,
                TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);*/
        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        when(retrofitErrorMock.getMessage()).thenReturn("ERROR");
        //when(momentsClientMock.getMomentsHistory(TEST_USER_ID, TEST_USER_ID, "")).thenThrow(retrofitErrorMock);
        when(momentsClientMock.getMomentsHistory(accessProviderMock.getUserId(),
                accessProviderMock.getUserId(), "")).thenThrow(retrofitErrorMock);
        RetrofitError retrofitError = fetcher.fetchDataSince(null);

        assertThat(retrofitError).isNull();
    }

    @Test
    public void ShouldPostError_On_Error() throws Exception {

        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn("url");
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class,
                TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);

        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 403, string, headers, typedByteArrayMock);
        final RetrofitError retrofitError = mock(RetrofitError.class);
        when(retrofitError.getResponse()).thenReturn(response);

        doThrow(RetrofitError.class).when(momentsClientMock).getMomentsHistory(TEST_USER_ID, TEST_USER_ID, "url");

        RetrofitError retrofitError1 = fetcher.fetchDataSince(null);

        verify(momentsClientMock).getMomentsHistory(TEST_USER_ID, TEST_USER_ID, "url");
    }


    @Test
    public void ShouldPostError_On_Error_UNAUTHORIZED() throws Exception {

        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn("url");
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class,
                TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);

        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 401, string, headers, typedByteArrayMock);
        final RetrofitError retrofitError = mock(RetrofitError.class);
        when(retrofitError.getResponse()).thenReturn(response);

        doThrow(retrofitError).when(momentsClientMock).getMomentsHistory(TEST_USER_ID, TEST_USER_ID, "url");

        RetrofitError retrofitError1 = fetcher.fetchDataSince(null);

        verify(momentsClientMock).getMomentsHistory(TEST_USER_ID, TEST_USER_ID, "url");
    }

}