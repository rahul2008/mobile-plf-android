package com.philips.pins.shinelib.datatypes;

public class SHNStreamData extends SHNDataRaw {

    private StreamIdentifier streamIdentifier;

    public SHNStreamData(byte[] rawData, StreamIdentifier streamIdentifier) {
        super(rawData);
        this.streamIdentifier = streamIdentifier;
    }

    public StreamIdentifier getStreamIdentifier() {
        return streamIdentifier;
    }
}
