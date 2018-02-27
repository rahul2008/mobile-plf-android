package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DeleteAllInsights;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteInsightResponse;
import com.philips.platform.core.events.DeleteSyncedMomentsRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmInsight;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeletingMonitorTest {

    private static final String CREATOR_ID = "creator";
    private static final String SUBJECT_ID = "subject";
    private static final DateTime NOW = new DateTime();

    private DeletingMonitor deletingMonitor;

    private SQLException sqlException;

    private Moment moment;

    private Insight insight;

    private List<Moment> momentList = new ArrayList<>();

    private List<Insight> insightList = new ArrayList<>();

    @Mock
    private DBDeletingInterface dbDeletingInterface;

    @Mock
    private DBRequestListener dbRequestListener;

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

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        deletingMonitor = new DeletingMonitor(dbDeletingInterface);
        deletingMonitor.start(eventingMock);
    }

    @Test
    public void deleteAll_WhenDataClearRequestIsPosted() throws SQLException {
        whenDataClearRequestEventIsPosted();
        thenVerifyDeleteAllIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForDeleteAll() throws SQLException {
        givenSQLExceptionThrownWhenDeleteAll();
        whenDataClearRequestEventIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void deleteMoment_WhenMomentBackendDeleteResponseIsPosted() throws SQLException {
        whenMomentBackendDeleteResponseIsPosted();
        thenVerifyDeleteMomentIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForDeleteMoment() throws SQLException {
        givenSQLExceptionThrownWhenDeleteMoment();
        whenMomentBackendDeleteResponseIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void deleteAllMoments_WhenDeleteAllMomentsRequestIsPosted() throws SQLException {
        whenDeleteAllMomentsRequestIsPosted();
        thenVerifyDeleteAllMomentsIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForDeleteAllMoments() throws SQLException {
        givenSQLExceptionThrownWhenDeleteAllMoments();
        whenDeleteAllMomentsRequestIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void markAsInActive_WhenMomentDeleteRequestIsPosted() throws SQLException {
        givenMomentToDelete();
        whenMomentDeleteRequestIsPosted();
        thenVerifyMarkAsInactiveIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForMarkAsInActive() throws SQLException {
        givenMomentToDelete();
        givenSQLExceptionThrownWhenMarkAsInactive();
        whenMomentDeleteRequestIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void markMomentsAsInActive_WhenMomentsDeleteRequestIsPosted() throws SQLException {
        givenMomentsDeletedInApp();
        whenMomentsDeleteRequestIsPosted();
        thenVerifyMarkMomentsAsInActiveIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForMarkMomentsAsInActive() throws SQLException {
        givenMomentsDeletedInApp();
        givenSQLExceptionThrownWhenMarkMomentsAsInactive();
        whenMomentsDeleteRequestIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void deleteAllExpiredMoments_WhenDeleteExpiredMomentRequestIsPosted() throws SQLException {
        whenDeleteExpiredMomentRequestIsPosted();
        thenVerifyDeleteAllExpiredMomentsIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForDeleteAllExpiredMoments() throws SQLException {
        givenSQLExceptionThrownWhenDeleteAllExpiredMomentsIsInvoked();
        whenDeleteExpiredMomentRequestIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void markInsightsAsInActive_WhenDeleteInsightFromDBIsPosted() throws SQLException {
        givenInsightToDelete();
        whenDeleteInsightFromDBIsPosted();
        thenVerifyMarkInsightsAsInActiveIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForMarkInsightsAsInActive() throws SQLException {
        givenInsightToDelete();
        givenSQLExceptionThrownWhenMarkInsightsAsInActiveIsInvoked();
        whenDeleteInsightFromDBIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    public void deleteInsight_WhenDeleteInsightResponseIsPosted() throws SQLException {
        givenInsightToDelete();
        whenDeleteInsightResponseIsPosted();
        thenVerifyDeleteInsightIsInvoked();
    }

    @Test
    public void deleteFailed_WhenExceptionIsThrown_ForDeleteInsight() throws SQLException {
        givenInsightToDelete();
        givenSQLExceptionThrownWhenDeleteInsightIsInvoked();
        whenDeleteInsightResponseIsPosted();
        thenVerifyDeleteFailedIsInvoked(sqlException);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_noRequestListener() throws SQLException {
        DeleteSyncedMomentsRequest event = new DeleteSyncedMomentsRequest(null);

        deletingMonitor.onEventBackGround(event);

        verify(dbDeletingInterface).deleteSyncedMoments((DBRequestListener<Moment>) eq(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_withRequestListener() throws SQLException {
        DeleteSyncedMomentsRequest event = new DeleteSyncedMomentsRequest(dbRequestListener);

        deletingMonitor.onEventBackGround(event);

        verify(dbDeletingInterface).deleteSyncedMoments((DBRequestListener<Moment>) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_callsDeleteFailedOnFailure() throws SQLException {
        doThrow(new SQLException()).when(dbDeletingInterface).deleteSyncedMoments((DBRequestListener<Moment>) any());
        DeleteSyncedMomentsRequest event = new DeleteSyncedMomentsRequest(dbRequestListener);

        deletingMonitor.onEventBackGround(event);

        verify(dbDeletingInterface).deleteFailed(any(SQLException.class), eq(dbRequestListener));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteAllInsights_noRequestListener() throws SQLException {
        DeleteAllInsights event = new DeleteAllInsights(null);
        deletingMonitor.onEventBackGround(event);

        verify(dbDeletingInterface).deleteAllInsights((DBRequestListener<Insight>) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteAllInsights_withRequestListener() throws SQLException {
        DeleteAllInsights event = new DeleteAllInsights(dbRequestListener);
        deletingMonitor.onEventBackGround(event);

        verify(dbDeletingInterface).deleteAllInsights((DBRequestListener<Insight>) any());
    }

    private void whenDataClearRequestEventIsPosted() {
        deletingMonitor.onEventBackGround(new DataClearRequest(dbRequestListener));
    }

    private void whenMomentBackendDeleteResponseIsPosted() {
        deletingMonitor.onEventBackGround(new MomentBackendDeleteResponse(moment, dbRequestListener));
    }

    private void whenDeleteAllMomentsRequestIsPosted() {
        deletingMonitor.onEventBackGround(new DeleteAllMomentsRequest(dbRequestListener));
    }

    private void whenMomentDeleteRequestIsPosted() {
        deletingMonitor.onEventBackGround(new MomentDeleteRequest(moment, dbRequestListener));
    }

    private void whenDeleteExpiredMomentRequestIsPosted() {
        deletingMonitor.onEventBackGround(new DeleteExpiredMomentRequest(dbRequestListener));
    }

    private void whenMomentsDeleteRequestIsPosted() {
        deletingMonitor.onEventBackGround(new MomentsDeleteRequest(momentList, dbRequestListener));
    }

    private void whenDeleteInsightFromDBIsPosted() {
        deletingMonitor.onEventBackGround(new DeleteInsightFromDB(insightList, dbRequestListener));
    }

    private void whenDeleteInsightResponseIsPosted() {
        deletingMonitor.onEventBackGround(new DeleteInsightResponse(insight, dbRequestListener));
    }

    private void thenVerifyDeleteAllIsInvoked() throws SQLException {
        verify(dbDeletingInterface).deleteAll(dbRequestListener);
    }

    private void thenVerifyDeleteMomentIsInvoked() throws SQLException {
        verify(dbDeletingInterface).deleteMoment(moment, dbRequestListener);
    }

    private void thenVerifyDeleteAllMomentsIsInvoked() throws SQLException {
        verify(dbDeletingInterface).deleteAllMoments(dbRequestListener);
    }

    private void thenVerifyDeleteFailedIsInvoked(SQLException exception) {
        verify(dbDeletingInterface).deleteFailed(exception, dbRequestListener);
    }

    private void thenVerifyMarkAsInactiveIsInvoked() throws SQLException {
        verify(dbDeletingInterface).markAsInActive(moment, dbRequestListener);
    }

    private void thenVerifyDeleteAllExpiredMomentsIsInvoked() throws SQLException {
        verify(dbDeletingInterface).deleteAllExpiredMoments(dbRequestListener);
    }

    private void thenVerifyMarkMomentsAsInActiveIsInvoked() throws SQLException {
        verify(dbDeletingInterface).markMomentsAsInActive(momentList, dbRequestListener);
    }

    private void thenVerifyMarkInsightsAsInActiveIsInvoked() throws SQLException {
        verify(dbDeletingInterface).markInsightsAsInActive(insightList, dbRequestListener);
    }

    private void thenVerifyDeleteInsightIsInvoked() throws SQLException {
        verify(dbDeletingInterface).deleteInsight(insight, dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenDeleteAll() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).deleteAll(dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenDeleteMoment() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).deleteMoment(moment, dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenDeleteAllMoments() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).deleteAllMoments(dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenMarkAsInactive() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).markAsInActive(moment, dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenDeleteAllExpiredMomentsIsInvoked() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).deleteAllExpiredMoments(dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenMarkMomentsAsInactive() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).markMomentsAsInActive(momentList, dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenMarkInsightsAsInActiveIsInvoked() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).markInsightsAsInActive(insightList, dbRequestListener);
    }

    private void givenSQLExceptionThrownWhenDeleteInsightIsInvoked() throws SQLException {
        sqlException = new SQLException();
        doThrow(sqlException).when(dbDeletingInterface).deleteInsight(insight, dbRequestListener);
    }

    private void givenMomentToDelete() throws SQLException {
        moment = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
    }

    private void givenMomentsDeletedInApp() throws SQLException {
        givenMomentToDelete();
        momentList.add(moment);
    }

    private void givenInsightToDelete() {
        insight = new OrmInsight();
        insightList.add(insight);
    }
}