package com.philips.platform.datasync.moments;

import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.spy.EventingSpy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsDataFetcherTest {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final String SUBJECT_ID = "SUBJECT_ID";

    private static final String TEST_MOMENT_SYNC_URL = "TEST_MOMENT_SYNC_URL";
    private static final String START_DATE = "timestampStart";
    private static final String END_DATE = "timestampEnd";
    private static final String LAST_MODIFIED_START_DATE = "lastModifiedStart";
    private static final String LAST_MODIFIED_END_DATE = "lastModifiedEnd";

    private static final String START_DATE_VALUE = new DateTime().toString();
    private static final String START_DATE2_VALUE = new DateTime().plusDays(1).toString();
    private static final String END_DATE_VALUE = new DateTime().toString();
    private static final String END_DATE2_VALUE = new DateTime().plusDays(1).toString();

    private MomentsDataFetcher fetcher;
    private UCoreMomentsHistory momentsHistory = new UCoreMomentsHistory();
    private UCoreMomentsHistory userMomentsHistory = new UCoreMomentsHistory();
    private List<UCoreMoment> uCoreMomentList = new ArrayList<>();
    private List<UCoreMoment> uCoreUserMomentList = new ArrayList<>();
    private Map<String, String> lastSyncTimeMap;
    private Map<String, String> lastSyncTimeMap2;
    private EventingSpy eventingSpy = new EventingSpy();
    private RetrofitError retrofitError;

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

    @Mock
    AppComponent appComponantMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    UserRegistrationInterface userRegistrationInterfaceMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockAccessProvider();
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(momentsClientMock);
        setTowUserMoments();
        setOneMoment();

        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        fetcher = new MomentsDataFetcher(coreAdapterMock, converterMock, gsonConverterMock);
        fetcher.eventing = eventingSpy;
        fetcher.accessProvider = accessProviderMock;
        fetcher.synchronisationManager = synchronisationManagerMock;
        fetcher.userRegistrationInterface = userRegistrationInterfaceMock;
    }

    private void setOneMoment() {
        momentsHistory.setUCoreMoments(uCoreMomentList);
        momentsHistory.setSyncurl(TEST_MOMENT_SYNC_URL);
    }

    private void setTowUserMoments() {
        uCoreUserMomentList.add(new UCoreMoment());
        uCoreUserMomentList.add(new UCoreMoment());
        userMomentsHistory.setUCoreMoments(uCoreUserMomentList);
        userMomentsHistory.setSyncurl(TEST_MOMENT_SYNC_URL);
    }

    private void mockAccessProvider() throws UnsupportedEncodingException {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(accessProviderMock.getSubjectId()).thenReturn(SUBJECT_ID);
        when(accessProviderMock.getLastSyncTimeStampByDateRange(START_DATE, END_DATE)).thenReturn(getLastSyncDateMap());
        when(accessProviderMock.getLastSyncTimeStampByDateRange(TEST_MOMENT_SYNC_URL)).thenReturn(getLastSyncDateFromSyncMap());
        when(accessProviderMock.getMomentLastSyncTimestamp()).thenReturn(TEST_MOMENT_SYNC_URL);
    }

    private Map<String, String> getLastSyncDateMap() throws UnsupportedEncodingException {
        this.lastSyncTimeMap = new HashMap<>();
        lastSyncTimeMap.put(START_DATE, URLEncoder.encode(START_DATE_VALUE, "UTF-8"));
        lastSyncTimeMap.put(END_DATE, URLEncoder.encode(END_DATE_VALUE, "UTF-8"));
        lastSyncTimeMap.put(LAST_MODIFIED_START_DATE, URLEncoder.encode(START_DATE_VALUE, "UTF-8"));
        lastSyncTimeMap.put(LAST_MODIFIED_END_DATE, URLEncoder.encode(END_DATE_VALUE, "UTF-8"));
        return lastSyncTimeMap;
    }

    private Map<String, String> getLastSyncDateFromSyncMap() throws UnsupportedEncodingException {
        this.lastSyncTimeMap2 = new HashMap<>();
        lastSyncTimeMap2.put(START_DATE, URLEncoder.encode(START_DATE2_VALUE, "UTF-8"));
        lastSyncTimeMap2.put(END_DATE, URLEncoder.encode(END_DATE2_VALUE, "UTF-8"));
        lastSyncTimeMap2.put(LAST_MODIFIED_START_DATE, URLEncoder.encode(START_DATE2_VALUE, "UTF-8"));
        lastSyncTimeMap2.put(LAST_MODIFIED_END_DATE, URLEncoder.encode(END_DATE2_VALUE, "UTF-8"));
        return lastSyncTimeMap2;
    }

    @Test
    public void fetchData_WithInvalidUser() {
        givenUserNotLoggedin();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsNull();
        thenVerifyZeroInteractionsWith(coreAdapterMock);
    }

    @Test
    public void fetchData_WithNoClient() {
        givenNoClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsNull();
        thenVerifyZeroInteractionsWith(momentsClientMock);

    }

    @Test
    public void fetchData_whenuCoreMomentsIsEmpty() {
        givenZeroUcoreMomentsFromClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsNull();
        thenNoEventIsPosted();
    }

    @Test
    public void fetchData_whenuCoreMoments() {
        givenUcoreMomentsFromClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsNull();
        thenVerifyEventIsPosted("BackendMomentListSaveRequest");
    }

    @Test
    public void fetchData_whenRetrofitError() {
        givenRetrofitErrorFromClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsReturned("error");
    }

    @Test
    public void fetchDataByDateRange_WithNoClient() {
        givenNoClient();
        whenFetchDataByDateRange();
    }

    @Test
    public void postPartialSynError_WhenFetchByDateRange() {
        givenPartialSuccessFromClient();
        whenFetchDataByDateRange();
        thenRetrofitErrorIsReturned("Partial Sync Completed till:");
        //verify against spy class is not working from jenkins, need to debug this
        //thenVerifyEventIsPosted("BackendMomentListSaveRequest");
    }

    @Test
    public void postSyncError_WhenFetchByDateRange() {
        givenRetrofitErrorFromClientWhenFetchDateByRange();
        whenFetchDataByDateRange();
        thenRetrofitErrorIsReturned("Error");
        thenNoEventIsPosted();
    }

    private void givenRetrofitErrorFromClientWhenFetchDateByRange() {
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE),
                lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE))).thenThrow(RetrofitError.unexpectedError("", new RuntimeException("Error")));
    }

    @Test
    public void fetchDataByDateRange_givenSyncUrl() {
        givenMomentsFromClient();
        whenFetchDataByDateRange();
        thenVerifyClientIsInvlokedTwice();
    }

    private void givenRetrofitErrorFromClient() {
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenThrow(RetrofitError.unexpectedError("error", new RuntimeException("error")));
    }

    private void givenUcoreMomentsFromClient() {
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, TEST_MOMENT_SYNC_URL)).thenReturn(userMomentsHistory);
    }

    private void givenZeroUcoreMomentsFromClient() {
        when(momentsClientMock.getMomentsHistory(USER_ID, USER_ID, TEST_MOMENT_SYNC_URL)).thenReturn(momentsHistory);

    }

    private void givenUserNotLoggedin() {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
    }

    private void givenMomentsFromClient() {
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE), lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE))).thenReturn(userMomentsHistory);
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap2.get(START_DATE), lastSyncTimeMap2.get(END_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_END_DATE))).thenReturn(momentsHistory);
    }

    private void givenPartialSuccessFromClient() {
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE), lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE))).thenReturn(userMomentsHistory);
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap2.get(START_DATE), lastSyncTimeMap2.get(END_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_END_DATE))).thenThrow(RetrofitError.unexpectedError("", new RuntimeException("error")));
    }

    private void givenNoClient() {
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(null);

    }

    private void whenFetchDataByDateRange() {
        retrofitError = fetcher.fetchDataByDateRange(START_DATE, END_DATE);
    }

    private void whenFetchDataIsInvoked() {
        retrofitError = fetcher.fetchData();
    }

    private void thenVerifyEventIsPosted(String event) {
        assertEquals(event, eventingSpy.postedEvent.getClass().getSimpleName());
    }

    private void thenVerifyClientIsInvlokedTwice() {
        verify(momentsClientMock).fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE), lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE));
        verify(momentsClientMock).fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap2.get(START_DATE), lastSyncTimeMap2.get(END_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_END_DATE));
    }

    private void thenNoEventIsPosted() {
        assertNull(eventingSpy.postedEvent);
    }

    private void thenVerifyZeroInteractionsWith(Object coreAdapterMock) {
        verifyZeroInteractions(coreAdapterMock);
    }

    private void thenRetrofitErrorIsNull() {
        assertNull(retrofitError);
    }

    private void thenRetrofitErrorIsReturned(String message) {
        assertNotNull(retrofitError);
        assertTrue(retrofitError.getCause().getMessage().contains(message));
    }

}