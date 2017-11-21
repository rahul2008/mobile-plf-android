/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

public interface ConnectivityPowerSleepContract {

    interface View {

        void updateSessionData(long sleepTime, long numberOfInteruptions, long deepSleepTime);

        void showError(Error error, String s);

    }

    interface UserActionsListener {

        void setUpApplicance(@NonNull RefAppBleReferenceAppliance appliance);

        void removeSessionPortListener(RefAppBleReferenceAppliance appliance);
    }
}
