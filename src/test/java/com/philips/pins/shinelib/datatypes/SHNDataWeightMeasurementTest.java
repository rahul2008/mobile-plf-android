package com.philips.pins.shinelib.datatypes;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDataWeightMeasurementTest {

    private static final byte IMPERIAL_SUPPORTED = 0x01;
    private static final byte TIMESTAMP_SUPPORTED = 0x02;
    private static final byte USER_ID_SUPPORTED = 0x04;
    private static final byte BMI_AND_HEIGHT_SUPPORTED = 0x08;

    @Test
    public void whenTheScaleMeasurementsIsSiThanWeightUnitIsKg() {
        byte[] data = new byte[]{0, 0, 0};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertEquals(SHNWeightUnit.KG, shnDataWeightMeasurement.getFlags().getShnWeightUnit());
        assertEquals(SHNHeightUnit.Meter, shnDataWeightMeasurement.getFlags().getShnHeightUnit());

        assertFalse(shnDataWeightMeasurement.getFlags().hasTimestamp());
        assertFalse(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertFalse(shnDataWeightMeasurement.getFlags().hasUserId());
    }

    @Test
    public void whenTheScaleMeasurementsIsImperialThanWeightUnitIsLb() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, 0, 0};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertEquals(SHNWeightUnit.LB, shnDataWeightMeasurement.getFlags().getShnWeightUnit());
        assertEquals(SHNHeightUnit.Inch, shnDataWeightMeasurement.getFlags().getShnHeightUnit());

        assertFalse(shnDataWeightMeasurement.getFlags().hasTimestamp());
        assertFalse(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertFalse(shnDataWeightMeasurement.getFlags().hasUserId());
    }

    @Test
    public void whenTheScaleMeasurementHasTimeStampThanFlagIsTrue() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED | TIMESTAMP_SUPPORTED, 0, 0,
                (byte)0xDF, (byte)0x07, // year 2015 = 0x07DF
                7,                      // month july
                1,                      // day 1st
                9,                      // hour 9
                30,                     // minutes 30
                10,                     // seconds 10
        };

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasTimestamp());
        assertFalse(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertFalse(shnDataWeightMeasurement.getFlags().hasUserId());
    }

    @Test
    public void whenTheScaleMeasurementsHasUserIdThanFlagIsTrue() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED | USER_ID_SUPPORTED, 0, 0, 0x01};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasUserId());
        assertFalse(shnDataWeightMeasurement.getFlags().hasTimestamp());
        assertFalse(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
    }

    @Test
    public void whenTheScaleMeasurementsHasBMIAndHeightThanFlagIsTrue() {
        byte[] data = new byte[]{BMI_AND_HEIGHT_SUPPORTED, 0, 0,
                0, 1, // bmi
                0, 1 // height
        };

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertFalse(shnDataWeightMeasurement.getFlags().hasUserId());
        assertFalse(shnDataWeightMeasurement.getFlags().hasTimestamp());
    }

    @Test
    public void whenTheScaleMeasurementHasAllPropertiesThanAllFlagsAreTrue() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED | TIMESTAMP_SUPPORTED | USER_ID_SUPPORTED | BMI_AND_HEIGHT_SUPPORTED, 0, 0,
                (byte)0xDF, (byte)0x07, // year 2015 = 0x07DF
                7,
                1,
                9,
                30,
                10,
                0x01, // user ID
                0, 1, // bmi
                0, 1 // height
        };

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertTrue(shnDataWeightMeasurement.getFlags().hasUserId());
        assertTrue(shnDataWeightMeasurement.getFlags().hasTimestamp());
    }

    @Test
    public void whenTheScaleMeasurementValueIsPassedInSiThanItIsParsedInKGResolution() {
        byte[] data = new byte[]{0, 0x04, 0x07};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertEquals(1796 * 0.005f, shnDataWeightMeasurement.getWeight(), 0.001);
    }

    @Test
    public void whenTheScaleMeasurementValueIsPassedInImperialThanItIsParsedInLBResolution() {
        byte[] data = new byte[]{IMPERIAL_SUPPORTED, 0x04, 0x07};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertEquals(1796 * 0.01f, shnDataWeightMeasurement.getWeight(), 0.001);
    }

    @Test
    public void whenTheScaleMeasurementHasTimeStampThenItIsParsedProperly() {
        byte[] data = new byte[]{
                IMPERIAL_SUPPORTED | TIMESTAMP_SUPPORTED, 0x04, 0x07,
                (byte)0xDF, (byte)0x07, // year 2015 = 0x07DF
                7,                      // month july
                1,                      // day 1st
                9,                      // hour 9
                30,                     // minutes 30
                11,                     // seconds 11
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasTimestamp());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        assertEquals("2015-07-01 09:30:11", simpleDateFormat.format(shnDataWeightMeasurement.getTimestamp()));
    }

    @Test
    public void whenTheScaleMeasurementHasUserIdThenItIsParsedProperly() {
        byte[] data = new byte[]{
                IMPERIAL_SUPPORTED | USER_ID_SUPPORTED, 0x04, 0x07, 0x06
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasUserId());
        assertEquals(6, shnDataWeightMeasurement.getUserId());
    }

    @Test
    public void whenTheScaleMeasurementHasUserIdIs255ThenUserIsUnknown() {
        byte[] data = new byte[]{
                IMPERIAL_SUPPORTED | USER_ID_SUPPORTED, 0x04, 0x07, (byte)255
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.isUserIdUnknown());
    }

    @Test
    public void whenTheScaleMeasurementHasBMIThenItIsParsedProperly() {
        byte[] data = new byte[]{
                IMPERIAL_SUPPORTED | BMI_AND_HEIGHT_SUPPORTED, 0x04, 0x07, 
                0x20, 0x06, //1568
                0x19, 0x05,
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertEquals(1568 * 0.1f, shnDataWeightMeasurement.getBMI(), 0.001);
    }

    @Test
    public void whenTheScaleMeasurementHasHeightThenItIsParsedProperly() {
        byte[] data = new byte[]{
                IMPERIAL_SUPPORTED | BMI_AND_HEIGHT_SUPPORTED, 0x04, 0x07,
                0x20, 0x06,
                0x19, 0x05, // 1305
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);

        assertTrue(shnDataWeightMeasurement.getFlags().hasBmiAndHeight());
        assertEquals(1305 * 0.1f, shnDataWeightMeasurement.getHeight(), 0.001);
    }
}
