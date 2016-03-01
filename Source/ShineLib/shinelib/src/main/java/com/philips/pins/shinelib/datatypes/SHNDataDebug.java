/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
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
