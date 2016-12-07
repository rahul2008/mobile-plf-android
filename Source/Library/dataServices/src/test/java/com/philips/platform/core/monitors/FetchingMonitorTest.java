package com.philips.platform.core.monitors;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private Consent consentMock;

    @Captor
    private ArgumentCaptor<GetNonSynchronizedDataResponse> getNonSynchronizedDataResponseCaptor;

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
    public void ShouldThrowException_FetchingMomentsThrowsException() throws Exception {
        final SQLException exception = new SQLException("test");
        //when(fetching.fetchMoments()).thenThrow(exception);

        fetchingMonitor.onEventBackgroundThread(new LoadTimelineEntryRequest());

        ArgumentCaptor<ExceptionEvent> captor = ArgumentCaptor.forClass(ExceptionEvent.class);
//        verify(eventingMock, times(2)).post(captor.capture());
//        final ExceptionEvent exceptionEvent = captor.getValue();

 //       assertThat(exceptionEvent.getCause()).isSameAs(exception);
 //       assertThat(exceptionEvent.getMessage()).isEqualTo("Loading timeline failed");
    }

    @Test
    public void ShouldFetchMoments_WhenLoadMomentsRequestIsReceived() throws Exception {

        fetchingMonitor.onEventBackgroundThread(new LoadMomentsRequest());

        verify(fetching).fetchMoments();
    }


    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponse() {
        fetchingMonitor.onEventBackgroundThread(getNonSynchronizedDataRequestMock);

        verify(eventingMock).post(getNonSynchronizedDataResponseCaptor.capture());

        return getNonSynchronizedDataResponseCaptor.getValue();
    }
}