package com.philips.platform.datasync.characteristics;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UCoreCharacteristicsDetail {


    private final String type;
    private final String value;

    public UCoreCharacteristicsDetail(String type, String value) {
        this.type = type;

        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
