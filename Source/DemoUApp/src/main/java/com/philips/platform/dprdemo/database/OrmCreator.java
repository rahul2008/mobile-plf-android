/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dprdemo.database;

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
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;

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
    public Moment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type) {
        return null;
    }

    @Override
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type,
                                           @NonNull final Moment moment) {
        return null;
    }


    @Override
    @NonNull
    public Measurement createMeasurement(@NonNull final String type,
                                         @NonNull final MeasurementGroup MeasurementGroup) {
        return null;
    }

    @Override
    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     @NonNull final Measurement measurement) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull final String guid, final boolean inactive,
                                                         @NonNull DateTime lastModifiedTime, final int version) {
        return null;
    }


    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber) {

        return new OrmConsentDetail(type, status, version, deviceIdentificationNumber);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value, @NonNull Characteristics characteristics) {
        return null;
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value) {
        return null;
    }

    @NonNull
    @Override
    public Settings createSettings(String type, String value) {
        return null;
    }





    //Insight
    @NonNull
    @Override
    public Insight createInsight() {
        return null;
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        return null;
    }

}
