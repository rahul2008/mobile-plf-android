package com.philips.pins.shinelib.protocols.moonshinestreaming;

public enum MoonshineStreamIdentifier {
    STREAM_0(0, (byte)0x00),
    STREAM_1(1, (byte)0x80),
    STREAM_2(2, (byte)0xC0);

    private int value;
    private byte headerValue;

    MoonshineStreamIdentifier(int value, byte headerValue) {
        this.value = value;
        this.headerValue = headerValue;
    }

    public int getValue() {
        return value;
    }

    public byte getHeaderValue() {
        return this.headerValue;
    }

    public static MoonshineStreamIdentifier fromHeaderValue(byte headerValue) {
        for(MoonshineStreamIdentifier identifier : values()) {
            if (identifier.getHeaderValue() == headerValue) {
                return identifier;
            }
        }
        return STREAM_1;
    }

    public static MoonshineStreamIdentifier fromValue(int value) {
        for (MoonshineStreamIdentifier identifier : values()) {
            if (identifier.getValue() == value) {
                return identifier;
            }
        }
        return STREAM_1;
    }
}
