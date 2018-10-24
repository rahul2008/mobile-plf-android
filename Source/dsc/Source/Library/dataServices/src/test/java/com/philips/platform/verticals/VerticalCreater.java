/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.verticals;

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
import com.philips.testing.verticals.OrmTypeCheckingMock;
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

public class VerticalCreater implements BaseAppDataCreator {
    @NonNull
    @Override
    public TestMoment createMoment(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type, DateTime expirationDate) {
        final TestMomentType testMomentType = new TestMomentType(MomentType.getIDFromDescription(type), type);
        return new TestMoment(creatorId, subjectId, testMomentType, expirationDate);
    }

    @NonNull
    @Override
    public MomentDetail createMomentDetail(@NonNull String type, @NonNull Moment moment) {
        try {
            TestMomentDetailType testMomentDetailType = new TestMomentDetailType(MomentDetailType.getIDFromDescription(type), type);
            return new TestMomentDetail(testMomentDetailType, OrmTypeCheckingMock.checkOrmType(moment, TestMoment.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @Override
    public Measurement createMeasurement(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        try {
            TestMeasurementType testMeasurementType = new TestMeasurementType(MeasurementType.getIDFromDescription(type),
                    type,
                    MeasurementType.getUnitFromDescription(type));

            return new TestMeasurement(testMeasurementType, OrmTypeCheckingMock.checkOrmType(measurementGroup, TestMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementDetail createMeasurementDetail(@NonNull String type, @NonNull Measurement measurement) {
        TestMeasurementDetailType testMeasurementDetailType = new TestMeasurementDetailType(MeasurementDetailType.getIDFromDescription(type), type);
        try {
            return new TestMeasurementDetail(testMeasurementDetailType, OrmTypeCheckingMock.checkOrmType(measurement, TestMeasurement.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        try {
            return new TestMeasurementGroup(OrmTypeCheckingMock.checkOrmType(measurementGroup, TestMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        try {
            return new TestMeasurementGroup(OrmTypeCheckingMock.checkOrmType(moment, TestMoment.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        TestMeasurementGroupDetailType testMeasurementGroupDetailType = new TestMeasurementGroupDetailType(MeasurementGroupDetailType.getIDFromDescription(type), type);
        try {
            return new TestMeasurementGroupDetail(testMeasurementGroupDetailType, OrmTypeCheckingMock.checkOrmType(measurementGroup, TestMeasurementGroup.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
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
        return new TestInsight();
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        try {
            return new TestInsightMetaData(key, value, OrmTypeCheckingMock.checkOrmType(insight, TestInsight.class));
        } catch (OrmTypeCheckingMock.OrmTypeException e) {
            //Debug Log
        }
        return null;
    }
}
