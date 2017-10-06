/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

enum Manufacturer {
    Samsung("samsung"),
    Huawei("huawei"),
    Motorola("motorola");

    private String name;

    public String getName() {
        return name;
    }

    Manufacturer(String name) {
        this.name = name;
    }
}
