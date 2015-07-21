package com.philips.pins.shinelib.services.weightscale;

import com.philips.pins.shinelib.datatypes.SHNHeightUnit;
import com.philips.pins.shinelib.datatypes.SHNWeightUnit;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNBodyCompositionMeasurementTest {

    private static final int IMPERIAL_SUPPORTED = 1;
    private static final int TIMESTAMP_SUPPORTED = 2;
    private static final int USER_ID_SUPPORTED = 4;
    private static final int BASAL_METABOLISM_SUPPORTED = 8;

    private static final int MUSCLE_PERCENTAGE_SUPPORTED = 16;
    private static final int MUSCLE_MASS_SUPPORTED = 32;
    private static final int FAT_FREE_MASS_SUPPORTED = 64;
    private static final int SOFT_LEAN_MASS_SUPPORTED = 128;

    private static final int BODY_WATER_MASS_SUPPORTED = 1;
    private static final int IMPEDANCE_MASS_SUPPORTED = 2;
    private static final int WEIGHT_SUPPORTED = 4;
    private static final int HEIGHT_SUPPORTED = 8;
    private static final int MULTIPLE_PACKET_MEASUREMENT = 16;

    public static final float MASS_RESOLUTION_SI = 0.005f;
    public static final float MASS_RESOLUTION_IMPERIAL = 0.01f;

    private SHNBodyCompositionMeasurement generateMeasurement(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return new SHNBodyCompositionMeasurement(byteBuffer);
    }

    @Test
    public void whenTheMeasurementsIsSiThenUnitIsKgAndMeters() {
        byte[] data = new byte[]{0, 0, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(SHNWeightUnit.KG, shnBodyCompositionMeasurement.getFlags().getShnWeightUnit());
        assertEquals(SHNHeightUnit.Meter, shnBodyCompositionMeasurement.getFlags().getShnHeightUnit());
    }

    @Test
    public void whenTheMeasurementsIsImperialThenUnitIsLbbAndInch() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, 0, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(SHNWeightUnit.LB, shnBodyCompositionMeasurement.getFlags().getShnWeightUnit());
        assertEquals(SHNHeightUnit.Inch, shnBodyCompositionMeasurement.getFlags().getShnHeightUnit());
    }

    @Test
    public void whenTheMeasurementHasTimeStampThenFlagIsTrue() {
        byte[] data = new byte[]{TIMESTAMP_SUPPORTED, 0, 0, 0,
                (byte) 0xDF, (byte) 0x07, // year 2015 = 0x07DF
                7,                      // month july
                1,                      // day 1st
                9,                      // hour 9
                30,                     // minutes 30
                10,                     // seconds 10
        };

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasTimestamp());
    }

    @Test
    public void whenTheMeasurementHasTimeStampThenDateIsParsedProperly() {
        byte[] data = new byte[]{TIMESTAMP_SUPPORTED, 0, 0, 0,
                (byte) 0xDF, (byte) 0x07, // year 2015 = 0x07DF
                7,                      // month july
                1,                      // day 1st
                9,                      // hour 9
                30,                     // minutes 30
                10,                     // seconds 10
        };

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertNotNull(shnBodyCompositionMeasurement.getTimestamp());

        Date date = new Date(2015 - 1900, 7 - 1, 1, 9, 30, 10);
        assertNotNull(shnBodyCompositionMeasurement.getTimestamp());
        assertEquals(shnBodyCompositionMeasurement.getTimestamp(), date);
    }

    @Test
    public void whenTheMeasurementsHasUserIdThenFlagIsTrue() {
        byte[] data = new byte[]{USER_ID_SUPPORTED, 0, 0, 0, 0x01};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasUserId());
    }

    @Test
    public void whenTheMeasurementsHasUserIdThenIdIsRed() {
        byte[] data = new byte[]{USER_ID_SUPPORTED, 0, 0, 0, 0x01};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x01, shnBodyCompositionMeasurement.getUserId());
        assertFalse(shnBodyCompositionMeasurement.isUserIdUnknown());
    }

    @Test
    public void whenTheMeasurementsHasNoUserIdThenUserIsUnknown() {
        byte[] data = new byte[]{0, 0, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(255, shnBodyCompositionMeasurement.getUserId()); // user unknown value
        assertTrue(shnBodyCompositionMeasurement.isUserIdUnknown());
    }

    @Test
    public void whenTheMeasurementsHasBasalMetabolismThenFlagIsTrue() {
        byte[] data = new byte[]{BASAL_METABOLISM_SUPPORTED, 0, 0, 0, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasBaselMetabolism());
    }

    @Test
    public void whenTheMeasurementsHasBasalMetabolismThenValueIsRed() {
        byte[] data = new byte[]{BASAL_METABOLISM_SUPPORTED, 0, 0, 0,
                0x3C, 0x05}; //1340

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(1340, shnBodyCompositionMeasurement.getBasalMetabolismInKiloJoules());
    }

    @Test
    public void whenTheMeasurementsHasMusclePercentageThenFlagIsTrue() {
        byte[] data = new byte[]{MUSCLE_PERCENTAGE_SUPPORTED, 0, 0, 0, (byte) 0xD0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasMusclePercentage());
    }

    @Test
    public void whenTheMeasurementsHasMusclePercentageThenValueIsRed() {
        byte[] data = new byte[]{MUSCLE_PERCENTAGE_SUPPORTED, 0, 0, 0, (byte) 0xD0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xD0 * 0.1, shnBodyCompositionMeasurement.getMusclePercentage(), 0.1);
    }

    @Test
    public void whenTheMeasurementsHasMuscleMassThenFlagIsTrue() {
        byte[] data = new byte[]{MUSCLE_MASS_SUPPORTED, 0, 0, 0, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasMuscleMass());
    }

    @Test
    public void whenTheMeasurementsHasMuscleMassInKGThenValueIsRed() {
        byte[] data = new byte[]{MUSCLE_MASS_SUPPORTED, 0, 0, 0,
                (byte) 0xA1, 0x01}; //417

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(417 * MASS_RESOLUTION_SI, shnBodyCompositionMeasurement.getMuscleMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasMuscleMassInLBSThenValueIsRed() {
        byte[] data = new byte[]{MUSCLE_MASS_SUPPORTED | IMPERIAL_SUPPORTED, 0, 0, 0,
                (byte) 0xA1, 0x01}; //417

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(417 * MASS_RESOLUTION_IMPERIAL, shnBodyCompositionMeasurement.getMuscleMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasFatFreeMassThenFlagIsTrue() {
        byte[] data = new byte[]{FAT_FREE_MASS_SUPPORTED, 0, 0, 0, 0x59, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasFatFreeMass());
    }

    @Test
    public void whenTheMeasurementsHasFatFreeMassInKGThenValueIsRed() {
        byte[] data = new byte[]{FAT_FREE_MASS_SUPPORTED, 0, 0, 0, (byte) 0xE1, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xE1 * MASS_RESOLUTION_SI, shnBodyCompositionMeasurement.getFatFreeMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasFatFreeMassInLBSThenValueIsRed() {
        byte[] data = new byte[]{FAT_FREE_MASS_SUPPORTED | IMPERIAL_SUPPORTED, 0, 0, 0, (byte) 0xE1, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xE1 * MASS_RESOLUTION_IMPERIAL, shnBodyCompositionMeasurement.getFatFreeMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasSoftLeanMassThenFlagIsTrue() {
        byte[] data = new byte[]{(byte) SOFT_LEAN_MASS_SUPPORTED, 0, 0, 0, (byte) 0xD1, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasSoftLeanMass());
    }

    @Test
    public void whenTheMeasurementsHasSoftLeanMassInKGThenValueIsRed() {
        byte[] data = new byte[]{(byte) SOFT_LEAN_MASS_SUPPORTED, 0, 0, 0, (byte) 0xD1, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xD1 * MASS_RESOLUTION_SI, shnBodyCompositionMeasurement.getSoftLeanMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasSoftLeanMassInLBSThenValueIsRed() {
        byte[] data = new byte[]{(byte) SOFT_LEAN_MASS_SUPPORTED | IMPERIAL_SUPPORTED, 0, 0, 0, (byte) 0xD1, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xD1 * MASS_RESOLUTION_IMPERIAL, shnBodyCompositionMeasurement.getSoftLeanMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasBodyWaterMassThenFlagIsTrue() {
        byte[] data = new byte[]{0, BODY_WATER_MASS_SUPPORTED, 0, 0, 0x04, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasBodyWaterMass());
    }

    @Test
    public void whenTheMeasurementsHasBodyWaterMassInKGThenValueIsRed() {
        byte[] data = new byte[]{0, BODY_WATER_MASS_SUPPORTED, 0, 0, 0x04, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x04 * MASS_RESOLUTION_SI, shnBodyCompositionMeasurement.getBodyWaterMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasBodyWaterMassInLBSThenValueIsRed() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, BODY_WATER_MASS_SUPPORTED, 0, 0, 0x04, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x04 * MASS_RESOLUTION_IMPERIAL, shnBodyCompositionMeasurement.getBodyWaterMass(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasImpedanceThenFlagIsTrue() {
        byte[] data = new byte[]{0, IMPEDANCE_MASS_SUPPORTED, 0, 0, 0x05, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasImpedance());
    }

    @Test
    public void whenTheMeasurementsHasImpedanceThenValueIsRed() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, IMPEDANCE_MASS_SUPPORTED, 0, 0, 0x05, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x05 * 0.01, shnBodyCompositionMeasurement.getImpedance(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasWeightThenFlagIsTrue() {
        byte[] data = new byte[]{0, WEIGHT_SUPPORTED, 0, 0, 0x3C, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasWeight());
    }

    @Test
    public void whenTheMeasurementsHasWeightInKGThenValueIsRed() {
        byte[] data = new byte[]{0, WEIGHT_SUPPORTED, 0, 0, 0x3C, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x3C * 0.005, shnBodyCompositionMeasurement.getWeight(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasWeightInLBSThenValueIsRed() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, WEIGHT_SUPPORTED, 0, 0, 0x3C, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0x3C * 0.01, shnBodyCompositionMeasurement.getWeight(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasHeightThenFlagIsTrue() {
        byte[] data = new byte[]{0, HEIGHT_SUPPORTED, 0, 0, (byte) 0xAB, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().hasHeight());
    }

    @Test
    public void whenTheMeasurementsHasHeightInMetersThenValueIsRed() {
        byte[] data = new byte[]{0, HEIGHT_SUPPORTED, 0, 0, (byte) 0xAB, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xAB * 0.001, shnBodyCompositionMeasurement.getHeight(), 0.001);
    }

    @Test
    public void whenTheMeasurementsHasHeightInInchThenValueIsRed() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, HEIGHT_SUPPORTED, 0, 0, (byte) 0xAB, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xAB * 0.1, shnBodyCompositionMeasurement.getHeight(), 0.001);
    }

    @Test
    public void whenTheMeasurementsIsMultiPacketThenFlagIsTrue() {
        byte[] data = new byte[]{0, MULTIPLE_PACKET_MEASUREMENT, 0, 0};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().isMultiplePacketMeasurement());
    }

    @Test
    public void whenTheMeasurementsHasMultipleFlagsThenProperFlagIsTrue() {
        byte[] data = new byte[]{(byte) (SOFT_LEAN_MASS_SUPPORTED) | MUSCLE_MASS_SUPPORTED | USER_ID_SUPPORTED, MULTIPLE_PACKET_MEASUREMENT, 0, 0,
                0x01, //user id
                0x01, 0x54, //muscle mass
                0x02, 0x13 // soft lean mass
        };

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertTrue(shnBodyCompositionMeasurement.getFlags().isMultiplePacketMeasurement());
        assertTrue(shnBodyCompositionMeasurement.getFlags().hasSoftLeanMass());
        assertTrue(shnBodyCompositionMeasurement.getFlags().hasUserId());
        assertTrue(shnBodyCompositionMeasurement.getFlags().hasMuscleMass());

        assertFalse(shnBodyCompositionMeasurement.getFlags().hasWeight());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasHeight());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasBaselMetabolism());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasFatFreeMass());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasImpedance());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasTimestamp());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasMusclePercentage());
        assertFalse(shnBodyCompositionMeasurement.getFlags().hasBodyWaterMass());
    }

    @Test
    public void whenTheMeasurementsIsCreatedThenFatPercentageIsConverted() {
        byte[] data = new byte[]{0, 0, (byte) 0xE1, 0}; // 225

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(0xE1 * 0.1, shnBodyCompositionMeasurement.getBodyFatPercentage(), 0.1);
    }

    @Test
    public void whenTheMeasurementsHasSpecialValueThenFatPercentageIsNan() {
        byte[] data = new byte[]{0, 0, (byte) 0xFF, (byte) 0xFF};

        SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = generateMeasurement(data);

        assertEquals(Float.NaN, shnBodyCompositionMeasurement.getBodyFatPercentage(), 0.01);
    }
}