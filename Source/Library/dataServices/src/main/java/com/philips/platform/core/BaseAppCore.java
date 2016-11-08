/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.monitors.DBMonitors;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppCore implements BaseAppDataCreator {

    private final Eventing eventing;
    BaseAppDataCreator database;
    private DBMonitors dbMonitors;
    private final BaseAppBackend appBackend;
    private List<EventMonitor> eventMonitors;

    @Inject
    public BaseAppCore(@NonNull final Eventing eventing, @NonNull final BaseAppDataCreator database, final BaseAppBackend backend, @NonNull List<EventMonitor> eventMonitors, final DBMonitors dbMonitors) {
        this.eventing = eventing;
        this.database = database;
        this.appBackend = backend;
        this.eventMonitors = eventMonitors;
        this.dbMonitors = dbMonitors;
    }

    public void start() {
        try {
            dbMonitors.start(eventing);
            appBackend.start(eventing);

            for (EventMonitor eventMonitor : eventMonitors) {
                eventMonitor.start(eventing);
            }
        }catch (NullPointerException e){
            if (e.getMessage() != null)
                Log.i("***SPO***", "e = " + e.getMessage());
        }
    }

    public void stop() {
        for (EventMonitor eventMonitor : eventMonitors) {
            eventMonitor.stop();
        }

        appBackend.stop();
        dbMonitors.stop();
    }

    @NonNull
    public Moment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull MomentType type) {
        return database.createMoment(creatorId, subjectId, type);
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final MomentType type) {
        return database.createMomentWithoutUUID(creatorId, subjectId, type);
    }

    @NonNull
    public MomentDetail createMomentDetail(@NonNull final MomentDetailType type, @NonNull final Moment moment) {
        return database.createMomentDetail(type, moment);
    }

    @NonNull
    public Measurement createMeasurement(@NonNull final MeasurementType type, @NonNull final Moment moment) {
        return database.createMeasurement(type, moment);
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                     @NonNull final Measurement measurement) {
        return database.createMeasurementDetail(type, measurement);
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return database.createSynchronisationData(guid, inactive, lastModifiedTime, version);
    }


}