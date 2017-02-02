package com.philips.platform.core.trackers;

import android.content.Context;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
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
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.verticals.OrmCharacteristics;
import com.philips.platform.verticals.VerticalCreater;
import com.philips.platform.verticals.VerticalUCoreAccessProvider;
import com.philips.platform.verticals.VerticalUserRegistrationInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by indrajitkumar on 08/12/16.
 */
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

    private UserRegistrationInterface userRegistrationInterface;

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
    private Consent consentMock;

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
//    @Mock
    //UserCharacteristics userCharacteristicsMock;


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
    private AppComponent appComponantMock;

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

        when(requestEventMock.getEventId()).thenReturn(TEST_REFERENCE_ID);
    }

    @Test
    public void ShouldPostSaveEvent_WhenSaveIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.save(momentMock, dbRequestListener);

        verify(eventingMock).post(any(MomentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateEvent_WhenUpdateIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.update(momentMock, dbRequestListener);

        verify(eventingMock).post(any(MomentUpdateRequest.class));
    }

    @Test
    public void ShouldPostFetchEvent_WhenFetchIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.fetch(dbRequestListener, new Integer(1));

        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchMomentByIdEvent_WhenFetchMomentByIdIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.fetchMomentById(1, dbRequestListener);

        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchAllDataEvent_WhenFetchAllDataIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.fetchAllData(dbRequestListener);

        verify(eventingMock).post(any(LoadMomentsRequest.class));
    }

    @Test
    public void ShouldPostFetchConsentEvent_WhenFetchConsentIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.fetchConsent(dbRequestListener);

        verify(eventingMock).post(any(LoadConsentsRequest.class));
    }

    @Test
    public void ShouldPostFetchSettingsEvent_WhenFetchSettingsIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.fetchSettings(dbRequestListener);

        verify(eventingMock).post(any(LoadSettingsRequest.class));
    }

    @Test
    public void ShouldcreateConsent_WhenConsentIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createConsent();

//        verify(baseAppDataCreator).createConsent("fsdf");
    }

    @Test
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() throws Exception {
        tracker.createConsentDetail(consentMock, TEST_CONSENT_DETAIL_TYPE, ConsentDetailStatusType.ACCEPTED, "fsdfsdf");
    }

    @Test
    public void ShouldCreateSettings_WhenCreateSettingsIsCalled() throws Exception {
        // tracker.createSettings(anyString(),anyString(),anyString());
    }


    //TODO: Spoorti - Fix later
