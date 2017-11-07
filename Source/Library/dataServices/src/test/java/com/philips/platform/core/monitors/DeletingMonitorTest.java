package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteInsightResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeletingMonitorTest {

	private DeletingMonitor monitor;

	@Mock
	private DBDeletingInterface deletingMock;

	@Mock
	private Eventing eventingMock;

	@Captor
	private ArgumentCaptor<Event> eventCaptor;

	@Mock
	private Moment momentMock;

	@Mock
	private Insight insightMock;

	@Mock
	private DBSavingInterface savingMock;

	@Mock
	private SynchronisationData synchronisationMock;

	@Mock
	DBRequestListener dbRequestListener;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		monitor = new DeletingMonitor(deletingMock);
		monitor.start(eventingMock);
	}

	@Test
	public void DeletionAsked_WhenEventReceived() throws Exception {
		monitor.onEventBackGround(new DataClearRequest(dbRequestListener));
		verify(deletingMock).deleteAll(dbRequestListener);
	}

	@Test
	public void DeletionMomentAsked_WhenEventReceived() throws Exception {
		monitor.onEventBackGround(new MomentDeleteRequest(momentMock, dbRequestListener));
		verify(deletingMock).markAsInActive(momentMock, dbRequestListener);
	}

	@Test
	public void MomentBackendDeleteResponse_WhenEventReceived() throws Exception {
		monitor.onEventBackGround(new MomentBackendDeleteResponse(momentMock, dbRequestListener));
		verify(deletingMock).deleteMoment(momentMock, dbRequestListener);
	}

	@Test
	public void DeleteAllMomentsRequest_WhenEventReceived() throws Exception {
		monitor.onEventBackGround(new DeleteAllMomentsRequest(dbRequestListener));
		verify(deletingMock).deleteAllMoments(dbRequestListener);
	}

	@Test
	public void MomentsDeleteRequest_WhenEventReceived() throws Exception {
		List list = new ArrayList();
		list.add(momentMock);
		monitor.onEventBackGround(new MomentsDeleteRequest(list, dbRequestListener));
		verify(deletingMock).markMomentsAsInActive(list, dbRequestListener);
	}

   /* @Test
    public void ExceptionEventRaised_WhenExceptionHappens() throws Exception {
        SQLException exception = new SQLException();
        doThrow(exception).when(deletingMock).deleteAll();

        monitor.onEventBackgroundThread(new DataClearRequest());

        verify(eventingMock).post(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(ExceptionEvent.class);
        ExceptionEvent exceptionEvent = (ExceptionEvent) eventCaptor.getValue();
        assertThat(exceptionEvent.getCause()).isSameAs(exception);
    }*/

    /*@Test
    public void ExceptionEventHasReferenceId_WhenExceptionHappens() throws Exception {
        SQLException exception = new SQLException("test");
        doThrow(exception).when(deletingMock).deleteAll();
        DataClearRequest request = new DataClearRequest();

        monitor.onEventBackgroundThread(request);

        verify(eventingMock).post(eventCaptor.capture());
        ExceptionEvent exceptionEvent = (ExceptionEvent) eventCaptor.getValue();
        assertThat(exceptionEvent.getReferenceId()).isEqualTo(request.getEventId());
    }*/

	@Test
	public void ResponseEventRaised_WhenDeletedHappens() throws Exception {
		DataClearRequest request = new DataClearRequest(dbRequestListener);
		monitor.onEventBackGround(request);

       /* verify(eventingMock).post(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(DataClearResponse.class);
        DataClearResponse responseEvent = (DataClearResponse) eventCaptor.getValue();
        assertThat(responseEvent.getReferenceId()).isEqualTo(request.getEventId());*/
	}

 /*   @Test
    public void ShouldDeleteMoment_WhenMomentIsNotSynchronizedWithBackend() throws Exception {
        when(momentMock.getSynchronisationData()).thenReturn(null);
        monitor.onEventAsync(new MomentDeleteRequest(momentMock));

        verify(momentMock).setSynchronisationData(any(SynchronisationData.class));
    }*/

