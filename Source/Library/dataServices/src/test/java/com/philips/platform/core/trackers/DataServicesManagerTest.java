package com.philips.platform.core.trackers;

import android.content.Context;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.injection.AppComponent;
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
import com.philips.testing.verticals.datatyes.MomentType;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    DataFetcher dataFetcherMock;
    @Mock
    private Eventing eventingMock;

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
    private Moment momentMock;

    @Mock
    private MomentDetail momentDetailMock;

    @Mock
    private MeasurementGroup measurementGroupMock;
    @Mock
    private Measurement measurementMock;

    @Mock
    private MeasurementDetail measurementDetailMock;

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
    DBFetchRequestListner dbFetchRequestListner;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    SynchronisationCompleteListener synchronisationCompleteListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tracker = DataServicesManager.getInstance();
        tracker.setAppComponant(appComponantMock);

        baseAppDataCreator = new VerticalCreater();
        userRegistrationInterface = new VerticalUserRegistrationInterface();
        uCoreAccessProvider = new VerticalUCoreAccessProvider(userRegistrationInterface);

        tracker.mEventing = eventingMock;
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
        verify(eventingMock).post(any(MomentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateEvent_WhenUpdateIsCalled() throws Exception {
        tracker.updateMoment(momentMock, dbRequestListener);
        verify(eventingMock).post(any(MomentUpdateRequest.class));
    }

    @Test
    public void ShouldPostFetchEvent_WhenFetchIsCalled() throws Exception {
        tracker.fetchMomentWithType(dbFetchRequestListner, MomentType.TEMPERATURE);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchMomentByIdEvent_WhenFetchMomentByIdIsCalled() throws Exception {
        tracker.fetchMomentForMomentID(1, dbFetchRequestListner);
        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchConsentEvent_WhenFetchConsentIsCalled() throws Exception {
        tracker.fetchConsentDetail(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadConsentsRequest.class));
    }

    @Test
    public void ShouldPostFetchSettingsEvent_WhenFetchSettingsIsCalled() throws Exception {
        tracker.fetchUserSettings(dbFetchRequestListner);
        verify(eventingMock).post(any(LoadSettingsRequest.class));
    }

    @Test
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() throws Exception {
        tracker.createConsentDetail(TEST_CONSENT_DETAIL_TYPE, ConsentDetailStatusType.ACCEPTED, ConsentDetail.DEFAULT_DOCUMENT_VERSION, "fsdfsdf");
    }

    //TODO: Spoorti - Fix later
/*    @Test(expected = NullPointerException.class)
    public void ShouldAddConcentDetail_WhenConsentDetailIsCreated() throws Exception {
       // tracker.initializeDataServices(null, null, null,null);
        ConsentDetail consentDetail = baseAppDataCreator.createConsentDetail("TEMPERATURE", TEST_CONSENT_DETAIL_TYPE, "", "fsdfsdf", true, consentMock);
        verify(consentMock).addConsentDetails(consentDetail);
    }*/

    //TODO: Spoorti -- Fix it later
    /*@Test
    public void ShouldAddConcentDetail_WhenConsentIsNull() throws Exception {
        tracker.createConsentDetail(null, TEST_CONSENT_DETAIL_TYPE, ConsentDetailStatusType.ACCEPTED, "fsdfsdf");
    }*/

    @Test
    public void ShouldPostSaveConsentEvent_WhenSaveConsentIsCalled() throws Exception {
        tracker.saveConsentDetails(anyListOf(ConsentDetail.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseConsentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateSettingsEvent_WhenUpdateSettingsIsCalled() throws Exception {
        tracker.updateUserSettings(any(Settings.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseSettingsUpdateRequest.class));
    }

    @Test
    public void ShouldPostUpdateCharacteristicsRequest_WhenUpdateCharacteristicsIsCalled() throws Exception {
        tracker.updateUserCharacteristics(anyListOf(Characteristics.class), dbRequestListener);
    }

    @Test
    public void ShouldPostFetchCharacteristicsRequest_WhenFetchCharacteristicsIsCalled() throws Exception {
        tracker.fetchUserCharacteristics(dbFetchRequestListner);
    }

    @Test
    public void ShouldPostUpdateConsentEvent_WhenUpdateConsentIsCalled() throws Exception {
        tracker.updateConsentDetails(anyListOf(ConsentDetail.class), dbRequestListener);
        verify(eventingMock).post(any(DatabaseConsentSaveRequest.class));
    }

    @Test
    public void ShouldPostdeleteAllMomentEvent_WhendeleteAllMomentIsCalled() throws Exception {
        tracker.deleteAllMoments(dbRequestListener);
        verify(eventingMock).post(any(DataClearRequest.class));
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
    public void unRegisterDeviceTokenTest() throws Exception {
        tracker.unRegisterDeviceToken("token", "variant", null);
    }

    @Test
    public void registerDeviceTokenTest() throws Exception {
        tracker.registerDeviceToken("token", "variant", "protocol provider", null);
    }

    @Test
    public void handlePushNotificationPayloadTest() throws Exception {
        tracker.handlePushNotificationPayload(jsonObject);
    }
}