/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

public enum Manufacturer {
    SAMSUNG("samsung"),
    HUAWEI("huawei"),
    MOTOROLA("motorola");

    private String name;

    public String getName() {
        return name;
    }

    Manufacturer(String name) {
        this.name = name;
    }
}
