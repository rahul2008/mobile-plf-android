/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.verticals;

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
import com.philips.testing.verticals.OrmTypeCheckingMock;
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

public class VerticalCreater implements BaseAppDataCreator {
    @NonNull
    @Override
    public OrmMoment createMoment(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type, DateTime expirationDate) {
        final OrmMomentType ormMomentType = new OrmMomentType(MomentType.getIDFromDescription(type), type);
        return new OrmMoment(creatorId, subjectId, ormMomentType, expirationDate);
    }

    @NonNull
    @Override
    public MomentDetail createMomentDetail(@NonNull String type, @NonNull Moment moment) {
        try {
            OrmMomentDetailType ormMomentDetailType = new OrmMomentDetailType(MomentDetailType.getIDFromDescription(type), type);
            return new OrmMomentDetail(ormMomentDetailType, OrmTypeCheckingMock.checkOrmType(moment, OrmMoment.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @Override
    public Measurement createMeasurement(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        try {
            OrmMeasurementType ormMeasurementType = new OrmMeasurementType(MeasurementType.getIDFromDescription(type),
                    type,
                    MeasurementType.getUnitFromDescription(type));

            return new OrmMeasurement(ormMeasurementType, OrmTypeCheckingMock.checkOrmType(measurementGroup, OrmMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementDetail createMeasurementDetail(@NonNull String type, @NonNull Measurement measurement) {
        OrmMeasurementDetailType ormMeasurementDetailType = new OrmMeasurementDetailType(MeasurementDetailType.getIDFromDescription(type), type);
        try {
            return new OrmMeasurementDetail(ormMeasurementDetailType, OrmTypeCheckingMock.checkOrmType(measurement, OrmMeasurement.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        try {
            return new OrmMeasurementGroup(OrmTypeCheckingMock.checkOrmType(measurementGroup, OrmMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        try {
            return new OrmMeasurementGroup(OrmTypeCheckingMock.checkOrmType(moment, OrmMoment.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        OrmMeasurementGroupDetailType ormMeasurementGroupDetailType = new OrmMeasurementGroupDetailType(MeasurementGroupDetailType.getIDFromDescription(type), type);
        try {
            return new OrmMeasurementGroupDetail(ormMeasurementGroupDetailType, OrmTypeCheckingMock.checkOrmType(measurementGroup, OrmMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return new OrmSynchronisationData(guid, inactive, lastModifiedTime, version);
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber) {
        return new OrmConsentDetail("SLEEP", "Accepted", "1.0", "");
    }

    @NonNull
    @Override
    public Settings createSettings(String type, String value, final String timeZone) {
        return new OrmSettings(type, value);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value, @NonNull Characteristics characteristics) {
        try {
            return new OrmCharacteristics(type, value, OrmTypeCheckingMock.checkOrmType(characteristics, OrmCharacteristics.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value) {
        return new OrmCharacteristics(type, value);
    }

    @NonNull
    @Override
    public Insight createInsight() {
        return new OrmInsight();
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        try {
            return new OrmInsightMetaData(key, value, OrmTypeCheckingMock.checkOrmType(insight, OrmInsight.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }
}
