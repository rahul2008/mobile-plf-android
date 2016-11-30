/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
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
                                  final String subjectId, String type) {
        return creator.createMoment(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(final String creatorId,
                                          final String subjectId, final String type) {
        return creator.createMomentWithoutUUID(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public OrmMomentDetail createMomentDetail(final String type,
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
    public OrmMeasurement createMeasurement(final String type,
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
    public OrmMeasurementDetail createMeasurementDetail(final String type,
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

    @NonNull
    @Override
    public Consent createConsent(@NonNull String creatorId) {
        return creator.createConsent(creatorId);
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber, boolean isSynchronized, @NonNull Consent consent) {
        try {
            OrmConsent ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            return creator.createConsentDetail(type, status, version, deviceIdentificationNumber, isSynchronized, ormConsent);
        } catch (OrmTypeChecking.OrmTypeException e) {
            throw new IllegalArgumentException("Consent was not OrmConsent");
        }
    }


}
