package com.philips.platform.datasync.moments;

import android.app.Application;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.testing.verticals.AssertHelper;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;
import com.philips.testing.verticals.datatyes.MeasurementDetailType;
import com.philips.testing.verticals.datatyes.MeasurementGroupDetailType;
import com.philips.testing.verticals.datatyes.MeasurementType;
import com.philips.testing.verticals.datatyes.MomentDetailType;
import com.philips.testing.verticals.datatyes.MomentType;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsConverterTest {
    public static final DateTime TEST_TIMESTAMP = DateTime.now().plusHours(1);
    public static final String TEST_GUID = "TEST_GUID";
    public static final boolean TEST_INACTIVE = true;
    public static final DateTime TEST_LAST_MODIFIED = DateTime.now().minusHours(1);
    public static final String TEST_CREATOR_ID = "TEST_CREATOR_ID";
    public static final String TEST_SUBJECT_ID = "TEST_SUBJECT_ID";
    public static final String TEST_VALUE_STRING = "TEST_VALUE_STRING";
    public static final String TEST_MOMENT_TYPE = MomentType.TEMPERATURE;
    public static final String TEST_MOMENT_DETAIL_TYPE = MomentDetailType.PHASE;
    public static final String TEST_MEASUREMENT_TYPE = MeasurementType.TEMPERATURE;
    public static final String TEST_MEASUREMENT_DETAIL_TYPE = MeasurementDetailType.LOCATION;
    //public static final String TEST_MEASUREMENT_DETAIL_VALUE = BottleType.CLASSIC_BOTTLE_300;
    public static final int TEST_VERSION = 111;
    public static final String TEST_VALUE_DOUBLE = "222.333";
    private final String LEFT = "LEFT";
    private final String LOCATION = "BANGALORE";
    private MomentsConverter momentsConverter;
    private UCoreMoment uCoreMoment;
    private UCoreMoment uCoreMomentWithoutExpirationDate;
    private UCoreDetail uCoreMomentDetail;
    private UCoreDetail uCoreMeasurementDetail;
    private UCoreMeasurement uCoreMeasurement;
    private UCoreMeasurementGroups uCoreMeasurementGroup;
    private UCoreMeasurementGroups uCoreMeasurementGroupInside;
    private UCoreMeasurementGroupDetail uCoreMeasurementGroupDetail;

    private OrmCreatorTest ormCreatorTest;

    private Moment moment;
    private MeasurementGroup measurementGroup;
    private MeasurementGroup measurementGroupInside;
    private MomentType momentTypeMap;
    private MeasurementDetailType measurementDetailValueMap;
    private final DateTime TEST_MOMENT_DATETIME = DateTime.now().minusDays(0);
    private final DateTime TEST_MEASUREMENT_DATETIME = DateTime.now();
    private MeasurementGroupDetail measurementGroupDetail;
    private Application context;
    private DataServicesManager dataServicesManager;
    private ErrorHandlerImplTest errorHandler;
    @Mock
    private AppComponent appComponantMock;

    @Mock
    BaseAppDataCreator dataCreator;

    private BaseAppDataCreator verticalDataCreater;
//    verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
//    errorHandlerImplTest = new ErrorHandlerImplTest();
//
//    DataServicesManager.getInstance().mAppComponent = appComponantMock;
//    consentsMonitor = new ConsentsMonitor(uCoreAdapterMock, consentsConverterMock, gsonConverterMock);
//    consentsMonitor.uCoreAccessProvider = accessProviderMock;
//    consentsMonitor.start(eventingMock);



    @Before
    public void setUp() {
        initMocks(this);

        ormCreatorTest = new OrmCreatorTest(new UuidGenerator());

        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        momentsConverter = new MomentsConverter();
        momentsConverter.baseAppDataCreater = verticalDataCreater;
        errorHandler = new ErrorHandlerImplTest();
        initializeUCoreMoment();

        initializeMoment();

    }

    private void initializeMoment() {
        moment = ormCreatorTest.createMoment(TEST_CREATOR_ID, TEST_SUBJECT_ID, MomentType.TEMPERATURE, TEST_TIMESTAMP.plusWeeks(2));
        moment.setDateTime(TEST_TIMESTAMP);

        MomentDetail momentDetail = ormCreatorTest.createMomentDetail(MomentDetailType.PHASE,moment);
        momentDetail.setValue(LOCATION);

        moment.addMomentDetail(momentDetail);

        Measurement measurement = ormCreatorTest.createMeasurement(MeasurementType.DURATION, measurementGroup);
        measurement.setDateTime(TEST_TIMESTAMP);
        measurement.setValue(TEST_VALUE_DOUBLE);

        measurementGroup = ormCreatorTest.createMeasurementGroup(moment);
        measurementGroup.addMeasurement(measurement);

        measurementGroupInside = ormCreatorTest.createMeasurementGroup(measurementGroup);
        measurementGroupInside.addMeasurement(measurement);

      //  measurementGroup.addMeasurement(measurementGroupInside);

        measurementGroupDetail = ormCreatorTest.createMeasurementGroupDetail(MeasurementDetailType.LOCATION, measurementGroupInside);
        measurementGroupDetail.setValue(TEST_MEASUREMENT_DETAIL_TYPE);

        MeasurementDetail measurementDetail = ormCreatorTest.createMeasurementDetail(MeasurementDetailType.LOCATION, measurement);
        measurementDetail.setValue(LEFT);
        measurement.addMeasurementDetail(measurementDetail);

        measurementGroup.addMeasurementGroupDetail(measurementGroupDetail);

        moment.addMeasurementGroup(measurementGroup);
    }

    private void initializeUCoreMoment() {
        uCoreMoment = new UCoreMoment();
        uCoreMoment.setTimestamp(TEST_TIMESTAMP.toString());
        uCoreMoment.setGuid(TEST_GUID);
        uCoreMoment.setInactive(TEST_INACTIVE);
        uCoreMoment.setLastModified(TEST_LAST_MODIFIED.toString());
        uCoreMoment.setCreatorId(TEST_CREATOR_ID);
        uCoreMoment.setSubjectId(TEST_SUBJECT_ID);
        uCoreMoment.setType(MomentType.TEMPERATURE);
        uCoreMoment.setVersion(TEST_VERSION);
        uCoreMoment.setExpirationDate(TEST_TIMESTAMP.plusWeeks(5).toString());

        uCoreMomentDetail = new UCoreDetail();
        uCoreMomentDetail.setType(MomentType.TEMPERATURE);
        uCoreMomentDetail.setValue(TEST_VALUE_STRING);

        uCoreMoment.setDetails(Collections.singletonList(uCoreMomentDetail));

        uCoreMomentWithoutExpirationDate = new UCoreMoment();
        uCoreMomentWithoutExpirationDate.setTimestamp(TEST_TIMESTAMP.toString());
        uCoreMomentWithoutExpirationDate.setGuid(TEST_GUID);
        uCoreMomentWithoutExpirationDate.setInactive(TEST_INACTIVE);
        uCoreMomentWithoutExpirationDate.setLastModified(TEST_LAST_MODIFIED.toString());
        uCoreMomentWithoutExpirationDate.setCreatorId(TEST_CREATOR_ID);
        uCoreMomentWithoutExpirationDate.setSubjectId(TEST_SUBJECT_ID);
        uCoreMomentWithoutExpirationDate.setType(MomentType.TEMPERATURE);
        uCoreMomentWithoutExpirationDate.setVersion(TEST_VERSION);
        uCoreMomentWithoutExpirationDate.setExpirationDate(TEST_TIMESTAMP.plusWeeks(5).toString());

        uCoreMomentDetail = new UCoreDetail();
        uCoreMomentDetail.setType(MomentType.TEMPERATURE);
        uCoreMomentDetail.setValue(TEST_VALUE_STRING);

        uCoreMomentWithoutExpirationDate.setDetails(Collections.singletonList(uCoreMomentDetail));



        uCoreMeasurement = new UCoreMeasurement();
        uCoreMeasurement.setType(MeasurementDetailType.LOCATION);
        uCoreMeasurement.setValue(TEST_VALUE_DOUBLE);
        uCoreMeasurement.setTimestamp(TEST_TIMESTAMP.toString());

        uCoreMeasurementDetail = new UCoreDetail();
        uCoreMeasurementDetail.setType(MeasurementDetailType.LOCATION);
        uCoreMeasurementDetail.setValue(MeasurementDetailType.LOCATION);
        uCoreMeasurement.setDetails(Collections.singletonList(uCoreMeasurementDetail));

        uCoreMeasurementGroupDetail = new UCoreMeasurementGroupDetail();
        uCoreMeasurementGroupDetail.setType(MeasurementGroupDetailType.TEMP_OF_DAY);
        uCoreMeasurementGroupDetail.setValue(MeasurementGroupDetailType.TEMP_OF_DAY);


        uCoreMeasurementGroup = new UCoreMeasurementGroups();
    //    uCoreMeasurementGroup.setMeasurementGroups(Collections.singletonList(uCoreMeasurementGroup));
        uCoreMeasurementGroup.setMeasurements(Collections.singletonList(uCoreMeasurement));
        uCoreMeasurementGroup.setDetails(Collections.singletonList(uCoreMeasurementGroupDetail));

        uCoreMeasurementGroupInside = new UCoreMeasurementGroups();
        //    uCoreMeasurementGroup.setMeasurementGroups(Collections.singletonList(uCoreMeasurementGroup));
        uCoreMeasurementGroupInside.setMeasurements(Collections.singletonList(uCoreMeasurement));
        uCoreMeasurementGroupInside.setDetails(Collections.singletonList(uCoreMeasurementGroupDetail));

        uCoreMeasurementGroup.addMeasurementGroups(uCoreMeasurementGroupInside);

        uCoreMoment.setMeasurementGroups(Collections.singletonList(uCoreMeasurementGroup));
    }

    @Test
    public void ShouldReturnEmptyList_WhenProvidedWithAnEmptyList() {
        List<Moment> moments = momentsConverter.convert(new ArrayList<UCoreMoment>());

        assertThat(moments).isEmpty();
    }

    @Test
   public void ShouldAddSingleMomentWithSyncData_WhenMomentWithoutMeasurementsOrDetailsIsProvided() {
        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);

        Moment moment = moments.get(0);
        assertThat(moment.getCreatorId()).isEqualTo(TEST_CREATOR_ID);
        assertThat(moment.getSubjectId()).isEqualTo(TEST_SUBJECT_ID);
        assertThat("TEMPERATURE").isEqualTo(TEST_MOMENT_TYPE);
        assertThat(moment.getDateTime()).isEqualTo(TEST_TIMESTAMP);

        SynchronisationData synchronisationData = moment.getSynchronisationData();
        assertThat(synchronisationData.getVersion()).isEqualTo(TEST_VERSION);
        assertThat(synchronisationData.getGuid()).isEqualTo(TEST_GUID);
        assertThat(synchronisationData.getLastModified()).isEqualTo(TEST_LAST_MODIFIED);
        assertThat(synchronisationData.isInactive()).isEqualTo(TEST_INACTIVE);
    }

    @Test
    public void ShouldAddMultipleMomentWithSyncData_WhenMultipleMomentsAreProvided() {
        List<Moment> moments = momentsConverter.convert(Arrays.asList(uCoreMoment, uCoreMoment, uCoreMoment));

        assertThat(moments).hasSize(3);

        AssertHelper.assertEquals(moments.get(0), moments.get(1));
        AssertHelper.assertEquals(moments.get(0), moments.get(2));
    }

    @Test
    public void ShouldAddSingleMomentDetail_WhenMomentWithDetailsIsProvided() {
        uCoreMoment.setDetails(Collections.singletonList(uCoreMomentDetail));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
        assertThat(momentDetails).hasSize(1); //one for uuid

        MomentDetail momentDetail = momentDetails.iterator().next();
        assertThat("TEMPERATURE").isEqualTo(TEST_MOMENT_TYPE);
        assertThat(momentDetail.getValue()).isEqualTo(TEST_VALUE_STRING);
        assertThat(momentDetail.getMoment()).isEqualTo(moments.get(0));
    }

    @Test
    public void ShouldAddMultipleMomentDetails_WhenMomentWithMultipleDetailsAreProvided() {
        uCoreMoment.setDetails(Arrays.asList(uCoreMomentDetail, uCoreMomentDetail));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
        assertThat(momentDetails).hasSize(2);

        Iterator<? extends MomentDetail> iterator = momentDetails.iterator();
        AssertHelper.assertEquals(iterator.next(), iterator.next());
    }

    @Test
    public void ShouldReturnSingleMomentDetails_WhenMomentWithMultipleDetailsAreProvidedAndTheFirstTypeIsUnknown() {
        UCoreDetail uCoreDetailUnknownType = new UCoreDetail();
        uCoreDetailUnknownType.setType("RANDOM_TYPE_kbdsghsdfbvfh");
        uCoreDetailUnknownType.setValue(TEST_VALUE_STRING);

        uCoreMoment.setDetails(Arrays.asList(uCoreDetailUnknownType, uCoreMomentDetail));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
         //assertThat(momentDetails).hasSize(1);

        MomentDetail momentDetail = momentDetails.iterator().next();
        assertThat("RANDOM_TYPE_kbdsghsdfbvfh").isEqualTo("RANDOM_TYPE_kbdsghsdfbvfh");
        assertThat(momentDetail.getValue()).isEqualTo(TEST_VALUE_STRING);
    }

    @Test
    public void ShouldAddSingleMeasurement_WhenMomentWithDetailsIsProvided() {
        uCoreMoment.setMeasurementGroups((Collections.singletonList(uCoreMeasurementGroup)));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MeasurementGroup> measurementsGroup = moment.getMeasurementGroups();
        assertThat(measurementsGroup).hasSize(1);

        MeasurementGroup measurementGroup1 = measurementsGroup.iterator().next();
        assertThat("TEMPERATURE").isEqualTo(TEST_MEASUREMENT_TYPE);
        assertThat("222.333").isEqualTo(TEST_VALUE_DOUBLE);
      //  assertThat(DateTime.now().plusHours(1)).isEqualTo(TEST_TIMESTAMP);
       // assertThat(measurementGroup1.ge).isEqualTo(moments.get(0));
    }

    @Test
    public void ShouldAddMultipleMeasurements_WhenMomentWithMultipleDetailsAreProvided() {
        uCoreMoment.setMeasurementGroups((Arrays.asList(uCoreMeasurementGroup, uCoreMeasurementGroup)));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MeasurementGroup> measurementsGroup = moment.getMeasurementGroups();
        assertThat(measurementsGroup).hasSize(2);

      //  Iterator<? extends MeasurementGroup> iterator = measurementsGroup.iterator();
        //AssertHelper.assertEquals(iterator.next(), iterator.next());
    }

    @Test
    public void ShouldReturnSingleMeasurements_WhenMomentWithMultipleMeasurementsAreProvidedAndTheFirstTypeIsUnknown() {
        UCoreMeasurement uCoreMeasurementUnknownType = new UCoreMeasurement();
        uCoreMeasurementUnknownType.setType("RANDOM_TYPE_kbdsghsdfbvfh");
        uCoreMeasurementUnknownType.setValue(TEST_VALUE_DOUBLE);
        uCoreMeasurementUnknownType.setTimestamp(TEST_TIMESTAMP.toString());

        uCoreMoment.setMeasurementGroups((Arrays.asList(uCoreMeasurementGroup)));

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));

        assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MeasurementGroup> measurements = moment.getMeasurementGroups();
        assertThat(measurements).hasSize(1);

        MeasurementGroup measurement = measurements.iterator().next();
        assertThat("TEMPERATURE").isEqualTo(TEST_MEASUREMENT_TYPE);
        assertThat("222.333").isEqualTo(TEST_VALUE_DOUBLE);
    }

    @Test
    public void ShouldThrowExceptionWhenConvertIsCalled(){
        List<Moment> moments = momentsConverter.convert(null);
        assertThat(moments.size()).isEqualTo(0);
    }

    @Test
    public void ShouldCreateMomentReturnIfNullIsPassed(){
        UCoreDetail uCoreMeasurementDetailUnknownType = new UCoreDetail();
        uCoreMeasurementDetailUnknownType.setType("Phase");
        uCoreMeasurementDetailUnknownType.setValue("RANDOM_TYPE_kbdsghsdfbvfh");

        uCoreMeasurement.setDetails(Arrays.asList(uCoreMeasurementDetailUnknownType, uCoreMeasurementDetail));
        uCoreMoment.setMeasurementGroups(Collections.singletonList(uCoreMeasurementGroup));

        momentsConverter.baseAppDataCreater = dataCreator;

        when(dataCreator.createMoment(anyString(),anyString(),anyString(), isA(DateTime.class))).thenReturn(null);

        List<Moment> moments = momentsConverter.convert(Collections.singletonList(uCoreMoment));
        //TODO: Verify nothing after this executed ??
    }

    @Test
    public void ShouldSetProperDateTime_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        DateTime uCoreTimeStamp = new DateTime(uCoreMoment.getTimestamp());

        assertThat(uCoreTimeStamp).isEqualTo(TEST_TIMESTAMP);
    }

    @Test
    public void ShouldNotSetExpirationDate_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);
        assertThat(uCoreMoment.getExpirationDate()).isNull();
    }


    @Test
    public void ShouldSetExpirationDate_WhenUCoreMomentIsConvertedToMoment() {
        List<Moment> moments = momentsConverter.convert(Arrays.asList(uCoreMoment, uCoreMomentWithoutExpirationDate));
        assertThat(moments).isNotNull();
        assertThat(moments.size()).isEqualTo(2);
        assertThat(moments.get(0).getExpirationDate().toString()).isEqualTo(uCoreMoment.getExpirationDate());
        assertThat(moments.get(1).getExpirationDate().toString()).isEqualTo(uCoreMomentWithoutExpirationDate.getExpirationDate());
    }

    @Test
    public void ShouldSetProperMomentType_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        assertThat(uCoreMoment.getType()).isEqualTo("TEMPERATURE");
    }

    @Test
    public void ShouldSetProperNumberOfMeasurements_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        assertThat(uCoreMoment.getMeasurementGroups()).hasSize(1);
    }

    @Test
    public void ShouldSetProperNumberOfMomentDetails_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        assertThat(uCoreMoment.getDetails()).hasSize(1);
    }

    @Test
    public void ShouldSetProperNumberOfMeasurementsDetails_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
     //   List<UCoreDetail> details = measurements.get(0).getMeasurements().get(0).getDetails();

        assertThat(measurements).hasSize(1);
    }

    @Test
    public void ShouldSetProperMeasurementsTimeStamp_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
        /*UCoreMeasurementGroups uCoreMeasurement = measurements.get(0);
        DateTime measurementTimestamp = new DateTime(uCoreMeasurement.getMeasurements().get(0).getTimestamp());*/

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    public void ShouldSetProperMeasurementsValue_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
      //  UCoreMeasurementGroups uCoreMeasurement = measurements.get(0);

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    public void ShouldSetProperMeasurementsType_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
        //UCoreMeasurementGroups uCoreMeasurement = measurements.get(0);

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    public void ShouldSetProperMeasurementDetailsType_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
      //  UCoreMeasurement uCoreMeasurement = measurements.get(0).getMeasurements().get(0);
 /*       List<UCoreDetail> details = uCoreMeasurement.getDetails();
        UCoreDetail uCoreDetail = details.get(0);*/

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    public void ShouldSetProperMeasurementDetailsValue_WhenTemperatureMomentIsConvertedToUCoreMoment() {
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        List<UCoreMeasurementGroups> measurements = uCoreMoment.getMeasurementGroups();
        /*UCoreMeasurement uCoreMeasurement = measurements.get(0).getMeasurements().get(0);
        List<UCoreDetail> details = uCoreMeasurement.getDetails();
        UCoreDetail uCoreDetail = details.get(0);*/

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    public void shouldSetVersionInUCoreMoment_WhenMomentHasSynchronizationData() throws Exception {
        moment.setSynchronisationData(ormCreatorTest.createSynchronisationData(TEST_GUID, false, DateTime.now(), TEST_VERSION));
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        assertThat(uCoreMoment.getVersion()).isEqualTo(TEST_VERSION);
    }

    @Test
    public void ShouldAddUUIDAsDetail_WhenMomentWithDetailsIsProvided() {
        UCoreDetail uCoreMomentDetail = new UCoreDetail();
        uCoreMomentDetail.setType(MomentDetailType.TAGGING_ID);
        uCoreMomentDetail.setValue("UUID");

        uCoreMoment.setDetails(Collections.singletonList(uCoreMomentDetail));

        List<UCoreMoment> uCoreMoments = new ArrayList<>();
        uCoreMoments.add(uCoreMoment);

        List<Moment> moments = momentsConverter.convert(uCoreMoments);

       // assertThat(moments).hasSize(1);
        Moment moment = moments.get(0);
        Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
        assertThat(momentDetails).hasSize(1); //one for uuid

        MomentDetail momentDetail = momentDetails.iterator().next();
        assertThat(momentDetail.getType()).isEqualTo(MomentDetailType.TAGGING_ID);
        assertThat(momentDetail.getValue()).isEqualTo("UUID");
    }

    @Test
    public void ShouldHaveUUIDInJson_WhenConverted() throws Exception {
        final UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(moment);

        final List<UCoreDetail> details = uCoreMoment.getDetails();
        assertThat(details).hasSize(1);
    }

//    @Test
//    public void ShouldSetMeasurementTimeAsMomentTime_WhenMomentTypeIsBottleFeed() throws Exception {
//        initNonDurationMoment("BOTTLE_FEED");
//
//        final List<Moment> momentList = momentsConverter.convert(Collections.singletonList(uCoreMoment));
//
//        assertMeasurementTimestamp(TEST_MOMENT_DATETIME, momentList);
//    }


    private void initNonDurationMoment(final String momentTypeString) {
        uCoreMoment.setType(momentTypeString);
        uCoreMoment.setTimestamp(TEST_MOMENT_DATETIME.toString());
        uCoreMeasurement.setTimestamp(TEST_MEASUREMENT_DATETIME.toString());
        uCoreMoment.setMeasurementGroups(Collections.singletonList(uCoreMeasurementGroup));
    }

    private void assertMeasurementTimestamp(final DateTime momentDatetime, final List<Moment> momentList) {
        final Collection<? extends MeasurementGroup> measurementGroups = momentList.get(0).getMeasurementGroups();
        for (MeasurementGroup measurementGroup : measurementGroups) {
            for (Measurement measurement : measurementGroup.getMeasurements()) {
                assertThat(measurement.getDateTime()).isEqualTo(momentDatetime);
            }
        }
    }

    @Test
    public void ShouldThrowExceptionWhileconvertToUCoreMomentIsCalled(){
        UCoreMoment uCoreMoment = momentsConverter.convertToUCoreMoment(null);
        assertThat(uCoreMoment.getMeasurementGroups()).isEqualTo(null);
    }

}