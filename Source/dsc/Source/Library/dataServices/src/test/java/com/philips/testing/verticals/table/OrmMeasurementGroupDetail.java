package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.MeasurementGroupDetail;

import java.io.Serializable;

/**
 * Created by 310218660 on 11/17/2016.
 */
public class OrmMeasurementGroupDetail implements MeasurementGroupDetail, Serializable {

    private static final long serialVersionUID = 11L;
    private int id;

    private OrmMeasurementGroupDetailType type;

    private String value;
    private OrmMeasurementGroup ormMeasurementGroup;


    @Override
    public int getId() {
        return id;
    }

    public OrmMeasurementGroupDetail(final OrmMeasurementGroupDetailType type, final OrmMeasurementGroup ormMeasurementGroup) {
        this.type = type;
        this.ormMeasurementGroup = ormMeasurementGroup;
        this.id = -1;
    }

    @Override
    public String getType() {
        return type.getType();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public OrmMeasurementGroup getOrmMeasurementGroup() {
        return ormMeasurementGroup;
    }

    @Override
    public String toString() {
        return "[OrmMeasurementDetail, id=" + id + ", ormMeasurementDetailType=" + type + ", value=" + value + ", ormMeasurement=" + ormMeasurementGroup + "]";
    }
}