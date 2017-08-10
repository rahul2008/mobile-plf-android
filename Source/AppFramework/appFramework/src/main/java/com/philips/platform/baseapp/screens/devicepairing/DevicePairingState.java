/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.devicepairing;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappInterface;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappLaunchInput;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DevicePairingState extends BaseState {

    public static final String TAG = DevicePairingState.class.getSimpleName();
    private FragmentLauncher mFragmentLauncher;

    public DevicePairingState() {
        super(AppStates.DEVICE_PAIRING);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        mFragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity) mFragmentLauncher.getFragmentActivity()).handleFragmentBackStack(null, null, getUiStateData().getFragmentLaunchState());
        launchDevicePairing();
    }

    public void launchDevicePairing() {
        DevicePairingUappLaunchInput devicePairingUappLaunchInput = new DevicePairingUappLaunchInput();
        new DevicePairingUappInterface().launch(mFragmentLauncher, devicePairingUappLaunchInput);
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void updateDataModel() {
    }
}
