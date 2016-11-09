package com.philips.platform.appframework.connectivity;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.dicommsupport.DiCommPort;

import java.util.Map;

public class DicommDeviceMeasurementPort extends DiCommPort {

    public static final String DeviceMeasurement = "devicemeasurement";

    public static class Key {
        public static final String MEASUREMENT_VALUE = "measurementvalue";
    }


    public DicommDeviceMeasurementPort(@NonNull Handler internalHandler) {
        super(DeviceMeasurement, internalHandler);
    }

    public int GetMeasurementValue() {
        Map<String, Object> properties = getProperties();

        Object value = properties.get(Key.MEASUREMENT_VALUE);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return -1;
    }

}
