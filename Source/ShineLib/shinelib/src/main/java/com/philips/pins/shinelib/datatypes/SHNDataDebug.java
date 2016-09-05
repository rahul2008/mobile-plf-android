/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataDebug extends SHNData {
    private final long data;

    public SHNDataDebug(long data) {
        this.data = data;
    }

    public long getData() {
        return data;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.DebugMoonshine;
    }

    @Override
    public String toString() {
        return "DebugMoonshine: " + getData();
    }
}
