/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

class SessionProviderFactory {

    SessionProvider createBleSessionProvider(BleReferenceAppliance appliance,
                                             long sessionNumber,
                                             SessionProvider.Callback callback) {
        return new SessionProvider(appliance, sessionNumber, callback);
    }
}
