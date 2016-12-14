package com.philips.platform.datasync.moments;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.testing.verticals.DataServiceManagerMock;

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
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 07/12/16.
 */
public class MomentsDataFetcherTest {
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String USER_ID = "TEST_GUID";
    public static final String SUBJECT_ID = "SUBJECT_ID";
    public static final String DATE_TIME = "TEST_DATE_TIME";
    private static final String TEST_MOMENT_SYNC_URL = "TEST_MOMENT_SYNC_URL";

    private MomentsDataFetcher fetcher;

    private UCoreMomentsHistory momentsHistory = new UCoreMomentsHistory();
    private UCoreMomentsHistory userMomentsHistory = new UCoreMomentsHistory();
    private List<UCoreMoment> uCoreMomentList = new ArrayList<>();
    private List<Moment> momentList = new ArrayList<>();

    @Mock
    private Eventing eventing;

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
    DataServiceManagerMock dataServiceManagerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(dataServiceManagerMock.getUCoreAccessProvider()).thenReturn(accessProviderMock);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(accessProviderMock.getSubjectId()).thenReturn(SUBJECT_ID);
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);
        momentsHistory.setUCoreMoments(uCoreMomentList);
        momentsHistory.setSyncurl(TEST_MOMENT_SYNC_URL);

        fetcher = new MomentsDataFetcher(coreAdapterMock, converterMock, eventing, gsonConverterMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotFetchData_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotFetchData_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotFetchData_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        RetrofitError retrofitError = fetcher.fetchDataSince(DateTime.now());

        assertThat(retrofitError).isNull();
        verifyZeroInteractions(coreAdapterMock);
    }

    @Test(expected = NullPointerException.class)
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

        verify(eventing).post(saveRequestCaptor.capture());
        BackendMomentListSaveRequest request = (BackendMomentListSaveRequest) saveRequestCaptor.getAllValues().get(0);
        assertThat(request.getList()).isSameAs(momentList);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotPostEvent_WhenMomentReturnedIsLessThanZero() {
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn(DATE_TIME);
        when(momentsClientMock.getMomentsHistory(USER_ID, SUBJECT_ID, DATE_TIME)).thenReturn(momentsHistory);
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, DATE_TIME)).thenReturn(momentsHistory);

        when(converterMock.convert(uCoreMomentList)).thenReturn(momentList);

        RetrofitError result = fetcher.fetchDataSince(null);

        assertThat(result).isNull();
        verify(accessProviderMock).saveLastSyncTimeStamp(TEST_MOMENT_SYNC_URL, UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY);

        verifyZeroInteractions(eventing);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotPostEvent_WhenFetchBabyIdIsEmpty() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getSubjectId()).thenReturn("");
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(null);

        RetrofitError retrofitError = fetcher.fetchDataSince(null);

        verify(momentsClientMock, never()).getMomentsHistory(anyString(), anyString(), eq(new DateTime().toString()));
        verify(converterMock, never()).convert(uCoreMomentList);
        verify(eventing, never()).post(isA(BackendMomentListSaveRequest.class));
    }

    @Test(expected = NullPointerException.class)
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

        verify(converterMock, times(2)).convert(uCoreMomentList);
        verify(eventing).post(isA(BackendMomentListSaveRequest.class));
    }
}