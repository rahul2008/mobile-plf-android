/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

import javax.inject.Inject;

class AllSessionsProviderFactory {

    @Inject
    public AllSessionsProviderFactory() {
    }

    @NonNull
    AllSessionsProvider createAllSessionProvider(BleReferenceAppliance appliance) {
        return new AllSessionsProvider(appliance, new SessionProviderFactory()/*, new RetryHelper()*/);
    }
}
