package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 01/12/16.
 */
public class UpdatingMonitorTest {

    @Mock
    DBUpdatingInterface dbUpdatingInterface;


    @Mock
    DBSavingInterface dbSavingInterface;

    @Mock
    DBDeletingInterface dbDeletingInterface;

    @Mock
    UserCharacteristicsSegregator userCharacteristicsSegregator;

    @Mock
    MomentUpdateRequest momentUpdateRequestmock;

    @Mock
    MomentsUpdateRequest momentsUpdateRequestMock;

    @Mock
    DBRequestListener dbRequestListener;

    @Mock
    DatabaseConsentUpdateRequest consentUpdateRequestmock;

    @Mock
    BackendMomentListSaveRequest backendMomentListSaveRequestMock;

    @Mock
    MomentDataSenderCreatedRequest momentDataSenderCreatedRequestMock;

    @Mock
    ConsentBackendSaveResponse consentBackendSaveResponseMock;
    @Mock
    Moment momentMock;

    @Mock
    ConsentDetail consentDetailMock;

    @Mock
    Settings settingsMock;
    @Mock
    DBFetchingInterface dbFetchingInterface;

    UpdatingMonitor updatingMonitor;
    @Mock
    BackendResponse backendResponseMock;
    @Mock
    BackendDataRequestFailed backendDataRequestFailedMock;
    @Mock
    private Eventing eventingMock;
    @Mock
    MomentsSegregator momentsSegregatorMock;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    private DBChangeListener dbChangeListener;

    DataServicesManager mDataServices;

    @Mock
    private DatabaseSettingsUpdateRequest databaseSettingsUpdateRequestMock;

    @Mock
    private SyncBitUpdateRequest synBitUpdateRequest;

