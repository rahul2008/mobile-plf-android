package com.philips.platform.appframework.connectivity.models;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Measurement {
    public static final String TIME_STAMP = "timestamp";
    public static final String TYPE = "type";
    public static final String UNIT = "unit";
    public static final String VALUE = "value";
    public static final String DETAILS = "details";
    private ArrayList<MomentDetail> momentDetailArrayList = new ArrayList<MomentDetail>();
    private String timestamp;
    private String type;
    private String unit;
    private String value;

    public ArrayList<MomentDetail> getMomentDetailArrayList() {
        return momentDetailArrayList;
    }

    public void setMomentDetailArrayList(final ArrayList<MomentDetail> momentDetailArrayList) {
        this.momentDetailArrayList = momentDetailArrayList;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
