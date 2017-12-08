/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataRespirationRate extends SHNData {
    private final int respirationRateBrpm;

    public SHNDataRespirationRate(int respirationRateBrpm) {
        super();
        this.respirationRateBrpm = respirationRateBrpm;
    }

    public int getRespirationRateBrpm() {
        return respirationRateBrpm;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.RespirationRate;
    }

    @Override
    public String toString() {
        return "RespirationRate: " + getRespirationRateBrpm();
    }
}
