/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import com.philips.pins.shinelib.capabilities.StreamIdentifier;

public class StreamData extends SHNDataRaw {

    private StreamIdentifier streamIdentifier;

    public StreamData(byte[] rawData, StreamIdentifier streamIdentifier) {
        super(rawData);
        this.streamIdentifier = streamIdentifier;
    }

    public StreamIdentifier getStreamIdentifier() {
        return streamIdentifier;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.StreamData;
    }
}
