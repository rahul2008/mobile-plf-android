/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.air;

import com.philips.cdp2.commlib.core.port.PortProperties;

public interface AirPortProperties extends PortProperties {
    String KEY_LIGHT_STATE = "aqil"; // Air Quality Indicator Light

    boolean getLightOn();

    boolean lightIsSet();
}
