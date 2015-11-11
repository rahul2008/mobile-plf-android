package com.philips.pins.shinelib;

import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNTemperatureMeasurementResultListener {
    void onActionCompleted(SHNTemperatureMeasurement value, SHNResult result);
}
