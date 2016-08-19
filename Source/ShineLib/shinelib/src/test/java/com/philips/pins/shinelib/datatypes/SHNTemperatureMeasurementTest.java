package com.philips.pins.shinelib.datatypes;

import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNTemperatureMeasurementTest {
    private static final byte CELSIUS = 0x00;
    private static final byte FAHRENHEIT = 0x01;
    private static final byte TIMESTAMP = 0x02;
    private static final byte TEMPTYPE = 0x04;
    private SimpleDateFormat simpleDateFormat;

    // TODO decompose tests to test a unique item per test.

    @Before
    public void setUp() {
        simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.US);
    }

    @Test
    public void whenTheThermometersupliesOnlyATemperatureThenOnlyTheTemperatureIsPresent() {
        // Setup
        byte[] data = new byte[] {
                CELSIUS,        // Flags
                1, 0, 0,        // Temperature mantissa LSB
                0               // Temperature exponent base 10
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Execute
        SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);

        // Verify
        assertEquals(SHNTemperatureUnit.Celsius, shnTemperatureMeasurement.getFlags().getShnTemperatureUnit());
        assertFalse(shnTemperatureMeasurement.getFlags().hasTimestamp());
        assertFalse(shnTemperatureMeasurement.getFlags().hasTemperatureType());
        assertNull(shnTemperatureMeasurement.getTimestamp());
        assertNull(shnTemperatureMeasurement.getSHNTemperatureType());
        assertEquals(1f, shnTemperatureMeasurement.getTemperature(), 0.01f);
    }

    @Test
    public void whenTheThermometersupliesATemperatureAndATimestampThenBothArePresent() {
        // Setup
        byte[] data = new byte[] {
                FAHRENHEIT|TIMESTAMP,       // Flags
                1, 0, 0,                    // Temperature mantissa LSB
                1,                          // Temperature exponent base 10
                (byte)0xDF, (byte)0x07,     // year 2015 = 0x07DF
                6,                          // month june = 6
                8,                          // day 8th
                8,                          // hour 8
                34,                         // minutes 34
                45                          // seconds 45
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Execute
        SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);

        // Verify
        assertEquals(SHNTemperatureUnit.Fahrenheit, shnTemperatureMeasurement.getFlags().getShnTemperatureUnit());
        assertTrue(shnTemperatureMeasurement.getFlags().hasTimestamp());
        assertFalse(shnTemperatureMeasurement.getFlags().hasTemperatureType());
        assertEquals(10f, shnTemperatureMeasurement.getTemperature(), 0.01f);

        assertNotNull(shnTemperatureMeasurement.getTimestamp());

        assertEquals("2015-06-08 08:34:45", simpleDateFormat.format(shnTemperatureMeasurement.getTimestamp()));
    }

    @Test
    public void whenTheThermometersupliesATemperatureAndATimestampAndATemperatureTypeThenAllValuesArePresent() {
        // Setup
        byte[] data = new byte[] {
                FAHRENHEIT|TIMESTAMP|TEMPTYPE,  // Flags
                1, 1, 0,                        // Temperature mantissa LSB
                -1,                             // Temperature exponent base 10
                (byte)0xDF, (byte)0x07,         // year 2015 = 0x07DF
                6,                              // month june = 6
                8,                              // day 8th
                18,                             // hour 18
                34,                             // minutes 34
                45,                             // seconds 45
                1                               // TemperatureType = Armpit
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Execute
        SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);

        // Verify
        assertEquals(SHNTemperatureUnit.Fahrenheit, shnTemperatureMeasurement.getFlags().getShnTemperatureUnit());
        assertTrue(shnTemperatureMeasurement.getFlags().hasTimestamp());
        assertTrue(shnTemperatureMeasurement.getFlags().hasTemperatureType());
        assertEquals(25.7f, shnTemperatureMeasurement.getTemperature(), 0.01f);

        assertNotNull(shnTemperatureMeasurement.getTimestamp());

        assertEquals("2015-06-08 18:34:45", simpleDateFormat.format(shnTemperatureMeasurement.getTimestamp()));

        assertEquals(SHNTemperatureType.Armpit, shnTemperatureMeasurement.getSHNTemperatureType());
    }
}