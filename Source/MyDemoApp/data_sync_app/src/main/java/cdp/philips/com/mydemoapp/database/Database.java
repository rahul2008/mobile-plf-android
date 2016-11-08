/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

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

import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;

/*import org.joda.time.DateTime;*/


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Database implements BaseAppDataCreator {
    @NonNull
    final private OrmCreator creator;

    public Database(OrmCreator creator) {
        this.creator = creator;
    }

    @NonNull
    @Override
    public OrmMoment createMoment(final String creatorId,
                                  final String subjectId, MomentType type) {
        return creator.createMoment(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(final String creatorId,
                                          final String subjectId, final MomentType type) {
        return creator.createMomentWithoutUUID(creatorId ,subjectId, type);
    }

    @NonNull
    @Override
    public OrmMomentDetail createMomentDetail(final MomentDetailType type,
                                              final Moment moment) {
        try {
            OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            return creator.createMomentDetail(type, ormMoment);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Moment was not OrmMoment");
        }
    }

    @NonNull
    @Override
    public OrmMeasurement createMeasurement(final MeasurementType type,
                                            final Moment moment) {
        try {
            final OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            return creator.createMeasurement(type, ormMoment);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Moment was not OrmMoment");
        }
    }

    @NonNull
    @Override
    public OrmMeasurementDetail createMeasurementDetail(final MeasurementDetailType type,
                                                        final Measurement measurement) {
        try {
            final OrmMeasurement ormMeasurement =
                    OrmTypeChecking.checkOrmType(measurement, OrmMeasurement.class);
            return creator.createMeasurementDetail(type, ormMeasurement);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Measurement was not OrmMeasurement");
        }
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(String guid, boolean inactive,
                                                         DateTime lastModifiedTime, int version) {
        return creator.createSynchronisationData(guid, inactive, lastModifiedTime, version);
    }


}
