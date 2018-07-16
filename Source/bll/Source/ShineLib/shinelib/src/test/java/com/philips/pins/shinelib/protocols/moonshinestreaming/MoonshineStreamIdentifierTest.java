package com.philips.pins.shinelib.protocols.moonshinestreaming;

    import org.junit.Test;

    import static org.junit.Assert.*;

public class MoonshineStreamIdentifierTest {

    private final static byte HEADER_0 = (byte)0b00000000;
    private final static byte HEADER_1 = (byte)0b10000000;
    private final static byte HEADER_2 = (byte)0b11000000;

    @Test
    public void whenUsingStream0_thenitShouldUseStreamHeader0() {
        assertEquals(HEADER_0, MoonshineStreamIdentifier.STREAM_0.getHeaderValue());
    }

    @Test
    public void whenUsingStream1_thenitShouldUseStreamHeader1() {
        assertEquals(HEADER_1, MoonshineStreamIdentifier.STREAM_1.getHeaderValue());
    }

    @Test
    public void whenUsingStream2_thenitShouldUseStreamHeader2() {
        assertEquals(HEADER_2, MoonshineStreamIdentifier.STREAM_2.getHeaderValue());
    }
}