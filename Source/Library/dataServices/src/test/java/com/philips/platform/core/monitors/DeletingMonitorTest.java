package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
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
        monitor.onEventBackgroundThread(new DataClearRequest(dbRequestListener));
        verify(deletingMock).deleteAll(dbRequestListener);
    }

    @Test
    public void DeletionMomentAsked_WhenEventReceived() throws Exception {
        monitor.onEventAsync(new MomentDeleteRequest(momentMock,dbRequestListener));
        verify(deletingMock).markAsInActive(momentMock,dbRequestListener);
    }

    @Test
    public void MomentBackendDeleteResponse_WhenEventReceived() throws Exception {
        monitor.onEventBackgroundThread(new MomentBackendDeleteResponse(momentMock,dbRequestListener));
        verify(deletingMock).deleteMoment(momentMock,dbRequestListener);
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
        monitor.onEventBackgroundThread(request);

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
        assertThat(momentChangeEvent.getMoment()).isSameAs(momentMock);
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
}