package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.LoadCharacterSicsRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;

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

    @Mock
    private Consent consentMock;

    @Captor
    private ArgumentCaptor<GetNonSynchronizedDataResponse> getNonSynchronizedDataResponseCaptor;

    @Captor
    private ArgumentCaptor<GetNonSynchronizedMomentsRequest> getNonSynchronizedMomentsResponseCaptor;


    @Mock
    private Moment ormMomentMock;

    @Mock
    private SynchronisationData synchronizationDataMock;
    @Mock
    private ConsentDetail consentDetailsMock;

    private DateTime uGrowDateTime;

    @Mock
    private DBFetchingInterface fetchingMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fetchingMonitor = new FetchingMonitor(fetching);
        uGrowDateTime = new DateTime();
        fetchingMonitor.start(eventingMock);
    }

    @Test
    public void ShouldFetchMomentsInsightsAndBabyProfile_WhenLoadTimelineEntryRequestIsReceived() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadTimelineEntryRequest());

        verify(fetching).fetchMoments();
//        verify(fetching).fetchConsent();
        //      verify(fetching).fetchConsent();
        //    verify(fetching).fetchNonSynchronizedMoments();
    }

    @Test
    public void ShouldThrowException_FetchingMoments() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadTimelineEntryRequest());
        verify(fetching).fetchMoments();
    }

    @Test
    public void fetchingMomentsLoadLastMomentRequest() throws Exception {
        fetchingMonitor.onEventBackgroundThread(new LoadLastMomentRequest("temperature"));
    }

    @Test
    public void ShouldFetchMoments_WhenLoadMomentsRequestIsReceived() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadMomentsRequest());

        verify(fetching).fetchMoments();
    }

    @Test
    public void ShouldFetchConsents_WhenLoadConsentsRequest() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadConsentsRequest());

        verify(fetching).fetchConsents();
    }

//<<<<<<< HEAD
//    @Test
//    public void ShouldFetchCharacteristics_WhenLoadCharacterSicsRequest() throws Exception {
//
//        fetchingMonitor.onEventBackgroundThread(new LoadCharacterSicsRequest());
//
//        verify(fetching).fetchCharacteristics();
//    }
//
//    /*@Test
//=======

    @Test
    public void getNonSynchronizedDataRequestTest() throws SQLException {
        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedDataRequestMock);
        Map<Class, List<?>> dataToSync = new HashMap<>();
//        verify(fetching.putMomentsForSync(dataToSync));
//        verify(fetching.putConsentForSync(dataToSync));
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }

    @Test
    public void getNonSynchronizedMomentRequestTest() throws SQLException {
        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedMomentsRequestMock);
        Map<Class, List<?>> dataToSync = new HashMap<>();
//        verify(fetching.putMomentsForSync(dataToSync));
//        verify(fetching.putConsentForSync(dataToSync));
        eventingMock.post(new GetNonSynchronizedDataResponse(1, dataToSync));
    }


//    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponse() {
//        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedDataRequestMock);
//
//        verify(eventingMock).post(getNonSynchronizedDataResponseCaptor.capture());
//
//        return getNonSynchronizedDataResponseCaptor.getValue();
//    }
//
//    private GetNonSynchronizedMomentsRequest getNonSynchronizedMomentsRequest() {
//        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedMomentsRequestMock);
//
//        verify(eventingMock).post(getNonSynchronizedMomentsResponseCaptor.capture());
//
//        return getNonSynchronizedMomentsResponseCaptor.getValue();
//    }
}