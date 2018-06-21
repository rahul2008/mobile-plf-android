package com.philips.platform.core.trackers;

import android.content.SharedPreferences;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnGetServiceUrlListener;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
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
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.DeleteAllInsights;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredInsightRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteSubjectProfileRequestEvent;
import com.philips.platform.core.events.DeleteSyncedMomentsRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.GetPairedDeviceRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileRequestEvent;
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
import com.philips.platform.verticals.VerticalUserRegistrationInterface;
import com.philips.spy.DSPaginationSpy;
import com.philips.testing.verticals.datatyes.ConsentDetailStatusType;
import com.philips.testing.verticals.datatyes.MomentType;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK;
import static com.philips.platform.core.utils.DataServicesConstants.BASE_URL_KEY;
import static com.philips.platform.core.utils.DataServicesConstants.COACHING_SERVICE_URL_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(RobolectricTestRunner.class)
public class DataServicesManagerTest {
    private static final DateTime START_DATE = new DateTime();
    private static final DateTime END_DATE = new DateTime();

    private static final int TEST_REFERENCE_ID = 111;
    public static final String TEST_USER_ID = "TEST_USER_ID";
    private static final String TEST_CONSENT_DETAIL_TYPE = "TEMPERATURE";
    private static final String TEST_MEASUREMENT_DETAIL_TYPE = "BOTTLE_CONTENTS";
    private final String GDPR_MIGRATION_FLAG = "gdpr_migration_flag";

    private DataServicesManager mDataServicesManager;

    @Mock
    private SynchronisationCompleteListener synchronisationCompleteListenerMock;
    @Mock
    private Eventing eventingMock;
    @Mock
    private JSONObject jsonObject;
    @Mock
    private SynchronisationManager synchronisationManagerMock;

    private UserRegistrationInterface userRegistrationInterface;
    private BaseAppDataCreator baseAppDataCreator;

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
    @Mock
    private DBDeletingInterface deletingInterfaceMock;
    @Mock
    private DBFetchingInterface fetchingInterfaceMock;
    @Mock
    private DBSavingInterface savingInterfaceMock;
    @Mock
    private DBUpdatingInterface updatingInterfaceMock;
    @Mock
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
    private DBFetchRequestListner dbFetchRequestListner;
    @Mock
    private AppComponent appComponantMock;
    @Mock
    private BaseAppDataCreator dataCreatorMock;
    @Mock
    private SynchronisationCompleteListener synchronisationCompleteListener;
    @Mock
    private Settings settingsMock;
    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;
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

        // Since the DataServiceManager is a singleton, we need to clear the
        // cached ServiceUrl values forcefully to get to clean state for testing
        mDataServicesManager.mDataServicesBaseUrl = null;
        mDataServicesManager.mDataServicesCoachingServiceUrl = null;

        baseAppDataCreator = new VerticalCreater();
        userRegistrationInterface = new VerticalUserRegistrationInterface();
        mDSPagination = new DSPaginationSpy();
        mDataServicesManager.mEventing = eventingMock;
        mDataServicesManager.dataCreator = baseAppDataCreator;
        mDataServicesManager.mBackendIdProvider = uCoreAccessProvider;
        mDataServicesManager.mCore = coreMock;
        mDataServicesManager.mSynchronisationMonitor = synchronisationMonitorMock;
        mDataServicesManager.userRegistrationInterface = userRegistrationInterface;
        mDataServicesManager.errorHandlingInterface = errorHandlingInterfaceMock;
        mDataServicesManager.mSynchronisationManager = synchronisationManagerMock;
        mDataServicesManager.mSynchronisationCompleteListener = synchronisationCompleteListenerMock;
        mDataServicesManager.gdprStorage = prefsMock;
        mDataServicesManager.mBackendIdProvider = uCoreAccessProvider;
        mDataServicesManager.setServiceDiscoveryInterface(serviceDiscoveryInterfaceMock);
        when(requestEventMock.getEventId()).thenReturn(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostSaveEvent_WhenSaveIsCalled() {
        mDataServicesManager.saveMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateEvent_WhenUpdateIsCalled() {
        mDataServicesManager.updateMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentUpdateRequest.class));
    }

