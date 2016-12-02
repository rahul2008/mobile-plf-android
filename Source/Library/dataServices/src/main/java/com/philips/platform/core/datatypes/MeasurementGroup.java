package com.philips.platform.core.datatypes;

import java.util.Collection;

/**
 * Created by 310218660 on 11/17/2016.
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
}
