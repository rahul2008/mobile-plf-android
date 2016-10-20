/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import com.philips.platform.appframework.database.table.OrmMeasurement;
import com.philips.platform.appframework.database.table.OrmMeasurementDetail;
import com.philips.platform.appframework.database.table.OrmMoment;
import com.philips.platform.appframework.database.table.OrmMomentDetail;

/*import org.joda.time.DateTime;*/


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Database implements BaseAppDataCreator {
    @NonNull
    private com.philips.platform.appframework.database.OrmCreator creator;

    public Database(@NonNull com.philips.platform.appframework.database.OrmCreator creator) {
        this.creator = creator;
    }

    @Override
    @NonNull
    public OrmMoment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull MomentType type) {
        return creator.createMoment(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final MomentType type) {
        return creator.createMomentWithoutUUID(creatorId ,subjectId, type);
    }

    @Override
    @NonNull
    public OrmMomentDetail createMomentDetail(@NonNull final MomentDetailType type,
                                                                                       @NonNull final Moment moment) {
        try {
            OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            return creator.createMomentDetail(type, ormMoment);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Moment was not OrmMoment");
        }
    }

    @Override
    @NonNull
    public OrmMeasurement createMeasurement(@NonNull final MeasurementType type,
                                                                                     @NonNull final Moment moment) {
        try {
            OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            return creator.createMeasurement(type, ormMoment);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Moment was not OrmMoment");
        }
    }

    @Override
    @NonNull
    public OrmMeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                                                                 @NonNull final Measurement measurement) {
        try {
            OrmMeasurement ormMeasurement = OrmTypeChecking.checkOrmType(measurement, OrmMeasurement.class);
            return creator.createMeasurementDetail(type, ormMeasurement);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Measurement was not OrmMeasurement");
        }
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return creator.createSynchronisationData(guid, inactive, lastModifiedTime, version);
    }


}
