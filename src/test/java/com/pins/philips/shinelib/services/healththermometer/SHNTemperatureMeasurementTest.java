package com.pins.philips.shinelib.services.healththermometer;

import com.pins.philips.shinelib.datatypes.SHNTemperatureUnit;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNTemperatureMeasurementTest {

    @Test
    public void test() {
        byte[] data = new byte[] {  0,          // Flags
                                    1, 0, 0,    // Temperature mantissa LSB
                                    0           // Temperature exponent base 10
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);
        assertEquals(SHNTemperatureUnit.Celsius, shnTemperatureMeasurement.getFlags().getShnTemperatureUnit());
        assertFalse(shnTemperatureMeasurement.getFlags().hasTimestamp());
        assertFalse(shnTemperatureMeasurement.getFlags().hasTemperatureType());
        assertEquals(1f, shnTemperatureMeasurement.getTemperature(), 0.01f);
    }
}