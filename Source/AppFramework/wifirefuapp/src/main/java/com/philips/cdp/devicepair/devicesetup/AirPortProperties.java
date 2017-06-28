/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.devicesetup;

import com.philips.cdp2.commlib.core.port.PortProperties;

public interface AirPortProperties extends PortProperties {
    String KEY_LIGHT_STATE = "aqil"; // Air Quality Indicator Light

    boolean getLightOn();

    boolean lightIsSet();
}