/*    @Test(expected = NullPointerException.class)
    public void ShouldAddConcentDetail_WhenConsentDetailIsCreated() throws Exception {
       // tracker.initialize(null, null, null,null);
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
        //noinspection ConstantConditions
        tracker.saveConsent(consentMock, dbRequestListener);

        verify(eventingMock).post(any(DatabaseConsentSaveRequest.class));
    }

    @Test
    public void ShouldPostUpdateSettingsEvent_WhenUpdateSettingsIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.updateSettings(any(Settings.class), dbRequestListener);

        verify(eventingMock).post(any(DatabaseSettingsUpdateRequest.class));
    }

    @Test
    public void ShouldPostUpdateCharacteristicsRequest_WhenUpdateCharacteristicsIsCalled() throws Exception {
        List<Characteristics> characteristicsList = new ArrayList<>();
        tracker.updateCharacteristics(characteristicsList, dbRequestListener);
    }

    @Test
    public void ShouldPostFetchCharacteristicsRequest_WhenFetchCharacteristicsIsCalled() throws Exception {
        tracker.fetchUserCharacteristics(dbRequestListener);
    }

    @Test
    public void ShouldPostUpdateConsentEvent_WhenUpdateConsentIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.updateConsent(consentMock, dbRequestListener);

        verify(eventingMock).post(any(DatabaseConsentSaveRequest.class));
    }

    @Test
    public void ShouldPostdeleteAllMomentEvent_WhendeleteAllMomentIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.deleteAllMoment(dbRequestListener);

        verify(eventingMock).post(any(DataClearRequest.class));
    }

    //TODO: Spoorti - revisit this
    @Test
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMoment("jh");

        //      verify(baseAppDataCreator).createMoment("fsdf", "", "jh");
    }

    @Test
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMeasurementGroup(momentMock);
    }

    @Test
    public void ShouldCreateMeasurementGroupDetail_WhenCreateMeasurementGroupDetailIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMeasurementGroupDetail("", measurementGroupMock);
    }

    /*@Test
    public void ShouldReleaseDataServicesInstances_WhenReleaseDataServicesInstancesIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.releaseDataServicesInstances();
    }*/


    //TODO: Spoorti - revisit
    @Test
    public void ShouldAddMomentDetail_WhenCreateMomentDetailIsCreated() throws Exception {
        baseAppDataCreator.createMomentDetail(TEST_MEASUREMENT_DETAIL_TYPE, momentMock);
        // assertThat(momentDetail).isSameAs(momentMock);
        //   verify(momentMock).addMomentDetail(momentDetail);
    }

    @Test
    public void ShouldStopCore_WhenStopCoreIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.stopCore();
    }

    @Test(expected = RuntimeException.class)
    public void ShouldinitializeSyncMonitors_WheninitializeSyncMonitorsIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.initializeSyncMonitors(null, new ArrayList<DataFetcher>(), new ArrayList<DataSender>());
    }

    @Test
    public void ShouldSynchchronize_WhenSynchchronizeIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.synchchronize();
    }

    @Test
    public void ShouldCreateMeasurementDetail_WhenCreateMeasurementDetailIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMeasurementDetail(TEST_MEASUREMENT_TYPE, measurementMock);
    }

    @Test
    public void ShouldCreateMeasurement_WhenCreateMeasurementIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMeasurement(TEST_MEASUREMENT_TYPE, measurementGroupMock);
    }

    @Test
    public void ShouldCreateMeasurement_WhenqCreateMeasurementIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.createMeasurementGroup(measurementGroupMock);

        // verify(baseAppDataCreator).createMeasurementGroup(measurementGroupMock);
    }

    @Test
    public void ShouldInitializeDBMonitors_WhenInitializeDBMonitorsIsCalled() throws Exception {
        //noinspection ConstantConditions
        tracker.initializeDBMonitors(null, deletingInterfaceMock, fetchingInterfaceMock, savingInterfaceMock, updatingInterfaceMock);
    }


    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailsIsCalled() throws Exception {
        tracker.createUserCharacteristics("TYPE", "VALUE", mock(OrmCharacteristics.class));
//        verify(baseAppDataCreator).createUserCharacteristics(TEST_USER_ID);
    }

    @Test
    public void ShouldCreateCharacteristicsDetails_WhenCreateCharacteristicsDetailIsNULL() throws Exception {
        tracker.createUserCharacteristics("TYPE", "VALUE", null);
//        verify(baseAppDataCreator).createUserCharacteristics(TEST_USER_ID);
    }

    @Test
    public void ShouldIsPullComplete_IsTrue() throws Exception {
        tracker.setPullComplete(true);
        tracker.isPullComplete();
    }

    @Test
    public void ShouldIsPushComplete_IsTrue() throws Exception {
        tracker.setPushComplete(true);
        tracker.isPushComplete();
    }
//    @Test(expected = NullPointerException.class)
//    public void ShouldAddMeasurement_WhenCreateMeasurementIsCreated() throws Exception {
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//        Measurement measurement = baseAppDataCreator.createMeasurement( TEST_MEASUREMENT_DETAIL_TYPE, measurementGroupMock);
//        verify(measurementGroupMock).addMeasurement(measurement);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldAddMeasurementDetail_WhenCreateMeasurementDetailIsCreated() throws Exception {
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//        MeasurementDetail measurementGroup = baseAppDataCreator.createMeasurementDetail( TEST_MEASUREMENT_DETAIL_TYPE, measurementMock);
//        verify(measurementMock).addMeasurementDetail(measurementGroup);
//    }

