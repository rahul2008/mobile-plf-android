/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

public enum Phone {
    SAMSUNG_NOTE_4(Manufacturer.SAMSUNG, "SM-N910F"),
    SAMSUNG_S7(Manufacturer.SAMSUNG, "SM-G930F"),
    SAMSUNG_S4(Manufacturer.SAMSUNG, "GT-I9505"),
    NEXUS_6P(Manufacturer.HUAWEI, "Nexus 6P"),
    MOTOROLA_MOTO_G3(Manufacturer.MOTOROLA, "MotoG3");

    private Manufacturer manufacturer;
    private String modelName;

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    Phone(Manufacturer manufacturer, String modelName) {
        this.manufacturer = manufacturer;
        this.modelName = modelName;
    }
}