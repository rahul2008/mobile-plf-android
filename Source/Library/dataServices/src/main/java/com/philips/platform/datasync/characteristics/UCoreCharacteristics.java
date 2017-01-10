/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import java.util.List;

public class UCoreCharacteristics{
    private String type;
    private String value;

    public UCoreCharacteristics(String type, String value, List<UCoreCharacteristics> characteristics) {
        this.type = type;
        this.value = value;
        this.characteristics = characteristics;
    }

    private List<UCoreCharacteristics> characteristics;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<UCoreCharacteristics> getCharacteristics() {
        return characteristics;
    }
}