/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.dataservices.database;

import android.support.annotation.NonNull;

import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristicsDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsent;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroup;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.utils.UuidGenerator;

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
    public OrmMoment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type) {
        final OrmMoment ormMoment = createMomentWithoutUUID(creatorId, subjectId, type);
        //   final OrmMomentDetail ormMomentDetail = createMomentDetail(MomentDetailType.TAGGING_ID, ormMoment);

        //    ormMomentDetail.setValue(uuidGenerator.generateRandomUUID());
        //    ormMoment.addMomentDetail(ormMomentDetail);

        return ormMoment;
    }

    @NonNull
    @Override
    public OrmMoment createMomentWithoutUUID(@NonNull final String creatorId, @NonNull final String subjectId,
                                             @NonNull final String type) {
        final OrmMomentType ormMomentType = new OrmMomentType(MomentType.getIDFromDescription(type), type);

        return new OrmMoment(creatorId, subjectId, ormMomentType);
    }

    @Override
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type,
                                           @NonNull final Moment moment) {
        return createMomentDetail(type, (OrmMoment) moment);
    }

//    @NonNull
//    @Override
//    public Measurement createMeasurement(@NonNull String type, @NonNull Moment moment) {
//        return createMeasurement(type, (OrmMoment) moment);
//    }

   /* @Override
    @NonNull
    public Measurement createMeasurement(@NonNull final String type,
                                         @NonNull final Moment moment) {
        return createMeasurement(type, (OrmMoment) moment);
    }*/

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
    public Consent createConsent(@NonNull String creatorId) {
        return new OrmConsent(creatorId);
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber, boolean isSynchronized, @NonNull Consent consent) {

        return new OrmConsentDetail(type, status, version, deviceIdentificationNumber, (OrmConsent) consent, isSynchronized);
    }

    @NonNull
    @Override
    public UserCharacteristics createCharacteristics(@NonNull String creatorId) {
        return new OrmCharacteristics(creatorId);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristicsDetails(@NonNull String type, @NonNull String value, @NonNull UserCharacteristics userCharacteristics, @NonNull Characteristics characteristics) {
        return new OrmCharacteristicsDetail(type,value,(OrmCharacteristics) userCharacteristics,(OrmCharacteristicsDetail) characteristics);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristicsDetails(@NonNull String type, @NonNull String value, @NonNull UserCharacteristics userCharacteristics) {
        return new OrmCharacteristicsDetail(type,value,(OrmCharacteristics) userCharacteristics);
    }

    @NonNull
    public OrmMomentDetail createMomentDetail(@NonNull final String type,
                                              @NonNull final OrmMoment moment) {
        OrmMomentDetailType ormMomentDetailType = new OrmMomentDetailType(MomentDetailType.getIDFromDescription(type), type);
        return new OrmMomentDetail(ormMomentDetailType, moment);
    }

    /*@NonNull
    public OrmMeasurement createMeasurement(@NonNull final MeasurementType type,
    @NonNull
    public OrmMeasurement createMeasurement(@NonNull final String type,
                                            @NonNull final OrmMoment moment) {
        OrmMeasurementType ormMeasurementType = new OrmMeasurementType(MeasurementType.getIDFromDescription(type),
                type,
                MeasurementType.getUnitFromDescription(type));
        return new OrmMeasurement(ormMeasurementType, moment);
    }*/

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
}
