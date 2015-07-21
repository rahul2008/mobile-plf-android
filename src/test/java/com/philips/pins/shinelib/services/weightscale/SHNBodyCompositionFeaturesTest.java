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
public class SHNBodyCompositionFeaturesTest {
    private static final int TIME_STAMP_SUPPORTED = 1;
    private static final int MULTIPLE_USER_SUPPORTED = 2;

    private static final int BASAL_METABOLISM_SUPPORTED = 4;

    private static final int MUSCLE_PERCENTAGE_SUPPORTED = 8;
    private static final int MUSCLE_MASS_SUPPORTED = 16;
    private static final int FAT_FREE_MASS_SUPPORTED = 32;
    private static final int SOFT_LEAN_MASS_SUPPORTED = 64;

    private static final byte IMPEDANCE_SUPPORTED = 1;
    private static final byte WEIGHT_SUPPORTED = 2;
    private static final byte HEIGHT_SUPPORTED = 4;

    private static final int BODY_WATER_MASS_SUPPORTED = 128;

    private SHNBodyCompositionFeatures getSHNBodyCompositionFeatures(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return new SHNBodyCompositionFeatures(byteBuffer);
    }

    @Test
    public void whenTheBodyCompositionIsEmptyThenNoFeaturesAreSupportedSupported() {
        byte[] data = new byte[]{0, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertFalse(shnBodyCompositionFeatures.isTimeStampSupported());
        assertFalse(shnBodyCompositionFeatures.isMultiUserSupported());
        assertFalse(shnBodyCompositionFeatures.isBaselMetabolismSupported());
        assertFalse(shnBodyCompositionFeatures.isMusclePercentageSupported());
        assertFalse(shnBodyCompositionFeatures.isMuscleMassSupported());
        assertFalse(shnBodyCompositionFeatures.isFatFreeMassSupported());
        assertFalse(shnBodyCompositionFeatures.isSoftLeanMassSupported());
        assertFalse(shnBodyCompositionFeatures.isBodyWaterMassSupported());
        assertFalse(shnBodyCompositionFeatures.isImpedanceSupported());
        assertFalse(shnBodyCompositionFeatures.isWeightSupported());
        assertFalse(shnBodyCompositionFeatures.isHeightSupported());
    }

    @Test
    public void whenTheBodyCompositionHasTimeStampsThenFeatureIsSupported() {
        byte[] data = new byte[]{TIME_STAMP_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isTimeStampSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsMultipleUsersThenFeatureIsSupported() {
        byte[] data = new byte[]{MULTIPLE_USER_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isMultiUserSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsBasalMetabolismThenFeatureIsSupported() {
        byte[] data = new byte[]{BASAL_METABOLISM_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isBaselMetabolismSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsMusclePercentageThenFeatureIsSupported() {
        byte[] data = new byte[]{MUSCLE_PERCENTAGE_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isMusclePercentageSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsMassThenFeatureIsSupported() {
        byte[] data = new byte[]{MUSCLE_MASS_SUPPORTED | FAT_FREE_MASS_SUPPORTED | SOFT_LEAN_MASS_SUPPORTED | (byte) BODY_WATER_MASS_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isMuscleMassSupported());
        assertTrue(shnBodyCompositionFeatures.isFatFreeMassSupported());
        assertTrue(shnBodyCompositionFeatures.isBodyWaterMassSupported());
        assertTrue(shnBodyCompositionFeatures.isSoftLeanMassSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsImpedanceThenFeatureIsSupported() {
        byte[] data = new byte[]{0, IMPEDANCE_SUPPORTED, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isImpedanceSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsWeightThenFeatureIsSupported() {
        byte[] data = new byte[]{0, WEIGHT_SUPPORTED, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isWeightSupported());
    }

    @Test
    public void whenTheBodyCompositionSupportsHeightThenFeatureIsSupported() {
        byte[] data = new byte[]{0, HEIGHT_SUPPORTED, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isHeightSupported());
    }

    @Test
    public void whenWeightResolutionIsNotSupportedThanValueIsFalse() {
        byte[] data = new byte[]{TIME_STAMP_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertFalse(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);

        assertFalse(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenWeightResolutionIsLowThenWeightResolutionIsSupportedAndLow() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x08, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0.5, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(1, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightResolutionIsMediumThenWeightResolutionIsSupportedAndMedium() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x20, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0.05, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0.1, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightResolutionIsHighThenWeightResolutionIsSupportedAndHigh() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x38, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0.005, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0.01, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenWeightResolutionIsOutSideTheRangeThenWeightResolutionIsNotSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x40, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertFalse(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(0, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);
    }

    @Test
    public void whenHeightResolutionIsNotSupportedThanValueIsFalse() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED, 0, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertFalse(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightResolutionIsLowThenHeightResolutionIsSupportedAndLow() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | (byte) 128, 0, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0.01, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(1, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightResolutionIsMediumThenHeightResolutionIsSupportedAndMedium() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x20, 0x01, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0.005, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0.5, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightResolutionIsHighThenHeightResolutionIsSupportedAndHigh() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | (byte) 128, 0x01, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0.001, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0.1, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenHeightResolutionIsOutSideTheRangeThenHeightResolutionIsNotSupported() {
        byte[] data = new byte[]{
                TIME_STAMP_SUPPORTED | MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x40, 0x06, 0
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertFalse(shnBodyCompositionFeatures.isHeightResolutionSpecified());
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInMeters(), 0.0005);
        assertEquals(0, shnBodyCompositionFeatures.getHeightResolutionInInch(), 0.0005);
    }

    @Test
    public void whenDataContainsNotSupportedBitsThanTheyAreIgnored() {
        byte[] data = new byte[]{
                MULTIPLE_USER_SUPPORTED, HEIGHT_SUPPORTED | 0x08, 0x16 | 0x01, 0x25
        };
        SHNBodyCompositionFeatures shnBodyCompositionFeatures = getSHNBodyCompositionFeatures(data);

        assertTrue(shnBodyCompositionFeatures.isMultiUserSupported());
        assertTrue(shnBodyCompositionFeatures.isHeightSupported());

        assertFalse(shnBodyCompositionFeatures.isTimeStampSupported());

        assertTrue(shnBodyCompositionFeatures.isWeightResolutionSpecified());
        assertEquals(0.5f, shnBodyCompositionFeatures.getWeightResolutionInKG(), 0.0005);
        assertEquals(1f, shnBodyCompositionFeatures.getWeightResolutionInLBS(), 0.0005);

        assertFalse(shnBodyCompositionFeatures.isHeightResolutionSpecified());
    }
}