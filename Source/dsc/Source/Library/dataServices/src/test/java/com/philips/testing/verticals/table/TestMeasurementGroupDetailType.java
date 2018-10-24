package com.philips.testing.verticals.table;

import java.io.Serializable;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class TestMeasurementGroupDetailType implements Serializable {
    private static final long serialVersionUID = 11L;

    private int id;

    private String description;


    public TestMeasurementGroupDetailType(final int id, final String momentType) {
        this.id = id;
        this.description = momentType;
    }

    public String getType() {
        return description;
    }

    @Override
    public String toString() {
        return "[TestMeasurementDetailType, id=" + id + ", description=" + description + "]";
    }
}