package com.philips.pins.shinelib.protocols.moonshinestreaming;

    import org.junit.Test;

    import static org.junit.Assert.*;

public class MoonshineStreamIdentifierTest {

    private final static byte HEADER_0 = (byte)0b00000000;
    private final static byte HEADER_1 = (byte)0b10000000;
    private final static byte HEADER_2 = (byte)0b11000000;

    @Test
    public void whenUsingStream0_thenItShouldUseStreamHeader0() {
        assertEquals(HEADER_0, MoonshineStreamIdentifier.STREAM_0.getHeaderValue());
    }

    @Test
    public void whenUsingStream1_thenItShouldUseStreamHeader1() {
        assertEquals(HEADER_1, MoonshineStreamIdentifier.STREAM_1.getHeaderValue());
    }

    @Test
    public void whenUsingStream2_thenItShouldUseStreamHeader2() {
        assertEquals(HEADER_2, MoonshineStreamIdentifier.STREAM_2.getHeaderValue());
    }

    @Test
    public void whenUsingStream0_thenItShouldUseValue0() {
        assertEquals(0, MoonshineStreamIdentifier.STREAM_0.getValue());
    }

    @Test
    public void whenUsingStream1_thenItShouldUseValue1() {
        assertEquals(1, MoonshineStreamIdentifier.STREAM_1.getValue());
    }

    @Test
    public void whenUsingStream2_thenItShouldUseValue2() {
        assertEquals(2, MoonshineStreamIdentifier.STREAM_2.getValue());
    }

    @Test
    public void whenResolvingHeaderValues_thenItShouldReturnCorrectEnums() {
        assertEquals(MoonshineStreamIdentifier.STREAM_0, MoonshineStreamIdentifier.fromHeaderValue(HEADER_0));
        assertEquals(MoonshineStreamIdentifier.STREAM_1, MoonshineStreamIdentifier.fromHeaderValue(HEADER_1));
        assertEquals(MoonshineStreamIdentifier.STREAM_2, MoonshineStreamIdentifier.fromHeaderValue(HEADER_2));
    }

    @Test
    public void whenResolvingValues_thenItShouldReturnCorrectEnums() {
        assertEquals(MoonshineStreamIdentifier.STREAM_0, MoonshineStreamIdentifier.fromValue(0));
        assertEquals(MoonshineStreamIdentifier.STREAM_1, MoonshineStreamIdentifier.fromValue(1));
        assertEquals(MoonshineStreamIdentifier.STREAM_2, MoonshineStreamIdentifier.fromValue(2));
    }
}