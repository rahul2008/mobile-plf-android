package com.philips.pins.shinelib.services.weightscale;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNWeightScaleFeaturesTest {

    private static final byte TIME_STAMP_SUPPORTED = 0x01;

    private static final byte MULTIPLE_USER_SUPPORTED = 0x02;

    private static final byte BMI_SUPPORTED = 0x04;

    @Test
    public void whenTheScaleSupportsTimeStampThenWeightScaleFeaturesTimeStampIsSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | 0x50, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isTimeStampSupported());
        assertFalse(shnWeightScaleFeatures.isMultiUserSupported());
        assertFalse(shnWeightScaleFeatures.isBmiSupported());
    }

    @Test
    public void whenTheScaleSupportsMultipleUsersThenWeightScaleFeaturesMultipleUsersIsSupported() {
        byte[] data = new byte[]{
                MULTIPLE_USER_SUPPORTED | 0x50, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isMultiUserSupported());
        assertFalse(shnWeightScaleFeatures.isTimeStampSupported());
        assertFalse(shnWeightScaleFeatures.isBmiSupported());
    }

    @Test
    public void whenTheScaleSupportsBMIThenWeightScaleFeaturesBMIIsSupported() {
        byte[] data = new byte[]{
                BMI_SUPPORTED | 0x50, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isBmiSupported());
        assertFalse(shnWeightScaleFeatures.isMultiUserSupported());
        assertFalse(shnWeightScaleFeatures.isTimeStampSupported());
    }

    @Test
    public void whenTheScaleSupportsBMIAndTimeStampThenWeightScaleFeaturesBMIIAndTimeStampIsSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | 0x50, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isTimeStampSupported());
        assertTrue(shnWeightScaleFeatures.isBmiSupported());
        assertFalse(shnWeightScaleFeatures.isMultiUserSupported());
    }

    @Test
    public void whenTheScaleSupportsAllFeaturedThenWeightScaleFeaturesAllIsSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | MULTIPLE_USER_SUPPORTED | 0x50, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isMultiUserSupported());
        assertTrue(shnWeightScaleFeatures.isTimeStampSupported());
        assertTrue(shnWeightScaleFeatures.isBmiSupported());
    }

    @Test
    public void whenWeightScaleResolutionIsNotSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | 0x00, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertFalse(shnWeightScaleFeatures.isWeightResolutionSpecified());
        assertEquals(0, shnWeightScaleFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0, shnWeightScaleFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightScaleResolutionIsLowThenWeightResolutionIsSupportedAndLow() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | MULTIPLE_USER_SUPPORTED | 0x08, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isWeightResolutionSpecified());
        assertEquals(0.5, shnWeightScaleFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(1, shnWeightScaleFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightScaleResolutionIsMediumThenWeightResolutionIsSupportedAndMedium() {
        byte[] data = new byte[]{
                BMI_SUPPORTED | 0x20, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isWeightResolutionSpecified());
        assertEquals(0.05, shnWeightScaleFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0.1, shnWeightScaleFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightScaleResolutionIsHighThenWeightResolutionIsSupportedAndHigh() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED | 0x38, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isWeightResolutionSpecified());
        assertEquals(0.005, shnWeightScaleFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0.01, shnWeightScaleFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightScaleResolutionIsOutSideTheRangeThenWeightResolutionIsNotSupported() {
        byte[] data = new byte[]{
                MULTIPLE_USER_SUPPORTED | BMI_SUPPORTED | 0x40, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertFalse(shnWeightScaleFeatures.isWeightResolutionSpecified());
        assertEquals(0, shnWeightScaleFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0, shnWeightScaleFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    ///////////
    @Test
    public void whenHeightScaleResolutionIsNotSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | 0x00, 0x00, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertFalse(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightScaleResolutionIsLowThenWeightResolutionIsSupportedAndLow() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | BMI_SUPPORTED | MULTIPLE_USER_SUPPORTED | (byte) 128, 0, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0.01, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(1, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightScaleResolutionIsMediumThenWeightResolutionIsSupportedAndMedium() {
        byte[] data = new byte[]{
                BMI_SUPPORTED | 0x20, 0x01, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0.005, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0.5, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightScaleResolutionIsHighThenWeightResolutionIsSupportedAndHigh() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED | (byte)128, 0x01, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0.001, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0.1, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightScaleResolutionIsOutSideTheRangeThenWeightResolutionIsNotSupported() {
        byte[] data = new byte[]{
                MULTIPLE_USER_SUPPORTED | BMI_SUPPORTED | 0x40, 0x06, 0, 0
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertFalse(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenDataContainsNotSupportedBitsThanTheyAreIgnored() {
        byte[] data = new byte[]{
                MULTIPLE_USER_SUPPORTED | BMI_SUPPORTED | 0x40, 0x01, 0x16, 0x25
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNWeightScaleFeatures shnWeightScaleFeatures = new SHNWeightScaleFeatures(byteBuffer);

        assertTrue(shnWeightScaleFeatures.isMultiUserSupported());
        assertTrue(shnWeightScaleFeatures.isBmiSupported());

        assertFalse(shnWeightScaleFeatures.isTimeStampSupported());

        assertTrue(shnWeightScaleFeatures.isHeightResolutionSpecified());
        assertEquals(0.005, shnWeightScaleFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0.5, shnWeightScaleFeatures.getHeightResolutionInInch(), 0.0005);

        assertFalse(shnWeightScaleFeatures.isWeightResolutionSpecified());
    }
}
