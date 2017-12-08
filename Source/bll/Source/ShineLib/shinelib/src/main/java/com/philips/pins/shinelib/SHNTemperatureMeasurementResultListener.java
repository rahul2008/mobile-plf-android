/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;

/**
 * A callback used to receive the result of a request for a temperature measurement. Returns {@link SHNResult} and an obtained temperature measurement.
 *
 * @publicPluginApi
 */
public interface SHNTemperatureMeasurementResultListener extends ResultListener<SHNTemperatureMeasurement> {
}
