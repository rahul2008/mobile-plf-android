package com.philips.platform.dscdemo.database.table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.dscdemo.database.EmptyForeignCollection;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by 310218660 on 11/17/2016.
 */
@DatabaseTable
public class OrmMeasurementGroup implements MeasurementGroup, Serializable {
    private static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true, unique = true,canBeNull = false)
    private int id;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurement> ormMeasurements = new EmptyForeignCollection<>();

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = true)
    private OrmMoment ormMoment;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = true)
    private OrmMeasurementGroup ormMeasurementGroup;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurementGroupDetail> ormMeasurementGroupDetails = new EmptyForeignCollection<>();

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurementGroup> ormMeasurementGroups = new EmptyForeignCollection<>();

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
        ormMeasurementGroups = (ForeignCollection<OrmMeasurementGroup>) groups;
    }

    @DatabaseConstructor
    public OrmMeasurementGroup() {
    }

/*    @Override
    public Collection<? extends OrmMeasurement> getMeasurements() {
        return ormMeasurements;
    }

    @Override
    public void addMeasurement(final Measurement measurement) {
        ormMeasurements.add((OrmMeasurement) measurement);
    }*/
}
