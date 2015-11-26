package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 26/11/15.
 */
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
