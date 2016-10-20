/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

import com.philips.platform.appframework.database.EmptyForeignCollection;
import com.philips.platform.appframework.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMoment implements Moment, Serializable {

    public static final long serialVersionUID = 11L;
    public static final String NO_ID = "No ID";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String creatorId;

    @DatabaseField(canBeNull = false)
    private String subjectId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private com.philips.platform.appframework.database.table.OrmMomentType type;

    @DatabaseField(canBeNull = false)
    private DateTime dateTime = new DateTime();

    @DatabaseField
    private boolean synced;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurement> ormMeasurements = new EmptyForeignCollection<>();

    @ForeignCollectionField(eager = true)
    ForeignCollection< com.philips.platform.appframework.database.table.OrmMomentDetail> ormMomentDetails = new EmptyForeignCollection<>();

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true)
    private  com.philips.platform.appframework.database.table.OrmSynchronisationData synchronisationData;

    @DatabaseConstructor
    OrmMoment() {
    }

    public OrmMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final  com.philips.platform.appframework.database.table.OrmMomentType type) {
        this.creatorId = creatorId;
        this.subjectId = subjectId;
        this.type = type;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    @NonNull
    public String getAnalyticsId() {
        for ( com.philips.platform.appframework.database.table.OrmMomentDetail d : ormMomentDetails) {
            if (d.getType() == MomentDetailType.TAGGING_ID) {
                return d.getValue();
            }
        }

        return NO_ID;
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
    @NonNull
    public MomentType getType() {
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

    @Override
    public Collection<? extends OrmMeasurement> getMeasurements() {
        return ormMeasurements;
    }

    @Override
    public void addMeasurement(final Measurement measurement) {
        ormMeasurements.add((OrmMeasurement) measurement);
    }

    @Override
    public Collection<? extends com.philips.platform.appframework.database.table.OrmMomentDetail> getMomentDetails() {
        return ormMomentDetails;
    }

    @Override
    public void addMomentDetail(final MomentDetail momentDetail) {
        ormMomentDetails.add((com.philips.platform.appframework.database.table.OrmMomentDetail) momentDetail);
    }

    @Override
    @Nullable
    public com.philips.platform.appframework.database.table.OrmSynchronisationData getSynchronisationData() {
        return synchronisationData;
    }

    @Override
    public void setSynchronisationData(SynchronisationData synchronisationData) {
        this.synchronisationData = (com.philips.platform.appframework.database.table.OrmSynchronisationData) synchronisationData;
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
