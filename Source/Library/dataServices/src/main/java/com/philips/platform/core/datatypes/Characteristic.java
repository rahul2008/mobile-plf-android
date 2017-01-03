package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Characteristic {
    public final static String USER_CHARACTERISTIC_TYPE = "BookmarkIDs";

    private List<Characteristics> characteristics;
    private String type;
    private String value;

    public Characteristic(final String type, final String value) {
        this.type = type;
        this.value = value;
    }

    public void setCharacteristics(List<Characteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}