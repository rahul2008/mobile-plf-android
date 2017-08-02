/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.uappdependencies;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.devicepair.ui.DevicePairingBaseFragment;
import com.philips.platform.devicepair.ui.PairingFragment;
import com.philips.platform.devicepair.ui.DevicePairingLaunchActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DevicePairingUappInterface implements UappInterface {
    private Context mContext;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        mContext = uappSettings.getContext();
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
}
