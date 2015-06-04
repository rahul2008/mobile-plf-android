package com.pins.philips.shinelib;

import com.pins.philips.shinelib.services.healththermometer.SHNTemperatureMeasurement;
import com.pins.philips.shinelib.services.healththermometer.SHNTemperatureMeasurementInterval;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNTemperatureMeasurementIntervalResultListener {
    void onActionCompleted(SHNTemperatureMeasurementInterval value, SHNResult result);
}
