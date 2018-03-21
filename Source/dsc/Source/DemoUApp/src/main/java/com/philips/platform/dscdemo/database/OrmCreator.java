/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database;

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
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dscdemo.database.datatypes.MeasurementDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementType;
import com.philips.platform.dscdemo.database.datatypes.MomentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementType;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmMomentDetailType;
import com.philips.platform.dscdemo.database.table.OrmMomentType;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;

import org.joda.time.DateTime;

import javax.inject.Singleton;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmCreator implements BaseAppDataCreator {

    private final UuidGenerator uuidGenerator;

    @Singleton
    public OrmCreator(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;

    }

    @Override
    @NonNull
    public OrmMoment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type, DateTime expirationDate) {
        final OrmMomentType ormMomentType = new OrmMomentType(MomentType.getIDFromDescription(type), type);

        return new OrmMoment(creatorId, subjectId, ormMomentType, expirationDate);
    }

    @Override
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type,
                                           @NonNull final Moment moment) {
        return createMomentDetail(type, (OrmMoment) moment);
    }


    @Override
    @NonNull
    public Measurement createMeasurement(@NonNull final String type,
                                         @NonNull final MeasurementGroup MeasurementGroup) {
        return createMeasurement(type, (OrmMeasurementGroup) MeasurementGroup);
    }

    @Override
    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     @NonNull final Measurement measurement) {
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
    public OrmSynchronisationData createSynchronisationData(@NonNull final String guid, final boolean inactive,
                                                            @NonNull DateTime lastModifiedTime, final int version) {
        return new OrmSynchronisationData(guid, inactive, lastModifiedTime, version);
    }


    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber) {

        return new OrmConsentDetail(type, status, version, deviceIdentificationNumber);
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
    @Override
    public Settings createSettings(String unitSystem, String locale, String timeZone) {
        return new OrmSettings(unitSystem, locale, timeZone);
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
