/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataRaw extends SHNData {
    private byte[] data;

    public  SHNDataRaw(byte[] data) {
        this.data = data;
    }

    public byte[] getRawData() {
        return data.clone();
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.Raw;
    }
}
