package com.philips.platform.core;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.EventMonitor;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by indrajitkumar on 07/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseAppCoreTest {
    private static final String UUID = "UUID";
    @Mock
    private BaseAppCore baseAppCoreCreator;
    @Mock
    private Eventing eventing;
    @Mock
    BaseAppDataCreator database;
    @Mock
    private DBMonitors dbMonitors;
    @Mock
    private BaseAppBackend appBackend;
    @Mock
    private List<EventMonitor> eventMonitors;

    @Before
    public void setUp() {
        baseAppCoreCreator = new BaseAppCore(eventing, database, appBackend, eventMonitors, dbMonitors);
        baseAppCoreCreator.start();
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMoment_WhenCreateMomentIsCalled() {
        Moment moment = baseAppCoreCreator.createMoment("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");

        assertThat(moment.getCreatorId()).isEqualTo("TEST_CREATOR_ID");
        assertThat(moment.getSubjectId()).isEqualTo("TEST_SUBJECT_ID");
        assertThat(moment.getType()).isEqualTo("BREAST_FEED");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldAddGuidToMoment_WhenCreateMomentIsCalled() throws Exception {
        Moment moment = baseAppCoreCreator.createMoment("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");

        Collection<? extends MomentDetail> details = moment.getMomentDetails();
        assertThat(details).hasSize(1);
        assertThat(details.iterator().next().getValue()).isEqualTo(UUID);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMoment_WhenCreateMomentWithoutUUIDIsCalled() {
        Moment moment = baseAppCoreCreator.createMomentWithoutUUID("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");

        assertThat(moment.getCreatorId()).isEqualTo("TEST_CREATOR_ID");
        assertThat(moment.getSubjectId()).isEqualTo("TEST_SUBJECT_ID");
        assertThat(moment.getType()).isEqualTo("BREAST_FEED");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotContainMomentDetail_WhenCreateMomentWithoutUUIDIsCalled() {
        Moment moment = baseAppCoreCreator.createMomentWithoutUUID("TEST_CREATOR_ID", "TEST_SUBJECT_ID", "BREAST_FEED");
        assertThat(moment.getMomentDetails()).hasSize(0);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurement_WhenCreateMeasurementIsCalled() {
        //noinspection ConstantConditions
        Measurement measurement = baseAppCoreCreator.createMeasurement("DURATION", Mockito.mock(MeasurementGroup.class));
        assertThat(measurement.getType()).isEqualTo("DURATION");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementDetail_WhenCreateMeasurementDetailIsCalled() {
        MeasurementDetail measurementDetail = baseAppCoreCreator.createMeasurementDetail("BOTTLE_CONTENTS", Mockito.mock(Measurement.class));
        assertThat(measurementDetail.getType()).isEqualTo("BOTTLE_CONTENTS");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMomentDetail_WhenCreateMomentDetailIsCalled() {
        MomentDetail momentDetail = baseAppCoreCreator.createMomentDetail("NOTE", Mockito.mock(Moment.class));
        assertThat(momentDetail.getType()).isEqualTo("NOTE");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateSynchronizationData_WhenCreateSyncDataIsCalled() {
        SynchronisationData synchronisationData = baseAppCoreCreator.createSynchronisationData("TEST_GUID", false, new DateTime(), 1);
        assertThat(synchronisationData.getGuid()).isEqualTo("TEST_GUID");
        assertThat(synchronisationData.isInactive()).isFalse();
    }

//    @Test
//    public void ShouldCreateInsight_WhenAsked() {
//
//        Insight insights = ormCreator.createInsight("Test_Rule", "Test_Title", "Test_Tags", "Test_SubjectId", "Test_MomentId", DateTime.now(), "", "", "");
//
//        assertThat(insights).isNotNull();
//        assertThat(insights.getRuleId()).isEqualTo("Test_Rule");
//        assertThat(insights.getTitle()).isEqualTo("Test_Title");
//        assertThat(insights.getTag()).isEqualTo("Test_Tags");
//        assertThat(insights.getSubjectId()).isEqualTo("Test_SubjectId");
//        assertThat(insights.getMomentId()).isEqualTo("Test_MomentId");
//    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateConsent_WhenCreateConsentIsCalled() {
        Consent consent = baseAppCoreCreator.createConsent("TEST_CREATOR_ID");
        assertThat(consent.getCreatorId()).isEqualTo("TEST_CREATOR_ID");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateConsentDetail_WhenCreateConsentDetailIsCalled() {
        Consent consent = baseAppCoreCreator.createConsent("TEST_CREATOR_ID");
        ConsentDetail consentDetail = baseAppCoreCreator.createConsentDetail("HEIGHT", "accepted", "1.0", Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER, true, consent);

        assertThat(consentDetail.getType()).isEqualTo("HEIGHT");
        assertThat(consentDetail.getStatus()).isEqualTo("accepted");
        assertThat(consentDetail.getVersion()).isEqualTo("1.0");
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalled() {
        MeasurementGroup measurementGroup = baseAppCoreCreator.createMeasurementGroup(Mockito.mock(MeasurementGroup.class));
        assertThat(measurementGroup.getId()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementGroup_WhenCreateMeasurementGroupIsCalledWhenMomentPassed() {
        MeasurementGroup measurementGroup = baseAppCoreCreator.createMeasurementGroup(Mockito.mock(Moment.class));
        assertThat(measurementGroup.getId()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCreateMeasurementGroupDetail_WhenCreateMeasurementGroupDetailIsCalled() {
        MeasurementGroupDetail measurementGroup = baseAppCoreCreator.createMeasurementGroupDetail("HEIGHT", Mockito.mock(MeasurementGroup.class));
        assertThat(measurementGroup.getId()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldStop_WhenCalledStop() {
        baseAppCoreCreator.stop();
        Mockito.verify(appBackend, Mockito.atLeast(1)).stop();
        Mockito.verify(dbMonitors, Mockito.atLeast(1)).stop();
        Mockito.verify(eventMonitors.get(0), Mockito.atLeast(1)).stop();
    }

}