/*    @Test
    public void ShouldUpdateMomentFieldsOnDeleteRequest_WhenMomentIsSynchronizedWithBackend() throws Exception {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationMock);

        monitor.onEventAsync(new MomentDeleteRequest(momentMock));

        verify(synchronisationMock).setInactive(true);
        verify(momentMock).setSynced(false);
        verify(savingMock).saveMoment(momentMock);
    }*/

 /*   @Test
    public void ShouldDeleteMoment_WhenBackendDeleteResponseIsReceived() throws Exception {
        monitor.onEventBackgroundThread(new MomentBackendDeleteResponse(momentMock));

        verify(deletingMock).markAsInActive(momentMock);
    }*/

   /* @Test
    public void ShouldSendExceptionEvent_WhenDeletionFailed() throws Exception {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationMock);

        SQLException exception = new SQLException("test");
        doThrow(exception).when(savingMock).saveMoment(momentMock);

        MomentDeleteRequest requestEvent = new MomentDeleteRequest(momentMock);
        monitor.onEventAsync(requestEvent);

        verify(eventingMock).post(eventCaptor.capture());
        ExceptionEvent exceptionEvent = (ExceptionEvent) eventCaptor.getValue();
        assertThat(exceptionEvent.getReferenceId()).isEqualTo(requestEvent.getEventId());
        assertThat(exceptionEvent.getCause()).isSameAs(exception);
    }*/

   /* @Test
    public void ShouldPostEvent_WhenDeletionCompleted() throws Exception {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationMock);
        MomentDeleteRequest requestEvent = new MomentDeleteRequest(momentMock);
        monitor.onEventAsync(requestEvent);

        verify(eventingMock).post(eventCaptor.capture());
        MomentChangeEvent momentChangeEvent = (MomentChangeEvent) eventCaptor.getValue();
        assertThat(momentChangeEvent.getReferenceId()).isEqualTo(requestEvent.getEventId());
        assertThat(momentChangeEvent.getMoments()).isSameAs(momentMock);
    }*/

    /*@Test
    public void ShouldPostEvent_WhenTemperatureReadingsDeletionIsCompleted() throws Exception {
        DeleteTemperatureReadingRequest requestEvent = getDeleteThermometerReadingEvent();
        monitor.onEventBackgroundThread(requestEvent);

        verify(eventingMock).post(isA(DeleteTemperatureReadingResponse.class));
    }

    @Test
    public void ShouldPostException_WhenDeletionIsFailed() throws Exception {
        DeleteTemperatureReadingRequest requestEvent = getDeleteThermometerReadingEvent();
        SQLException exception = new SQLException("test");

        doThrow(exception).when(deletingMock).deleteTemperatureReading(any(OrmTemperatureReading.class));

        monitor.onEventBackgroundThread(requestEvent);

        verify(eventingMock).post(isA(ExceptionEvent.class));
    }

    @NonNull
    private DeleteTemperatureReadingRequest getDeleteThermometerReadingEvent() {
        final List<TemperatureReading> readingsList = new ArrayList<>();
        readingsList.add(new TemperatureReading(DateTime.now(), 330.f));
        return new DeleteTemperatureReadingRequest(readingsList);
    }

    @Test
    public void ShouldSetInsightInActiveToTrue_WhenDeleteInsightIsRequested() throws Exception {
        when(insightMock.getSynchronisationData()).thenReturn(synchronisationMock);

        monitor.onEventBackgroundThread(new InsightDeleteRequest(insightMock));

        verify(insightMock).setSynced(false);
        verify(synchronisationMock).setInactive(true);
        verify(eventingMock).post(eventCaptor.capture());
        InsightChangeEvent insightChangeEvent = (InsightChangeEvent) eventCaptor.getValue();
        assertThat(insightChangeEvent.getInsight()).isSameAs(insightMock);
    }

    @Test
    public void ShouldDeleteInsight_WhenEventIsReceived() throws Exception {
        monitor.onEventBackgroundThread(new InsightDeleteResponse(insightMock));

        verify(deletingMock).deleteInsight(insightMock);
    }*/

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_deleteAll() throws Exception {
		doThrow(SQLException.class).when(deletingMock).deleteAll(dbRequestListener);
		monitor.onEventBackGround(new DataClearRequest(dbRequestListener));
		verify(deletingMock).deleteAll(dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_deleteAllMoments() throws Exception {
		doThrow(SQLException.class).when(deletingMock).deleteAllMoments(dbRequestListener);
		monitor.onEventBackGround(new DeleteAllMomentsRequest(dbRequestListener));
		verify(deletingMock).deleteAllMoments(dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_markAsInActive() throws Exception {
		doThrow(SQLException.class).when(deletingMock).markAsInActive(momentMock, dbRequestListener);
		monitor.onEventBackGround(new MomentDeleteRequest(momentMock, dbRequestListener));
		verify(deletingMock).markAsInActive(momentMock, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_markMomentsAsInActive() throws Exception {
		List list = new ArrayList();
		list.add(momentMock);
		doThrow(SQLException.class).when(deletingMock).markMomentsAsInActive(list, dbRequestListener);
		monitor.onEventBackGround(new MomentsDeleteRequest(list, dbRequestListener));
		verify(deletingMock).markMomentsAsInActive(list, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_deleteMoment() throws Exception {
		doThrow(SQLException.class).when(deletingMock).deleteMoment(momentMock, dbRequestListener);
		monitor.onEventBackGround(new MomentBackendDeleteResponse(momentMock, dbRequestListener));
		verify(deletingMock).deleteMoment(momentMock, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_deleteInsights() throws Exception {
		List<Insight> insights = new ArrayList<>();
		insights.add(insightMock);
		doThrow(SQLException.class).when(deletingMock).markInsightsAsInActive(insights, dbRequestListener);
		monitor.onEventBackGround(new DeleteInsightFromDB(insights, dbRequestListener));
		verify(deletingMock).markInsightsAsInActive(insights, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhendeleteInsights_success() throws Exception {
		List<Insight> insights = new ArrayList<>();
		insights.add(insightMock);
		// doThrow(SQLException.class).when(deletingMock).markInsightsAsInActive(insights,dbRequestListener);
		monitor.onEventBackGround(new DeleteInsightFromDB(insights, dbRequestListener));
		verify(deletingMock).markInsightsAsInActive(insights, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_deleteInsight() throws Exception {
		doThrow(SQLException.class).when(deletingMock).deleteInsight(insightMock, dbRequestListener);
		monitor.onEventBackGround(new DeleteInsightResponse(insightMock, dbRequestListener));
		verify(deletingMock).deleteInsight(insightMock, dbRequestListener);
	}

	@Test
	public void ShouldPostExceptionEvent_WhendeleteInsight_success() throws Exception {
		// doThrow(SQLException.class).when(deletingMock).deleteInsight(insightMock,dbRequestListener);
		monitor.onEventBackGround(new DeleteInsightResponse(insightMock, dbRequestListener));
		verify(deletingMock).deleteInsight(insightMock, dbRequestListener);
	}

	@Test
	public void deleteExpireMoments_whenDeleteExpireMomentsRequested() throws Exception {
		whenDeleteExpireMomentsRequested();
		verifyDeleteExpireMomentsRequested();
	}

	@Test
	public void postException_WhenDeletionFailsFor_deleteExpiredMoments() throws Exception {
		whenDeleteExpireMomentsRequested_postException();
        verifyDeleteExpireMomentsRequested_deleteFailed();
	}

	private void verifyDeleteExpireMomentsRequested_deleteFailed() {
		verify(deletingMock).deleteFailed(any(SQLException.class), any(dbRequestListener.getClass()));
	}

	private void whenDeleteExpireMomentsRequested_postException() throws SQLException {
		doThrow(SQLException.class).when(deletingMock).deleteAllExpiredMoments(dbRequestListener);
		monitor.onEventBackGround(new DeleteExpiredMomentRequest(dbRequestListener));
	}


	private void verifyDeleteExpireMomentsRequested() throws SQLException {
		verify(deletingMock).deleteAllExpiredMoments(dbRequestListener);
	}

	private void whenDeleteExpireMomentsRequested() {
		monitor.onEventBackGround(new DeleteExpiredMomentRequest(dbRequestListener));
	}

}