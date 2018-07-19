package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

    private AppInfra mAppInfra;
    private MomentsDataFetcher fetcher;
    private Map<String, String> lastSyncTimeMap;
    private Map<String, String> lastSyncTimeMap2;
    private EventingMock eventing = new EventingMock();
    private Exception returnValue;
    private AppConfigurationInterface mConfigInterface;

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
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        mAppInfra = mock(AppInfra.class);
        mConfigInterface = mock(AppConfigurationInterface.class);
        when(mAppInfra.getConfigInterface()).thenReturn(mConfigInterface);
    }

    @Test
    public void fetchData_WithInvalidUser() {
        givenAllMomentTypesAreSupported();
        givenUserNotLoggedin();
        whenFetchDataIsInvoked();
        thenNoErrorIsReturned();
        thenVerifyZeroInteractionsWith(coreAdapterMock);
    }

    @Test
    public void fetchData_WithNoClient() {
        givenAllMomentTypesAreSupported();
        givenNoClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsReturned();
    }

    @Test
    public void fetchData_whenuCoreMomentsIsEmpty() {
        givenAllMomentTypesAreSupported();
        givenBackendReturns();
        whenFetchDataIsInvoked();
        thenNoErrorIsReturned();
        thenNoMomentsAreStored();
    }

    @Test
    public void fetchData_storesReceivesMoments() {
        givenSupportedMomentTypes("MomentType1", "MomentType2");
        givenBackendReturns(momentOfType("MomentType1"), momentOfType("MomentType2"));
        whenFetchDataIsInvoked();
        thenNoErrorIsReturned();
        thenMomentsAreFetchedFromBackend("MomentType1", "MomentType2");
        thenMomentsAreStored("MomentType1", "MomentType2");
    }

    @Test
    public void fetchData_whenRetrofitError() {
        givenAllMomentTypesAreSupported();
        givenRetrofitErrorFromClient();
        whenFetchDataIsInvoked();
        thenRetrofitErrorIsReturned();
        thenBackendDataRequestFailedEventIsPosted();
    }

    @Test
    public void fetchDataByDateRange_dontStoreMomentsWithUnsupportedType() {
        givenSupportedMomentTypes("SupportedMomentType");
        givenBackendReturnsForDateRange(momentOfType("UnsupportedMomentType"), momentOfType("SupportedMomentType"));
        whenFetchDataByDateRange();
        thenMomentsAreStored("SupportedMomentType");
    }

    @Test
    public void fetchDataByDateRange_storesAllMoments_whenSupportedMomentTypeListIsEmpty() {
        givenAllMomentTypesAreSupported();
        givenBackendReturnsForDateRange(momentOfType("UnsupportedMomentType"), momentOfType("SupportedMomentType"));
        whenFetchDataByDateRange();
        thenMomentsAreStored("UnsupportedMomentType", "SupportedMomentType");
    }

    @Test
    public void fetchDataByDateRange_WithNoClient() {
        givenAllMomentTypesAreSupported();
        givenNoClient();
        whenFetchDataByDateRange();
    }

    @Test
    public void postSyncError_WhenFetchByDateRange() {
        givenAllMomentTypesAreSupported();
        givenRetrofitErrorFromClientWhenFetchDateByRange();
        whenFetchDataByDateRange();
        thenRetrofitErrorIsReturned();
        thenNoMomentsAreStored();
    }

    @Test
    public void fetchDataByDateRange_givenSyncUrl() {
        givenAllMomentTypesAreSupported();
        givenBackendReturnsForDateRange(momentOfType("UnsupportedMomentType"), momentOfType("SupportedMomentType"));
        whenFetchDataByDateRange();
        thenVerifyClientIsInvokedTwice();
    }

    private void givenBackendReturns(UCoreMoment... backendMoments) {
        UCoreMomentsHistory history = new UCoreMomentsHistory();
        history.setUCoreMoments(Arrays.asList(backendMoments));
        history.setSyncurl(TEST_MOMENT_SYNC_URL);
        when(momentsClientMock.getMomentsHistory(anyString(), anyString(), anyString(), ArgumentMatchers.<String>anyList())).thenReturn(history);
        givenConverterReturns(backendMoments);
    }

    private void givenBackendReturnsForDateRange(UCoreMoment... backendMoments) {
        UCoreMomentsHistory emptyHistory = new UCoreMomentsHistory();
        emptyHistory.setUCoreMoments(new ArrayList<UCoreMoment>());
        UCoreMomentsHistory history = new UCoreMomentsHistory();
        history.setUCoreMoments(Arrays.asList(backendMoments));
        history.setSyncurl(TEST_MOMENT_SYNC_URL);
        when(momentsClientMock.fetchMomentByDateRange(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(history).thenReturn(emptyHistory);
        givenConverterReturns(backendMoments);
    }

    private void givenConverterReturns(UCoreMoment[] backendMoments) {
        List<Moment> moments = new ArrayList<>();
        for (UCoreMoment backendMoment : backendMoments) {
            moments.add(new TestMoment(backendMoment.getType()));
        }
        when(converterMock.convert(ArgumentMatchers.<UCoreMoment>anyList())).thenReturn(moments);
    }

    private void givenAllMomentTypesAreSupported() {
        givenSupportedMomentTypes();
    }

    private void givenSupportedMomentTypes(String... momentTypes) {
        List<String> supportedMomentTypesList = new ArrayList(Arrays.asList(momentTypes));
        when(mConfigInterface.getPropertyForKey(anyString(), anyString(), ArgumentMatchers.<AppConfigurationInterface.AppConfigurationError>any())).thenReturn(supportedMomentTypesList);
        when(mAppInfra.getLogging()).thenReturn(mock(LoggingInterface.class));
        fetcher = new MomentsDataFetcher(coreAdapterMock, converterMock, gsonConverterMock, mAppInfra);
        fetcher.eventing = eventing;
        fetcher.accessProvider = accessProviderMock;
        fetcher.synchronisationManager = synchronisationManagerMock;
        fetcher.userRegistrationInterface = userRegistrationInterfaceMock;
    }

    private void givenRetrofitErrorFromClientWhenFetchDateByRange() {
        when(momentsClientMock.fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE),
                lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE))).thenThrow(RetrofitError.unexpectedError("", new RuntimeException("Error")));
    }

    private void givenRetrofitErrorFromClient() {
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenThrow(RetrofitError.unexpectedError("error", new RuntimeException("error")));
    }

    private void givenUserNotLoggedin() {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
    }

    private void givenNoClient() {
        when(coreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(null);

    }

    private void whenFetchDataByDateRange() {
        returnValue = fetcher.fetchDataByDateRange(START_DATE, END_DATE);
    }

    private void whenFetchDataIsInvoked() {
        returnValue = fetcher.fetchData();
    }

    private void thenMomentsAreStored(String... expectedMoments) {
        List<String> momentTypes = new ArrayList<>();
        for (Moment moment : eventing.backendSavedMoments) {
            momentTypes.add(moment.getType());
        }
        assertArrayEquals(expectedMoments, momentTypes.toArray());
    }

    private void thenBackendDataRequestFailedEventIsPosted() {
        assertTrue(eventing.backendDataRequestFailed);
    }

    private void thenNoMomentsAreStored() {
        assertTrue(eventing.backendSavedMoments.isEmpty());
    }

    private void thenVerifyClientIsInvokedTwice() {
        verify(momentsClientMock).fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap.get(START_DATE), lastSyncTimeMap.get(END_DATE), lastSyncTimeMap.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap.get(LAST_MODIFIED_END_DATE));
        verify(momentsClientMock).fetchMomentByDateRange(USER_ID, USER_ID, lastSyncTimeMap2.get(START_DATE), lastSyncTimeMap2.get(END_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_START_DATE), lastSyncTimeMap2.get(LAST_MODIFIED_END_DATE));
    }

    private void thenVerifyZeroInteractionsWith(Object coreAdapterMock) {
        verifyZeroInteractions(coreAdapterMock);
    }

    private void thenRetrofitErrorIsReturned() {
        assertTrue(returnValue instanceof RetrofitError);
    }

    private void thenNoErrorIsReturned() {
        assertNull(returnValue);
    }

    private void thenMomentsAreFetchedFromBackend(String... momentTypes) {
        verify(momentsClientMock).getMomentsHistory(USER_ID, USER_ID, TEST_MOMENT_SYNC_URL, Arrays.asList(momentTypes));
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

    private UCoreMoment momentOfType(String type) {
        UCoreMoment moment = new UCoreMoment();
        moment.setType(type);
        return moment;
    }

    class TestMoment implements Moment {

        private String type;

        public TestMoment(String type) {
            this.type = type;
        }

        @Override
        public String getCreatorId() {
            return null;
        }

        @Override
        public String getSubjectId() {
            return null;
        }

        @Override
        public DateTime getExpirationDate() {
            return null;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public void setDateTime(@NonNull DateTime dateTime) {

        }

        @Override
        public Collection<? extends MeasurementGroup> getMeasurementGroups() {
            return null;
        }

        @Override
        public void addMeasurementGroup(MeasurementGroup measurementGroup) {

        }

        @Override
        public Collection<? extends MomentDetail> getMomentDetails() {
            return null;
        }

        @Override
        public void addMomentDetail(MomentDetail momentDetail) {

        }

        @Nullable
        @Override
        public SynchronisationData getSynchronisationData() {
            return null;
        }

        @Override
        public void setSynchronisationData(SynchronisationData synchronisationData) {

        }

        @Override
        public void setSynced(boolean b) {

        }

        @Override
        public void setId(int id) {

        }

        @NonNull
        @Override
        public String getAnalyticsId() {
            return null;
        }

        @Override
        public void setExpirationDate(DateTime expirationDate) {

        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public DateTime getDateTime() {
            return null;
        }
    }


}