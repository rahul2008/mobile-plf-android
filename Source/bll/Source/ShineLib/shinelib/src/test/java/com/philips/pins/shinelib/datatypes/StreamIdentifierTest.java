package com.philips.pins.shinelib.datatypes;

import org.junit.Test;

import static org.junit.Assert.*;

public class StreamIdentifierTest {

    @Test
    public void whenUsingStream0_thenItShouldUseValue0() {
        assertEquals(0, StreamIdentifier.STREAM_0.getValue());
    }

    @Test
    public void whenUsingStream1_thenItShouldUseValue1() {
        assertEquals(1, StreamIdentifier.STREAM_1.getValue());
    }

    @Test
    public void whenUsingStream2_thenItShouldUseValue2() {
        assertEquals(2, StreamIdentifier.STREAM_2.getValue());
    }

    @Test
    public void whenUsingUnknownValue_thenItShouldReturnMinusOne() {
        assertEquals(-1, StreamIdentifier.UNKNOWN.getValue());
    }

    @Test
    public void whenResolvingValues_thenItShouldReturnCorrectEnums() {
        assertEquals(StreamIdentifier.STREAM_0, StreamIdentifier.fromValue(0));
        assertEquals(StreamIdentifier.STREAM_1, StreamIdentifier.fromValue(1));
        assertEquals(StreamIdentifier.STREAM_2, StreamIdentifier.fromValue(2));
    }

    @Test
    public void whenResolvingValuesWithInvalidValues_thenItShouldReturnUnknown() {
        assertEquals(StreamIdentifier.UNKNOWN, StreamIdentifier.fromValue(-1));
        assertEquals(StreamIdentifier.UNKNOWN, StreamIdentifier.fromValue(3));
        assertEquals(StreamIdentifier.UNKNOWN, StreamIdentifier.fromValue(50));
    }
}