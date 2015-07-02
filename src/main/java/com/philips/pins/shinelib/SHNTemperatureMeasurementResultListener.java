package com.philips.pins.shinelib;

import com.philips.pins.shinelib.datatypes.SHNDataTemperatureMeasurement;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNTemperatureMeasurementResultListener {
    void onActionCompleted(SHNDataTemperatureMeasurement value, SHNResult result);
}
