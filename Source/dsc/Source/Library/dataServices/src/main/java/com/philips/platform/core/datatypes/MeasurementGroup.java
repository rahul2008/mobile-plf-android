/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import java.util.Collection;

/**
 * Data-Base Interface for creating MeasurementGroup Object
 */
public interface MeasurementGroup {
    Collection<? extends Measurement> getMeasurements();

    public Collection<? extends MeasurementGroup> getMeasurementGroups();

    void addMeasurement(Measurement measurement);

    void addMeasurementGroup(MeasurementGroup measurementGroup);

    public Collection<? extends MeasurementGroupDetail> getMeasurementGroupDetails();

    public void addMeasurementGroupDetail(final MeasurementGroupDetail measurementGroupDetail);

    int getId();

    void setId(int id);

    void setMeasurementGroups(Collection<? extends MeasurementGroup> groups);
}
