/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * Data-Base Interface for creating Moment Object
 */
public interface Moment extends BaseAppData, DateData, Serializable {
    String MOMENT_NEVER_SYNCED_AND_DELETED_GUID = "-1";

    String getCreatorId();

    String getSubjectId();

    DateTime getExpirationDate();

    String getType();

    void setDateTime(@NonNull DateTime dateTime);

    Collection<? extends MeasurementGroup> getMeasurementGroups();

    void addMeasurementGroup(MeasurementGroup measurementGroup);

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
