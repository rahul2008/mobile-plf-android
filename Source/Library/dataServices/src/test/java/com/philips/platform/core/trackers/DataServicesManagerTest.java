package com.philips.platform.core.trackers;

import android.content.Context;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.verticals.VerticalCreater;
import com.philips.platform.verticals.VerticalUCoreAccessProvider;
import com.philips.platform.verticals.VerticalUserRegistrationInterface;
import com.philips.spy.DSPaginationSpy;
import com.philips.spy.EventingSpy;
import com.philips.testing.verticals.datatyes.MomentType;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataServicesManagerTest {

    public static final int TEST_REFERENCE_ID = 111;
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_BABY_ID = "TEST_BABY_ID";
    private static final String TEST_CONSENT_DETAIL_TYPE = "TEMPERATURE";
    private String TEST_MOMENT_TYPE = "CRYING";
    private String TEST_MOMENT_DETAIL_TYPE = "STICKER";
    private String TEST_MEASUREMENT_TYPE = "DURATION";
    private String TEST_MEASUREMENT_DETAIL_TYPE = "BOTTLE_CONTENTS";

    @Mock
    DataSender dataSenderMock;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListenerMock;

    @Mock
    DataFetcher dataFetcherMock;

    @Mock
    JSONObject jsonObject;

    @Mock
    private File fileMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    private UserRegistrationInterface userRegistrationInterface;

    @Mock
    SynchronisationCompleteListener mSynchronisationCompleteListener;

    private BaseAppDataCreator baseAppDataCreator;

    @Mock
    private BackendIdProvider backendIdProviderMock;

    @Mock
    private Event requestEventMock;

    @Mock
    Insight insightMock;

    @Mock
    private Moment momentMock;

    @Mock
    private MomentDetail momentDetailMock;

    @Mock
    private MeasurementGroup measurementGroupMock;
    @Mock
    private Measurement measurementMock;

    @Mock
    private MeasurementDetail measurementDetailMock;

    @Mock
    private MeasurementGroupDetail measurementGroupDetailMock;

    @Captor
    private ArgumentCaptor<MomentSaveRequest> momentSaveRequestCaptor;

    @Captor
    private ArgumentCaptor<MomentDeleteRequest> momentDeleteEventCaptor;

    @Captor
    private ArgumentCaptor<MomentUpdateRequest> momentUpdateEventCaptor;
    DataServicesManager tracker;
    @Mock
    DBDeletingInterface deletingInterfaceMock;
    @Mock
    DBFetchingInterface fetchingInterfaceMock;
    @Mock
    DBSavingInterface savingInterfaceMock;
    @Mock
    DBUpdatingInterface updatingInterfaceMock;
    @Spy
    Context mockContext;
    @Mock
    private ConsentDetail consentDetailMock;

    UCoreAccessProvider uCoreAccessProvider;

    @Mock
    BaseAppCore coreMock;

    @Mock
    SynchronisationMonitor synchronisationMonitorMock;

    @Mock
    ErrorHandlingInterface errorHandlingInterfaceMock;

    @Mock
    DBRequestListener dbRequestListener;

    @Mock
    Characteristics CharacteristicsMock;

    @Mock
    DBFetchRequestListner dbFetchRequestListner;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    BaseAppDataCreator dataCreatorMock;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListener;

    @Mock
    private Settings settingsMock;

    private EventingSpy eventingSpy;

    DSPaginationSpy mDSPagination;

    @Before
    public void setUp() {
        initMocks(this);

        eventingSpy = new EventingSpy();

        tracker = DataServicesManager.getInstance();
        tracker.setAppComponant(appComponantMock);

        baseAppDataCreator = new VerticalCreater();
        userRegistrationInterface = new VerticalUserRegistrationInterface();
        uCoreAccessProvider = new VerticalUCoreAccessProvider(userRegistrationInterface);
        mDSPagination = new DSPaginationSpy();
        tracker.mEventing = eventingSpy;
        tracker.mDataCreater = baseAppDataCreator;
        tracker.mBackendIdProvider = uCoreAccessProvider;
        tracker.mCore = coreMock;
        tracker.mSynchronisationMonitor = synchronisationMonitorMock;
        tracker.userRegistrationInterface = userRegistrationInterface;
        tracker.errorHandlingInterface = errorHandlingInterfaceMock;
        tracker.mSynchronisationManager = synchronisationManagerMock;
        when(requestEventMock.getEventId()).thenReturn(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostSaveEvent_WhenSaveIsCalled() throws Exception {
        tracker.saveMoment(momentMock, dbRequestListener);
        thenVerifyEventIsPosted("MomentSaveRequest");
    }

    @Test
    public void ShouldPostUpdateEvent_WhenUpdateIsCalled() throws Exception {
        tracker.updateMoment(momentMock, dbRequestListener);
        thenVerifyEventIsPosted("MomentUpdateRequest");
    }

    @Test
    public void ShouldPostFetchEvent_WhenFetchIsCalled() throws Exception {
        tracker.fetchMomentWithType(dbFetchRequestListner, MomentType.TEMPERATURE);
        thenVerifyEventIsPosted("LoadMomentsRequest");
    }

    @Test
    public void ShouldPostFetchLatestMomentByType_WhenFetchIsCalled() throws Exception {
        tracker.fetchLatestMomentByType(MomentType.TEMPERATURE, dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadLatestMomentByTypeRequest");
    }

    @Test
    public void ShouldPostFetchMomentByDateType_WhenFetchIsCalled() throws Exception {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date startDate = sdf.parse("10/11/17");
        Date endDate = sdf.parse("10/23/17");
        tracker.fetchMomentsWithTypeAndTimeLine(MomentType.TEMPERATURE, startDate, endDate, createPagination(), dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadMomentsByDate");
    }

    @Test
    public void ShouldPostFetchMomentByDateRange_WhenFetchIsCalled() throws Exception {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date startDate = sdf.parse("10/11/17");
        Date endDate = sdf.parse("10/23/17");
        tracker.fetchMomentsWithTimeLine(startDate, endDate, createPagination(), dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadMomentsByDate");
    }

    @Test
    public void ShouldPostFetchMomentByIdEvent_WhenFetchMomentByIdIsCalled() throws Exception {
        tracker.fetchMomentForMomentID(1, dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadMomentsRequest");
    }

    @Test
    public void ShouldPostFetchConsentEvent_WhenFetchConsentIsCalled() throws Exception {
        tracker.fetchConsentDetail(dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadConsentsRequest");
    }

    @Test
    public void ShouldPostFetchSettingsEvent_WhenFetchSettingsIsCalled() throws Exception {
        tracker.fetchUserSettings(dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadSettingsRequest");
    }

    @Test
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() throws Exception {
        tracker.createConsentDetail(TEST_CONSENT_DETAIL_TYPE, ConsentDetailStatusType.ACCEPTED, ConsentDetail.DEFAULT_DOCUMENT_VERSION, "fsdfsdf");
    }

    @Test
    public void ShouldAddConcentDetail_WhenConsentIsNull() throws Exception {
        tracker.createConsentDetail("Phase", ConsentDetailStatusType.ACCEPTED, "2", "fsdfsdf");
    }

    @Test
    public void ShouldPostSaveConsentEvent_WhenSaveConsentIsCalled() throws Exception {
        tracker.saveConsentDetails(null, dbRequestListener);
        thenVerifyEventIsPosted("DatabaseConsentSaveRequest");
    }

    @Test
    public void ShouldPostUpdateSettingsEvent_WhenUpdateSettingsIsCalled() throws Exception {
        tracker.updateUserSettings(null, dbRequestListener);
        thenVerifyEventIsPosted("DatabaseSettingsUpdateRequest");
    }

    @Test
    public void ShouldPostUpdateCharacteristicsRequest_WhenUpdateCharacteristicsIsCalled() throws Exception {
        tracker.updateUserCharacteristics(null, dbRequestListener);
    }

    @Test
    public void ShouldPostFetchCharacteristicsRequest_WhenFetchCharacteristicsIsCalled() throws Exception {
        tracker.fetchUserCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostUpdateConsentEvent_WhenUpdateConsentIsCalled() throws Exception {
        tracker.updateConsentDetails(null, dbRequestListener);
        thenVerifyEventIsPosted("DatabaseConsentUpdateRequest");
    }

    @Test
    public void ShouldPostdeleteAllMomentEvent_WhendeleteAllMomentIsCalled() throws Exception {
        tracker.deleteAllMoments(dbRequestListener);
        thenVerifyEventIsPosted("DeleteAllMomentsRequest");
    }

    //TODO: Spoorti - revisit this
    @Test
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() throws Exception {
        tracker.createMoment("jh");
    }

    @Test
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() throws Exception {
        tracker.createMeasurementGroup(momentMock);
    }

    //TODO: Spoorti - revisit
    @Test
    public void ShouldAddMomentDetail_WhenCreateMomentDetailIsCreated() throws Exception {
        baseAppDataCreator.createMomentDetail(TEST_MEASUREMENT_DETAIL_TYPE, momentMock);
    }

    @Test
    public void ShouldStopCore_WhenStopCoreIsCalled() throws Exception {
        tracker.stopCore();
    }

    @Test(expected = RuntimeException.class)
    public void ShouldinitializeSyncMonitors_WheninitializeSyncMonitorsIsCalled() throws Exception {
        tracker.initializeSyncMonitors(null, new ArrayList<DataFetcher>(), new ArrayList<DataSender>(), synchronisationCompleteListener);
    }

    @Test
    public void ShouldCreateMeasurement_WhenqCreateMeasurementIsCalled() throws Exception {
        tracker.createMeasurementGroup(measurementGroupMock);
    }

    @Test
    public void ShouldInitializeDBMonitors_WhenInitializeDBMonitorsIsCalled() throws Exception {
        tracker.initializeDatabaseMonitor(null, deletingInterfaceMock, fetchingInterfaceMock, savingInterfaceMock, updatingInterfaceMock);
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailsIsCalled() throws Exception {
        tracker.createUserCharacteristics("TYPE", "VALUE", mock(Characteristics.class));
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailIsNULL() throws Exception {
        tracker.createUserCharacteristics("TYPE", "VALUE", null);
    }

    @Test
    public void Should_fetchAllMoment_called() throws Exception {
        tracker.fetchAllMoment(dbFetchRequestListner);
        thenVerifyEventIsPosted("LoadMomentsRequest");
    }

    @Test
    public void Should_createUserSettings_called() throws Exception {
        Settings settings = tracker.createUserSettings("en_us", "metric");
        assertThat(settings).isNotNull();
        assertThat(settings).isInstanceOf(Settings.class);
    }

    @Test
    public void Should_createsaveUserSettings_called() throws Exception {
        tracker.saveUserSettings(settingsMock, dbRequestListener);
        thenVerifyEventIsPosted("DatabaseSettingsSaveRequest");
    }

    @Test
    public void Should_createMomentDetail_called() throws Exception {
        tracker.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMomentDetail("Temperature", momentMock)).thenReturn(momentDetailMock);
        MomentDetail detail = tracker.createMomentDetail("Temperature", "23", momentMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(detail).isInstanceOf(MomentDetail.class);
        assertThat(detail).isNotNull();
    }

    @Test
    public void Should_createMeasurement_called() throws Exception {
        tracker.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurement("Temperature", measurementGroupMock)).thenReturn(measurementMock);
        Measurement measurement = tracker.createMeasurement("Temperature", "23", "celcius", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurement).isInstanceOf(Measurement.class);
        assertThat(measurement).isNotNull();
    }

    @Test
    public void Should_createMeasurementDetail_called() throws Exception {
        tracker.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurementDetail("Temperature", measurementMock)).thenReturn(measurementDetailMock);
        MeasurementDetail measurementDetail = tracker.createMeasurementDetail("Temperature", "23", measurementMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementDetail).isInstanceOf(MeasurementDetail.class);
        assertThat(measurementDetail).isNotNull();
    }

    @Test
    public void Should_deleteMoment_called() throws Exception {
        tracker.deleteMoment(momentMock, dbRequestListener);
        thenVerifyEventIsPosted("MomentDeleteRequest");
    }

    @Test
    public void Should_deleteMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        tracker.deleteMoments(list, dbRequestListener);
        thenVerifyEventIsPosted("MomentsDeleteRequest");
    }

    @Test
    public void Should_updateMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        tracker.updateMoments(list, dbRequestListener);
        thenVerifyEventIsPosted("MomentsUpdateRequest");
    }

    @Test
    public void Should_deleteAll_called() throws Exception {
        tracker.deleteAll(dbRequestListener);
        thenVerifyEventIsPosted("DataClearRequest");
    }

    @Test
    public void Should_createMeasurementGroupDetail_called() throws Exception {
        tracker.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurementGroupDetail("Temperature", measurementGroupMock)).thenReturn(measurementGroupDetailMock);
        MeasurementGroupDetail measurementGroupDetail = tracker.createMeasurementGroupDetail("Temperature", "23", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementGroupDetail).isInstanceOf(MeasurementGroupDetail.class);
        assertThat(measurementGroupDetail).isNotNull();
    }

    @Test
    public void Should_saveUserCharacteristics_called() throws Exception {
        List list = new ArrayList();
        list.add(consentDetailMock);
        tracker.saveUserCharacteristics(list, dbRequestListener);
        thenVerifyEventIsPosted("UserCharacteristicsSaveRequest");
    }

    @Test
    public void Should_unRegisterDBChangeListener_called() throws Exception {
        tracker.unRegisterDBChangeListener();
        DBChangeListener dbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        assertThat(dbChangeListener).isNull();
    }

    @Test
    public void Should_registerSynchronisationCompleteListener_called() throws Exception {
        tracker.registerSynchronisationCompleteListener(synchronisationCompleteListenerMock);
        assertThat(DataServicesManager.getInstance().mSynchronisationCompleteListener).isInstanceOf(SynchronisationCompleteListener.class);
    }

    @Test
    public void Should_saveMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        tracker.saveMoments(list, dbRequestListener);
        thenVerifyEventIsPosted("MomentsSaveRequest");
    }

    @Test
    public void Should_unRegisterSynchronisationCosmpleteListener_called() throws Exception {
        tracker.unRegisterSynchronisationCosmpleteListener();
        assertThat(DataServicesManager.getInstance().mSynchronisationCompleteListener).isNull();
    }

    @Test
    public void Should_fetchInsights_called() throws Exception {
        tracker.fetchInsights(dbFetchRequestListner);
        thenVerifyEventIsPosted("FetchInsightsFromDB");
    }

    @Test
    public void Should_deleteInsights_called() throws Exception {
        List list = new ArrayList();
        list.add(insightMock);
        tracker.deleteInsights(list, dbRequestListener);
        thenVerifyEventIsPosted("DeleteInsightFromDB");
    }

    @Test
    public void Should_ClearExpiredMoments_called() {
        tracker.clearExpiredMoments(dbRequestListener);
        thenVerifyEventIsPosted("DeleteExpiredMomentRequest");
    }

    //Push Notification test
    @Test
    public void unRegisterDeviceTokenTest() throws Exception {
        tracker.unRegisterDeviceToken("token", "variant", null);
        thenVerifyEventIsPosted("UnRegisterDeviceToken");
    }

    @Test
    public void registerDeviceTokenTest() throws Exception {
        tracker.registerDeviceToken("token", "variant", "protocol provider", null);
        thenVerifyEventIsPosted("RegisterDeviceToken");
    }

    @Test
    public void handlePushNotificationPayloadTest() throws Exception {
        tracker.handlePushNotificationPayload(jsonObject);
    }

    //Subject Profile Test
    @Test
    public void createSubjectProfileTest() throws Exception {
        tracker.createSubjectProfile("test user", "2013-05-05", "female", 78.88, "2015-10-01T12:11:10.123+0100", null);
        thenVerifyEventIsPosted("CreateSubjectProfileRequestEvent");
    }

    @Test
    public void getSubjectProfilesTest() throws Exception {
        tracker.getSubjectProfiles(null);
        thenVerifyEventIsPosted("GetSubjectProfileListRequestEvent");
    }

    @Test
    public void getSubjectProfileTest() throws Exception {
        tracker.getSubjectProfile("39989890000898989", null);
        thenVerifyEventIsPosted("GetSubjectProfileRequestEvent");
    }

    @Test
    public void deleteSubjectProfileTest() throws Exception {
        tracker.deleteSubjectProfile("78798089987868789", null);
        thenVerifyEventIsPosted("DeleteSubjectProfileRequestEvent");
    }

    //Device Pairing test
    @Test
    public void pairDevicesTest() throws Exception {
        tracker.pairDevices("77908787878978", "RefNode", null, null, "rxd", null);
        thenVerifyEventIsPosted("PairDevicesRequestEvent");
    }

    @Test
    public void unPairDeviceTest() throws Exception {
        tracker.unPairDevice("7867697879787", null);
        thenVerifyEventIsPosted("UnPairDeviceRequestEvent");
    }

    @Test
    public void getPairedDevicesTest() throws Exception {
        tracker.getPairedDevices(null);
        thenVerifyEventIsPosted("GetPairedDeviceRequestEvent");
    }

    private DSPaginationSpy createPagination() {
        mDSPagination.setOrdering(DSPagination.DSPaginationOrdering.DESCENDING);
        mDSPagination.setPageLimit(1);
        mDSPagination.setPageNumber(1);
        mDSPagination.setOrderBy("timestamp");
        return mDSPagination;
    }


    @Test
    public void synchronize() {
        whenSynchronizeIsInvoked();
        thenVerifyMonitorsAreInitialized();
        thenVerifyEventIsPosted("Synchronize");
    }

    @Test
    public void pullSyncByDateRange() {
        whenPullSyncIsInvoked();
        thenVerifyMonitorsAreInitialized();
        thenVerifyEventIsPosted("SynchronizeWithFetchByDateRange");
    }

    private void whenSynchronizeIsInvoked() {
        tracker.synchronize();
    }

    private void whenPullSyncIsInvoked() {
        tracker.synchronizeWithFetchByDateRange(START_DATE, END_DATE, synchronisationCompleteListenerMock);
    }

    private void thenVerifyMonitorsAreInitialized() {
        verify(coreMock).start();
        verify(synchronisationMonitorMock).start(eventingSpy);
    }

    private void thenVerifySynchronisationManagerIsCalledForSynchronize() {
        verify(synchronisationManagerMock).startSync(synchronisationCompleteListener);
    }

    private void thenVerifySynchronisationManagerIsCalled() {
        verify(synchronisationManagerMock).startSyncWithFetchByDateRange(START_DATE.toString(), END_DATE.toString(), synchronisationCompleteListenerMock);
    }


    private void thenVerifyEventIsPosted(String event) {
        assertEquals(event, eventingSpy.postedEvent.getClass().getSimpleName());
    }

    private static final DateTime START_DATE = new DateTime();
    private static final DateTime END_DATE = new DateTime();

}

