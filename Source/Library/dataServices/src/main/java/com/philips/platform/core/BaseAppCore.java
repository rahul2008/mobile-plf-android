/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.ErrorMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.Backend;

import org.joda.time.DateTime;

import javax.inject.Inject;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppCore implements BaseAppDataCreator {
    @Inject
    Eventing eventing;

    @Inject
    BaseAppDataCreator database;

    @Inject
    DBMonitors dbMonitors;

    @Inject
    Backend appBackend;

    @Inject
    ErrorMonitor errorMonitor;

    @Inject
    public BaseAppCore() {
        DataServicesManager.getInstance().mAppComponent.injectBaseAppCore(this);
    }

    public void start() {
        try {
            dbMonitors.start(eventing);
            appBackend.start(eventing);
            errorMonitor.start(eventing);
        } catch (NullPointerException e) {
            if (e.getMessage() != null)
                DSLog.i("***SPO***", "e = " + e.getMessage());
        }
    }

    public void stop() {
        appBackend.stop();
        dbMonitors.stop();
        errorMonitor.stop();
    }

    @NonNull
    public Moment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type) {
        return database.createMoment(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final String type) {
        return database.createMomentWithoutUUID(creatorId, subjectId, type);
    }

    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type, @NonNull final Moment moment) {
        return database.createMomentDetail(type, moment);
    }

    @NonNull
    public Measurement createMeasurement(@NonNull final String type, @NonNull final MeasurementGroup measurementGroup) {
        return database.createMeasurement(type, measurementGroup);
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     @NonNull final Measurement measurement) {
        return database.createMeasurementDetail(type, measurement);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return database.createMeasurementGroup(measurementGroup);
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return database.createMeasurementGroup(moment);
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return database.createMeasurementGroupDetail(type, measurementGroup);
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return database.createSynchronisationData(guid, inactive, lastModifiedTime, version);
    }

    @NonNull
    @Override
    public Consent createConsent(@NonNull String creatorId) {
        return database.createConsent(creatorId);
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber, @NonNull boolean isSynchronized, @NonNull Consent consent) {
        return database.createConsentDetail(type, status, version, deviceIdentificationNumber, isSynchronized, consent);
    }


}