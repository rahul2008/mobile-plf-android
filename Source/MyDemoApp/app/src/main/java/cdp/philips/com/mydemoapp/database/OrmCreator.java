/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.utils.UuidGenerator;

import org.joda.time.DateTime;

import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementType;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMomentType;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmCreator implements BaseAppDataCreator {

    private final UuidGenerator uuidGenerator;

    public OrmCreator(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    @NonNull
    public OrmMoment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull MomentType type) {
        final OrmMoment ormMoment = createMomentWithoutUUID(creatorId, subjectId, type);
        final OrmMomentDetail ormMomentDetail = createMomentDetail(MomentDetailType.TAGGING_ID, ormMoment);

        ormMomentDetail.setValue(uuidGenerator.generateRandomUUID());
        ormMoment.addMomentDetail(ormMomentDetail);

        return ormMoment;
    }

    @NonNull
    @Override
    public OrmMoment createMomentWithoutUUID(@NonNull final String creatorId, @NonNull final String subjectId,
                                             @NonNull final MomentType type) {
        final OrmMomentType ormMomentType = new OrmMomentType(type);

        return new OrmMoment(creatorId, subjectId, ormMomentType);
    }

    @Override
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final MomentDetailType type,
                                           @NonNull final Moment moment) {
        return createMomentDetail(type, (OrmMoment) moment);
    }

    @Override
    @NonNull
    public Measurement createMeasurement(@NonNull final MeasurementType type,
                                         @NonNull final Moment moment) {
        return createMeasurement(type, (OrmMoment) moment);
    }

    @Override
    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                     @NonNull final Measurement measurement) {
        return createMeasurementDetail(type, (OrmMeasurement) measurement);
    }

    @NonNull
    @Override
    public OrmSynchronisationData createSynchronisationData(@NonNull final String guid, final boolean inactive,
                                                            @NonNull DateTime lastModifiedTime, final int version) {
        return new OrmSynchronisationData(guid, inactive, lastModifiedTime, version);
    }

    @NonNull
    public OrmMomentDetail createMomentDetail(@NonNull final MomentDetailType type,
                                              @NonNull final OrmMoment moment) {
        OrmMomentDetailType ormMomentDetailType = new OrmMomentDetailType(type);
        return new OrmMomentDetail(ormMomentDetailType, moment);
    }

    @NonNull
    public OrmMeasurement createMeasurement(@NonNull final MeasurementType type,
                                            @NonNull final OrmMoment moment) {
        OrmMeasurementType ormMeasurementType = new OrmMeasurementType(type);
        return new OrmMeasurement(ormMeasurementType, moment);
    }

    @NonNull
    public OrmMeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                        @NonNull final OrmMeasurement measurement) {
        OrmMeasurementDetailType ormMeasurementDetailType = new OrmMeasurementDetailType(type);
        return new OrmMeasurementDetail(ormMeasurementDetailType, measurement);
    }




}
