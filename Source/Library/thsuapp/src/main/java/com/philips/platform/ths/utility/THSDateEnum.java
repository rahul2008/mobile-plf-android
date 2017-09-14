package com.philips.platform.ths.utility;


public enum THSDateEnum {
    DEFAULT(0),
    HIDEPREVIOUSDATE(1),
    HIDEFUTUREDATE(2);
    private int value;

    THSDateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
