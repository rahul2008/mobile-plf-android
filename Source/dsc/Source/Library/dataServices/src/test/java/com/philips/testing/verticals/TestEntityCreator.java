package com.philips.testing.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.verticals.OrmCharacteristics;
import com.philips.testing.verticals.datatyes.MeasurementDetailType;
import com.philips.testing.verticals.datatyes.MeasurementGroupDetailType;
import com.philips.testing.verticals.datatyes.MeasurementType;
import com.philips.testing.verticals.datatyes.MomentDetailType;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.TestInsight;
import com.philips.testing.verticals.table.TestInsightMetaData;
import com.philips.testing.verticals.table.TestMeasurement;
import com.philips.testing.verticals.table.TestMeasurementDetail;
import com.philips.testing.verticals.table.TestMeasurementDetailType;
import com.philips.testing.verticals.table.TestMeasurementGroup;
import com.philips.testing.verticals.table.TestMeasurementGroupDetail;
import com.philips.testing.verticals.table.TestMeasurementGroupDetailType;
import com.philips.testing.verticals.table.TestMeasurementType;
import com.philips.testing.verticals.table.TestMoment;
import com.philips.testing.verticals.table.TestMomentDetail;
import com.philips.testing.verticals.table.TestMomentDetailType;
import com.philips.testing.verticals.table.TestMomentType;
import com.philips.testing.verticals.table.TestSettings;
import com.philips.testing.verticals.table.TestSynchronisationData;

import org.joda.time.DateTime;

import javax.inject.Singleton;

public class TestEntityCreator implements BaseAppDataCreator{

    private final UuidGenerator uuidGenerator;

    @Singleton
    public TestEntityCreator(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @NonNull
    @Override
    public Moment createMoment(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type, DateTime expirationDate) {
        final TestMomentType testMomentType = new TestMomentType(MomentType.getIDFromDescription(type), type);

        return new TestMoment(creatorId, subjectId, testMomentType, expirationDate);
    }

    @NonNull
    @Override
    public MomentDetail createMomentDetail(@NonNull String type, @NonNull Moment moment) {
        return createMomentDetail(type, (TestMoment) moment);
    }

    @Override
    public Measurement createMeasurement(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return createMeasurement(type, (TestMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public MeasurementDetail createMeasurementDetail(@NonNull String type, @NonNull Measurement measurement) {
        return createMeasurementDetail(type, (TestMeasurement) measurement);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return createMeasurementGroup((TestMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return createMeasurementGroup((TestMoment) moment);
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return createMeasurementGroupDetail(type, (TestMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return new TestSynchronisationData(guid, inactive, lastModifiedTime, version);
    }

    @NonNull
    @Override
    public Settings createSettings(String type, String value, String timeZone) {
        return new TestSettings(type, value, timeZone);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value, @NonNull Characteristics characteristics) {
        return new OrmCharacteristics(type, value, (OrmCharacteristics) characteristics);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value) {
        return new OrmCharacteristics(type, value);
    }

    @NonNull
    public TestMomentDetail createMomentDetail(@NonNull final String type,
                                               @NonNull final TestMoment moment) {
        TestMomentDetailType testMomentDetailType = new TestMomentDetailType(MomentDetailType.getIDFromDescription(type), type);
        return new TestMomentDetail(testMomentDetailType, moment);
    }

    @NonNull
    public TestMeasurement createMeasurement(@NonNull final String type,
                                             @NonNull final TestMeasurementGroup testMeasurementGroup) {
        TestMeasurementType testMeasurementType = new TestMeasurementType(MeasurementType.getIDFromDescription(type),
                type,
                MeasurementType.getUnitFromDescription(type));
        return new TestMeasurement(testMeasurementType, testMeasurementGroup);
    }

    @NonNull
    public TestMeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                         @NonNull final TestMeasurement measurement) {
        TestMeasurementDetailType testMeasurementDetailType = new TestMeasurementDetailType(MeasurementDetailType.getIDFromDescription(type), type);
        return new TestMeasurementDetail(testMeasurementDetailType, measurement);
    }

    @NonNull
    public TestMeasurementGroup createMeasurementGroup(@NonNull TestMoment testMoment) {
        return new TestMeasurementGroup(testMoment);
    }


    @NonNull
    public TestMeasurementGroup createMeasurementGroup(@NonNull TestMeasurementGroup testMeasurementGroup) {
        return new TestMeasurementGroup(testMeasurementGroup);
    }

    @NonNull
    public TestMeasurementGroupDetail createMeasurementGroupDetail(@NonNull final String type,
                                                                   @NonNull final TestMeasurementGroup measurementGroup) {
        TestMeasurementGroupDetailType testMeasurementGroupDetailType = new TestMeasurementGroupDetailType(MeasurementGroupDetailType.getIDFromDescription(type), type);
        return new TestMeasurementGroupDetail(testMeasurementGroupDetailType, measurementGroup);
    }

    //Insight
    @NonNull
    @Override
    public Insight createInsight() {
        return new TestInsight();
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        return new TestInsightMetaData(key, value, (TestInsight) insight);
    }
}
