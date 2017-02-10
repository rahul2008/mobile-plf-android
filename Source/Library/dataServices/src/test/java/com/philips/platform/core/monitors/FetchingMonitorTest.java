package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentsSegregator;
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

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        fetchingMonitor = new FetchingMonitor(fetching);
        fetchingMonitor.momentsSegregator = momentsSegregatorMock;
        fetchingMonitor.consentsSegregator = consentsSegregatorMock;
        fetchingMonitor.settingsSegregator = settingsSegregatorMock;
        uGrowDateTime = new DateTime();
        fetchingMonitor.start(eventingMock);
    }

    @Test
    public void ShouldFetchMomentsInsightsAndBabyProfile_WhenLoadTimelineEntryRequestIsReceived() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadTimelineEntryRequest(dbRequestListener));

        verify(fetching).fetchMoments(dbRequestListener);
//        verify(fetching).fetchConsentDetails();
        //      verify(fetching).fetchConsentDetails();
        //    verify(fetching).fetchNonSynchronizedMoments();
    }

    @Test
    public void ShouldThrowException_FetchingMoments() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadTimelineEntryRequest(dbRequestListener));
        verify(fetching).fetchMoments(dbRequestListener);
    }

    @Test
    public void fetchingMomentsLoadLastMomentRequest() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadLastMomentRequest("temperature", dbRequestListener));
        verify(fetching).fetchLastMoment("temperature", dbRequestListener);
    }

    @Test
    public void ShouldFetchMoments_WhenLoadMomentsRequestIsReceived() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadMomentsRequest(dbRequestListener));

        verify(fetching).fetchMoments(dbRequestListener);
    }

    @Test
    public void ShouldFetchConsents_WhenLoadConsentsRequest() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadConsentsRequest(dbRequestListener));

        verify(fetching).fetchConsentDetails(dbRequestListener);
    }

    @Test
    public void ShouldFetchSettings_WhenLoadSettingsRequestIsCalled() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadSettingsRequest(dbRequestListener));

        verify(fetching).fetchSettings(dbRequestListener);
    }

    @Test
    public void ShouldFetchCharacteristics_WhenLoadCharacterSicsRequest() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadUserCharacteristicsRequest(dbRequestListener));

        verify(fetching).fetchCharacteristics(dbRequestListener);
    }

    @Test
    public void getNonSynchronizedDataRequestTest() throws SQLException {
        fetchingMonitor.onEventBackgroundThread(new GetNonSynchronizedDataRequest(1));
        Map<Class, List<?>> dataToSync = new HashMap<>();
        verify(momentsSegregatorMock).putMomentsForSync(dataToSync);
        verify(consentsSegregatorMock).putConsentForSync(dataToSync);
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }

    @Test
    public void getNonSynchronizedMomentRequestTest() throws SQLException {
        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedMomentsRequestMock);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        verify(fetching).fetchConsentDetails();
        verify(fetching).fetchNonSynchronizedMoments();
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }

}