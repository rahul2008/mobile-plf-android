/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

public interface AirPortProperties {
    String KEY_LIGHT_STATE = "aqil"; // Air Quality Indicator Light

    boolean getLightOn();
    boolean lightIsSet();
}
