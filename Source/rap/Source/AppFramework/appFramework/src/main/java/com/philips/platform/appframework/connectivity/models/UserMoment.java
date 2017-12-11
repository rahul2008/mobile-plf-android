package com.philips.platform.appframework.connectivity.models;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserMoment {
    public static final String DETAILS = "details";
    public static final String MEASUREMENTS = "measurements";
    public static final String TIME_STAMP = "timestamp";
    public static final String TYPE = "type";
    private ArrayList<MomentDetail> momentDetailArrayList = new ArrayList<MomentDetail>();
    private ArrayList<Measurement> measurementArrayList = new ArrayList<Measurement>();
    private String timestamp;
    private String type;

    public ArrayList<MomentDetail> getMomentDetailArrayList() {
        return momentDetailArrayList;
    }

    public void setMomentDetailArrayList(final ArrayList<MomentDetail> momentDetailArrayList) {
        this.momentDetailArrayList = momentDetailArrayList;
    }

    public ArrayList<Measurement> getMeasurementArrayList() {
        return measurementArrayList;
    }

    public void setMeasurementArrayList(final ArrayList<Measurement> measurementArrayList) {
        this.measurementArrayList = measurementArrayList;
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
}
