/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.ui;

import com.philips.cdp.devicepair.pojo.PairDevice;
import com.philips.cdp.devicepair.states.GetPairedDevicesState;
import com.philips.cdp.devicepair.states.StateContext;
import com.philips.cdp.devicepair.states.UnPairDeviceState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

class LaunchFragmentPresenter {
    private LaunchFragmentViewListener mLaunchFragmentViewListener;

    LaunchFragmentPresenter(LaunchFragmentViewListener fragmentViewListener) {
        this.mLaunchFragmentViewListener = fragmentViewListener;
    }

    void pairDevice(PairDevice pairDevice, DeviceStatusListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new GetPairedDevicesState(pairDevice, getFragmentLauncher(), deviceStatusListener));
        stateContext.start();
    }

    void unPairDevice(String deviceID, DeviceStatusListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new UnPairDeviceState(deviceID, deviceStatusListener, getFragmentLauncher()));
        stateContext.start();
    }

    public FragmentLauncher getFragmentLauncher() {
        return mLaunchFragmentViewListener.getFragmentLauncher();
    }
}
