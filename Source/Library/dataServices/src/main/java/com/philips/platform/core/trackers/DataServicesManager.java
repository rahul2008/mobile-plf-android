/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DataServicesManager {

    @NonNull
    private final Eventing eventing;

    @NonNull
    private final BaseAppDataCreator dataCreator;

    @Inject
    DataPullSynchronise mDataPullSynchronise;

    @Inject
    DataPushSynchronise mDataPushSynchronise;

    private BackendIdProvider backendIdProvider;

    @Inject
    public DataServicesManager(@NonNull final Eventing eventing, @NonNull final BaseAppDataCreator dataCreator, @NonNull final BackendIdProvider backendIdProvider) {
        this.eventing = eventing;
        this.dataCreator = dataCreator;
        this.backendIdProvider = backendIdProvider;
    }

    @NonNull
    public Moment save(@NonNull final Moment moment) {
        eventing.post(new MomentSaveRequest(moment));
        return moment;
    }

    public Moment update(@NonNull final Moment moment) {
        eventing.post(new MomentUpdateRequest(moment));
        return moment;
    }

    public void fetch(final @NonNull MomentType... type){
        eventing.post(new LoadMomentsRequest(type[0]));
    }

    @NonNull
    public Moment createMoment(@NonNull final MomentType type) {
        return dataCreator.createMoment(backendIdProvider.getUserId(), backendIdProvider.getSubjectId(), type);
    }

    @NonNull
    public MomentDetail createMomentDetail(@NonNull final MomentDetailType type, @NonNull final Moment moment) {
        MomentDetail momentDetail = dataCreator.createMomentDetail(type, moment);
        moment.addMomentDetail(momentDetail);
        return momentDetail;
    }

    @NonNull
    public Measurement createMeasurement(@NonNull final MeasurementType type, @NonNull final Moment moment) {
        Measurement measurement = dataCreator.createMeasurement(type, moment);
        moment.addMeasurement(measurement);
        return measurement;
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                     @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = dataCreator.createMeasurementDetail(type, measurement);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    public void deleteMoment(final Moment moment) {
        eventing.post(new MomentDeleteRequest(moment));
    }

    public void updateMoment(Moment moment){
        eventing.post((new MomentUpdateRequest(moment)));
    }

    public void syncData(){
        synchronize();
      //  sendPushEvent();
        sendPullDataEvent();
    }

 /*   private void sendPushEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("***SPO***", "In DataServicesManager.sendPushEvent");
                eventing.post(new WriteDataToBackendRequest());
            }
        }, 20 * DateTimeConstants.MILLIS_PER_SECOND);

    }*/

    private void sendPullDataEvent() {
        Log.i("***SPO***", "In DataServicesManager.sendPullDataEvent");
        eventing.post(new ReadDataFromBackendRequest(null));
    }

    private void synchronize() {
        Log.i("***SPO***", "In DataServicesManager.Synchronize");
        SynchronisationMonitor monitor = new SynchronisationMonitor(mDataPullSynchronise,mDataPushSynchronise);
        monitor.start(eventing);
    }
}