//    @Test(expected = NullPointerException.class)
//    public void ShouldCreateMoment_WhenAsked() throws Exception {
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//        when(eventingMock).thenReturn(new EventingImpl(new EventBus(), new Handler()));
//        when(backendIdProviderMock.getUserId()).thenReturn(TEST_USER_ID);
//        when(backendIdProviderMock.getSubjectId()).thenReturn(TEST_BABY_ID);
//        when(baseAppDataCreator.createMoment(TEST_USER_ID, TEST_BABY_ID, TEST_MOMENT_TYPE)).thenReturn(momentMock);
//
//        Moment moment = tracker.createMoment(TEST_MOMENT_TYPE);
//        verify(baseAppDataCreator).createMoment(TEST_USER_ID, TEST_BABY_ID, TEST_MOMENT_TYPE)l
//
//        assertThat(moment).isSameAs(momentMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldSetSubjectIdAsUserId_WhenMomentTypeIsPumping() throws Exception {
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//
//        when(backendIdProviderMock.getUserId()).thenReturn(TEST_USER_ID);
//        when(baseAppDataCreator.createMoment(TEST_USER_ID, TEST_USER_ID, "PUMPING")).thenReturn(momentMock);
//
//        Moment moment = tracker.createMoment("PUMPING");
//
//        assertThat(moment).isSameAs(momentMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldAddMomentDetailToMoment_WhenMomentDetailIsCreated() throws Exception {
//        //noinspection ConstantConditions
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//
//        when(baseAppDataCreator.createMomentDetail(TEST_MOMENT_DETAIL_TYPE, momentMock)).thenReturn(momentDetailMock);
//
//        MomentDetail momentDetail = tracker.createMomentDetail(TEST_MOMENT_DETAIL_TYPE, momentMock);
//
//        assertThat(momentDetail).isSameAs(momentDetailMock);
//        verify(momentMock).addMomentDetail(momentDetailMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldAddMeasurementToMoment_WhenMeasurementIsCreated() throws Exception {
//        //noinspection ConstantConditions
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//
//
//        when(baseAppDataCreator.createMeasurement(TEST_MEASUREMENT_TYPE, measurementGroupMock)).thenReturn(measurementMock);
//
//        Measurement measurement = tracker.createMeasurement(TEST_MEASUREMENT_TYPE, measurementGroupMock);
//
//        assertThat(measurement).isSameAs(measurementGroupMock);
//        verify(momentMock).addMeasurementGroup(measurementGroupMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldAddMeasurementDetailToMoment_WhenMeasurementDetailIsCreated() throws Exception {
//        //noinspection ConstantConditions
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//
//        when(baseAppDataCreator.createMeasurementDetail(TEST_MEASUREMENT_DETAIL_TYPE, measurementMock)).thenReturn(measurementDetailMock);
//
//        MeasurementDetail measurementDetail = tracker.createMeasurementDetail(TEST_MEASUREMENT_DETAIL_TYPE, measurementMock);
//
//        assertThat(measurementDetail).isSameAs(measurementDetailMock);
//        verify(measurementMock).addMeasurementDetail(measurementDetailMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldPostDeleteEvent_WhenDeleteMomentAsked() throws Exception {
//        //noinspection ConstantConditions
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//
//        tracker.markAsInActive(momentMock);
//
//        verify(eventingMock).post(momentDeleteEventCaptor.capture());
//        assertThat(momentDeleteEventCaptor.getValue().getMoment()).isSameAs(momentMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void ShouldPostUpdateEvent_WhenEditMomentAsked() throws Exception {
//        //noinspection ConstantConditions
//        tracker.initialize(mockContext, baseAppDataCreator, userRegistrationInterface);
//        when(eventingMock).thenReturn(new EventingImpl(new EventBus(), new Handler()));
//        tracker.update(momentMock);
//
//        verify(eventingMock).post(momentUpdateEventCaptor.capture());
//        assertThat(momentUpdateEventCaptor.getValue().getMoment()).isSameAs(momentMock);
//    }
}