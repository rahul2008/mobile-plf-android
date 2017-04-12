/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

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
import com.philips.platform.core.utils.BlobDataCreater;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.blob.BlobMetaData;
import com.philips.platform.datasync.insights.UCoreInsight;

import org.joda.time.DateTime;

import java.util.Map;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.database.datatypes.MeasurementDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementGroupDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.database.table.OrmBlobMetaData;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmInsight;
import cdp.philips.com.mydemoapp.database.table.OrmInsightMetaData;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroup;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroupDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroupDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementType;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMomentType;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmCreator implements BaseAppDataCreator, BlobDataCreater {

    private final UuidGenerator uuidGenerator;

    @Singleton
    public OrmCreator(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;

    }

    @Override
    @NonNull
    public OrmMoment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type) {
        final OrmMomentType ormMomentType = new OrmMomentType(MomentType.getIDFromDescription(type), type);

        return new OrmMoment(creatorId, subjectId, ormMomentType);
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
    public Settings createSettings(String type, String value) {
        return new OrmSettings(type, value);
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

    @NonNull
    @Override
    public BlobMetaData createBlobMetaData() {
        return new OrmBlobMetaData();
    }

}
