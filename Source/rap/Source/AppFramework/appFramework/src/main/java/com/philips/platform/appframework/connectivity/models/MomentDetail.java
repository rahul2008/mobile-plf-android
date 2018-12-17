package com.philips.platform.appframework.connectivity.models;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentDetail {
    public static final String TYPE="type";
    public static final String VALUE="value";
    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
