package com.philips.platform.core.trackers;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
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
import com.philips.platform.core.events.CreateSubjectProfileRequestEvent;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteSubjectProfileRequestEvent;
import com.philips.platform.core.events.DeleteSyncedMomentsRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.GetPairedDeviceRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileRequestEvent;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLatestMomentByTypeRequest;
import com.philips.platform.core.events.LoadMomentsByDate;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.PairDevicesRequestEvent;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnPairDeviceRequestEvent;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
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
import com.philips.testing.verticals.datatyes.MomentType;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataServicesManagerTest {
    private static final DateTime START_DATE = new DateTime();
    private static final DateTime END_DATE = new DateTime();

    private static final int TEST_REFERENCE_ID = 111;
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_BABY_ID = "TEST_BABY_ID";
    private static final String TEST_CONSENT_DETAIL_TYPE = "TEMPERATURE";
    private String TEST_MOMENT_TYPE = "CRYING";
    private String TEST_MOMENT_DETAIL_TYPE = "STICKER";
    private String TEST_MEASUREMENT_TYPE = "DURATION";
    private String TEST_MEASUREMENT_DETAIL_TYPE = "BOTTLE_CONTENTS";

    @Mock
    private DataSender dataSenderMock;

    @Mock
    private SynchronisationCompleteListener synchronisationCompleteListenerMock;

    @Mock
    private DataFetcher dataFetcherMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private JSONObject jsonObject;

    @Mock
    private File fileMock;

    @Mock
    private SynchronisationManager synchronisationManagerMock;

    private UserRegistrationInterface userRegistrationInterface;

    @Mock
    private SynchronisationCompleteListener mSynchronisationCompleteListener;

    private BaseAppDataCreator baseAppDataCreator;

    @Mock
    private BackendIdProvider backendIdProviderMock;

    @Mock
    private Event requestEventMock;

    @Mock
    private Insight insightMock;

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

    private DataServicesManager mDataServicesManager;

    @Mock
    private DBDeletingInterface deletingInterfaceMock;
    @Mock
    private DBFetchingInterface fetchingInterfaceMock;
    @Mock
    private DBSavingInterface savingInterfaceMock;
    @Mock
    private DBUpdatingInterface updatingInterfaceMock;
    @Spy
    private Context mockContext;
    @Mock
    private ConsentDetail consentDetailMock;

    private UCoreAccessProvider uCoreAccessProvider;

    @Mock
    private BaseAppCore coreMock;

    @Mock
    private SynchronisationMonitor synchronisationMonitorMock;

    @Mock
    private ErrorHandlingInterface errorHandlingInterfaceMock;

    @Mock
    private DBRequestListener dbRequestListener;

    @Mock
    private Characteristics CharacteristicsMock;

    @Mock
    private DBFetchRequestListner dbFetchRequestListner;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    private BaseAppDataCreator dataCreatorMock;

    @Mock
    private SynchronisationCompleteListener synchronisationCompleteListener;

    @Mock
    private Settings settingsMock;

    private DSPaginationSpy mDSPagination;

    @Mock
    private SharedPreferences prefsMock;

    @Mock
    private SharedPreferences.Editor prefsEditorMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.setAppComponent(appComponantMock);

        baseAppDataCreator = new VerticalCreater();
        userRegistrationInterface = new VerticalUserRegistrationInterface();
        uCoreAccessProvider = new VerticalUCoreAccessProvider(userRegistrationInterface);
        mDSPagination = new DSPaginationSpy();
        mDataServicesManager.mEventing = eventingMock;
        mDataServicesManager.mDataCreater = baseAppDataCreator;
        mDataServicesManager.mBackendIdProvider = uCoreAccessProvider;
        mDataServicesManager.mCore = coreMock;
        mDataServicesManager.mSynchronisationMonitor = synchronisationMonitorMock;
        mDataServicesManager.userRegistrationInterface = userRegistrationInterface;
        mDataServicesManager.errorHandlingInterface = errorHandlingInterfaceMock;
        mDataServicesManager.mSynchronisationManager = synchronisationManagerMock;
        mDataServicesManager.mSynchronisationCompleteListener = synchronisationCompleteListenerMock;
        mDataServicesManager.gdprStorage = prefsMock;
        when(requestEventMock.getEventId()).thenReturn(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostSaveEvent_WhenSaveIsCalled() throws Exception {
        mDataServicesManager.saveMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateEvent_WhenUpdateIsCalled() throws Exception {
        mDataServicesManager.updateMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentUpdateRequest.class));
    }

    @Test
    public void ShouldPostFetchEvent_WhenFetchIsCalled() throws Exception {
        mDataServicesManager.fetchMomentWithType(dbFetchRequestListner, MomentType.TEMPERATURE);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchLatestMomentByType_WhenFetchIsCalled() throws Exception {
        mDataServicesManager.fetchLatestMomentByType(MomentType.TEMPERATURE, dbFetchRequestListner);
        verify(eventingMock).post(any(LoadLatestMomentByTypeRequest.class));
    }

    @Test
    public void ShouldPostFetchMomentByDateType_WhenFetchIsCalled() throws Exception {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date startDate = sdf.parse("10/11/17");
        Date endDate = sdf.parse("10/23/17");
        mDataServicesManager.fetchMomentsWithTypeAndTimeLine(MomentType.TEMPERATURE, startDate, endDate, createPagination(), dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsByDate.class));
    }

    @Test
    public void ShouldPostFetchMomentByDateRange_WhenFetchIsCalled() throws Exception {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date startDate = sdf.parse("10/11/17");
        Date endDate = sdf.parse("10/23/17");
        mDataServicesManager.fetchMomentsWithTimeLine(startDate, endDate, createPagination(), dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsByDate.class));
    }

    @Test
    public void ShouldPostFetchMomentByIdEvent_WhenFetchMomentByIdIsCalled() throws Exception {
        mDataServicesManager.fetchMomentForMomentID(1, dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchConsentEvent_WhenFetchConsentIsCalled() throws Exception {
        mDataServicesManager.fetchConsentDetail(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadConsentsRequest.class));
    }

    @Test
    public void ShouldPostFetchSettingsEvent_WhenFetchSettingsIsCalled() throws Exception {
        mDataServicesManager.fetchUserSettings(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadSettingsRequest.class));
    }

    @Test
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() throws Exception {
        mDataServicesManager.createConsentDetail(TEST_CONSENT_DETAIL_TYPE, ConsentDetailStatusType.ACCEPTED, ConsentDetail.DEFAULT_DOCUMENT_VERSION, "fsdfsdf");
    }

    @Test
    public void ShouldAddConcentDetail_WhenConsentIsNull() throws Exception {
        mDataServicesManager.createConsentDetail("Phase", ConsentDetailStatusType.ACCEPTED, "2", "fsdfsdf");
    }

    @Test
    public void ShouldPostSaveConsentEvent_WhenSaveConsentIsCalled() throws Exception {
        mDataServicesManager.saveConsentDetails(anyListOf(ConsentDetail.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseConsentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateSettingsEvent_WhenUpdateSettingsIsCalled() throws Exception {
        mDataServicesManager.updateUserSettings(any(Settings.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseSettingsUpdateRequest.class));
    }

    @Test
    public void ShouldPostUpdateCharacteristicsRequest_WhenUpdateCharacteristicsIsCalled() throws Exception {
        mDataServicesManager.updateUserCharacteristics(anyListOf(Characteristics.class), dbRequestListener);
    }

    @Test
    public void ShouldPostFetchCharacteristicsRequest_WhenFetchCharacteristicsIsCalled() throws Exception {
        mDataServicesManager.fetchUserCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostUpdateConsentEvent_WhenUpdateConsentIsCalled() throws Exception {
        mDataServicesManager.updateConsentDetails(anyListOf(ConsentDetail.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseConsentUpdateRequest.class));
    }

    @Test
    public void ShouldPostdeleteAllMomentEvent_WhendeleteAllMomentIsCalled() throws Exception {
        mDataServicesManager.deleteAllMoments(dbRequestListener);
        verify(eventingMock).post(any(DeleteAllMomentsRequest.class));
    }

    //TODO: Spoorti - revisit this
    @Test
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() throws Exception {
        mDataServicesManager.createMoment("jh");
    }

    @Test
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() throws Exception {
        mDataServicesManager.createMeasurementGroup(momentMock);
    }

    //TODO: Spoorti - revisit
    @Test
    public void ShouldAddMomentDetail_WhenCreateMomentDetailIsCreated() throws Exception {
        baseAppDataCreator.createMomentDetail(TEST_MEASUREMENT_DETAIL_TYPE, momentMock);
    }

    @Test
    public void ShouldStopCore_WhenStopCoreIsCalled() throws Exception {
        mDataServicesManager.stopCore();
    }

    @Test(expected = RuntimeException.class)
    public void ShouldinitializeSyncMonitors_WheninitializeSyncMonitorsIsCalled() throws Exception {
        mDataServicesManager.initializeSyncMonitors(null, new ArrayList<DataFetcher>(), new ArrayList<DataSender>(), synchronisationCompleteListener);
    }

    @Test
    public void ShouldCreateMeasurement_WhenqCreateMeasurementIsCalled() throws Exception {
        mDataServicesManager.createMeasurementGroup(measurementGroupMock);
    }

    @Test
    public void ShouldInitializeDBMonitors_WhenInitializeDBMonitorsIsCalled() throws Exception {
        mDataServicesManager.initializeDatabaseMonitor(null, deletingInterfaceMock, fetchingInterfaceMock, savingInterfaceMock, updatingInterfaceMock);
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailsIsCalled() throws Exception {
        mDataServicesManager.createUserCharacteristics("TYPE", "VALUE", mock(Characteristics.class));
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailIsNULL() throws Exception {
        mDataServicesManager.createUserCharacteristics("TYPE", "VALUE", null);
    }

    @Test
    public void Should_fetchAllMoment_called() throws Exception {
        mDataServicesManager.fetchAllMoment(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void Should_createUserSettings_called() throws Exception {
        Settings settings = mDataServicesManager.createUserSettings("en_us", "metric");
        assertThat(settings).isNotNull();
        assertThat(settings).isInstanceOf(Settings.class);
    }

    @Test
    public void Should_createsaveUserSettings_called() throws Exception {
        mDataServicesManager.saveUserSettings(settingsMock, dbRequestListener);
        verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
    }

    @Test
    public void Should_createMomentDetail_called() throws Exception {
        mDataServicesManager.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMomentDetail("Temperature", momentMock)).thenReturn(momentDetailMock);
        MomentDetail detail = mDataServicesManager.createMomentDetail("Temperature", "23", momentMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(detail).isInstanceOf(MomentDetail.class);
        assertThat(detail).isNotNull();
    }

    @Test
    public void Should_createMeasurement_called() throws Exception {
        mDataServicesManager.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurement("Temperature", measurementGroupMock)).thenReturn(measurementMock);
        Measurement measurement = mDataServicesManager.createMeasurement("Temperature", "23", "celcius", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurement).isInstanceOf(Measurement.class);
        assertThat(measurement).isNotNull();
    }

    @Test
    public void Should_createMeasurementDetail_called() throws Exception {
        mDataServicesManager.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurementDetail("Temperature", measurementMock)).thenReturn(measurementDetailMock);
        MeasurementDetail measurementDetail = mDataServicesManager.createMeasurementDetail("Temperature", "23", measurementMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementDetail).isInstanceOf(MeasurementDetail.class);
        assertThat(measurementDetail).isNotNull();
    }

    @Test
    public void Should_deleteMoment_called() throws Exception {
        mDataServicesManager.deleteMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentDeleteRequest.class));
    }

    @Test
    public void Should_deleteMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.deleteMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsDeleteRequest.class));
    }

    @Test
    public void Should_updateMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.updateMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsUpdateRequest.class));
    }

    @Test
    public void Should_deleteAll_called() throws Exception {
        mDataServicesManager.deleteAll(dbRequestListener);
        verify(eventingMock).post(any(DataClearRequest.class));
    }

    @Test
    public void Should_createMeasurementGroupDetail_called() throws Exception {
        mDataServicesManager.mDataCreater = dataCreatorMock;
        when(dataCreatorMock.createMeasurementGroupDetail("Temperature", measurementGroupMock)).thenReturn(measurementGroupDetailMock);
        MeasurementGroupDetail measurementGroupDetail = mDataServicesManager.createMeasurementGroupDetail("Temperature", "23", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementGroupDetail).isInstanceOf(MeasurementGroupDetail.class);
        assertThat(measurementGroupDetail).isNotNull();
    }

    @Test
    public void Should_saveUserCharacteristics_called() throws Exception {
        List list = new ArrayList();
        list.add(consentDetailMock);
        mDataServicesManager.saveUserCharacteristics(list, dbRequestListener);
        verify(eventingMock).post(any(UserCharacteristicsSaveRequest.class));
    }

    @Test
    public void Should_unRegisterDBChangeListener_called() throws Exception {
        mDataServicesManager.unRegisterDBChangeListener();
        DBChangeListener dbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        assertThat(dbChangeListener).isNull();
    }

    @Test
    public void Should_registerSynchronisationCompleteListener_called() throws Exception {
        mDataServicesManager.registerSynchronisationCompleteListener(synchronisationCompleteListenerMock);
        assertThat(DataServicesManager.getInstance().mSynchronisationCompleteListener).isInstanceOf(SynchronisationCompleteListener.class);
    }

    @Test
    public void Should_saveMoments_called() throws Exception {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.saveMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsSaveRequest.class));
    }

    @Test
    public void Should_unRegisterSynchronisationCosmpleteListener_called() throws Exception {
        mDataServicesManager.unRegisterSynchronisationCosmpleteListener();
        assertThat(DataServicesManager.getInstance().mSynchronisationCompleteListener).isNull();
    }

    @Test
    public void Should_fetchInsights_called() throws Exception {
        mDataServicesManager.fetchInsights(dbFetchRequestListner);
        verify(eventingMock).post(any(FetchInsightsFromDB.class));
    }

    @Test
    public void Should_deleteInsights_called() throws Exception {
        List list = new ArrayList();
        list.add(insightMock);
        mDataServicesManager.deleteInsights(list, dbRequestListener);
        verify(eventingMock).post(any(DeleteInsightFromDB.class));
    }

    @Test
    public void Should_ClearExpiredMoments_called() {
        mDataServicesManager.clearExpiredMoments(dbRequestListener);
        verify(eventingMock).post(any(DeleteExpiredMomentRequest.class));
    }

    //Push Notification test
    @Test
    public void unRegisterDeviceTokenTest() throws Exception {
        mDataServicesManager.unRegisterDeviceToken("token", "variant", null);
        verify(eventingMock).post(any(UnRegisterDeviceToken.class));
    }

    @Test
    public void registerDeviceTokenTest() throws Exception {
        mDataServicesManager.registerDeviceToken("token", "variant", "protocol provider", null);
        verify(eventingMock).post(any(RegisterDeviceToken.class));
    }

    @Test
    public void handlePushNotificationPayloadTest() throws Exception {
        mDataServicesManager.handlePushNotificationPayload(jsonObject);
    }

    //Subject Profile Test
    @Test
    public void createSubjectProfileTest() throws Exception {
        mDataServicesManager.createSubjectProfile("test user", "2013-05-05", "female", 78.88, "2015-10-01T12:11:10.123+0100", null);
        verify(eventingMock).post(any(CreateSubjectProfileRequestEvent.class));
    }

    @Test
    public void getSubjectProfilesTest() throws Exception {
        mDataServicesManager.getSubjectProfiles(null);
        verify(eventingMock).post(any(GetSubjectProfileListRequestEvent.class));
    }

    @Test
    public void getSubjectProfileTest() throws Exception {
        mDataServicesManager.getSubjectProfile("39989890000898989", null);
        verify(eventingMock).post(any(GetSubjectProfileRequestEvent.class));
    }

    @Test
    public void deleteSubjectProfileTest() throws Exception {
        mDataServicesManager.deleteSubjectProfile("78798089987868789", null);
        verify(eventingMock).post(any(DeleteSubjectProfileRequestEvent.class));
    }

    //Device Pairing test
    @Test
    public void pairDevicesTest() throws Exception {
        mDataServicesManager.pairDevices("77908787878978", "RefNode", null, null, "rxd", null);
        verify(eventingMock).post(any(PairDevicesRequestEvent.class));
    }

    @Test
    public void unPairDeviceTest() throws Exception {
        mDataServicesManager.unPairDevice("7867697879787", null);
        verify(eventingMock).post(any(UnPairDeviceRequestEvent.class));
    }

    @Test
    public void getPairedDevicesTest() throws Exception {
        mDataServicesManager.getPairedDevices(null);
        verify(eventingMock).post(any(GetPairedDeviceRequestEvent.class));
    }

    private DSPaginationSpy createPagination() {
        mDSPagination.setOrdering(DSPagination.DSPaginationOrdering.DESCENDING);
        mDSPagination.setPageLimit(1);
        mDSPagination.setPageNumber(1);
        mDSPagination.setOrderBy("timestamp");
        return mDSPagination;
    }

    @Test
    public void synchronize_deletesExpiredMoments() {
        mDataServicesManager.synchronize();
        verify(eventingMock).post(any(DeleteExpiredMomentRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void postException_WhenServiceDiscoveryInterfaceIsNull() {
        givenNullServiceDiscoveryInterface();
        whenFetchBaseUrlIsInvoked();
        whenFetchCoachingServiceUrlIsInvoked();
    }

    @Test
    public void synchronize() {
        whenSynchronizeIsInvoked();
        thenVerifyMonitorsAreInitialized();
        thenVerifySynchronisationManagerForSynchronizeIsCalled();
    }

    @Test
    public void synchronizeMomentsByDateRange() {
        whenSynchronizeMomentsByDateRange();
        thenVerifyMonitorsAreInitialized();
        thenVerifySynchronisationManagerIsCalled();
    }

    @Test
    public void deleteSyncedMoments_noResultListener() {
        mDataServicesManager.deleteSyncedMoments(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    public void deleteSyncedMoments_withResultListener() {
        //
    }

    @Test
    public void migrateGDPR_noResultListener() {
        mDataServicesManager.migrateGDPR(null);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener() {
        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagNotSet() {
        when(prefsMock.getBoolean("gdpr_migration_flag", false)).thenReturn(false); // Returns default

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagSetTrue() {
        when(prefsMock.getBoolean("gdpr_migration_flag", false)).thenReturn(true);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(eventingMock, never()).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagNotChanged() {
        when(prefsMock.getBoolean("gdpr_migration_flag", false)).thenReturn(true);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(prefsMock, never()).edit();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_withResultListener_migrationFlagSetFalse() {
        when(prefsMock.getBoolean("gdpr_migration_flag", false)).thenReturn(false); // Set to false

        mDataServicesManager.deleteSyncedMoments(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagNotSet_shouldSetMigrationFlag() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DeleteSyncedMomentsRequest arg0 = (DeleteSyncedMomentsRequest) invocation.getArguments()[0];
                DBRequestListener<Moment> listener = arg0.getDbRequestListener();

                listener.onSuccess(null);
                return null;
            }
        }).when(eventingMock).post((Event) any());
        when(prefsMock.getBoolean(eq("gdpr_migration_flag"), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(prefsEditorMock).putBoolean(eq("gdpr_migration_flag"), eq(true));
        verify(prefsEditorMock).apply();
    }

    private void whenSynchronizeIsInvoked() {
        mDataServicesManager.synchronize();
    }

    private void whenSynchronizeMomentsByDateRange() {
        mDataServicesManager.synchronizeMomentsByDateRange(START_DATE, END_DATE, synchronisationCompleteListenerMock);
    }

    private void thenVerifyMonitorsAreInitialized() {
        verify(coreMock).start();
        verify(synchronisationMonitorMock).start(eventingMock);
    }

    private void thenVerifySynchronisationManagerForSynchronizeIsCalled() {
        verify(synchronisationManagerMock).startSync(synchronisationCompleteListenerMock);
    }

    private void thenVerifySynchronisationManagerIsCalled() {
        verify(synchronisationManagerMock).startSync(START_DATE, END_DATE, synchronisationCompleteListenerMock);
    }

    private void givenNullServiceDiscoveryInterface() {
        mDataServicesManager.setServiceDiscoveryInterface(null);
    }

    private void whenFetchBaseUrlIsInvoked() {
        mDataServicesManager.fetchBaseUrlFromServiceDiscovery();
    }

    private void whenFetchCoachingServiceUrlIsInvoked() {
        mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery();
    }
}

