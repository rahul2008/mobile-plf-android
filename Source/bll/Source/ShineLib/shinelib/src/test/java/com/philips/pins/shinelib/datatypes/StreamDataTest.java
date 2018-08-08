/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import com.philips.pins.shinelib.capabilities.StreamIdentifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class StreamDataTest {

    @Test
    public void itShouldConstructStreamData() {
        StreamData subject = new StreamData(null, StreamIdentifier.STREAM_0);
        assertNotNull(subject);
    }

    @Test
    public void itShouldReturnTheStreamIdentifierWhenRequested() {
        StreamData subject = new StreamData(null, StreamIdentifier.STREAM_0);

        assertEquals(StreamIdentifier.STREAM_0, subject.getStreamIdentifier());
    }

    @Test
    public void itShouldReturnBinaryPayloadWHenRequested() {
        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40};
        StreamData subject = new StreamData(testData, StreamIdentifier.STREAM_0);

        assertEquals(testData[0], subject.getRawData()[0]);
        assertEquals(testData[1], subject.getRawData()[1]);
        assertEquals(testData[2], subject.getRawData()[2]);
        assertEquals(testData[3], subject.getRawData()[3]);
    }

    @Test
    public void itShouldReturnRawDataTypeWhenRequestingDataType() {
        StreamData subject = new StreamData(null, StreamIdentifier.STREAM_0);

        assertEquals(SHNDataType.StreamData, subject.getSHNDataType());
    }
}