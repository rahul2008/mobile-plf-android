package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.FetchInsightsResponse;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.insights.InsightSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdatingMonitorTest {

    @Mock
    private DBUpdatingInterface dbUpdatingInterface;
    @Mock
    private DBSavingInterface dbSavingInterface;
    @Mock
    private DBDeletingInterface dbDeletingInterface;
    @Mock
    private UserCharacteristicsSegregator userCharacteristicsSegregator;
    @Mock
    private MomentUpdateRequest momentUpdateRequestmock;
    @Mock
    private DBRequestListener<Moment> dbRequestListener;
    @Mock
    private DBRequestListener<Insight> insightDBRequestListener;
    @Mock
    private DBRequestListener<ConsentDetail> consentDetailDBRequestListener;
    @Mock
    private DBRequestListener<Characteristics> characteristicsDBRequestListener;
    @Mock
    private Moment momentMock;
    @Mock
    private ConsentDetail consentDetailMock;
    @Mock
    private Settings settingsMock;
    @Mock
    private DBFetchingInterface dbFetchingInterface;
    @Mock
    private Eventing eventingMock;
    @Mock
    private MomentsSegregator momentsSegregatorMock;
    @Mock
    private DBChangeListener dbChangeListener;
    @Mock
    private DatabaseSettingsUpdateRequest databaseSettingsUpdateRequestMock;
    @Mock
    private Insight insightMock;
    @Mock
    private InsightSegregator insightSegregatorMock;
    @Mock
    private AppComponent appComponentMock;

    private UpdatingMonitor updatingMonitor;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        mDataServices.setAppComponent(appComponentMock);
        mDataServices.registerDBChangeListener(dbChangeListener);

        updatingMonitor = new UpdatingMonitor(dbUpdatingInterface, dbDeletingInterface, dbFetchingInterface, dbSavingInterface);
        updatingMonitor.momentsSegregator = momentsSegregatorMock;
        updatingMonitor.insightSegregator = insightSegregatorMock;
        updatingMonitor.mUserCharacteristicsSegregator = userCharacteristicsSegregator;
        updatingMonitor.start(eventingMock);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentUpdateRequestIsCalled() throws Exception {
        // when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        when(momentUpdateRequestmock.getMoment()).thenReturn(momentMock);
        updatingMonitor.onEventBackGround(momentUpdateRequestmock);
        verify(momentMock).setSynced(false);
        verify(dbUpdatingInterface).updateMoment(momentMock, momentUpdateRequestmock.getDbRequestListener());
    }

    @Test
    public void shouldUpdateSettings_whenDatabaseSettingsUpdateRequestIsCalled() {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        updatingMonitor.onEventBackGround(databaseSettingsUpdateRequestMock);
    }


/*    @Test
    public void shouldDeleteUpdateAndPostMoment_whenonEventBackgroundThreadIsCalled() throws Exception {

        when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        updatingMonitor.onEventBackgroundThread(backendResponseMock);
        verify(dbUpdatingInterface).postRetrofitError(backendResponseMock.getCallException());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadIsCalled() throws Exception {

        when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        updatingMonitor.onEventBackgroundThread(backendMomentRequestFailedMock);
        verify(dbUpdatingInterface).updateFailed(backendMomentRequestFailedMock.getException());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenReadDataFromBackendResponsePassed() throws Exception {
        updatingMonitor.onEventAsync(readDataFromBackendResponseMock);
        verify(dbFetchingInterface).fetchMoments(readDataFromBackendResponseMock.getDbFetchRequestListener());
    }*/

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestPassed() throws Exception {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);

        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(Collections.singletonList(moment1), dbRequestListener));

        //noinspection unchecked
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend(eq(Collections.singletonList(moment1)), (DBRequestListener<Moment>) isNotNull());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestPassedWithNull() {
        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(new ArrayList<Moment>(), dbRequestListener));
        verifyNoMoreInteractions(momentsSegregatorMock);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestFailedWithException() throws Exception {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        doThrow(SQLException.class).when(momentsSegregatorMock).processMomentsReceivedFromBackend(Collections.singletonList(moment1), null);

        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(Collections.singletonList(moment1), dbRequestListener));

        //noinspection unchecked
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend(eq(Collections.singletonList(moment1)), (DBRequestListener<Moment>) isNotNull());
    }


    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenMomentDataSenderCreatedRequestPassed() {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);

        updatingMonitor.onEventBackGround(new MomentDataSenderCreatedRequest(Collections.singletonList(moment1), dbRequestListener));

        //noinspection unchecked
        verify(momentsSegregatorMock).processCreatedMoment(eq(Collections.singletonList(moment1)), (DBRequestListener<Moment>) isNotNull());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponsePassed() throws Exception {
        updatingMonitor.onEventBackGround(new ConsentBackendSaveResponse(null, 500, consentDetailDBRequestListener));
        verify(dbFetchingInterface).isSynced(SyncType.CONSENT.getId());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponsePassedAndIsSyncTypeIsConsent() throws Exception {
        when(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())).thenReturn(true);
        final ConsentBackendSaveResponse consentBackendSaveResponse = new ConsentBackendSaveResponse(null, 500, consentDetailDBRequestListener);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponseFailed() throws Exception {
        when(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())).thenReturn(true);
        final ConsentBackendSaveResponse consentBackendSaveResponse = new ConsentBackendSaveResponse(null, 500, consentDetailDBRequestListener);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenMomentDataSenderCreatedRequestPassedWithNullMoments() {
        updatingMonitor.onEventBackGround(new MomentDataSenderCreatedRequest(new ArrayList<Moment>(), dbRequestListener));
        verifyNoMoreInteractions(momentsSegregatorMock);
    }

    @Test
    public void should_Catch_SqlException_whenMomentUpdateRequestIsCalled() throws Exception {
        doThrow(SQLException.class).when(dbUpdatingInterface).updateMoment(momentMock, dbRequestListener);
        updatingMonitor.onEventBackGround(new MomentUpdateRequest(momentMock, dbRequestListener));
        verify(dbUpdatingInterface).updateMoment(momentMock, dbRequestListener);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentsUpdateRequestIsCalled() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        updatingMonitor.onEventBackGround(new MomentsUpdateRequest(list, null));
        verify(dbUpdatingInterface).updateMoments(list, null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_updateMoments() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateMoments(list, dbRequestListener);
        updatingMonitor.onEventBackGround(new MomentsUpdateRequest(list, dbRequestListener));
        verify(dbUpdatingInterface).updateMoments(list, dbRequestListener);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_DatabaseConsentUpdateRequest() throws Exception {
        List list = new ArrayList();
        list.add(consentDetailMock);
        when(dbUpdatingInterface.updateConsent(list, null)).thenReturn(true);
        updatingMonitor.onEventBackGround(new DatabaseConsentUpdateRequest(list, null));
        verify(dbUpdatingInterface).updateConsent(list, null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_updateConsent() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(list, consentDetailDBRequestListener);
        updatingMonitor.onEventBackGround(new DatabaseConsentUpdateRequest(list, consentDetailDBRequestListener));
        verify(dbUpdatingInterface).updateConsent(list, consentDetailDBRequestListener);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenUCDBUpdateFromBackendRequestPassed() throws Exception {
        when(userCharacteristicsSegregator.isUCSynced()).thenReturn(true);
        final UCDBUpdateFromBackendRequest consentBackendSaveResponse = new UCDBUpdateFromBackendRequest(null, characteristicsDBRequestListener);
        // doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenUCDBUpdateFromBackendRequestFailedWithException() throws Exception {
        when(userCharacteristicsSegregator.isUCSynced()).thenReturn(true);
        final UCDBUpdateFromBackendRequest consentBackendSaveResponse = new UCDBUpdateFromBackendRequest(null, characteristicsDBRequestListener);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
    }

    @Test
    public void shouldUpdateSettings_whenDatabaseSettingsUpdateRequestFailedWithException() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateSettings(settingsMock, null);
        updatingMonitor.onEventBackGround(databaseSettingsUpdateRequestMock);
        verify(dbSavingInterface).saveSettings(settingsMock, databaseSettingsUpdateRequestMock.getDbRequestListener());
        verify(dbUpdatingInterface).updateSyncBit(SyncType.SETTINGS.getId(), false);
    }

    @Test
    public void shouldUpdateSettings_whenupdateSyncBitFailedWithException() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateSyncBit(SyncType.CHARACTERISTICS.getId(), true);
        updatingMonitor.onEventBackGround(new SyncBitUpdateRequest(SyncType.CHARACTERISTICS, true));
        verify(dbUpdatingInterface).updateSyncBit(SyncType.CHARACTERISTICS.getId(), true);
    }

    @Test
    public void shouldUpdateSettings_whenupdateSyncBit_success() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        // doThrow(SQLException.class).when(dbUpdatingInterface).updateSyncBit(SyncType.CHARACTERISTICS.getId(), true);
        updatingMonitor.onEventBackGround(new SyncBitUpdateRequest(SyncType.CHARACTERISTICS, true));
        verify(dbUpdatingInterface).updateSyncBit(SyncType.CHARACTERISTICS.getId(), true);
    }

    @Test
    public void shouldUpdateSettings_whenIsSyncedFailedWithException() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        when(dbFetchingInterface.isSynced(SyncType.SETTINGS.getId())).thenReturn(true);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateSettings(settingsMock, null);
        updatingMonitor.onEventBackGround(new SettingsBackendSaveResponse(settingsMock));
        verify(dbUpdatingInterface).updateSettings(settingsMock, null);
    }

    @Test
    public void shouldUpdateSettings_whenIsSyncedSuccess() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        when(dbFetchingInterface.isSynced(SyncType.SETTINGS.getId())).thenReturn(true);
        //  doThrow(SQLException.class).when(dbUpdatingInterface).updateSettings(settingsMock,null);
        updatingMonitor.onEventBackGround(new SettingsBackendSaveResponse(settingsMock));
        verify(dbUpdatingInterface).updateSettings(settingsMock, null);
    }

    @Test
    public void shouldUpdateSettings_whenprocessInsightsFailsWithException() throws Exception {
        List<Insight> insights = new ArrayList<>();
        insights.add(insightMock);
        doThrow(SQLException.class).when(insightSegregatorMock).processInsights(insights, insightDBRequestListener);
        updatingMonitor.onEventBackGround(new FetchInsightsResponse(insights, insightDBRequestListener));
        verify(insightSegregatorMock).processInsights(insights, insightDBRequestListener);
    }

    @Test
    public void shouldUpdateSettings_whenprocessInsightsSuccess() throws Exception {
        List<Insight> insights = new ArrayList<>();
        insights.add(insightMock);
        // doThrow(SQLException.class).when(insightSegregatorMock).processInsights(insights,dbRequestListener);
        updatingMonitor.onEventBackGround(new FetchInsightsResponse(insights, insightDBRequestListener));
        verify(insightSegregatorMock).processInsights(insights, insightDBRequestListener);
    }

    @Test
    public void shouldUpdateSettings_whenprocessInsightsSuccessWithRequestListenerNull() throws Exception {
        DataServicesManager.getInstance().registerDBChangeListener(null);
        List<Insight> insights = new ArrayList<>();
        insights.add(insightMock);
        // doThrow(SQLException.class).when(insightSegregatorMock).processInsights(insights,dbRequestListener);
        updatingMonitor.onEventBackGround(new FetchInsightsResponse(insights, insightDBRequestListener));
        verify(insightSegregatorMock).processInsights(insights, insightDBRequestListener);
    }

    // MomentDataSenderCreatedRequest
    @Test
    public void givenMonitorSetup_whenPostMomentDataSenderCreatedRequest_thenShouldNotCallback() {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        MomentDataSenderCreatedRequest event = new MomentDataSenderCreatedRequest(Collections.singletonList(moment1), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(dbRequestListener, never()).onSuccess((List<? extends Moment>) any());
    }


    @Test
    public void givenMonitorSetup_whenPostMomentDataSenderCreatedRequest_thenShouldCallMomentSegregator() {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        MomentDataSenderCreatedRequest event = new MomentDataSenderCreatedRequest(Collections.singletonList(moment1), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(momentsSegregatorMock).processCreatedMoment((List<Moment>)any(), (DBRequestListener<Moment>) any());
    }

    @Test
    public void givenMonitorSetup_andNoListener_whenPostMomentDataSenderCreatedRequest_thenShouldCallMomentSegregator() {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        MomentDataSenderCreatedRequest event = new MomentDataSenderCreatedRequest(Collections.singletonList(moment1), null);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(momentsSegregatorMock).processCreatedMoment((List<Moment>)any(), (DBRequestListener<Moment>) isNull());
    }

    @Test
    public void givenMonitorSetup_andSendingZeroMoments_whenPostMomentDataSenderCreatedRequest_thenShouldNotCallback() {
        MomentDataSenderCreatedRequest event = new MomentDataSenderCreatedRequest(new ArrayList<OrmMoment>(), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(dbRequestListener, never()).onSuccess((List<? extends Moment>) any());
    }

    // BackendMomentListSaveRequest
    @Test
    public void givenMonitorSetup_whenPostBackendMomentListSaveRequest_thenShouldNotCallback() {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        BackendMomentListSaveRequest event = new BackendMomentListSaveRequest(Collections.singletonList(moment1), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(dbRequestListener, never()).onSuccess((List<? extends Moment>) any());
    }


    @Test
    public void givenMonitorSetup_whenPostBackendMomentListSaveRequest_thenShouldCallMomentSegregator() throws SQLException {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        BackendMomentListSaveRequest event = new BackendMomentListSaveRequest(Collections.singletonList(moment1), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend((List<Moment>) any(), (DBRequestListener<Moment>) any());
    }

    @Test
    public void givenMonitorSetup_andNoListener_whenPostBackendMomentListSaveRequest_thenShouldCallMomentSegregator() throws SQLException {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        BackendMomentListSaveRequest event = new BackendMomentListSaveRequest(Collections.singletonList(moment1), null);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend((List<Moment>) any(), (DBRequestListener<Moment>) any());
    }

    @Test
    public void givenMonitorSetup_andSendingZeroMoments_whenPostBackendMomentListSaveRequest_thenShouldNotCallback() {
        BackendMomentListSaveRequest event = new BackendMomentListSaveRequest(new ArrayList<Moment>(), dbRequestListener);

        updatingMonitor.onEventBackGround(event);

        //noinspection unchecked
        verify(dbRequestListener, never()).onSuccess((List<Moment>) any());
    }
}