    @Test
    public void ShouldPostFetchEvent_WhenFetchIsCalled() {
        mDataServicesManager.fetchMomentWithType(dbFetchRequestListner, MomentType.TEMPERATURE);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchLatestMomentByType_WhenFetchIsCalled() {
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
    public void ShouldPostFetchMomentByIdEvent_WhenFetchMomentByIdIsCalled() {
        mDataServicesManager.fetchMomentForMomentID(1, dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchSettingsEvent_WhenFetchSettingsIsCalled() {
        mDataServicesManager.fetchUserSettings(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadSettingsRequest.class));
    }

    @Test
    public void ShouldPostUpdateSettingsEvent_WhenUpdateSettingsIsCalled() {
        mDataServicesManager.updateUserSettings(any(Settings.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseSettingsUpdateRequest.class));
    }

    @Test
    public void ShouldPostUpdateCharacteristicsRequest_WhenUpdateCharacteristicsIsCalled() {
        mDataServicesManager.updateUserCharacteristics(ArgumentMatchers.<Characteristics>anyList(), dbRequestListener);
    }

    @Test
    public void ShouldPostFetchCharacteristicsRequest_WhenFetchCharacteristicsIsCalled() {
        mDataServicesManager.fetchUserCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostdeleteAllMomentEvent_WhendeleteAllMomentIsCalled() {
        mDataServicesManager.deleteAllMoments(dbRequestListener);
        verify(eventingMock).post(any(DeleteAllMomentsRequest.class));
    }

    //TODO: Spoorti - revisit this
    @Test
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() {
        mDataServicesManager.createMoment("jh");
    }

    @Test
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() {
        mDataServicesManager.createMeasurementGroup(momentMock);
    }

    //TODO: Spoorti - revisit
    @Test
    public void ShouldAddMomentDetail_WhenCreateMomentDetailIsCreated() {
        baseAppDataCreator.createMomentDetail(TEST_MEASUREMENT_DETAIL_TYPE, momentMock);
    }

    @Test
    public void ShouldStopCore_WhenStopCoreIsCalled() {
        mDataServicesManager.stopCore();
    }

    @Test(expected = RuntimeException.class)
    public void ShouldinitializeSyncMonitors_WheninitializeSyncMonitorsIsCalled() {
        mDataServicesManager.initializeSyncMonitors(null, new ArrayList<DataFetcher>(), new ArrayList<DataSender>(), synchronisationCompleteListener);
    }

    @Test
    public void ShouldCreateMeasurement_WhenqCreateMeasurementIsCalled() {
        mDataServicesManager.createMeasurementGroup(measurementGroupMock);
    }

    @Test
    public void ShouldInitializeDBMonitors_WhenInitializeDBMonitorsIsCalled() {
        mDataServicesManager.initializeDatabaseMonitor(null, deletingInterfaceMock, fetchingInterfaceMock, savingInterfaceMock, updatingInterfaceMock);
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailsIsCalled() {
        mDataServicesManager.createUserCharacteristics("TYPE", "VALUE", mock(Characteristics.class));
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailIsNULL() {
        mDataServicesManager.createUserCharacteristics("TYPE", "VALUE", null);
    }

    @Test
    public void Should_fetchAllMoment_called() {
        mDataServicesManager.fetchAllMoment(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void Should_createUserSettings_called() {
        Settings settings = mDataServicesManager.createUserSettings("en_us", "metric", null);
        assertThat(settings).isNotNull();
        assertThat(settings).isInstanceOf(Settings.class);
    }

    @Test
    public void Should_createsaveUserSettings_called() {
        mDataServicesManager.saveUserSettings(settingsMock, dbRequestListener);
        verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
    }

    @Test
    public void Should_createMomentDetail_called() {
        mDataServicesManager.dataCreator = dataCreatorMock;
        when(dataCreatorMock.createMomentDetail("Temperature", momentMock)).thenReturn(momentDetailMock);
        MomentDetail detail = mDataServicesManager.createMomentDetail("Temperature", "23", momentMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(detail).isInstanceOf(MomentDetail.class);
        assertThat(detail).isNotNull();
    }

    @Test
    public void Should_createMeasurement_called() {
        mDataServicesManager.dataCreator = dataCreatorMock;
        when(dataCreatorMock.createMeasurement("Temperature", measurementGroupMock)).thenReturn(measurementMock);
        Measurement measurement = mDataServicesManager.createMeasurement("Temperature", "23", "celcius", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurement).isInstanceOf(Measurement.class);
        assertThat(measurement).isNotNull();
    }

    @Test
    public void Should_createMeasurementDetail_called() {
        mDataServicesManager.dataCreator = dataCreatorMock;
        when(dataCreatorMock.createMeasurementDetail("Temperature", measurementMock)).thenReturn(measurementDetailMock);
        MeasurementDetail measurementDetail = mDataServicesManager.createMeasurementDetail("Temperature", "23", measurementMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementDetail).isInstanceOf(MeasurementDetail.class);
        assertThat(measurementDetail).isNotNull();
    }

    @Test
    public void Should_deleteMoment_called() {
        mDataServicesManager.deleteMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentDeleteRequest.class));
    }

    @Test
    public void Should_deleteMoments_called() {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.deleteMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsDeleteRequest.class));
    }

    @Test
    public void Should_updateMoments_called() {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.updateMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsUpdateRequest.class));
    }

    @Test
    public void Should_deleteAll_called() {
        mDataServicesManager.deleteAll(dbRequestListener);
        verify(eventingMock).post(any(DataClearRequest.class));
    }

    @Test
    public void Should_createMeasurementGroupDetail_called() {
        mDataServicesManager.dataCreator = dataCreatorMock;
        when(dataCreatorMock.createMeasurementGroupDetail("Temperature", measurementGroupMock)).thenReturn(measurementGroupDetailMock);
        MeasurementGroupDetail measurementGroupDetail = mDataServicesManager.createMeasurementGroupDetail("Temperature", "23", measurementGroupMock);
        //verify(eventingMock).post(any(DatabaseSettingsSaveRequest.class));
        assertThat(measurementGroupDetail).isInstanceOf(MeasurementGroupDetail.class);
        assertThat(measurementGroupDetail).isNotNull();
    }

    @Test
    public void Should_unRegisterDBChangeListener_called() {
        mDataServicesManager.unRegisterDBChangeListener();
        DBChangeListener dbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        assertThat(dbChangeListener).isNull();
    }

    @Test
    public void Should_registerSynchronisationCompleteListener_called() {
        mDataServicesManager.registerSynchronisationCompleteListener(synchronisationCompleteListenerMock);
        assertThat(mDataServicesManager.mSynchronisationCompleteListener).isInstanceOf(SynchronisationCompleteListener.class);
    }

    @Test
    public void Should_saveMoments_called() {
        List list = new ArrayList();
        list.add(momentMock);
        mDataServicesManager.saveMoments(list, dbRequestListener);
        verify(eventingMock).post(any(MomentsSaveRequest.class));
    }

    @Test
    public void Should_unRegisterSynchronisationCosmpleteListener_called() {
        mDataServicesManager.unRegisterSynchronisationCosmpleteListener();
        assertThat(mDataServicesManager.mSynchronisationCompleteListener).isNull();
    }

    @Test
    public void Should_fetchInsights_called() {
        mDataServicesManager.fetchInsights(dbFetchRequestListner);
        verify(eventingMock).post(any(FetchInsightsFromDB.class));
    }

    @Test
    public void Should_deleteInsights_called() {
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
    public void unRegisterDeviceTokenTest() {
        mDataServicesManager.unRegisterDeviceToken("token", "variant", null);
        verify(eventingMock).post(any(UnRegisterDeviceToken.class));
    }

    @Test
    public void registerDeviceTokenTest() {
        mDataServicesManager.registerDeviceToken("token", "variant", "protocol provider", null);
        verify(eventingMock).post(any(RegisterDeviceToken.class));
    }

    @Test
    public void handlePushNotificationPayloadTest() throws Exception {
        mDataServicesManager.handlePushNotificationPayload(jsonObject);
    }

    //Subject Profile Test
    @Test
    public void createSubjectProfileTest() {
        mDataServicesManager.createSubjectProfile("test user", "2013-05-05", "female", 78.88, "2015-10-01T12:11:10.123+0100", null);
        verify(eventingMock).post(any(CreateSubjectProfileRequestEvent.class));
    }

    @Test
    public void getSubjectProfilesTest() {
        mDataServicesManager.getSubjectProfiles(null);
        verify(eventingMock).post(any(GetSubjectProfileListRequestEvent.class));
    }

    @Test
    public void getSubjectProfileTest() {
        mDataServicesManager.getSubjectProfile("39989890000898989", null);
        verify(eventingMock).post(any(GetSubjectProfileRequestEvent.class));
    }

    @Test
    public void deleteSubjectProfileTest() {
        mDataServicesManager.deleteSubjectProfile("78798089987868789", null);
        verify(eventingMock).post(any(DeleteSubjectProfileRequestEvent.class));
    }

    //Device Pairing test
    @Test
    public void pairDevicesTest() {
        mDataServicesManager.pairDevices("77908787878978", "RefNode", null, null, "rxd", null);
        verify(eventingMock).post(any(PairDevicesRequestEvent.class));
    }

    @Test
    public void unPairDeviceTest() {
        mDataServicesManager.unPairDevice("7867697879787", null);
        verify(eventingMock).post(any(UnPairDeviceRequestEvent.class));
    }

    @Test
    public void getPairedDevicesTest() {
        mDataServicesManager.getPairedDevices(null);
        verify(eventingMock).post(any(GetPairedDeviceRequestEvent.class));
    }

    @Test
    public void synchronize_deletesExpiredMoments() {
        mDataServicesManager.synchronize();
        verify(eventingMock).post(any(DeleteExpiredMomentRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void fetchBaseUrlThrowsExceptionWhenServiceDiscoveryInterfaceIsNull() {
        givenNullServiceDiscoveryInterface();
        whenFetchBaseUrlIsInvoked();
    }

    public void fetchBaseUrlReturnsNullWhenServiceDiscoveryEncounterError() {
        givenServiceDiscoveryReturnsError("Intentional error");
        assertThat(mDataServicesManager.fetchBaseUrlFromServiceDiscovery()).isNull();
    }

    @Test
    public void fetchBaseUrlReportsErrorWhenServiceDiscoveryEncounterError() {
        givenServiceDiscoveryReturnsError("Intentional error");
        whenFetchBaseUrlIsInvoked();
        verify(errorHandlingInterfaceMock).onServiceDiscoveryError("Intentional error");
    }

    @Test
    public void fetchBaseUrlReturnsServiceDiscoveryBaseUrl() {
        givenServiceDiscoveryReturnsServiceUrl("http://api.example.com");
        assertThat(mDataServicesManager.fetchBaseUrlFromServiceDiscovery()).isEqualTo("http://api.example.com");
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(BASE_URL_KEY), any(OnGetServiceUrlListener.class));
    }

    @Test
    public void fetchBaseUrlReturnsCachedServiceDiscoveryBaseUrl() {
        givenServiceDiscoveryReturnsServiceUrl("http://api.example.com");
        whenFetchBaseUrlIsInvokedTwice();
        thenGetServiceUrlWithCountryPreferenceIsCalledOnlyOnce();
    }

    @Test(expected = RuntimeException.class)
    public void fetchCoachingServiceUrlThrowsExceptionWhenServiceDiscoveryInterfaceIsNull() {
        givenNullServiceDiscoveryInterface();
        whenFetchCoachingServiceUrlIsInvoked();
    }

    public void fetchCoachingServiceUrlReturnsNullWhenServiceDiscoveryEncounterError() {
        givenServiceDiscoveryReturnsError("Intentional error");
        assertThat(mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery()).isNull();
    }

    @Test
    public void fetchCoachingServiceUrlReportsErrorWhenServiceDiscoveryEncounterError() {
        givenServiceDiscoveryReturnsError("Intentional error");
        whenFetchCoachingServiceUrlIsInvoked();
        verify(errorHandlingInterfaceMock).onServiceDiscoveryError("Intentional error");
    }

    @Test
    public void fetchCoachingServiceUrlReturnsServiceDiscoveryCoachingServiceUrl() {
        givenServiceDiscoveryReturnsServiceUrl("http://api.example.com");
        assertThat(mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery()).isEqualTo("http://api.example.com");
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(COACHING_SERVICE_URL_KEY), any(OnGetServiceUrlListener.class));
    }

    @Test
    public void fetchCoachingServiceUrlReturnsCachedServiceDiscoveryCoachingServiceUrl() {
        givenServiceDiscoveryReturnsServiceUrl("http://api.example.com");
        whenFetchCoachingServiceUrlIsInvokedTwice();
        thenGetServiceUrlWithCountryPreferenceIsCalledOnlyOnce();
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
        mDataServicesManager.deleteSyncedMoments(null);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_withResultListener() {
        mDataServicesManager.deleteSyncedMoments(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
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
        when(prefsMock.getBoolean(GDPR_MIGRATION_FLAG, false)).thenReturn(false); // Returns default

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagSetTrue() {
        when(prefsMock.getBoolean(GDPR_MIGRATION_FLAG, false)).thenReturn(true);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(eventingMock, never()).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagNotChanged() {
        when(prefsMock.getBoolean(GDPR_MIGRATION_FLAG, false)).thenReturn(true);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(prefsMock, never()).edit();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteSyncedMoments_withResultListener_migrationFlagSetFalse() {
        when(prefsMock.getBoolean(GDPR_MIGRATION_FLAG, false)).thenReturn(false); // Set to false

        mDataServicesManager.deleteSyncedMoments(dbRequestListener);

        verify(eventingMock).post((DeleteSyncedMomentsRequest) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_migrationFlagNotSet_shouldSetMigrationFlag() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(prefsEditorMock).putBoolean(eq(GDPR_MIGRATION_FLAG), eq(true));
        verify(prefsEditorMock).apply();
    }

    @Test
    public void migrateGDPR_withResultListener_cannotDeleteMoments_shouldCallFailureCallback() {
        givenFailureDeleteSyncedMomentRequest();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onFailure((Exception) any());
    }

    @Test
    public void migrateGDPR_withResultListener_shouldClearSyncTimeCache() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(uCoreAccessProvider).clearSyncTimeCache();
    }

    @Test
    public void migrateGDPR_withResultListener_shouldStartSync() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(synchronisationManagerMock).startSync(any(SynchronisationCompleteListener.class));
    }

    @Test
    public void migrateGDPR_withResultListener_shouldSetLastExpiredDeletionDateTimeToTheBeginningOfTime() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false);
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(synchronisationManagerMock).resetLastExpirationDeletionDateTime();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_shouldCallbackWhenDone() {
        givenHandlerExecutesImmediately();
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSyncCompletedOnStartSync();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onSuccess(anyList());
    }

    @Test
    public void migrateGDPR_withResultListener_shouldCallFailureCallback_whenFailed() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSyncFailedOnStartSync();
        givenSuccessfulDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onFailure((Exception) any());
    }

    @Test
    public void deleteAllInsights_noResultListener() {
        mDataServicesManager.deleteAllInsights(null);

        verify(eventingMock).post((Event) any());
    }

    @Test
    public void deleteAllInsights_withResultListener() {
        mDataServicesManager.deleteAllInsights(dbRequestListener);

        verify(eventingMock).post((Event) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_shouldCallFailureCallback_whenFailedDeletingAllInsights() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenFailedDeleteAllInsights();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);
        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onFailure((Exception) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_withResultListener_shouldCallDeleteAllInsights() {
        givenSuccessfulDeleteSyncedMomentsRequest();
        givenSuccessfulDeleteAllInsights();
        givenSyncCompletedOnStartSync();
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(false); // Returns default
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);
        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onSuccess(anyList());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void migrateGDPR_shouldStartSync_whenMigrationFlagIsTrue() {
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(true);
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.migrateGDPR(dbRequestListener);

        verify(dbRequestListener).onSuccess(anyList());
    }

    @Test
    public void resetMigrationFlag_shouldSetGDPRMigrationFlag() {
        when(prefsMock.getBoolean(eq(GDPR_MIGRATION_FLAG), eq(false))).thenReturn(true);
        when(prefsMock.edit()).thenReturn(prefsEditorMock);
        when(prefsEditorMock.putBoolean(anyString(), anyBoolean())).thenReturn(prefsEditorMock);

        mDataServicesManager.resetGDPRMigrationFlag();

        verify(prefsEditorMock).putBoolean(eq(GDPR_MIGRATION_FLAG), eq(false));
        verify(prefsEditorMock).apply();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void clearExpiredInsights_shouldCallPost_whenMethodCalled() {
        mDataServicesManager.clearExpiredInsights(dbRequestListener);

        verify(eventingMock).post((DeleteExpiredInsightRequest) any());
    }

    @Test
    public void clearExpiredInsights_shouldCallPost_whenMethodCalledWithNullParam() {
        mDataServicesManager.clearExpiredInsights(null);

        verify(eventingMock).post((DeleteExpiredInsightRequest) any());
    }

    @Test
    public void givenManagerExist_whenClearLastSyncTimeCache_thenShouldCallAccessProvider() {
        mDataServicesManager.clearLastSyncTimeCache();

        verify(uCoreAccessProvider).clearSyncTimeCache();
    }

    @Test
    public void settingLastSyncTime() {
        final String expectedString = "2017-01-01T12:05:40.000Z";
        DateTime expectedLastSyncTime = DateTime.parse(expectedString);
        mDataServicesManager.resetLastSyncTimestampTo(expectedLastSyncTime);

        verify(uCoreAccessProvider).saveLastSyncTime(eq(expectedString), eq(UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY));
        verify(uCoreAccessProvider).saveLastSyncTime(eq(expectedString), eq(UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY));
    }

    private void givenFailedDeleteAllInsights() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DeleteAllInsights deleteAllInsights = (DeleteAllInsights) invocation.getArguments()[0];
                DBRequestListener<Insight> listener = deleteAllInsights.getDbRequestListener();

                listener.onFailure(new Exception());
                return null;
            }
        }).when(eventingMock).post(any(DeleteAllInsights.class));
    }

    private void givenSyncFailedOnStartSync() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                SynchronisationCompleteListener arg0 = (SynchronisationCompleteListener) invocation.getArguments()[0];

                arg0.onSyncFailed(new Exception());
                return null;
            }
        }).when(synchronisationManagerMock).startSync((SynchronisationCompleteListener) any());
    }

    private void givenSuccessfulDeleteSyncedMomentsRequest() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DeleteSyncedMomentsRequest arg0 = (DeleteSyncedMomentsRequest) invocation.getArguments()[0];
                DBRequestListener<Moment> listener = arg0.getDbRequestListener();

                listener.onSuccess(null);
                return null;
            }
        }).when(eventingMock).post(any(DeleteSyncedMomentsRequest.class));
    }

    private void givenSuccessfulDeleteAllInsights() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DeleteAllInsights deleteAllInsights = (DeleteAllInsights) invocation.getArguments()[0];
                DBRequestListener<Insight> listener = deleteAllInsights.getDbRequestListener();

                listener.onSuccess(null);
                return null;
            }
        }).when(eventingMock).post(any(DeleteAllInsights.class));
    }

    private void givenFailureDeleteSyncedMomentRequest() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DeleteSyncedMomentsRequest arg0 = (DeleteSyncedMomentsRequest) invocation.getArguments()[0];
                DBRequestListener<Moment> listener = arg0.getDbRequestListener();

                listener.onFailure(new Exception());
                return null;
            }
        }).when(eventingMock).post((Event) any());
    }

    private void givenNullServiceDiscoveryInterface() {
        mDataServicesManager.setServiceDiscoveryInterface(null);
    }

    private void givenServiceDiscoveryReturnsError(final String error) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                final OnGetServiceUrlListener callback = invocation.getArgument(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(NO_NETWORK, error);
                    }
                }).start();
                return null;
            }
        }).when(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(anyString(), any(OnGetServiceUrlListener.class));
    }

    private void givenServiceDiscoveryReturnsServiceUrl(final String serviceUrl) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                final OnGetServiceUrlListener callback = invocation.getArgument(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(safeUrl(serviceUrl));
                    }
                }).start();
                return null;
            }
        }).when(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(anyString(), any(OnGetServiceUrlListener.class));
    }

    private URL safeUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private void givenSyncCompletedOnStartSync() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                SynchronisationCompleteListener arg0 = (SynchronisationCompleteListener) invocation.getArguments()[0];

                arg0.onSyncComplete();
                return null;
            }
        }).when(synchronisationManagerMock).startSync((SynchronisationCompleteListener) any());
    }

    private void givenHandlerExecutesImmediately() {
        ShadowLooper.runUiThreadTasks();
    }

    private void whenSynchronizeIsInvoked() {
        mDataServicesManager.synchronize();
    }

    private void whenSynchronizeMomentsByDateRange() {
        mDataServicesManager.synchronizeMomentsByDateRange(START_DATE, END_DATE, synchronisationCompleteListenerMock);
    }

    private void whenFetchBaseUrlIsInvoked() {
        mDataServicesManager.fetchBaseUrlFromServiceDiscovery();
    }

    private void whenFetchBaseUrlIsInvokedTwice() {
        mDataServicesManager.fetchBaseUrlFromServiceDiscovery();
        mDataServicesManager.fetchBaseUrlFromServiceDiscovery();
    }

    private void thenGetServiceUrlWithCountryPreferenceIsCalledOnlyOnce() {
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(anyString(), any(OnGetServiceUrlListener.class));
    }

    private void whenFetchCoachingServiceUrlIsInvoked() {
        mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery();
    }

    private void whenFetchCoachingServiceUrlIsInvokedTwice() {
        mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery();
        mDataServicesManager.fetchCoachingServiceUrlFromServiceDiscovery();
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

    private DSPaginationSpy createPagination() {
        mDSPagination.setOrdering(DSPagination.DSPaginationOrdering.DESCENDING);
        mDSPagination.setPageLimit(1);
        mDSPagination.setPageNumber(1);
        mDSPagination.setOrderBy("timestamp");
        return mDSPagination;
    }
}

