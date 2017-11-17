/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.uappdependencies;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.User;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.devicepair.ui.DevicePairingBaseFragment;
import com.philips.platform.devicepair.ui.DevicePairingLaunchActivity;
import com.philips.platform.devicepair.ui.PairingFragment;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppSettings;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DevicePairingUappInterface implements UappInterface {
    private Context mContext;
    private static DevicePairingUappDependencies devicePairingUappDependencies;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        mContext = uappSettings.getContext();

        DSDemoAppuAppSettings dsDemoAppuAppSettings = new DSDemoAppuAppSettings(mContext);
        DSDemoAppuAppInterface dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
        devicePairingUappDependencies = ((DevicePairingUappDependencies) uappDependencies);

        dsDemoAppuAppInterface.init(new DSDemoAppuAppDependencies(uappDependencies.getAppInfra()), dsDemoAppuAppSettings);

        User user = new User(mContext);
        if (user.isUserSignIn()) {
            SyncScheduler.getInstance().scheduleSync();
        }
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity();
        } else {
            launchAsFragment(uiLauncher);
        }
    }

    private void launchAsActivity() {
        Intent intent = new Intent(mContext, DevicePairingLaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        DevicePairingBaseFragment pairingFragment = new PairingFragment();
        pairingFragment.showFragment(pairingFragment, fragmentLauncher);
    }

    public static CommCentral getCommCentral() {
        return devicePairingUappDependencies.getCommCentral();
    }
}
