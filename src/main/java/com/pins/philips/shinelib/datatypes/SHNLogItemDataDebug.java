package com.pins.philips.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNLogItemDataDebug implements SHNLogItemData {
    private final long data;

    public SHNLogItemDataDebug(long data) {
        this.data = data;
    }

    public long getData() {
        return data;
    }
}
