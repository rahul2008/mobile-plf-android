/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

enum Phone {
    Samsung_Note_4(Manufacturer.Samsung, "SM-N910F"),
    Samsung_S7(Manufacturer.Samsung, "SM-G930F"),
    Samsung_S4(Manufacturer.Samsung, "GT-I9505"),
    Nexus_6P(Manufacturer.Huawei, "Nexus 6P"),
    Motorola_Moto_G3(Manufacturer.Motorola, "MotoG3");

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