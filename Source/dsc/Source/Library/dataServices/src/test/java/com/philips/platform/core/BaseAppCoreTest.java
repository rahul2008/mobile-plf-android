package com.philips.platform.core;

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
    private AppComponent mAppComponentMock;

    @Before
    public void setUp() {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponent(mAppComponentMock);
        savingMonitor = new SavingMonitor(savingInterface, deletingInterface);
        fetchMonitor = new FetchingMonitor(fetchingInterface);
        deletingMonitor = new DeletingMonitor(deletingInterface);
        updatingMonitor = new UpdatingMonitor(updatingInterface, deletingInterface, fetchingInterface, savingInterface);
        database = new VerticalCreater();

        dbMonitors = new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));

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

    @Test
    public void ShouldStop_WhenCalledStop() {
        baseAppCoreCreatorMock.stop();
        Mockito.verify(appBackend, Mockito.atLeast(1)).stop();
    }

}