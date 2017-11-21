/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;

import java.util.List;

public interface ConnectivityPowerSleepContract {

    interface View {

        void showError(Error error, String s);

        void showProgressDialog();

        void hideProgressDialog();

        void showToast(String message);

        void updateScreenWithLatestSessionInfo(final Summary summary);

    }

    interface UserActionsListener {

        void savePowerSleepMomentsData(List<Session> sessionList);

        void synchroniseSessionData(BleReferenceAppliance bleReferenceAppliance);

    }

}
