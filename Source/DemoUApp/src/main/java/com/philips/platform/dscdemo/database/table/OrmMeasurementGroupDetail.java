package com.philips.platform.dscdemo.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import java.io.Serializable;


/**
 * Created by 310218660 on 11/17/2016.
 */
@DatabaseTable
public class OrmMeasurementGroupDetail implements MeasurementGroupDetail, Serializable{

    private static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true, unique = true,canBeNull = false)
    private int id;


    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private OrmMeasurementGroupDetailType type;

    @DatabaseField(canBeNull = false)
    private String value;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMeasurementGroup ormMeasurementGroup;

    @DatabaseConstructor
    OrmMeasurementGroupDetail() {
    }

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