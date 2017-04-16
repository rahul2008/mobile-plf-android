package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.insights.InsightSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.platform.datasync.settings.SettingsSegregator;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FetchingMonitorTest {

    private static final String TEST_SERVER_GUID = "TEST_SERVER_GUID";
    private final String TEST_CREATOR_ID = "TEST_CREATOR_ID";

    private FetchingMonitor fetchingMonitor;

    @Mock
    private DBFetchingInterface fetching;

    @Mock
    private Eventing eventingMock;

    @Mock
    private Moment momentMock;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Mock
    private GetNonSynchronizedDataRequest getNonSynchronizedDataRequestMock;

    @Mock
    private GetNonSynchronizedMomentsRequest getNonSynchronizedMomentsRequestMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponseMock;


    @Captor
    private ArgumentCaptor<GetNonSynchronizedDataResponse> getNonSynchronizedDataResponseCaptor;

    @Captor
    private ArgumentCaptor<GetNonSynchronizedMomentsRequest> getNonSynchronizedMomentsResponseCaptor;


    @Mock
    private Moment ormMomentMock;

    @Mock
    MomentsSegregator momentsSegregatorMock;

    @Mock
    ConsentsSegregator consentsSegregatorMock;

    @Mock
    SettingsSegregator settingsSegregatorMock;

    @Mock
    private SynchronisationData synchronizationDataMock;
    @Mock
    private ConsentDetail consentDetailDetailsMock;

    private DateTime uGrowDateTime;

    @Mock
    private AppComponent appComponantMock;


    @Mock
    private DBRequestListener dbRequestListener;

    @Mock
    private DBFetchRequestListner dbFetchRequestListner;

    @Mock
    InsightSegregator insightSegregatorMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        fetchingMonitor = new FetchingMonitor(fetching);
        fetchingMonitor.momentsSegregator = momentsSegregatorMock;
        fetchingMonitor.consentsSegregator = consentsSegregatorMock;
        fetchingMonitor.settingsSegregator = settingsSegregatorMock;
        fetchingMonitor.insightSegregator=insightSegregatorMock;
        uGrowDateTime = new DateTime();
        fetchingMonitor.start(eventingMock);
    }

