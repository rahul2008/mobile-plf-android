package com.philips.testing.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
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
import com.philips.testing.verticals.table.OrmConsentDetail;
import com.philips.testing.verticals.table.OrmInsight;
import com.philips.testing.verticals.table.OrmInsightMetaData;
import com.philips.testing.verticals.table.OrmMeasurement;
import com.philips.testing.verticals.table.OrmMeasurementDetail;
import com.philips.testing.verticals.table.OrmMeasurementDetailType;
import com.philips.testing.verticals.table.OrmMeasurementGroup;
import com.philips.testing.verticals.table.OrmMeasurementGroupDetail;
import com.philips.testing.verticals.table.OrmMeasurementGroupDetailType;
import com.philips.testing.verticals.table.OrmMeasurementType;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentDetail;
import com.philips.testing.verticals.table.OrmMomentDetailType;
import com.philips.testing.verticals.table.OrmMomentType;
import com.philips.testing.verticals.table.OrmSettings;
import com.philips.testing.verticals.table.OrmSynchronisationData;

import org.joda.time.DateTime;

import javax.inject.Singleton;

public class OrmCreatorTest implements BaseAppDataCreator{

    private final UuidGenerator uuidGenerator;

    @Singleton
    public OrmCreatorTest(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @NonNull
    @Override
    public Moment createMoment(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type, DateTime expirationDate) {
        final OrmMomentType ormMomentType = new OrmMomentType(MomentType.getIDFromDescription(type), type);

        return new OrmMoment(creatorId, subjectId, ormMomentType, expirationDate);
    }

    @NonNull
    @Override
    public MomentDetail createMomentDetail(@NonNull String type, @NonNull Moment moment) {
        return createMomentDetail(type, (OrmMoment) moment);
    }

    @Override
    public Measurement createMeasurement(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return createMeasurement(type, (OrmMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public MeasurementDetail createMeasurementDetail(@NonNull String type, @NonNull Measurement measurement) {
        return createMeasurementDetail(type, (OrmMeasurement) measurement);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return createMeasurementGroup((OrmMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return createMeasurementGroup((OrmMoment) moment);
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return createMeasurementGroupDetail(type, (OrmMeasurementGroup) measurementGroup);
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return new OrmSynchronisationData(guid, inactive, lastModifiedTime, version);
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber) {
        return new OrmConsentDetail(type, status, version, deviceIdentificationNumber);
    }


    @NonNull
    @Override
    public Settings createSettings(String type, String value, final String timeZone) {
        return new OrmSettings(type, value);
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
    public OrmMomentDetail createMomentDetail(@NonNull final String type,
                                              @NonNull final OrmMoment moment) {
        OrmMomentDetailType ormMomentDetailType = new OrmMomentDetailType(MomentDetailType.getIDFromDescription(type), type);
        return new OrmMomentDetail(ormMomentDetailType, moment);
    }

    @NonNull
    public OrmMeasurement createMeasurement(@NonNull final String type,
                                            @NonNull final OrmMeasurementGroup ormMeasurementGroup) {
        OrmMeasurementType ormMeasurementType = new OrmMeasurementType(MeasurementType.getIDFromDescription(type),
                type,
                MeasurementType.getUnitFromDescription(type));
        return new OrmMeasurement(ormMeasurementType, ormMeasurementGroup);
    }

    @NonNull
    public OrmMeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                        @NonNull final OrmMeasurement measurement) {
        OrmMeasurementDetailType ormMeasurementDetailType = new OrmMeasurementDetailType(MeasurementDetailType.getIDFromDescription(type), type);
        return new OrmMeasurementDetail(ormMeasurementDetailType, measurement);
    }

    @NonNull
    public OrmMeasurementGroup createMeasurementGroup(@NonNull OrmMoment ormMoment) {
        return new OrmMeasurementGroup(ormMoment);
    }


    @NonNull
    public OrmMeasurementGroup createMeasurementGroup(@NonNull OrmMeasurementGroup ormMeasurementGroup) {
        return new OrmMeasurementGroup(ormMeasurementGroup);
    }

    @NonNull
    public OrmMeasurementGroupDetail createMeasurementGroupDetail(@NonNull final String type,
                                                                  @NonNull final OrmMeasurementGroup measurementGroup) {
        OrmMeasurementGroupDetailType ormMeasurementGroupDetailType = new OrmMeasurementGroupDetailType(MeasurementGroupDetailType.getIDFromDescription(type), type);
        return new OrmMeasurementGroupDetail(ormMeasurementGroupDetailType, measurementGroup);
    }

    //Insight
    @NonNull
    @Override
    public Insight createInsight() {
        return new OrmInsight();
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        return new OrmInsightMetaData(key, value, (OrmInsight) insight);
    }
}
