/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Moment extends BaseAppData, DateData, Serializable {
    String MOMENT_NEVER_SYNCED_AND_DELETED_GUID = "-1";

    String getCreatorId();

    String getSubjectId();


    com.philips.platform.core.datatypes.MomentType getType();

    void setDateTime(@NonNull DateTime dateTime);

    Collection<? extends Measurement> getMeasurements();

    void addMeasurement(Measurement measurement);

    Collection<? extends com.philips.platform.core.datatypes.MomentDetail> getMomentDetails();

    void addMomentDetail(com.philips.platform.core.datatypes.MomentDetail momentDetail);

    @Nullable
    com.philips.platform.core.datatypes.SynchronisationData getSynchronisationData();

    void setSynchronisationData(com.philips.platform.core.datatypes.SynchronisationData synchronisationData);

    void setSynced(boolean b);

    void setId(int id);

    @NonNull
    String getAnalyticsId();
}
