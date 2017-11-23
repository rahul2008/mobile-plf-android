package com.philips.platform.core;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.ErrorMonitor;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.Backend;
import com.philips.platform.verticals.VerticalCreater;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseAppCoreTest {
    private static final String UUID = "UUID";
    @Mock
    private BaseAppCore baseAppCoreCreatorMock;
    @Mock
    private Eventing eventingMock;

    @Mock
    ErrorMonitor errorMonitorMock;

    BaseAppDataCreator database;

    SavingMonitor savingMonitor;

    FetchingMonitor fetchMonitor;

    DeletingMonitor deletingMonitor;

    UpdatingMonitor updatingMonitor;

    DBMonitors dbMonitors;

    @Mock
    DBSavingInterface savingInterface;

    @Mock
    DBFetchingInterface fetchingInterface;

    @Mock
    DBDeletingInterface deletingInterface;

    @Mock
    DBUpdatingInterface updatingInterface;

    @Mock
    private Backend appBackend;
    @Mock
    private List<EventMonitor> eventMonitors;
    @Mock
    private AppComponent mAppComponentMock;

    @Before
    public void setUp() {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponent(mAppComponentMock);
        savingMonitor = new SavingMonitor(savingInterface, deletingInterface, updatingInterface);
        fetchMonitor = new FetchingMonitor(fetchingInterface);
        deletingMonitor = new DeletingMonitor(deletingInterface);
        updatingMonitor = new UpdatingMonitor(updatingInterface, deletingInterface, fetchingInterface, savingInterface);
        database = new VerticalCreater();

        dbMonitors = new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));

        /*doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                baseAppCoreCreatorMock.eventing = eventingMock;
                baseAppCoreCreatorMock.dbMonitors = dbMonitors;
                baseAppCoreCreatorMock.database = database;
                baseAppCoreCreatorMock.appBackend = appBackend;
                baseAppCoreCreatorMock.errorMonitor = errorMonitorMock;
                return null;
            }
        }).when(mAppComponentMock).injectBaseAppCore(baseAppCoreCreatorMock);*/


        baseAppCoreCreatorMock = new BaseAppCore();
        baseAppCoreCreatorMock.eventing = eventingMock;
        baseAppCoreCreatorMock.dbMonitors = dbMonitors;
        baseAppCoreCreatorMock.database = database;
        baseAppCoreCreatorMock.appBackend = appBackend;
        baseAppCoreCreatorMock.errorMonitor = errorMonitorMock;


        baseAppCoreCreatorMock.start();
    }

    @Test
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() {
        Moment moment = baseAppCoreCreatorMock.createMoment("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED", null);

        assertThat(moment.getCreatorId()).isEqualTo("TEST_CREATOR_ID");
        assertThat(moment.getSubjectId()).isEqualTo("TEST_SUBJECT_ID");
        assertThat(moment.getType()).isEqualTo("BREAST_FEED");
    }

    @Test
    public void ShouldAddGuidToMoment_WhenCreateMomentIsCalled() throws Exception {
        Moment moment = baseAppCoreCreatorMock.createMoment("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED", null);

        Collection<? extends MomentDetail> details = moment.getMomentDetails();
        assertThat(details).hasSize(0);
        if (details.size() > 0)
            assertThat(details.iterator().next().getValue()).isEqualTo(UUID);
    }

    /*@Test
    public void ShouldCreateMoment_WhenCreateMomentWithoutUUIDIsCalled() {
        Moment moment = baseAppCoreCreatorMock.createMomentWithoutUUID("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");

        assertThat(moment.getCreatorId()).isEqualTo("TEST_CREATOR_ID");
        assertThat(moment.getSubjectId()).isEqualTo("TEST_SUBJECT_ID");
        assertThat(moment.getType()).isEqualTo("BREAST_FEED");
    }

    @Test
    public void ShouldNotContainMomentDetail_WhenCreateMomentWithoutUUIDIsCalled() {
        Moment moment = baseAppCoreCreatorMock.createMomentWithoutUUID("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");
        assertThat(moment.getMomentDetails()).hasSize(0);
    }*/

    @Test
    public void ShouldCreateMeasurement_WhenCreateMeasurementIsCalled() {
        //noinspection ConstantConditions
        Measurement measurement = baseAppCoreCreatorMock.createMeasurement("DURATION", Mockito.mock(MeasurementGroup.class));
        //  assertThat(measurement.getType()).isEqualTo("DURATION");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementDetail_WhenCreateMeasurementDetailIsCalled() {
        MeasurementDetail measurementDetail = baseAppCoreCreatorMock.createMeasurementDetail("BOTTLE_CONTENTS", Mockito.mock(Measurement.class));
        assertThat(measurementDetail.getType()).isEqualTo("BOTTLE_CONTENTS");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMomentDetail_WhenCreateMomentDetailIsCalled() {
        MomentDetail momentDetail = baseAppCoreCreatorMock.createMomentDetail("NOTE", Mockito.mock(Moment.class));
        assertThat(momentDetail.getType()).isEqualTo("NOTE");
    }

    @Test
    public void ShouldCreateSynchronizationData_WhenCreateSyncDataIsCalled() {
        SynchronisationData synchronisationData = baseAppCoreCreatorMock.createSynchronisationData("TEST_GUID", false, new DateTime(), 1);
        assertThat(synchronisationData.getGuid()).isEqualTo("TEST_GUID");
        assertThat(synchronisationData.isInactive()).isFalse();
    }

//    @Test
//    public void ShouldCreateInsight_WhenAsked() {
//
//        UCoreInsight insights = ormCreator.createInsight("Test_Rule", "Test_Title", "Test_Tags", "Test_SubjectId", "Test_MomentId", DateTime.now(), "", "", "");
//
//        assertThat(insights).isNotNull();
//        assertThat(insights.getRuleId()).isEqualTo("Test_Rule");
//        assertThat(insights.getTitle()).isEqualTo("Test_Title");
//        assertThat(insights.getTag()).isEqualTo("Test_Tags");
//        assertThat(insights.getSubjectId()).isEqualTo("Test_SubjectId");
//        assertThat(insights.getMomentId()).isEqualTo("Test_MomentId");
//    }


    @Test
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() {
        ConsentDetail consentDetailDetail = baseAppCoreCreatorMock.createConsentDetail("SLEEP", "Accepted", "1.0", ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        assertThat(consentDetailDetail.getType()).isEqualTo("SLEEP");
        assertThat(consentDetailDetail.getStatus()).isEqualTo("Accepted");
        assertThat(consentDetailDetail.getVersion()).isEqualTo("1.0");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() {
        MeasurementGroup measurementGroup = baseAppCoreCreatorMock.createMeasurementGroup(Mockito.mock(MeasurementGroup.class));
        assertThat(measurementGroup.getId()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalledWhenMomentPassed() {
        MeasurementGroup measurementGroup = baseAppCoreCreatorMock.createMeasurementGroup(Mockito.mock(Moment.class));
        assertThat(measurementGroup.getId()).isEqualTo(1);
    }

//    @Test
//    public void ShouldCreateMeasurementGroupDetail_WhenCreateMeasurementGroupDetailIsCalled() {
//        MeasurementGroupDetail measurementGroup = baseAppCoreCreatorMock.createMeasurementGroupDetail("HEIGHT", Mockito.mock(MeasurementGroup.class));
//        assertThat(measurementGroup.getId()).isEqualTo(1);
//    }

    @Test
    public void ShouldStop_WhenCalledStop() {
        baseAppCoreCreatorMock.stop();
        Mockito.verify(appBackend, Mockito.atLeast(1)).stop();
        // Mockito.verify(dbMonitors, Mockito.atLeast(1)).stop();
        //Mockito.verify(eventMonitors.get(0), Mockito.atLeast(1)).stop();
    }

}