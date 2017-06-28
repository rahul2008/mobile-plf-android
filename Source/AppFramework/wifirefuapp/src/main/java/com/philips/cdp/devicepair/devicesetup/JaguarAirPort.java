/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.devicesetup;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class JaguarAirPort extends AirPort<JaguarAirportProperties> {

    public JaguarAirPort(final CommunicationStrategy communicationStrategy) {
        super(communicationStrategy, JaguarAirportProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        putProperties(AirPortProperties.KEY_LIGHT_STATE, light ? JaguarAirportProperties.LIGHT_ON_STRING : JaguarAirportProperties.LIGHT_OFF_STRING);
    }
}
