package com.philips.pins.shinelib.protocols.moonshinestreaming;

public enum MoonshineStreamIdentifier {
    STREAM_0((byte)0x00),
    STREAM_1((byte)0x80),
    STREAM_2((byte)0xC0);

    private byte headerValue;

    MoonshineStreamIdentifier(byte headerValue) {
        this.headerValue = headerValue;
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
}
