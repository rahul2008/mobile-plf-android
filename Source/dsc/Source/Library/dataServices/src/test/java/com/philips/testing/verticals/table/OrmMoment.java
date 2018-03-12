/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.testing.verticals.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmMoment implements Moment, Serializable {

    private static final long serialVersionUID = 11L;
    public static final String NO_ID = "No ID";
    private int id;

    private String creatorId;

    private String subjectId;

    private OrmMomentType type;

    private DateTime dateTime = new DateTime();

    private boolean synced;

    List<OrmMomentDetail> ormMomentDetails = new ArrayList<>();

    List<OrmMeasurementGroup> ormMeasurementGroups = new ArrayList<>();

    private OrmSynchronisationData synchronisationData;
    private DateTime expirationDate;

    public OrmMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final OrmMomentType type, DateTime expirationDate) {
        this.creatorId = creatorId;
        this.subjectId = subjectId;
        this.expirationDate = expirationDate;
        this.type = type;
        this.id = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String getAnalyticsId() {
        return null;
    }

    @Override
    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }


    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public String getSubjectId() {
        return subjectId;
    }

    @Override
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    @NonNull
    public String getType() {
        return type.getType();
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public void setDateTime(@NonNull final DateTime dateTime) {
        this.dateTime = dateTime;
    }

   /* @Override
    public Collection<? extends OrmMeasurement> getMeasurements() {
        return ormMeasurements;
    }

    @Override
    public void addMeasurement(final Measurement measurement) {
        ormMeasurements.add((OrmMeasurement) measurement);
    }*/

    @Override
    public Collection<? extends OrmMeasurementGroup> getMeasurementGroups() {
        return ormMeasurementGroups;
    }

    @Override
    public void addMeasurementGroup(final MeasurementGroup measurementGroup) {
        ormMeasurementGroups.add((OrmMeasurementGroup) measurementGroup);
    }

    @Override
    public Collection<? extends OrmMomentDetail> getMomentDetails() {
        return ormMomentDetails;
    }

    @Override
    public void addMomentDetail(final MomentDetail momentDetail) {
        ormMomentDetails.add((OrmMomentDetail) momentDetail);
    }

    @Override
    @Nullable
    public OrmSynchronisationData getSynchronisationData() {
        return synchronisationData;
    }

    @Override
    public void setSynchronisationData(SynchronisationData synchronisationData) {
        this.synchronisationData = (OrmSynchronisationData) synchronisationData;
    }

    @Override
    public String toString() {
        return "[OrmMoment, id=" + id + ", creatorId=" + creatorId + ", subjectId=" + subjectId + ", ormMomentType=" + type + ", dateTime=" + dateTime + "]";
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

}
