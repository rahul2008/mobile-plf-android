package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class OrmMeasurementGroup implements MeasurementGroup, Serializable {
    private static final long serialVersionUID = 11L;

    private int id;

    List<OrmMeasurement> ormMeasurements = new ArrayList<>();

    private OrmMoment ormMoment;

    private OrmMeasurementGroup ormMeasurementGroup;

    List<OrmMeasurementGroupDetail> ormMeasurementGroupDetails = new ArrayList<>();

    List<OrmMeasurementGroup> ormMeasurementGroups = new ArrayList<>();

    public OrmMeasurementGroup(OrmMeasurementGroup ormMeasurementGroup) {
        this.ormMeasurementGroup = ormMeasurementGroup;
        this.id = -1;
    }

    public OrmMeasurementGroup(OrmMoment ormMoment) {
        this.ormMoment = ormMoment;
        this.id = -1;
    }

    @Override
    public Collection<? extends OrmMeasurement> getMeasurements() {
        return ormMeasurements;
    }

    @Override
    public Collection<? extends OrmMeasurementGroup> getMeasurementGroups() {
        return ormMeasurementGroups;
    }

    @Override
    public void addMeasurement(Measurement measurement) {
        ormMeasurements.add((OrmMeasurement) measurement);
    }

    @Override
    public void addMeasurementGroup(MeasurementGroup measurementGroup) {
        ormMeasurementGroups.add((OrmMeasurementGroup) measurementGroup);
    }

    @Override
    public Collection<? extends OrmMeasurementGroupDetail> getMeasurementGroupDetails() {
        return ormMeasurementGroupDetails;
    }

    @Override
    public void addMeasurementGroupDetail(final MeasurementGroupDetail measurementGroupDetail) {
        ormMeasurementGroupDetails.add((OrmMeasurementGroupDetail) measurementGroupDetail);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setMeasurementGroups(Collection<? extends MeasurementGroup> groups) {

    }
}