/*    @Test
    public void ShouldFetchMomentsInsightsAndBabyProfile_WhenLoadTimelineEntryRequestIsReceived() throws Exception {
        fetchingMonitor.onEventAsync(new LoadTimelineEntryRequest(dbFetchRequestListner));

        verify(fetching).fetchMoments(dbFetchRequestListner);
//        verify(fetching).fetchConsentDetails();
        //      verify(fetching).fetchConsentDetails();
        //    verify(fetching).fetchNonSynchronizedMoments();
    }*/

   /* @Test
    public void ShouldThrowException_FetchingMoments() throws Exception {
        fetchingMonitor.onEventAsync(new LoadTimelineEntryRequest(dbFetchRequestListner));
        verify(fetching).fetchMoments(dbFetchRequestListner);
    }*/

    @Test
    public void fetchingMomentsLoadLastMomentRequest() throws Exception {
        fetchingMonitor.onEventAsync(new LoadLastMomentRequest("temperature", dbFetchRequestListner));
        verify(fetching).fetchLastMoment("temperature", dbFetchRequestListner);
    }

    @Test
    public void ShouldFetchMoments_WhenLoadMomentsRequestIsReceived() throws Exception {

        fetchingMonitor.onEventAsync(new LoadMomentsRequest(dbFetchRequestListner));

        verify(fetching).fetchMoments(dbFetchRequestListner);
    }

    @Test
    public void ShouldFetchConsents_WhenLoadConsentsRequest() throws Exception {

        fetchingMonitor.onEventAsync(new LoadConsentsRequest(dbFetchRequestListner));

        verify(fetching).fetchConsentDetails(dbFetchRequestListner);
    }

    @Test
    public void ShouldFetchSettings_WhenLoadSettingsRequestIsCalled() throws Exception {

        fetchingMonitor.onEventAsync(new LoadSettingsRequest(dbFetchRequestListner));

        verify(fetching).fetchSettings(dbFetchRequestListner);
    }

    @Test
    public void ShouldFetchCharacteristics_WhenLoadCharacterSicsRequest() throws Exception {

        fetchingMonitor.onEventAsync(new LoadUserCharacteristicsRequest(dbFetchRequestListner));

        verify(fetching).fetchCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void getNonSynchronizedDataRequestTest() throws SQLException {
        fetchingMonitor.onEventAsync(new GetNonSynchronizedDataRequest(1));
        Map<Class, List<?>> dataToSync = new HashMap<>();
        verify(momentsSegregatorMock).putMomentsForSync(dataToSync);
        verify(consentsSegregatorMock).putConsentForSync(dataToSync);
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }

    @Test
    public void getNonSynchronizedMomentRequestTest() throws SQLException {
        fetchingMonitor.onEventAsync(getNonSynchronizedMomentsRequestMock);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        verify(fetching).fetchConsentDetails();
        verify(fetching).fetchNonSynchronizedMoments();
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }

/*    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchMoments() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchMoments(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadTimelineEntryRequest(dbFetchRequestListner));
        verify(fetching).fetchMoments(dbFetchRequestListner);
    }*/

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchLastMoment() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchLastMoment("Temperature",dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadLastMomentRequest("Temperature",dbFetchRequestListner));
        verify(fetching).fetchLastMoment("Temperature",dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_putUserCharacteristicsForSync() throws Exception {
        Map<Class, List<?>> dataToSync = new HashMap<>();
        doThrow(SQLException.class).when(fetching).putUserCharacteristicsForSync(dataToSync);
        fetchingMonitor.onEventAsync(new GetNonSynchronizedDataRequest(1));
       // final Map<Class, List<?>> classListMap = verify(fetching).putUserCharacteristicsForSync(null);
    }

    @Test
    public void LoadMomentsRequestTest() throws SQLException {
        fetchingMonitor.onEventAsync(new LoadMomentsRequest(dbFetchRequestListner));
        verify(fetching).fetchMoments(dbFetchRequestListner);
    }

    @Test
    public void LoadMomentsRequestTest_hasType() throws SQLException {
        fetchingMonitor.onEventAsync(new LoadMomentsRequest(dbFetchRequestListner,"Temperature"));
        verify(fetching).fetchMoments(dbFetchRequestListner,"Temperature");
    }

    @Test
    public void LoadMomentsRequestTest_hasID() throws SQLException {
        fetchingMonitor.onEventAsync(new LoadMomentsRequest(1,dbFetchRequestListner));
        verify(fetching).fetchMomentById(1,dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchMomentsBYID() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchMoments(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadMomentsRequest(dbFetchRequestListner));
        verify(fetching).fetchMoments(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchConsentDetails_1() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchConsentDetails(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadConsentsRequest(dbFetchRequestListner));
        verify(fetching).fetchConsentDetails(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchNonSynchronizedMoments() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchNonSynchronizedMoments();
        fetchingMonitor.onEventAsync(new GetNonSynchronizedMomentsRequest(dbRequestListener));
        verify(fetching).fetchNonSynchronizedMoments();
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchConsentDetails() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchConsentDetails();
        fetchingMonitor.onEventAsync(new GetNonSynchronizedMomentsRequest(dbRequestListener));
        verify(fetching).fetchConsentDetails();
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchCharacteristics() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchCharacteristics(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadUserCharacteristicsRequest(dbFetchRequestListner));
        verify(fetching).fetchCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchSettings() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchSettings(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadSettingsRequest(dbFetchRequestListner));
        verify(fetching).fetchSettings(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchConsentDetails_2() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchConsentDetails(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new LoadConsentsRequest(dbFetchRequestListner));
        verify(fetching).fetchConsentDetails(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_fetchActiveInsights() throws Exception {
        doThrow(SQLException.class).when(fetching).fetchActiveInsights(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new FetchInsightsFromDB(dbFetchRequestListner));
        verify(fetching).fetchActiveInsights(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostExceptionEvent_When_fetchActiveInsights_success() throws Exception {
       // doThrow(SQLException.class).when(fetching).fetchActiveInsights(dbFetchRequestListner);
        fetchingMonitor.onEventAsync(new FetchInsightsFromDB(dbFetchRequestListner));
        verify(fetching).fetchActiveInsights(dbFetchRequestListner);
    }
}