    @Before
    public void setUp() {
        initMocks(this);
        mDataServices = DataServicesManager.getInstance();
        mDataServices.setAppComponant(appComponantMock);
        mDataServices.registerDBChangeListener(dbChangeListener);
        updatingMonitor = new UpdatingMonitor(dbUpdatingInterface, dbDeletingInterface, dbFetchingInterface, dbSavingInterface);
        updatingMonitor.momentsSegregator = momentsSegregatorMock;
        updatingMonitor.mUserCharacteristicsSegregator = userCharacteristicsSegregator;
        updatingMonitor.start(eventingMock);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentUpdateRequestIsCalled() throws Exception {
       // when(dbUpdatingInterface.getOrmMoment(momentMock)).thenReturn(momentMock);
        when(momentUpdateRequestmock.getMoment()).thenReturn(momentMock);
        updatingMonitor.onEventBackGround(momentUpdateRequestmock);
        verify(momentMock).setSynced(false);
        verify(dbUpdatingInterface).updateMoment(momentMock,momentUpdateRequestmock.getDbRequestListener());
    }

    @Test
    public void shouldUpdateSettings_whenDatabaseSettingsUpdateRequestIsCalled() throws Exception {
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
        verify(dbFetchingInterface).fetchMoments(readDataFromBackendResponseMock.getDbFetchRequestListner());
    }*/

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestPassed() throws Exception {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(Arrays.asList(moment1), dbChangeListener));
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend(Arrays.asList(moment1),null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestPassedWithNull() throws Exception {
        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(null, dbChangeListener));
        verifyNoMoreInteractions(momentsSegregatorMock);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenBackendMomentListSaveRequestFailedWithException() throws Exception {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        doThrow(SQLException.class).when(momentsSegregatorMock).processMomentsReceivedFromBackend(Arrays.asList(moment1), null);
        updatingMonitor.onEventBackGround(new BackendMomentListSaveRequest(Arrays.asList(moment1), dbChangeListener));
        verify(momentsSegregatorMock).processMomentsReceivedFromBackend(Arrays.asList(moment1), null);
    }


    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenMomentDataSenderCreatedRequestPassed() throws Exception {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        updatingMonitor.onEventBackGround(new MomentDataSenderCreatedRequest(Arrays.asList(moment1), dbChangeListener));
         verify(momentsSegregatorMock).processCreatedMoment(Arrays.asList(moment1),null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponsePassed() throws Exception {
        updatingMonitor.onEventBackGround(new ConsentBackendSaveResponse(null, 500,dbRequestListener));
        verify(dbFetchingInterface).isSynced(SyncType.CONSENT.getId());
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponsePassedAndIsSyncTypeIsConsent() throws Exception {
        when(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())).thenReturn(true);
        final ConsentBackendSaveResponse consentBackendSaveResponse = new ConsentBackendSaveResponse(null, 500, dbRequestListener);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenConsentBackendSaveResponseFailed() throws Exception {
        when(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())).thenReturn(true);
        final ConsentBackendSaveResponse consentBackendSaveResponse = new ConsentBackendSaveResponse(null, 500, dbRequestListener);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenMomentDataSenderCreatedRequestPassedWithNullMoments() throws Exception {
        updatingMonitor.onEventBackGround(new MomentDataSenderCreatedRequest(null, dbChangeListener));
        verifyNoMoreInteractions(momentsSegregatorMock);
    }

    @Test
    public void should_Catch_SqlException_whenMomentUpdateRequestIsCalled() throws Exception {
        doThrow(SQLException.class).when(dbUpdatingInterface).updateMoment(momentMock,dbRequestListener);
        updatingMonitor.onEventBackGround(new MomentUpdateRequest(momentMock,dbRequestListener));
        verify(dbUpdatingInterface).updateMoment(momentMock,dbRequestListener);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentsUpdateRequestIsCalled() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        updatingMonitor.onEventBackGround(new MomentsUpdateRequest(list,null));
        verify(dbUpdatingInterface).updateMoments(list,null);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_updateMoments() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateMoments(list,dbRequestListener);
        updatingMonitor.onEventBackGround(new MomentsUpdateRequest(list,dbRequestListener));
        verify(dbUpdatingInterface).updateMoments(list,dbRequestListener);
    }

    @Test
    public void test_DatabaseConsentUpdateRequest() throws Exception {
        List list = new ArrayList();
        list.add(consentDetailMock);
        when(dbUpdatingInterface.updateConsent(list,null)).thenReturn(true);
        updatingMonitor.onEventBackGround(new DatabaseConsentUpdateRequest(list,null));
        verify(dbUpdatingInterface).updateConsent(list,null);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails_For_updateConsent() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(list,dbRequestListener);
        updatingMonitor.onEventBackGround(new DatabaseConsentUpdateRequest(list,dbRequestListener));
        verify(dbUpdatingInterface).updateConsent(list,dbRequestListener);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenUCDBUpdateFromBackendRequestPassed() throws Exception {
        when(userCharacteristicsSegregator.isUCSynced()).thenReturn(true);
        final UCDBUpdateFromBackendRequest consentBackendSaveResponse = new UCDBUpdateFromBackendRequest(null, dbRequestListener);
       // doThrow(SQLException.class).when(dbUpdatingInterface).updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
    }

    @Test
    public void shouldonEventBackgroundThreadMoment_whenonEventBackgroundThreadWhenUCDBUpdateFromBackendRequestFailedWithException() throws Exception {
        when(userCharacteristicsSegregator.isUCSynced()).thenReturn(true);
        final UCDBUpdateFromBackendRequest consentBackendSaveResponse = new UCDBUpdateFromBackendRequest(null, dbRequestListener);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
        updatingMonitor.onEventBackGround(consentBackendSaveResponse);
        verify(dbUpdatingInterface).updateCharacteristics(consentBackendSaveResponse.getUserCharacteristics(), null);
    }

    @Test
    public void shouldUpdateSettings_whenDatabaseSettingsUpdateRequestFailedWithException() throws Exception {
        when(databaseSettingsUpdateRequestMock.getSettings()).thenReturn(settingsMock);
        doThrow(SQLException.class).when(dbUpdatingInterface).updateSettings(settingsMock, null);
        updatingMonitor.onEventBackGround(databaseSettingsUpdateRequestMock);
        verify(dbUpdatingInterface).updateSettings(settingsMock, null);
    }

}