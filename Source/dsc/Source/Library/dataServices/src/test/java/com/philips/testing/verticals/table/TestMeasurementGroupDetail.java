package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.MeasurementGroupDetail;

import java.io.Serializable;

/**
 * Created by 310218660 on 11/17/2016.
 */
public class TestMeasurementGroupDetail implements MeasurementGroupDetail, Serializable {

    private static final long serialVersionUID = 11L;
    private int id;

    private TestMeasurementGroupDetailType type;

    private String value;
    private TestMeasurementGroup testMeasurementGroup;


    @Override
    public int getId() {
        return id;
    }

    public TestMeasurementGroupDetail(final TestMeasurementGroupDetailType type, final TestMeasurementGroup testMeasurementGroup) {
        this.type = type;
        this.testMeasurementGroup = testMeasurementGroup;
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
    public TestMeasurementGroup getOrmMeasurementGroup() {
        return testMeasurementGroup;
    }

    @Override
    public String toString() {
        return "[TestMeasurementDetail, id=" + id + ", ormMeasurementDetailType=" + type + ", value=" + value + ", ormMeasurement=" + testMeasurementGroup + "]";
    }
}