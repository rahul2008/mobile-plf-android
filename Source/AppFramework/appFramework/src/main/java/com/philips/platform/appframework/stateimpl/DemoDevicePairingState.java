/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappDependencies;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappInterface;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappLaunchInput;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DemoDevicePairingState extends BaseState {

    public static final String TAG = DemoDevicePairingState.class.getSimpleName();
    private FragmentLauncher mFragmentLauncher;
    private DevicePairingUappInterface mDevicePairingUappInterface;

    public DemoDevicePairingState() {
        super(AppStates.TEST_DEVICE_PAIRING);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        mFragmentLauncher = (FragmentLauncher) uiLauncher;
        launchDevicePairing();
    }

    private void launchDevicePairing() {
        DevicePairingUappLaunchInput devicePairingUappLaunchInput = new DevicePairingUappLaunchInput();
        mDevicePairingUappInterface.launch(mFragmentLauncher, devicePairingUappLaunchInput);
    }

    @Override
    public void init(Context context) {
        final AppInfraInterface appInfraInterface = ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
        DevicePairingUappSettings devicePairingUappSettings = new DevicePairingUappSettings(context.getApplicationContext());
        DevicePairingUappDependencies devicePairingUappDependencies = new DevicePairingUappDependencies(appInfraInterface);
        mDevicePairingUappInterface = new DevicePairingUappInterface();

        mDevicePairingUappInterface.init(devicePairingUappDependencies, devicePairingUappSettings);
    }

    @Override
    public void updateDataModel() {
    }
}
