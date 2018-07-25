package com.philips.pins.shinelib.protocols.moonshinestreaming;

import org.junit.Test;

import static org.junit.Assert.*;

public class SHNProtocolInformationTest {

    @Test
    public void whenProvidedWithValidBinaryData_thenItShouldParseCorrectProtocolVersionAndWindows() {
        byte[] testData = new byte[] {(byte)0x03, (byte)0x20, (byte)0x21};
        SHNProtocolInformation subject = SHNProtocolInformation.createFromData(testData);
        assertNotNull(subject);
        assertEquals(3, subject.protocolVersion);
        assertEquals(33, subject.rxWindowSize);
        assertEquals(32, subject.txWindowSize);
    }

    @Test
    public void whenProvidedWithMaxValues_thenItShouldReturnMaxVersionAndWindowValues() {
        byte[] testData = new byte[] {(byte)0xFF, (byte)0xFF, (byte)0xFF};
        SHNProtocolInformation subject = SHNProtocolInformation.createFromData(testData);
        assertNotNull(subject);
        assertEquals((byte)0xFF, (byte)subject.protocolVersion);
        assertEquals((byte)0xFF, (byte)subject.rxWindowSize);
        assertEquals((byte)0xFF, (byte)subject.txWindowSize);
    }

    @Test
    public void whenProvidedWithInvalidAmountOfBytes_thenItShouldNotParseValues_tooFewBytes() {
        byte[] testData = new byte[] {(byte)0x04};
        SHNProtocolInformation subject = SHNProtocolInformation.createFromData(testData);
        assertNull(subject);
    }

    @Test
    public void whenProvidedWithInvalidAmountOfBytes_thenItShouldNotParseValues_tooManyBytes() {
        byte[] testData = new byte[] {(byte)0x04, (byte)0x04, (byte)0x04, (byte)0x04};
        SHNProtocolInformation subject = SHNProtocolInformation.createFromData(testData);
        assertNull(subject);
    }
}